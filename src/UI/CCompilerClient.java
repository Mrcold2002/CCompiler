package UI;

import lexer.Lexer;
import parser.Parser;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CCompilerClient {
    private JFrame frame;
    private JTextArea codeTextArea;
    private JTextArea outputTextArea;
    private JTextArea additionalOutputTextArea;

    public CCompilerClient() {
        // 创建 JFrame 实例
        frame = new JFrame("C语言编译器客户端");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // 创建面板
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));

        // 创建左侧输入区域
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel inputLabel = new JLabel("in", SwingConstants.CENTER);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        codeTextArea = new JTextArea();
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        inputPanel.add(codeScrollPane, BorderLayout.CENTER);
        panel.add(inputPanel);

        // 创建右侧输出区域
        JPanel outputPanel = new JPanel(new BorderLayout());
        JLabel outputLabel = new JLabel("out", SwingConstants.CENTER);
        outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        panel.add(outputPanel);

        // 将面板添加到窗口
        frame.add(panel);

        // 创建运行按钮
        JButton runButton = new JButton("运行");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String code = codeTextArea.getText();
                try {
                    compileAndExecute(code);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        frame.add(runButton, BorderLayout.SOUTH);

        // 设置窗口可见
        frame.setVisible(true);
    }

    private void compileAndExecute(String code) throws IOException {

        String filePath = "src/out/midCodeOut.txt"; // 替换为你的文本文件路径
        //清空输出
        try (FileWriter fileWriter = new FileWriter(filePath, false);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("");
            System.out.println("文件内容已成功清空：" + filePath);
        } catch (IOException e) {
            System.out.println("清空文件内容时发生错误：" + e.getMessage());
        }

        Lexer lex = new Lexer(code + "$");
        Parser parse = new Parser(lex);
        parse.program();


        // 读取文本文件内容

        String fileContent = readFileContent(filePath);

        // 设置outputTextArea的文本为文件内容
        outputTextArea.setText(fileContent);
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8).forEach(line -> contentBuilder.append(line).append("\n"));
        return contentBuilder.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CCompilerClient();
            }
        });
    }
}
