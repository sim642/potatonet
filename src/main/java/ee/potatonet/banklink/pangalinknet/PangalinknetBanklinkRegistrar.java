package ee.potatonet.banklink.pangalinknet;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ee.potatonet.banklink.BanklinkRegistrar;
import ee.potatonet.banklink.BanklinkRegistry;

public class PangalinknetBanklinkRegistrar implements BanklinkRegistrar {

  private final PangalinknetBanklinkProperties properties;

  private final RestTemplate restTemplate;

  public PangalinknetBanklinkRegistrar(PangalinknetBanklinkProperties properties) {
    this.properties = properties;
    this.restTemplate = new RestTemplate();
  }

  @Override
  public void registerBanklinks(BanklinkRegistry registry) {
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(properties.getUrl()).pathSegment("api", "project");

    Projects projects = restTemplate.getForObject(uriComponentsBuilder.toUriString(), ProjectsDTO.class).getData();
    projects.getList().forEach(listProject -> {
      Project project = restTemplate.getForObject(uriComponentsBuilder.cloneBuilder().pathSegment(listProject.getId()).toUriString(), ProjectDTO.class).getData();

      PangalinknetBanklink banklink = new PangalinknetBanklink();
      banklink.setClientId(project.getClientId());
      banklink.setUrl(project.getPaymentUrl());
      banklink.setAccountNumber(project.getAccountNr());
      banklink.setAccountName(project.getAccountOwner());

      registry.registerBanklink(project.getId(), banklink);
    });
  }
}
