package com.cercli.employee.management.employeeservice.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimezoneUtil {

    private TimezoneUtil(){}

    public static ZonedDateTime convertToLocalTime(ZonedDateTime serverTime, ZoneId employeeZone) {
        return serverTime.withZoneSameInstant(employeeZone);
    }
}
