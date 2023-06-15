package inter;

import lexer.Token;

public class And extends Logical {
    public And(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    public void jumping(int t, int f) {
        int label = f != 0 ? f : newlabel();//防止失败位置标签为0
        expr1.jumping(0, label);//当B1为假的时候，直接跳到失败处
        expr2.jumping(t, f);//当B2为真的话跳到t处，否则到f处
        if (f == 0) emitlabel(label);
    }
}
