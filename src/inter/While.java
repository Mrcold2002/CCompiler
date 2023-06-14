package inter;

import symbols.Type;
//while节点拥有一个表达式和一个语句
public class While extends Stmt {
    Expr expr;
    Stmt stmt;

    public While() {
        expr = null;
        stmt = null;
    }

    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool) {//当表达式不是bool型
            expr.error("boolean required in while");
        }
    }

    public void gen(int b, int a) {//while语句生成代码，b为开始标签，a这条语句之后的标签
        begin = b;
        after = a;
        expr.jumping(0, a);//依据expr的判断跳到a
        int label = newlabel();
        emitlabel(label);//使用新的一个标签
        stmt.gen(label, b);//打印中间的语句
        emit("goto L" + b);//跳到起始的判断语句
        /*
            Lb: if !expr goto La
                Stmts;
                goyo Lb
            La
         */
    }
}
