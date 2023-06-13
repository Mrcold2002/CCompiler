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
    public static int line = 1;
    public String Code;
    public int now;
    char peek = ' ';
    Hashtable words = new Hashtable();
    Hashtable keywords = new Hashtable();
    private static String filePath="src/out/LexerOut.txt";

    void reserve(Word w) {
        words.put(w.lexeme, w);
    }
    public void lexerPrint(String output) {
        try (FileWriter fileWriter = new FileWriter(filePath,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(output);
           // System.out.println("内容已成功写入文件：" + filePath);
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
        Code=code;
    }

    void readch() throws IOException {
        //peek = (char) System.in.read();
        peek = Code.charAt(now);now++;
    }

    boolean readch(char c) throws IOException {
        readch();
        if (peek != c) return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for (; ; readch()) {
            if (peek == ' ' || peek == '\t') continue;
            else if (peek == '\n') line = line + 1;
            else break;
        }
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
        if (Character.isDigit(peek) || peek == '.') {
            int v = 0;
            if (peek != '.')
                do {
                    v = 10 * v + Character.digit(peek, 10);
                    readch();
                } while (Character.isDigit(peek));
            if (peek != '.')
            {
                lexerPrint("const value: "+Integer.toString(v)+"\n");
                return new Num(v);
            } 
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
        if (Character.isLetter(peek) || peek == '_') {
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek) || peek == '_');
            String s = b.toString();
            Word w = (Word) words.get(s);
            if (w != null)
            {
                Word keyword=(Word) keywords.get(s);
                if(keyword!=null)lexerPrint("key word: "+s+"\n");
                else lexerPrint("identifier: "+s+"\n");
                return w;
            }
            w = new Word(s, Tag.ID);
            words.put(s, w);
            lexerPrint("identifier: "+s+"\n");
            return w;
        }
        Token tok = new Token(peek);
        if(peek=='$')
            lexerPrint("EOF symbol: "+peek+"\n");
        else
            lexerPrint("delimiter: "+peek+"\n");
        peek = ' ';
        return tok;
    }
}
