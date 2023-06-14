package lexer;

import symbols.Type;
import java.io.IOException;
import java.util.Hashtable;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
public class Lexer {
    public static int line = 1;//当前行数
    public String Code;//输入代码字符串
    public int now;//当前读入代码位置
    char peek = ' ';//当前读入位置
    Hashtable words = new Hashtable();
    Hashtable keywords = new Hashtable();//关键字 的对应关系表，防止误用关键字

    void reserve(Word w) {words.put(w.lexeme, w);}//保存字符标记中字符串和tag的对应关系
    public void lexerPrint(String output) {//将词法分析器的结果导入到相应文件中
        String filePath = "src/out/LexerOut.txt";
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
             bufferedWriter.write(output);
        } catch (IOException e) {
            System.out.println("写入文件时发生错误：" + e.getMessage());
        }
        System.out.print(output);
    }
    //初始化单词的tag
    public Lexer(String code) {
        reserve(new Word("if", Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));
        reserve(new Word("continue", Tag.CONTINUE));
        reserve(Word.Ture);
        reserve(Word.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
        keywords=words;
        now=0;
        line=0;
        Code=code;
    }
    //从传入代码中持续读入字符
    void readch() throws IOException {peek = Code.charAt(now);now++;}

    boolean readch(char c) throws IOException {
        readch();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }
    //词法分析器核心方法，扫描输入代码进行词法分析，根据字符的类型生成对应的标记。
    public Token scan() throws IOException {
        //忽略开头空格和水平制表符Tab('\t')，当遇到换行时注意行号加一
        for (; ; readch()) {
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line = line + 1;
            else break;
        }
        /*
        判断是否为: &,&&,|,||,=,==,!,!=,<,<=,>,>=
         */
        switch (peek) {
            case '&':
                if (readch('&')) 
                {
                    lexerPrint("operator: &&\n");
                    return Word.and;
                }
                else 
                {
                    lexerPrint("operator: &\n");
                    return new Token('&');
                }
            case '|':
                if (readch('|')) 
                {
                    lexerPrint("operator: ||\n");
                    return Word.or;
                }
                else 
                {
                    lexerPrint("operator: |\n");
                    return new Token('|');
                }
            case '=':
                if (readch('=')) 
                {
                    lexerPrint("operator: ==\n");
                    return Word.eq;
                }
                else 
                {
                    lexerPrint("operator: =\n");
                    return new Token('=');
                }
            case '!':
                if (readch('=')) 
                {
                    lexerPrint("operator: !=\n");
                    return Word.ne;
                }
                else 
                {
                    lexerPrint("operator: !\n");
                    return new Token('!');
                }
            case '<':
                if (readch('=')) 
                {
                    lexerPrint("operator: <=\n");
                    return Word.le;
                }
                else 
                {
                    lexerPrint("operator: <\n");
                    return new Token('<');
                }
            case '>':
                if (readch('=')) 
                {
                    lexerPrint("operator: >=\n");
                    return Word.ge;
                }
                else 
                {
                    lexerPrint("operator: >\n");
                    return new Token('>');
                }
        }
        //如果开头为数字或者. 则表示为一个常量，NUM或FLOAT
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));
            if (peek != '.')//此时可以得到是一个整数
            {
                lexerPrint("const value: "+Integer.toString(v)+"\n");
                return new Num(v);
            }
            //记录小数部分
            float x = v, d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            lexerPrint("constant: "+Float.toString(x)+"\n");
            return new Real(x);
        }
        //当前词为字母或者_开头时，可能为变量
        if (Character.isLetter(peek) || peek == '_') {
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek) || peek == '_');
            String s = b.toString();
            Word w = (Word) words.get(s);//查看当前变量命是否已经出现过
            if (w != null) {//出现过时进一步判断是不是关键字
                Word keyword=(Word) keywords.get(s);
                if(keyword!=null)lexerPrint("key word: "+s+"\n");
                else lexerPrint("identifier: "+s+"\n");
                return w;
            }
            //没出现过直接设置为标识符
            w = new Word(s, Tag.ID);
            words.put(s, w);
            lexerPrint("identifier: "+s+"\n");
            return w;
        }
        //其它情况除过$直接返回分隔符
        Token tok = new Token(peek);
        if(peek=='$')
            lexerPrint("EOF symbol: "+peek+"\n");
        else
            lexerPrint("delimiter: "+peek+"\n");
        peek = ' ';
        return tok;
    }
}
