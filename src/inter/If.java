package inter;

import symbols.Type;

public class If extends Stmt {
    Expr expr;
    Stmt stmt;

    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) {//如果if表达式不是bool类型的，进行报错
            expr.error("boolean required in if");
        }
    }

    public void gen(int b, int a) {//代码生成
        int label = newlabel();//新建一个标签
        expr.jumping(0, a);//如果条件不满足跳到La标签，满足则继续
        emitlabel(label);//打印一个标签，即if语句的起始标签
        stmt.gen(label, a);//打印语句
        /*
            Lb:if not expr goto La
               stmt
            La:
         */
    }
}
