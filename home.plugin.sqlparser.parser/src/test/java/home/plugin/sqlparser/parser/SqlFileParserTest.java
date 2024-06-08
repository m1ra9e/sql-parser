package home.plugin.sqlparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import home.plugin.sqlparser.parser.model.SqlNode;
import home.plugin.sqlparser.parser.model.SqlNodeType;

final class SqlFileParserTest {

    private static final String FILE_NAME = "example.sql";

    @Test
    void parserTest() throws Exception {
        SqlNode expected = getExpected();

        Path path = Paths.get(getClass().getResource(FILE_NAME).toURI()).toAbsolutePath();
        SqlNode actual = SqlFileParser.parse(path);

        assertEquals(expected, actual);
        assertEquals(expected.toString(), getExpectedString());
        assertEquals(getExpectedString(), actual.toString());
    }

    private SqlNode getExpected() {
        SqlNode rootSelect = SqlNode.createRootNode();

        SqlNode colTCol1 = SqlNode.create(rootSelect,
                "t.col1", SqlNodeType.COLUMN);

        SqlNode colTCol2AsQwerty = SqlNode.create(rootSelect,
                "t.col2 AS qwerty", SqlNodeType.COLUMN);

        SqlNode selInColNoAlias1 = SqlNode.create(rootSelect,
                "column_as_select : [no_alias]", SqlNodeType.SELECT_IN_COLUMN);
        SqlNode colOC1 = SqlNode.create(selInColNoAlias1, "o.c1", SqlNodeType.COLUMN);
        SqlNode tblInFromHiddenSecretTableO = SqlNode.create(selInColNoAlias1,
                "hidden.secret_table o", SqlNodeType.TABLE_IN_FROM);

        SqlNode selInColM = SqlNode.create(rootSelect,
                "column_as_select : [m]", SqlNodeType.SELECT_IN_COLUMN);
        SqlNode colWC1 = SqlNode.create(selInColM, "w.c1", SqlNodeType.COLUMN);
        SqlNode selInColB = SqlNode.create(selInColM,
                "column_as_select : [b]", SqlNodeType.SELECT_IN_COLUMN);
        SqlNode colYC1 = SqlNode.create(selInColB, "y.c1", SqlNodeType.COLUMN);
        SqlNode tblInFromVeryHiddenVerySecretTableY = SqlNode.create(selInColB,
                "very_hidden.very_secret_table y", SqlNodeType.TABLE_IN_FROM);
        SqlNode tblInFromOtherHiddenOtherSecretTableW = SqlNode.create(selInColM,
                "other_hidden.other_secret_table w", SqlNodeType.TABLE_IN_FROM);

        SqlNode selInColNoAlias2 = SqlNode.create(rootSelect,
                "column_as_select : [no_alias]", SqlNodeType.SELECT_IN_COLUMN);
        SqlNode colHC1 = SqlNode.create(selInColNoAlias2, "h.c1", SqlNodeType.COLUMN);
        SqlNode selInColR = SqlNode.create(selInColNoAlias2,
                "column_as_select : [r]", SqlNodeType.SELECT_IN_COLUMN);
        SqlNode colJC1 = SqlNode.create(selInColR, "j.c1", SqlNodeType.COLUMN);
        SqlNode tblInFromVeryHiddenVerySecretTableJ = SqlNode.create(selInColR,
                "very_hidden.very_secret_table j", SqlNodeType.TABLE_IN_FROM);
        SqlNode selInFromH = SqlNode.create(selInColNoAlias2,
                "from_as_select : [h]", SqlNodeType.SELECT_IN_FROM);
        SqlNode colQ1 = SqlNode.create(selInFromH, "q1", SqlNodeType.COLUMN);
        SqlNode colQ2 = SqlNode.create(selInFromH, "q2", SqlNodeType.COLUMN);
        SqlNode tblInFromHiddenFromerHideTable = SqlNode.create(selInFromH,
                "hidden_fromer.hide_table", SqlNodeType.TABLE_IN_FROM);

        SqlNode selInFromT = SqlNode.create(rootSelect,
                "from_as_select : [t]", SqlNodeType.SELECT_IN_FROM);
        SqlNode colPCol1 = SqlNode.create(selInFromT, "p.col1", SqlNodeType.COLUMN);
        SqlNode colPCol2 = SqlNode.create(selInFromT, "p.col2", SqlNodeType.COLUMN);
        SqlNode selInFromP = SqlNode.create(selInFromT,
                "from_as_select : [p]", SqlNodeType.SELECT_IN_FROM);
        SqlNode colKCol1 = SqlNode.create(selInFromP, "k.col1", SqlNodeType.COLUMN);
        SqlNode colKCol2 = SqlNode.create(selInFromP, "k.col2", SqlNodeType.COLUMN);
        SqlNode colKCol3 = SqlNode.create(selInFromP, "k.col3", SqlNodeType.COLUMN);
        SqlNode tblInFromPublicSomeTableK = SqlNode.create(selInFromP,
                "public.some_table k", SqlNodeType.TABLE_IN_FROM);

        return rootSelect;
    }

    private String getExpectedString() {
        return """
                root_select
                |- t.col1
                |- t.col2 AS qwerty
                |- column_as_select : [no_alias]
                |   |- o.c1
                |   +- hidden.secret_table o
                |- column_as_select : [m]
                |   |- w.c1
                |   |- column_as_select : [b]
                |   |   |- y.c1
                |   |   +- very_hidden.very_secret_table y
                |   +- other_hidden.other_secret_table w
                |- column_as_select : [no_alias]
                |   |- h.c1
                |   |- column_as_select : [r]
                |   |   |- j.c1
                |   |   +- very_hidden.very_secret_table j
                |   +- from_as_select : [h]
                |       |- q1
                |       |- q2
                |       +- hidden_fromer.hide_table
                +- from_as_select : [t]
                    |- p.col1
                    |- p.col2
                    +- from_as_select : [p]
                        |- k.col1
                        |- k.col2
                        |- k.col3
                        +- public.some_table k
                """;
    }
}
