package com.example.hw04_gymlog_v300;

import androidx.room.TypeConverter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeTypeConverter {

    @TypeConverter
    public static long convertDateToLong(LocalDateTime dateTime) {
        if (dateTime == null) return 0L;
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @TypeConverter
    public static LocalDateTime convertLongToDate(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }
}
