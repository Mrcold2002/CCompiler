package inter;

public class Break extends Stmt {
    Stmt stmt;

    public Break() {
        if (Stmt.Enclosing == Stmt.Null) error("unenclosed break");//如果该语句不是循环结构
        stmt = Stmt.Enclosing;//将当前循环的语句赋给Break类中，方便找到Break后要跳到的地方
    }

    public void gen(int b, int a) {//Break后，要输出到当前语句的后面第一条语句
        emit("goto L" + stmt.after);
    }
}
