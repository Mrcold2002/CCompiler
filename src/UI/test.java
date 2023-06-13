package UI;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class test {
    private static String readFileContent(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(line -> contentBuilder.append(line).append("\n"));
        return contentBuilder.toString();
    }
    public static void main(String[] args) throws IOException {
        String filePath = "src/out/midCodeOut.txt"; // 替换为你的文本文件路径
        String fileContent = readFileContent(filePath);
        System.out.println(fileContent);
    }
}
