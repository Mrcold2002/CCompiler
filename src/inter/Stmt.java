package inter;

public class Stmt extends Node {
    public Stmt() {
    }

    public static Stmt Null = new Stmt();

    public void gen(int b, int a) {//生成代码
    }

    int after = 0, begin = 0;
    public static Stmt Enclosing = Stmt.Null;//标记该语句中是否有循环结构
}
