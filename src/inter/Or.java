package inter;

import lexer.Token;

public class Or extends Logical {
    public Or(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public void jumping(int t, int f) {//生成bool表达式 B=B1||B2的代码
        int label = t != 0 ? t : newlabel();//如果B为真的出口t是特殊标号0，则需要新生成一个标号
        expr1.jumping(label, 0);//如果B1为真的话可以直接跳到label处
        expr2.jumping(t, f);//否则就跳到f标签，即失败处
        if (t == 0) emitlabel(label);//最后打印新标签
    }
}
