package inter;

import lexer.Token;
import symbols.Type;
//表达式类
public class Expr extends Node {
    public Token op;//节点上的运算符
    public Type type;//类型

    Expr(Token tok, Type p) {
        op = tok;
        type = p;
    }

    public Expr gen() {
        return this;
    }//返回一个项，作为一个三地址指令的右部

    public Expr reduce() {
        return this;
    }//将一个表达式归约成一个单一地址

    public void jumping(int t, int f) {
        /*
            t=0，当该Expr语句不成立时跳转到Lf
         */
        emitjumps(toString(), t, f);
    }

    public void emitjumps(String test, int t, int f) {
        if (t != 0 && f != 0) {//当true和false都有出口时
            emit("if " + test + " goto L" + t);
            emit("goto L" + f);
        } else if (t != 0) {//只有t有出口
            emit("if " + test + " goto L" + t);
        } else if (f != 0) {//只有f有出口
            emit("if not " + test + " goto L" + f);
        }  ;
    }

    public String toString() {
        return op.toString();
    }
}
