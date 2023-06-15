package inter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import lexer.Lexer;
public class Node {
    int lexline = 0;
    private static final String filePath="src/out/midCodeOut.txt";

    Node() {
        lexline = Lexer.line;
    }

    void error(String s) {
        String filePath = "src/out/ParserOut.txt";
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("near line " + Lexer.line + ": " + s+"\n");
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
    }

    public static int labels = 0;
    public int newlabel() {
        return ++labels;
    }//标签计数+1

    public void emitlabel(int i) {//打印标签
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("L" + i + ":");
           // System.out.println("内容已成功写入文件：" + filePath);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        System.out.print("L" + i + ":");
    }

    public void emit(String s) {//打印字符串
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("    " + s+'\n');
            // System.out.println("内容已成功写入文件：" + filePath);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        System.out.println("    " + s);
    }
}
