## 文法详情
1. program -> block 程序由一个代码块组成
2. block -> '{' decls stmts '}' 代码块由一对大括号包围，其中包括变量声明（decls）和语句序列（stmts）。
3. decls -> decl decls | ε 变量声明可以是多个连续的声明（decl），或者可以为空。
4. decl -> type idlist ';' 变量声明由类型（type）和一个或多个标识符（idlist）组成，并以分号结尾。
5. type -> basic dims 类型由基本类型（basic）和可能的数组维度（dims）组成。
6. dims -> '[' num ']' dims | ε 数组维度由一对方括号和一个数字（num）组成，可以有多个连续的维度，或者可以为空。
7. idlist -> id | idlist ',' id 标识符列表由一个或多个标识符（id）组成，用逗号分隔。
8. stmts -> stmt stmts | ε 语句序列可以是多个连续的语句（stmt），或者可以为空。
9. stmt -> expr ';' | if '(' bool ')' stmt | if '(' bool ')' stmt else stmt |
        while '(' bool ')' stmt | do stmt while '(' bool ')' ';' |
        '{' stmts '}' 
语句可以是表达式语句（expr ';'），if语句（包括可选的else分支），while循环语句，do-while循环语句，代码块（'{' stmts '}'），或其他类型的语句。
10. expr -> id '=' bool | bool
11. bool -> bool || join | join
12. join -> join && equality | equality
13. equality -> equality == rel | equality != rel | rel
14. rel -> expr < expr | expr <= expr | expr >= expr | expr > expr | expr
15. expr -> expr + term | expr - term | term
16. term -> term * unary | term / unary | unary
17. unary -> -unary | !unary | factor
18. factor -> (bool) | id | id offset | num | real | true | false
19. offset -> '[' bool ']' offset | ε
