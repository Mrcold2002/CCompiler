## 文法详情
1. program -> block_outside_loop 程序由一个循环外的代码块组成
2. block_outside_loop -> '{' stmts_outside_loop '}' 循环外的代码块由一对大括号包围，其中包括和循环外的语句序列（stmts）。
3. block -> '{' stmts '}' 代码块由一对大括号包围，其中包括和语句序列（stmts）。
4. stmts_outside_loop ->  stmt_outside_loop (stmts_outside_loop)? 循环外的语句序列为单个循环外的语句，或单个循环外的语句跟随循环外的语句序列
5. stmts ->  stmt (stmts)? 语句序列为单个语句，或单个语句跟随语句序列
6. stmt_outside_loop ->  decl| if_stmt_outside_loop | while_stmt | do_while_stmt
   | block_outside_loop | assign_stmt
7. stmt ->  decl| if_stmt | while_stmt | do_while_stmt
   | break_stmt | continue_stmt | block | assign_stmt
8. decl ->  type ID (',' ID)* ';' 一种类型的变量可以通过,隔开多次声明
9. type -> BASIC (dims)? 变量类型为基础类型，同时可以增加维度变成数组
10. dims -> '[' NUM ']' (dims)? 维度可以叠加
11. if_stmt_outside_loop -> if '(' bool ')' stmt_outside_loop (else stmt_outside_loop)?
12. if_stmt -> if '(' bool ')' stmt (else stmt)?
13. while_stmt -> while '(' bool ')' stmt
14. do_while_stmt -> do stmt while '(' bool ')' ';'
15. break_stmt -> BREAK ';'
16. continue_stmt -> CONTINUE ';'
17. assign_stmt -> ID 'o=' bool ';' | ID offset ';'前者为直接给某一标识符赋值，后者通过数组赋值
18. bool -> equality [ '&&' join]
19. equality -> rel [|| equality]
20. rel -> expr (('!='|'==') expr )?
21. expr -> term (('+'|'-') term)?
22. term -> unary (('*'|'/') unary)?
23. unary -> '-' unary | '!' unary | factor
24. factor -> '(' bool ')'| NUM | REAL | TRUE | FALSE | ID(offest)?
25. offest -> '[' NUM ']' offest?