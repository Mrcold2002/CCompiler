package inter;

import lexer.Num;
import lexer.Token;
import lexer.Word;
import symbols.Type;
//常量
public class Constant extends Expr {
    public Constant(Token tok, Type p) {
        super(tok, p);
    }

    public Constant(int i) {
        super(new Num(i), Type.Int);
    }

    public static final Constant
            True = new Constant(Word.Ture, Type.Bool),
            False = new Constant(Word.False, Type.Bool);

    public void jumping(int t, int f) {
        if (this == True && t != 0) {//this为真且t不为0，跳转到t
            emit("goto L" + t);
        } else if (this == False && f != 0) {//this为假且f不为0，跳到f
            emit("goto L" + f);
        }
    }
}
