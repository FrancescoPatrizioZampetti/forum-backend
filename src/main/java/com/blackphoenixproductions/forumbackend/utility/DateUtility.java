package com.blackphoenixproductions.forumbackend.utility;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtility {

    public static String setTimeDifferenceFromNow(Date createDate) {
        Date now = new Date();
        LocalDate dateFrom = convertToLocalDate(createDate);
        LocalDate dateTo = convertToLocalDate(now);
        long seconds = ChronoUnit.SECONDS.between(createDate.toInstant(), now.toInstant());
        long minutes = ChronoUnit.MINUTES.between(createDate.toInstant(), now.toInstant());
        long hours = ChronoUnit.HOURS.between(createDate.toInstant(), now.toInstant());
        long days = ChronoUnit.DAYS.between(createDate.toInstant(), now.toInstant());
        long months = ChronoUnit.MONTHS.between(dateFrom, dateTo);
        long years = ChronoUnit.YEARS.between(dateFrom, dateTo);

        StringBuilder builder = new StringBuilder();
        if (years > 0) {
            builder.append(years + " anni fa");
        }
        else if(months > 0){
            builder.append(months + " mesi fa");
        }
        else if (days > 0) {
            builder.append(days + " giorni fa");
        } else if (hours > 0) {
            builder.append(hours + " ore fa");
        } else if (minutes > 0) {
            builder.append(minutes + " minuti fa");
        } else {
            builder.append(seconds + " secondi fa");
        }
        return builder.toString();
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
