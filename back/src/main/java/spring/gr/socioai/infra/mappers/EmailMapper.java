package spring.gr.socioai.infra.mappers;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.gr.socioai.infra.security.model.Email;

@Converter(autoApply = true)
public class EmailMapper implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute.getEmail();
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return new Email(dbData);
    }
}
