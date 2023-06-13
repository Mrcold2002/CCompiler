package symbols;

import lexer.Tag;
import lexer.Word;
/*
    种类标记：继承自标记
    与父类区别：增加一个位宽，其tag标识为Tag.BASIC
    int，float，char，bool：
 */
public class Type extends Word {
    public int width = 0;
    //构造函数
    public Type(String s, int tag, int w) {
        super(s, tag);
        width = w;
    }
    //确定一些Type型的参数
    public static final Type
            Int = new Type("int", Tag.BASIC, 4),
            Float = new Type("float", Tag.BASIC, 8),
            Char = new Type("char", Tag.BASIC, 1),
            Bool = new Type("bool", Tag.BASIC, 1);
    //判断是否为数字
    public static boolean numeric(Type p) {
        if (p == Type.Char || p == Type.Int || p == Type.Float) return true;
        else return false;
    }
    //不同Type间的比较，比较哪个Type大一些
    public static Type max(Type p1, Type p2) {
        if (!numeric(p1) || !numeric(p2)) return null;
        else if (p1 == Type.Float || p2 == Type.Float) return Type.Float;
        else if (p1 == Type.Int || p2 == Type.Int) return Type.Int;
        else return Type.Char;
    }
}
