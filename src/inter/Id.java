package inter;

import lexer.Word;
import symbols.Type;

public class Id extends Expr {//标识符类
    public int offset;//相对地址
    public int key;
    public static int now;//静态类型实现标识符计数

    public Id(Word id, Type p, int b) {
        super(id, p);
        offset = b;
        key = now++;
    }

    public String toString() {
        return ((Word) op).lexeme ;//+ "(" + key + ")";
    }
}
