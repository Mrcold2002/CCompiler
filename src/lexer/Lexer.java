package lexer;

import symbols.Type;

import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public static int line = 1;
    public String Code;
    public int now;
    char peek = ' ';
    Hashtable words = new Hashtable();

    void reserve(Word w) {
        words.put(w.lexeme, w);
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
                if (readch('&')) return Word.and;
                else return new Token('&');
            case '|':
                if (readch('|')) return Word.or;
                else return new Token('|');
            case '=':
                if (readch('=')) return Word.eq;
                else return new Token('=');
            case '!':
                if (readch('=')) return Word.ne;
                else return new Token('!');
            case '<':
                if (readch('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readch('=')) return Word.ge;
                else return new Token('>');
        }
        if (Character.isDigit(peek) || peek == '.') {
            int v = 0;
            if (peek != '.')
                do {
                    v = 10 * v + Character.digit(peek, 10);
                    readch();
                } while (Character.isDigit(peek));
            if (peek != '.') return new Num(v);
            float x = v, d = 10;
            for (; ; ) {
                readch();
                if (!Character.isDigit(peek)) break;
                x = x + Character.digit(peek, 10) / d;
                d = d * 10;
            }
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
            if (w != null) return w;
            w = new Word(s, Tag.ID);
            words.put(s, w);
            return w;
        }
        Token tok = new Token(peek);
        peek = ' ';
        return tok;
    }
}