package com.blackphoenixproductions.forumbackend.utility;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtility {

    public static String setTimeDifferenceFromNow(LocalDateTime dateFrom) {
        LocalDateTime dateTo = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(dateFrom, dateTo);
        long minutes = ChronoUnit.MINUTES.between(dateFrom, dateTo);
        long hours = ChronoUnit.HOURS.between(dateFrom, dateTo);
        long days = ChronoUnit.DAYS.between(dateFrom, dateTo);
        long months = ChronoUnit.MONTHS.between(dateFrom, dateTo);
        long years = ChronoUnit.YEARS.between(dateFrom, dateTo);

        StringBuilder builder = new StringBuilder();
        if (years > 0) {
            builder.append(years + " anni fa");
        } else if(months > 0){
            builder.append(months + " mesi fa");
        } else if (days > 0) {
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

}
