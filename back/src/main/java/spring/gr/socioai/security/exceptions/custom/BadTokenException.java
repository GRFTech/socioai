package spring.gr.socioai.security.exceptions.custom;

public class BadTokenException extends RuntimeException {
  public BadTokenException(String message) {
    super(message);
  }
}
