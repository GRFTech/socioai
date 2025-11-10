package spring.gr.socioai.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    /** Formatador para o padrão de data "dd/MM/yyyy". */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formatador para o padrão de data e hora "dd/MM/yyyy HH:mm:ss". */
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Formata um objeto {@link LocalDate} para o padrão "dd/MM/yyyy".
     * @param date A data a ser formatada.
     * @return A data formatada como String.
     */
    public static String formatarParaDDMMYYYY(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Converte uma String no formato "dd/MM/yyyy" para um objeto {@link LocalDate}.
     * @param dateString A string de data no formato "dd/MM/yyyy".
     * @return O objeto {@link LocalDate} correspondente.
     */
    public static LocalDate parsearParaLocalDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Converte uma String no formato "dd/MM/yyyy HH:mm:ss" para um objeto {@link LocalDateTime}.
     * @param dateTimeString A string de data/hora no formato "dd/MM/yyyy HH:mm:ss".
     * @return O objeto {@link LocalDateTime} correspondente.
     */
    public static LocalDateTime parsearParaLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    }
}