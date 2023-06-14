package UI;

import lexer.Lexer;
import parser.Parser;
import inter.Node;

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
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class CCompilerClient {
    private JFrame frame;
    private JTextArea codeTextArea;
    private JTextArea lineNumberTextArea;
    private JTextArea lexerOutputTextArea;
    private JTextArea parserOutputTextArea;
    private JTextArea midCodeOutputTextArea;
    private String midCodeOutPath = "src/out/midCodeOut.txt";
    private String lexerOutPath = "src/out/LexerOut.txt";
    private String parserOutPath = "src/out/ParserOut.txt";

    public CCompilerClient() {
        // 创建 JFrame 实例
        frame = new JFrame("C语言编译器客户端");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // 创建面板
        JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));

        // 创建左侧输入区域
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel inputLabel = new JLabel("in", SwingConstants.CENTER);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        codeTextArea = new JTextArea();
        codeTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        codeTextArea.getDocument().addDocumentListener(new LineNumberUpdater());
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        inputPanel.add(codeScrollPane, BorderLayout.CENTER);
        lineNumberTextArea = new JTextArea("1");
        lineNumberTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        lineNumberTextArea.setBackground(frame.getBackground());
        lineNumberTextArea.setEditable(false);
        JScrollPane lineNumberScrollPane = new JScrollPane(lineNumberTextArea);
        lineNumberScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        lineNumberScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        lineNumberScrollPane.setBorder(null);
        inputPanel.add(lineNumberScrollPane, BorderLayout.WEST);
        panel.add(inputPanel);

        // 创建右侧输出区域
        JPanel outputPanel = new JPanel(new GridLayout(3, 1, 5, 5));

        // 创建词法分析输出区域
        JPanel lexerOutputPanel = new JPanel(new BorderLayout());
        JLabel lexerOutputLabel = new JLabel("lexerOut", SwingConstants.CENTER);
        lexerOutputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lexerOutputPanel.add(lexerOutputLabel, BorderLayout.NORTH);
        lexerOutputTextArea = new JTextArea();
        lexerOutputTextArea.setEditable(false);
        JScrollPane lexerOutputScrollPane = new JScrollPane(lexerOutputTextArea);
        lexerOutputPanel.add(lexerOutputScrollPane, BorderLayout.CENTER);
        outputPanel.add(lexerOutputPanel);

        // 创建语法分析输出区域
        JPanel parserOutputPanel = new JPanel(new BorderLayout());
        JLabel parserOutputLabel = new JLabel("parserOut", SwingConstants.CENTER);
        parserOutputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        parserOutputPanel.add(parserOutputLabel, BorderLayout.NORTH);
        parserOutputTextArea = new JTextArea();
        parserOutputTextArea.setEditable(false);
        JScrollPane parserOutputScrollPane = new JScrollPane(parserOutputTextArea);
        parserOutputPanel.add(parserOutputScrollPane, BorderLayout.CENTER);
        outputPanel.add(parserOutputPanel);

        // 创建中间代码输出区域
        JPanel midCodeOutputPanel = new JPanel(new BorderLayout());
        JLabel midCodeOutputLabel = new JLabel("midCodeOut", SwingConstants.CENTER);
        midCodeOutputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        midCodeOutputPanel.add(midCodeOutputLabel, BorderLayout.NORTH);
        midCodeOutputTextArea = new JTextArea();
        midCodeOutputTextArea.setEditable(false);
        JScrollPane midCodeOutputScrollPane = new JScrollPane(midCodeOutputTextArea);
        midCodeOutputPanel.add(midCodeOutputScrollPane, BorderLayout.CENTER);
        outputPanel.add(midCodeOutputPanel);

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

    // 清空输出文件内容
    private void clearOut(String filePath) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            bufferedWriter.write("");
            System.out.println("文件内容已成功清空：" + filePath);
        } catch (IOException e) {
            System.out.println("清空文件内容时发生错误：" + e.getMessage());
        }
    }

    // 运行按钮绑定后的函数
    private void compileAndExecute(String code) throws IOException {
        //清空Node中的静态变量
        Node.labels=0;
        // 清空输出文件内容
        clearOut(midCodeOutPath);
        clearOut(lexerOutPath);
        clearOut(parserOutPath);
        //清空界面输出
        lexerOutputTextArea.setText("");
        parserOutputTextArea.setText("");
        midCodeOutputTextArea.setText("");
        // 开始运行
        Lexer lex = new Lexer(code + "$");
        Parser parse = new Parser(lex,this);
        parse.program();
        outPut();
    }
    public void outPut() throws IOException {
        // 设置语法分析输出
        String parserFileContent = readFileContent(parserOutPath);
        parserOutputTextArea.setText(parserFileContent);
        if(!parserFileContent.isEmpty()) return;
        else {parserOutputTextArea.setText("Compile successfully");}

        // 设置词法分析输出
        String lexerFileContent = readFileContent(lexerOutPath);
        lexerOutputTextArea.setText(lexerFileContent);

        // 设置中间代码输出
        String midCodeFileContent = readFileContent(midCodeOutPath);
        midCodeOutputTextArea.setText(midCodeFileContent);
    }
    // 读取文件内容
    private String readFileContent(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8)
                .forEach(line -> contentBuilder.append(line).append("\n"));
        return contentBuilder.toString();
    }

    // 更新行号显示
    private class LineNumberUpdater implements javax.swing.event.DocumentListener {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            updateLineNumbers();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            updateLineNumbers();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            updateLineNumbers();
        }

        private void updateLineNumbers() {
            String text = codeTextArea.getText();
            int caretPosition = codeTextArea.getDocument().getLength();
            Element root = codeTextArea.getDocument().getDefaultRootElement();
            StringBuilder lineNumbersText = new StringBuilder();
            for (int i = 1; i <= root.getElementIndex(caretPosition) + 1; i++) {
                lineNumbersText.append(i).append("\n");
            }
            lineNumberTextArea.setText(lineNumbersText.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CCompilerClient();
            }
        });
    }
}
