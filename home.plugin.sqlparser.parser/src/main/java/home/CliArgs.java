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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CliArgs {

    private static CliArgs filled;

    private CliArgs() {
    }

    private static CliArgs getFilled() {
        if (filled != null) {
            return filled;
        }

        throw new IllegalStateException("arguments must be processed before use!");
    }

    static void process(String[] args) {
        var cliArgs = new CliArgs();
        CliArgsProcessor.readAndCheck(args, cliArgs);
        CliArgs.filled = cliArgs;
    }

    private Path filePath;

    public static Path filePath() {
        return getFilled().getFilePath();
    }

    Path getFilePath() {
        return filePath;
    }

    void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
}

final class CliArgsProcessor {

    private static final int FILE_PATH_IDX = 0;

    private final String[] args;
    private final CliArgs cliArgs;

    private CliArgsProcessor(String[] args, CliArgs cliArgs) {
        this.args = args;
        this.cliArgs = cliArgs;
    }

    static void readAndCheck(String[] args, CliArgs cliArgs) {
        var processor = new CliArgsProcessor(args, cliArgs);
        processor.checkRawArgs();
        processor.fillCliArgs();
        processor.verifyCliArgs();
    }

    private void checkRawArgs() {
        if (args.length == 0) {
            throw new IllegalArgumentException("""
                    missed file path!
                    The correct command should look like this:
                    java -jar sql-parser.jar file_path_for_parse.sql""");
        }

        if (args.length != 1) {
            throw new IllegalArgumentException("the path to one file must be specified!");
        }
    }

    private void fillCliArgs() {
        Path filePath = Paths.get(args[FILE_PATH_IDX]);
        cliArgs.setFilePath(filePath);
    }

    private void verifyCliArgs() {
        Path filePath = cliArgs.getFilePath();

        if (!filePath.getFileName().toString().endsWith(".sql")) {
            throw new IllegalArgumentException("file does not have '*.sql' extension: '%s'!"
                    .formatted(filePath.toAbsolutePath().toString()));
        }

        if (Files.notExists(filePath)) {
            throw new IllegalArgumentException("there is no such file: '%s'!"
                    .formatted(filePath.toAbsolutePath().toString()));
        }
    }
}
