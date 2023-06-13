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
    private static String filePath="src/out/midCodeOut.txt";

    Node() {
        lexline = Lexer.line;
    }

    void error(String s) {
        throw new Error("near line " + lexline + ": " + s);
    }

    static int labels = 0;

    public int newlabel() {
        return ++labels;
    }

    public void emitlabel(int i) {
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("L" + i + ":");
           // System.out.println("内容已成功写入文件：" + filePath);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        System.out.print("L" + i + ":");
    }

    public void emit(String s) {
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(" " + s+"\n");
            // System.out.println("内容已成功写入文件：" + filePath);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        System.out.println("\t" + s);
    }
}
