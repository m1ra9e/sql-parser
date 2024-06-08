parser grammar SQLParser;

options {
    language = Java;
    tokenVocab=SQLLexer;
}

@header {package home.plugin.sqlparser.parser.antlr.generated;}

//// Attention! The order is important: the more general rule, should be placed lower.

// We process here only four cases: 
//   SELECT t.c1, ..., t.cn FROM schema.table t;
//   SELECT t.c1, ..., t.cn AS k FROM (select_expresseion) t;
//   SELECT t.c1, ..., (select_expresseion) FROM schema.table t;
//   SELECT t.c1, ..., (select_expresseion) AS k FROM (select_expresseion) t;

sql
    : query SEMI_COLON EOF
    ;

query
    : select_stmt
    ;

select_stmt
    : SELECT col (COMMA col)* FROM from_item (LIMIT INTEGER_NUMBER)?
    ;

col
    : (alias DOT)? col_name (AS alias)?
    | LEFT_PAREN select_stmt RIGHT_PAREN (AS alias)?
    ;

from_item
    : (table_name | (LEFT_PAREN select_stmt RIGHT_PAREN)) alias?
    ;

col_name
    : STRING
    ;

alias
    : STRING
    ;

table_name
    : (STRING DOT)? STRING
    ;
