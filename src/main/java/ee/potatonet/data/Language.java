package ee.potatonet.data;

import java.util.Locale;

public enum Language {
  EE(Locale.forLanguageTag("et-EE")),
  EN(Locale.ENGLISH);

  private Locale locale;

  Language(Locale locale) {
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }
}
