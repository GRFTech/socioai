package spring.gr.socioai.infra.security.exceptions.custom;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
