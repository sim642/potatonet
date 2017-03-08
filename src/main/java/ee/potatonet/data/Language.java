package ee.potatonet.data;

import java.util.Locale;

public enum Language {
  EN(Locale.ENGLISH),
  EE(Locale.forLanguageTag("et-EE"));

  private Locale locale;

  Language(Locale locale) {
    this.locale = locale;
  }

  public Locale getLocale() {
    return locale;
  }
}
