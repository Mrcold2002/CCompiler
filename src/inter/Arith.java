package inter;

import lexer.Token;
import symbols.Type;
//实现双面运算符 + - * /
public class Arith extends Op {
    public Expr expr1, expr2;

    public Arith(Token tok, Expr x1, Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type, expr2.type);
        if (type == null) error("type error");//检查是否可以转换为同一类型的数
    }

    public Expr gen() {
        return new Arith(op, expr1.reduce(), expr2.reduce());
    }////将一个表达式归约成一个单一地址
    //如a+b*c,调用 a.reduce 和 (b*c).reduce ，会得到a和t=b*c，随后得到 a+t,依次生成

    public String toString() {
        return expr1.toString() + " " + op.toString() + " " + expr2.toString();
    }
}
