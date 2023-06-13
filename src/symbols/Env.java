package symbols;

import inter.Id;
import lexer.Token;

import java.util.Hashtable;
/*
    符号表环境:每个代码块里面一个符号表，记录标记对应的标识符
 */
public class Env {
    public Hashtable table;
    protected Env prev;//上一个符号表环境

    public Env(Env n) {
        table = new Hashtable();
        prev = n;
    }

    public void put(Token w, Id i) {
        table.put(w, i);
    }
    //通过Hashtable逐级向上查找标记对应的标识符
    public Id get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.table.get(w));
            if (found != null) return found;
        }
        return null;
    }
}
