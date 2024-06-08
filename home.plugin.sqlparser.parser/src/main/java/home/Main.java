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
package home;

import home.plugin.sqlparser.parser.SqlFileParser;
import home.plugin.sqlparser.parser.model.SqlNode;
import home.plugin.sqlparser.utils.Log;

public final class Main {

    private static final Log LOG = new Log(Main.class);

    public static void main(String[] args) {
        try {
            runApplication(args);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage());
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    static void runApplication(String[] args) {
        CliArgs.process(args);
        SqlNode sqlNode = SqlFileParser.parse(CliArgs.filePath());
        LOG.info(sqlNode);
    }
}