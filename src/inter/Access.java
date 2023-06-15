package inter;

import lexer.Tag;
import lexer.Word;
import symbols.Type;


public class Access extends Op {
    public Id array;
    public Expr index;

    public Access(Id a, Expr i, Type p) {
        super(new Word("[]", Tag.INDEX), p);
        array = a;
        index = i;
    }

    public Expr gen() {
        return new Access(array, index.reduce(), type);
    }//生成普通代码

    public void jumping(int t, int f) {
        emitjumps(reduce().toString(), t, f);
    }//生成跳转代码，将数组访问规约成一个临时变量，然后调用emitjumps

    public String toString() {
        return array.toString() + " [ " + index.toString() + " ]";
    }
}
