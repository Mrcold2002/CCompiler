package parser;
/*
    代码进行了语法分析，在分析完成后，通过调用包inter中的节点定义，构建语法树，形成语法制导翻译，生成中间代码
 */
import UI.CCompilerClient;
import inter.*;
import lexer.*;
import UI.CCompilerClient.*;
import symbols.Array;
import symbols.Env;
import symbols.Type;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Parser {
    private Lexer lex;//词法分析器
    private Token look;//当前看到的下一个token
    private CCompilerClient cCompilerClient;
    Env top ;//当前符号表环境
    int used ;//跟踪内存地址
    //构造函数
    public Parser(Lexer l,CCompilerClient c) throws IOException {
        lex = l;cCompilerClient=c;
        top=null;used=0;
        move();
        inter.Id.now=0;
        Temp.count=0;
    }
    //移动到下一个Token
    void move() throws IOException {
        look = lex.scan();
    }
    //语法错误行数
    void error(String s) throws IOException {
        String filePath = "src/out/ParserOut.txt";
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("near line " + Lexer.line + ": " + s+'\n');
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        cCompilerClient.outPut();
    }
    //将当前Token与期望Token进行比较
    void match(int t) throws IOException {
        if (look.tag == t) move();
        else error("syntax error");
    }
    //语法的起始符号，一个程序由代码块构成
    public void program() throws IOException {
        Stmt s = block();
        int begin = s.newlabel();//在中间代码的开头和末尾生成标签
        int after = s.newlabel();
        s.emitlabel(begin);//打印标签
        s.gen(begin, after);//生成代码
        s.emitlabel(after);//打印标签
    }
    //这个歌方法代表一个代码块
    Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;//创建一个新的符号表环境，随后调用stmts()方法解析语句序列。
        top = new Env(top);
        Stmt s = stmts();
        match('}');
        top = savedEnv;
        return s;
    }
    //处理变量声明语句
    void decls() throws IOException {
        while (look.tag == Tag.BASIC) {
            Type p = type();//处理声明基础类型
            while (true) {
                Token tok = look;
                match(Tag.ID);//匹配一个标识符
                Id id = null;
                try {
                    id = new Id((Word) tok, p, used);//一个Token对应了一个标识符，标识符具有token,类型，内存位置
                }catch (Exception e){
                    error("wrong define");
                }
                if (top.table.get(tok) == null)//如果当前标识符没有被使用过
                    top.put(tok, id);//放入当前符号表
                else
                    error("redefine of id " + tok.toString());//否则报错重定义
                used = used + p.width;//used变量跟踪变量内存地址
                if (look.tag == ';') {//如果匹配到 ';' 那么语句结束
                    match(';');
                    break;
                } else if (look.tag == '=') {//如果匹配到 = ,则直接进行赋值
                    error("can't assign a value directly for now");
                    match('=');

                } else {//如果匹配到 ','，则继续定义变量
                    match(',');
                }
            }
        }
    }
    //处理变量声明语句的类型
    Type type() throws IOException {
        Type p = (Type) look;
        match(Tag.BASIC);
        if (look.tag != '[') return p;
        else return dims(p);
    }
    //处理变量声明语句中可能出现的维度
    Type dims(Type p) throws IOException {
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']');
        if (look.tag == '[') {
            p = dims(p);
        }
        return new Array((((Num) tok).value), p);
    }
    //处理语句序列
    Stmt stmts() throws IOException {
        if (look.tag == Tag.BASIC) decls();//变量声明语句
        if (look.tag == '}') return Stmt.Null;//空语句
        else return new Seq(stmt(), stmts());//语句序列
    }
    //处理单个语句
    Stmt stmt() throws IOException {
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt;
        switch (look.tag) {//根据下一个字符的种类
            case ';'://若为‘；’则为空语句
                move();
                return Stmt.Null;
            case Tag.IF://if语句的形式
                match(Tag.IF);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                if (look.tag != Tag.ELSE) return new If(x, s1);
                match(Tag.ELSE);
                s2 = stmt();
                return new Else(x, s1, s2);
            case Tag.WHILE://while语句的形式
                While whilenode = new While();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode;
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                s1 = stmt();
                whilenode.init(x, s1);
                Stmt.Enclosing = savedStmt;
                return whilenode;
            case Tag.DO://dowhile语句格式
                Do donode = new Do();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = donode;
                match(Tag.DO);
                s1 = stmt();
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')');
                match(';');
                donode.init(s1, x);
                Stmt.Enclosing = savedStmt;
                return donode;
            case Tag.BREAK://break语句
                match(Tag.BREAK);
                Stmt tmpBreak = new Break();
                match(';');
                return tmpBreak;
            case Tag.CONTINUE://continue语句
                match(Tag.CONTINUE);
                Stmt tmpContinue = new Continue();
                match(';');
                return tmpContinue;
            case '{'://新的代码块
                return block();
            default://赋值语句
                return assign();
        }
    }
    //处理赋值语句
    Stmt assign() throws IOException {
        Stmt stmt;
        Token t = look;
        match(Tag.ID);//匹配该Token为一个标记符
        Id id = top.get(t);//获得标记符对象
        if (id == null) error(t.toString() + " undeclared");//标记未定义过
        if (look.tag == '=') {//为等号则为直接复制
            move();
            stmt = new Set(id, bool());
        } else {//给数组某一元素赋值的情况
            Access x = offset(id);
            match('=');
            stmt = new SetElem(x, bool());
        }
        match(';');
        return stmt;
    }
    //处理布尔表达式，优先处理||操作，因其运算等级较低
    Expr bool() throws IOException {
        Expr x = join();
        while (look.tag == Tag.OR) {//如果存在 || 则继续的到下一个式子
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }
        return x;
    }
    //处理布尔表达式&&操作
    Expr join() throws IOException {
        Expr x = equality();
        while (look.tag == Tag.AND) {//如果存在&& 继续到下一个式子
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }
        return x;
    }
    //处理布尔表达式中 相等性比较表达式
    Expr equality() throws IOException {
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE) {//如果存在!= 或 ==  继续到下一个式子
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }
        return x;
    }
    //处理布尔表达式中的关系比较表达式
    Expr rel() throws IOException {
        Expr x = expr();//算数表达式
        switch (look.tag) {//对于 < <= > >= 四种情况，需要得到另一个算术表达式
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                return new Rel(tok, x, expr());
            default:
                return x;
        }
    }
    //处理算数表达式
    Expr expr() throws IOException {
        Expr x = term();
        while (look.tag == '+' || look.tag == '-') {
            Token tok = look;
            move();
            x = new Arith(tok, x, term());
        }
        return x;
    }
    //处理乘法表达式
    Expr term() throws IOException {
        Expr x = unary();
        while (look.tag == '*' || look.tag == '/') {
            Token tok = look;
            move();
            x = new Arith(tok, x, unary());
        }
        return x;
    }
    //处理一元表达式，如-，！
    Expr unary() throws IOException {
        if (look.tag == '-') {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!') {
            Token tok = look;
            move();
            return new Not(tok, unary());
        } else return factor();
    }
    //处理因子表达式，如括号括起来的表达式，数字常量，布尔常量，变量标识符等
    Expr factor() throws IOException {
        Expr x = null;
        switch (look.tag) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);
                if (id == null) error(look.toString() + " undeclared");
                move();
                if (look.tag != '[') return id;
                else return offset(id);
            default:
                error("syntax error");
                return x;
        }
    }
    //用来处理数组元素的偏离
    Access offset(Id a) throws IOException {
        Expr i;
        Expr w;
        Expr t1, t2;
        Expr loc;
        Type type = a.type;
        match('[');
        i = bool();//匹配一个表达式
        match(']');
        try {
            type = ((Array) type).of;
        } catch (Exception e) {
            error("this object doesn't have so many dimensions");
        }
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);//得到第一个的相对位移，i*w
        loc = t1;
        while (look.tag == '[') {//如果是多维数组的话，继续迭代
            match('[');
            i = bool();
            match(']');
            try {
                type = ((Array) type).of;
            } catch (Exception e) {
                error("this object doesn't have so many dimensions");
            }
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);//t1=i*w
            t2 = new Arith(new Token('+'), loc, t1);//t2=t1+loc
            loc = t2;
        }
        return new Access(a, loc, type);//返回标识符名字，相对偏移的计算式子，标识符的基本类型
    }
}
