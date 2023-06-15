package inter;

import lexer.Token;
import symbols.Type;
//运算类
public class Op extends Expr {
    public Op(Token tok, Type p) {
        super(tok, p);
    }

    public Expr reduce() {//将一个表达式归约成一个单一地址
        Expr x = gen();//生成表达式的序列
        Temp t = new Temp(type);//使用一个新的变量
        emit(t.toString() + " = " + x.toString());//输出这个变量等于这个值
        return t;//范围临时变量
    }
}
