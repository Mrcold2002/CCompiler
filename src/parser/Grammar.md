## 文法详情

program -> block

block -> '{' stmts '}'

decls -> ( type ID ( ',' ID ) * ';' ) *

type -> BASIC dims

dims -> '[' NUM ']' ( dims | ε )

stmts -> decls | stmt stmts | ε

stmt -> ';' | if_stmt| while_stmt| do_while_stmt| break_stmt| continue_stmt| block| assign_stmt

if_stmt -> IF '(' bool ')' stmt (ELSE stmt | ε )

while_stmt -> WHILE '(' bool ')' stmt

do_while_stmt -> DO stmt WHILE '(' bool ')' ';'

break_stmt -> BREAK ';'

continue_stmt -> CONTINUE ';'

assign_stmt -> ID '=' bool ';'

bool -> join (OR join ) *

join -> equality ( AND equality) *

equality -> rel ((EQ|NE) rel)*

rel -> expr ( ( LE | GE | '<' | '>' ) rel | ε )

expr -> term ( ( '+' | '-' ) term ) *

term -> unary ( ( '*' | '/' ) unary ) *

unary -> '-' unary | '!' unary  | factor

factor -> '(' bool ')' | NUM | REAL | TRUE | FALSE | ID ( offset | ε )

offset -> '[' bool ']' ( '[' bool ']'  ) *