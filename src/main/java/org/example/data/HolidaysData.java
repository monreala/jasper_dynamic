package org.example.data;

import org.example.model.Holiday;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public final class HolidaysData {

    public static final String COUNTRY_IT = "IT";
    public static final String COUNTRY_MD = "MD";

    private HolidaysData() {
    }

    public static List<Holiday> holidays2021() {
        return List.of(
                holiday(COUNTRY_IT, "Capodanno",                  2021,  1,  1),
                holiday(COUNTRY_IT, "Epifania",                   2021,  1,  6),
                holiday(COUNTRY_IT, "Pasqua",                     2021,  4,  4),
                holiday(COUNTRY_IT, "Lunedi dell'Angelo",         2021,  4,  5),
                holiday(COUNTRY_IT, "Festa della Liberazione",    2021,  4, 25),
                holiday(COUNTRY_IT, "Festa del Lavoro",           2021,  5,  1),
                holiday(COUNTRY_IT, "Festa della Repubblica",     2021,  6,  2),
                holiday(COUNTRY_IT, "Ferragosto",                 2021,  8, 15),
                holiday(COUNTRY_IT, "Tutti i Santi",              2021, 11,  1),
                holiday(COUNTRY_IT, "Immacolata Concezione",      2021, 12,  8),
                holiday(COUNTRY_IT, "Natale",                     2021, 12, 25),
                holiday(COUNTRY_IT, "Santo Stefano",              2021, 12, 26),

                holiday(COUNTRY_MD, "Anul Nou",                   2021,  1,  1),
                holiday(COUNTRY_MD, "A doua zi de Anul Nou",      2021,  1,  2),
                holiday(COUNTRY_MD, "Craciunul (calendar vechi)", 2021,  1,  7),
                holiday(COUNTRY_MD, "Craciunul a doua zi",        2021,  1,  8),
                holiday(COUNTRY_MD, "Ziua Internationala a Femeii", 2021, 3,  8),
                holiday(COUNTRY_MD, "Pastele",                    2021,  5,  2),
                holiday(COUNTRY_MD, "Ziua Muncii",                2021,  5,  1),
                holiday(COUNTRY_MD, "Pastele Blajinilor",         2021,  5, 10),
                holiday(COUNTRY_MD, "Ziua Victoriei",             2021,  5,  9),
                holiday(COUNTRY_MD, "Ziua Independentei",         2021,  8, 27),
                holiday(COUNTRY_MD, "Limba Noastra",              2021,  8, 31),
                holiday(COUNTRY_MD, "Craciunul",                  2021, 12, 25)
        );
    }

    private static Holiday holiday(String country, String name, int year, int month, int day) {
        Date date = Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new Holiday(country, name, date);
    }
}
