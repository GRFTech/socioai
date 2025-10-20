package spring.gr.socioai.service.mappers;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import spring.gr.socioai.model.valueobjects.Email;

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
