/*******************************************************************************
 * Copyright 2023-2024 Lenar Shamsutdinov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package home.plugin.sqlparser.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public final class Utils {

    private static final String DATE_FORMAT = "yyyy.MM.dd | HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT, Locale.ROOT);

    public static String getFormattedDate(long dateTimeInMilliseconds) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateTimeInMilliseconds),
                TimeZone.getDefault().toZoneId());
        return dateTime.format(DATE_FORMATTER);
    }

    private Utils() {
    }
}
