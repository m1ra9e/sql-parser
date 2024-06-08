package home;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class MainTest {

    private static final String FILE_NAME = "example.sql";

    @Test
    void validArgsTest() throws Exception {
        Path path = Paths.get(getClass().getResource(FILE_NAME).toURI()).toAbsolutePath();
        Main.runApplication(new String[] { path.toString() });
    }

    @ParameterizedTest
    @MethodSource("argsAndExpectedErrorMsgProvider")
    void errorArgsTest(String[] args, String expectedErrorMsg) {
        try {
            Main.runApplication(args);
            fail("Expected java.lang.IllegalArgumentException to be thrown, but nothing was thrown.");
        } catch (IllegalArgumentException e) {
            String actualErrorMsg = e.getMessage();
            assertTrue(actualErrorMsg.contains(expectedErrorMsg), """
                    Expected and actual error messages does not match:
                    expected: %s
                    actual: %s
                    """.formatted(expectedErrorMsg, actualErrorMsg));
        } catch (Exception e) {
            fail("Expected java.lang.IllegalArgumentException to be thrown, but %s was thrown."
                    .formatted(e.getClass().getSimpleName()));
        }
    }

    private static Stream<Arguments> argsAndExpectedErrorMsgProvider() {
        return Stream.of(
                Arguments.of(new String[0], """
                        missed file path!
                        The correct command should look like this:
                        java -jar sql-parser.jar file_path_for_parse.sql"""),

                Arguments.of(new String[] { "first_file.sql", "second_file.sql" },
                        "the path to one file must be specified!"),

                Arguments.of(new String[] { "non-existent.sql" },
                        "there is no such file:"),

                Arguments.of(new String[] { "incorrect_extension.txt" },
                        "file does not have '*.sql' extension:"));
    }
}
