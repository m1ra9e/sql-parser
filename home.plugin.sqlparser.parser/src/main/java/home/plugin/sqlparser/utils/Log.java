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

public final class Log {

    private static final String INFO = "[INFO] [%s] [%s] : \n";
    private static final String ERROR = "[ERROR] [%s] [%s] : \n";

    private final String className;

    public Log(Class<?> clazz) {
        className = clazz.getSimpleName();
    }

    public void info(Object obj) {
        info(obj.toString());
    }

    public void info(String msg) {
        System.out.println(INFO.formatted(getDataTime(), className) + msg);
    }

    public void error(Exception e) {
        error(e.getMessage());
        e.printStackTrace();
    }

    public void error(String msg, Exception e) {
        error(msg + "\nDetails:\n" + e.getMessage());
    }

    public void error(String msg) {
        System.err.println(ERROR.formatted(getDataTime(), className) + msg);
    }

    private String getDataTime() {
        return Utils.getFormattedDate(System.currentTimeMillis());
    }
}
