lexer grammar SQLLexer;

options {
    language = Java;
}

@header {package home.plugin.sqlparser.parser.antlr.generated;}

//// Attention! The order is important: the more general rule, should be placed lower.

// basic keywords
AS : A S;
FROM : F R O M;
LIMIT : L I M I T; 
SELECT : S E L E C T;

// punctuation marks and operations
SEMI_COLON : ';' ;
COMMA : ',' ;
DOT : '.' ;
LEFT_PAREN : '(' ;
RIGHT_PAREN : ')' ;

// custom string
STRING
    : [0-9]*[a-zA-Z_$][a-zA-Z_$0-9]*
    ;

// custom digits
INTEGER_NUMBER
    : DIGIT+
    ;

// fragments
fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];
fragment DIGIT : ('0'..'9');

// skipped elements
SPACE:          [ \t\r\n]+    -> channel(HIDDEN);
COMMENT_INPUT:  '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT:   ('-- ' | '#')
                ~[\r\n]*
                ('\r'? '\n' | EOF)
                              -> channel(HIDDEN);
