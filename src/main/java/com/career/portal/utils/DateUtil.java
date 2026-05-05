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
     * @param startDate the start date of the experience period
     * @param endDate   the end date of the experience period
     * @return the experience in years
     */
    public static long calculateExperienceInYears(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Both start and end dates are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        return ChronoUnit.YEARS.between(startDate, endDate);
    }
}
```