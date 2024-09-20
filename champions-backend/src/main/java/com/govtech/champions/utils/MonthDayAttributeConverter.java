package com.govtech.champions.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.MonthDay;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class MonthDayAttributeConverter implements AttributeConverter<MonthDay, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    @Override
    public String convertToDatabaseColumn(MonthDay attribute) {
        if (attribute == null) return null;
        return attribute.format(FORMATTER);
    }

    @Override
    public MonthDay convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return MonthDay.parse(dbData, FORMATTER);
    }
}