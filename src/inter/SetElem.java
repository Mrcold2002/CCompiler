package inter;

import symbols.Array;
import symbols.Type;
//数组元素赋值语句
public class SetElem extends Stmt {
    public Id array;//数组的标识符
    public Expr index;//数组索引表达式
    public Expr expr;//数组赋值表达式

    public SetElem(Access x, Expr y) {
        array = x.array;
        index = x.index;
        expr = y;
        if (check(x.type, y.type) == null) {
            error("type error");
        }
    }

    public Type check(Type p1, Type p2) {
        if (p1 instanceof Array || p2 instanceof Array) {
            return null;
        } else if (p1 == p2) {
            return p2;
        } else if (Type.numeric(p1) && Type.numeric(p2)) return p2;
        else return null;
    }

    public void gen(int b, int a) {
        String s1 = index.reduce().toString();
        String s2 = expr.reduce().toString();
        emit(array.toString() + " [ " + s1 + " ] = " + s2);
    }
}
