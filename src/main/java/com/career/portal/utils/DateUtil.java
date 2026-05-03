```java
package com.career.portal.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    // existing code...

    /**
     * Calculate experience in years between two dates.
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return the experience in years
     */
    public static long calculateExperienceInYears(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start date and end date must be provided");
        }
        return ChronoUnit.YEARS.between(startDate, endDate);
    }

    /**
     * Calculate experience in years between a start date and the current date.
     * 
     * @param startDate the start date
     * @return the experience in years
     */
    public static long calculateExperienceInYears(LocalDate startDate) {
        return calculateExperienceInYears(startDate, LocalDate.now());
    }
}
```