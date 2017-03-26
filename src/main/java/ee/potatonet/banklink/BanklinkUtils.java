package ee.potatonet.banklink;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedHashMap;
import org.apache.commons.codec.binary.Base64;

public final class BanklinkUtils {

  private BanklinkUtils() {

  }

  public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .appendPattern("xx")
      .parseStrict()
      .toFormatter();

  // http://www.pangaliit.ee/et/arveldused/viitenumber
  public static String getReferenceNumber(String stamp) {
    return stamp + get731Checksum(stamp);
  }

  private static int get731Checksum(String number) {
    int[] weights = {7, 3, 1};
    int sum = 0;
    for (int i = number.length() - 1, j = 0; i >= 0; i--, j++) {
      sum += Character.digit(number.charAt(i), 10) * weights[j % weights.length];
    }
    return ((int) Math.ceil(sum / 10.0)) * 10 - sum;
  }

  public static String signParams(Banklink banklink, LinkedHashMap<String, String> params) {
    String paramsSignatureString = getParamsSignatureString(params);

    try {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initSign(banklink.getPrivateKey());
      signature.update(paramsSignatureString.getBytes(StandardCharsets.UTF_8));
      return Base64.encodeBase64String(signature.sign());
    }
    catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean verifyParams(Banklink banklink, LinkedHashMap<String, String> params, String mac) {
    String paramsSignatureString = getParamsSignatureString(params);

    try {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initVerify(banklink.getBankCertificate());
      signature.update(paramsSignatureString.getBytes(StandardCharsets.UTF_8));
      return signature.verify(Base64.decodeBase64(mac));
    }
    catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getParamsSignatureString(LinkedHashMap<String, String> params) {
    StringBuilder sb = new StringBuilder();
    for (String value : params.values()) {
      sb.append(String.format("%03d", value.length()));
      sb.append(value);
    }
    return sb.toString();
  }
}
