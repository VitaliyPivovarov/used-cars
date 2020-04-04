package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtils {

    public static LocalDateTime now() {
        return java.time.LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static ZoneId UtcZone() {
        return ZoneId.of("UTC");
    }

}
