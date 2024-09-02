package br.com.petconnect.boarding.util;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {
    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula");
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}