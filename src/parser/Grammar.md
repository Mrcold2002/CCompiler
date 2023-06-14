## 文法详情
1. program -> block 程序由一个代码块组成
2. block -> '{' stmts '}' 代码块由一对大括号包围，其中包括和语句序列（stmts）。
3. stmts -> decls stmts| e | stmt stmts 语句序列可以包括变量声明（decls），或者单个语句，或者为空
4. decls -> decl decls | e 变量声明语句可以连续声明
5. decl ->  type ID (',' ID)* ';' 一种类型的变量可以通过,隔开多次声明
5. type -> BASIC (dims)? 变量类型为基础类型，同时可以增加维度编程数组
6. dims -> '[' NUM ']' (dims)? 维度可以叠加
7. stmt -> ; | if_stmt | while_stmt | do_while_stmt 
             | break_stmt | continue_stmt | block | assign_stmt
8. if_stmt -> if '(' bool ')' stmt (else stmt)
9. while_stmt -> while '(' bool ')' stmt
10. do_while_stmt -> do stmt while '(' bool ')' ';'
11. break_stmt -> BREAK ';'
12. continue_stmt -> CONTINUE ';'
13. assign_stmt -> ID 'o=' bool ';' | ID offset ';'//前者为直接给某一标识符赋值，后者通过数组赋值
14. bool -> equality [ '&&' join]
15. equality -> rel [|| equality]
16. rel -> expr [('!='|'==') expr ]
17. expr -> term [('+'|'-') term]
18. term -> unary [('*'|'/') unary]
19. unary -> '-' unary | '!' unary | factor
20. factor -> '(' bool ')'| NUM | REAL | TRUE | FALSE | ID(offest)?
21. offest -> '[' NUM ']' offest?