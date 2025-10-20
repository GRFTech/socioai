package spring.gr.socioai.model.valueobjects;

import spring.gr.socioai.security.exceptions.custom.InvalidEmailException;

import java.util.Objects;

public class Email {

    private String email;

    public Email(String email) {
        if(!isValid(email)) {
            throw new InvalidEmailException("Your email address is invalid. Please check and try again.");
        }

        this.email = email;
    }

    private boolean isValid(String email) {
        return email != null && email.contains("@");
    }

    public String getEmail() {
        return email.toLowerCase();
    }

    public void setEmail(String email) {
        if(!isValid(email)) {
            throw new InvalidEmailException("Your email address is invalid. Please check and try again.");
        }
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Email email1)) return false;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    @Override
    public String toString() {
        return email.toLowerCase();
    }
}