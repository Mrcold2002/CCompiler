package lexer;

//Token父类
/*子类
    num:整数 Tag.NUM,val
    Real：实数 Tag.REAL,val
    Word：单词标记 Tag未知,string
 */

public class Token {
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return String.valueOf((char) tag);
    }
}
