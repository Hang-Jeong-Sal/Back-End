package field.platform.format;

import java.time.LocalDateTime;

public class LocalDateTimeToString {

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(localDateTime.getYear());
        stringBuilder.append('-');
        stringBuilder.append(localDateTime.getMonth().getValue());
        stringBuilder.append('-');
        stringBuilder.append(localDateTime.getDayOfMonth());
        return stringBuilder.toString();
    }
}
