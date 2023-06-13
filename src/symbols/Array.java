package symbols;

import lexer.Tag;
/*
    数组标识符，从种类标记扩展
 */
public class Array extends Type {
    public Type of;
    public int size = 1;
    //构造函数，传入大小和种类标记
    public Array(int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width);
        size = sz;
        of = p;
    }

    public String toString() {
        return "[" + size + "]" + of.toString();
    }
}
