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
package home.plugin.sqlparser.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import home.plugin.sqlparser.parser.antlr.generated.SQLLexer;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser.AliasContext;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser.ColContext;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser.From_itemContext;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser.Select_stmtContext;
import home.plugin.sqlparser.parser.antlr.generated.SQLParser.SqlContext;
import home.plugin.sqlparser.parser.model.SqlNode;
import home.plugin.sqlparser.parser.model.SqlNodeType;

public final class SqlFileParser {

    public static SqlNode parse(Path file) {
        SqlContext sqlCtx = parseToRuleCtx(file);
        Select_stmtContext selectStmtCtx = sqlCtx.query().select_stmt();
        return createAndFillSqlNode(selectStmtCtx);
    }

    private static SqlContext parseToRuleCtx(Path file) {
        try (InputStream inputStream = Files.newInputStream(file)) {
            var lexer = new SQLLexer(CharStreams.fromStream(inputStream));
            var parser = new SQLParser(new CommonTokenStream(lexer));
            return parser.sql();
        } catch (IOException e) {
            throw new IllegalStateException("File read exeption: " + file, e);
        }
    }

    private static SqlNode createAndFillSqlNode(Select_stmtContext selectStmtCtx) {
        SqlNode root = SqlNode.createRootNode();
        processCtxAndAddResultToParent(selectStmtCtx, root);
        return root;
    }

    private static void processCtxAndAddResultToParent(Select_stmtContext selectStmtCtx, SqlNode parent) {
        for (ColContext colCtx : selectStmtCtx.col()) {
            if (colCtx.select_stmt() != null) {
                SqlNode colSelNode = SqlNode.create(parent, getNodeName(colCtx), SqlNodeType.SELECT_IN_COLUMN);
                Select_stmtContext colSelStmtCtx = colCtx.select_stmt();
                processCtxAndAddResultToParent(colSelStmtCtx, colSelNode);
            } else {
                SqlNode.create(parent, getNodeName(colCtx), SqlNodeType.COLUMN);
            }
        }

        From_itemContext fromItemCtx = selectStmtCtx.from_item();
        if (fromItemCtx.select_stmt() != null) {
            SqlNode fromSelNode = SqlNode.create(parent, getNodeName(fromItemCtx), SqlNodeType.SELECT_IN_FROM);
            Select_stmtContext subSelectCtx = fromItemCtx.select_stmt();
            processCtxAndAddResultToParent(subSelectCtx, fromSelNode);
        } else if (fromItemCtx.table_name() != null) {
            SqlNode.create(parent, getNodeName(fromItemCtx), SqlNodeType.TABLE_IN_FROM);
        }
    }

    private static String getNodeName(ParserRuleContext ctx) {
        if (ctx instanceof ColContext colCtx) {
            if (colCtx.select_stmt() != null) {
                return "column_as_select : ["
                        + (colCtx.AS() != null ? colCtx.alias(0).getText() : "no_alias")
                        + ']';
            }

            List<AliasContext> aliases = colCtx.alias();
            var sb = new StringBuilder();
            if (colCtx.DOT() != null) {
                sb.append(aliases.get(0).getText()).append('.');
            }
            sb.append(colCtx.col_name().getText());
            if (colCtx.AS() != null) {
                sb.append(" AS ").append(
                        aliases.size() == 2
                                ? aliases.get(1).getText()
                                : aliases.get(0).getText());
            }
            return sb.toString();
        } else if (ctx instanceof From_itemContext fromItemCtx) {
            if (fromItemCtx.select_stmt() != null) {
                return "from_as_select : ["
                        + (fromItemCtx.alias() != null ? fromItemCtx.alias().getText() : "no_alias")
                        + ']';
            } else if (fromItemCtx.table_name() != null) {
                return fromItemCtx.table_name().getText()
                        + (fromItemCtx.alias() != null ? " " + fromItemCtx.alias().getText() : "");
            }
        }

        throw new IllegalArgumentException(
                "Node name getting error, because of unsupported context type : "
                        + ctx.getClass().getSimpleName());
    }

    private SqlFileParser() {
    }
}
