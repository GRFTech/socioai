package spring.gr.socioai.infra.security.exceptions.custom;

public class BadTokenException extends RuntimeException {
  public BadTokenException(String message) {
    super(message);
  }
}
