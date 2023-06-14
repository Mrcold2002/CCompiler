package inter;

public class Continue extends Stmt {
    Stmt stmt;

    public Continue() {
        if (Stmt.Enclosing == Stmt.Null) {//如果该语句不是循环结构
            error("unenclosed continue");
        }
        stmt = Stmt.Enclosing;//将当前循环的语句赋给Continue类中，方便找到continue后要跳到的地方
    }

    public void gen(int b, int a) {
        emit("goto L" + stmt.begin);
    }//continue即返回到这句话的初始标签
    // goto L(stmt.begin)
}
