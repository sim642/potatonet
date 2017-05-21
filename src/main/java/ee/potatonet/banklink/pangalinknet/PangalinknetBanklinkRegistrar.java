package ee.potatonet.banklink.pangalinknet;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ee.potatonet.banklink.BanklinkRegistrar;
import ee.potatonet.banklink.BanklinkRegistry;
import ee.potatonet.security.OpenSslUtils;

public class PangalinknetBanklinkRegistrar implements BanklinkRegistrar {

  private final PangalinknetBanklinkProperties properties;

  private final RestTemplate restTemplate;

  public PangalinknetBanklinkRegistrar(PangalinknetBanklinkProperties properties) {
    this.properties = properties;
    this.restTemplate = new RestTemplate();
  }

  @Override
  public void registerBanklinks(BanklinkRegistry registry) {
    if (properties == null)
      return;

    UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(properties.getUrl());
    UriComponentsBuilder apiProjectUri = uri.cloneBuilder().pathSegment("api", "project");

    if (properties.getApiKey() != null)
      apiProjectUri.queryParam("access_token", properties.getApiKey());

    Projects projects = restTemplate.getForObject(apiProjectUri.toUriString(), ProjectsDTO.class).getData();
    projects.getList().forEach(listProject -> {
      try {
        UriComponentsBuilder apiProject = apiProjectUri.cloneBuilder().pathSegment(listProject.getId());
        Project project = restTemplate.getForObject(apiProject.toUriString(), ProjectDTO.class).getData();

        PangalinknetBanklink banklink = new PangalinknetBanklink();
        banklink.setDisplayName(project.getName());
        banklink.setClientId(project.getClientId());

        UriComponentsBuilder paymentUri = uri.cloneBuilder().replacePath(new URL(project.getPaymentUrl()).getPath());
        banklink.setUrl(paymentUri.toUriString());

        banklink.setAccountNumber(project.getAccountNr());
        banklink.setAccountName(project.getAccountOwner());

        banklink.setPrivateKey(OpenSslUtils.readPrivateKey(new StringReader(project.getPrivateKey())));
        banklink.setBankCertificate(OpenSslUtils.readCertificate(new StringReader(project.getBankCertificate())));

        registry.registerBanklink(project.getId(), banklink);
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
