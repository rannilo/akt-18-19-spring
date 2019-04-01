package week6;

import org.apache.commons.text.StringEscapeUtils;

import java.util.Objects;

public class Token {
    private final TokenType type;
    private final Object data; // NB! data tüüp sõltub type-ist, vt. täpsemalt TokenType juurest
    private int offset; // token-ile vastava teksti alguse indeks
    private int length; // token-ile vastava teksti pikkus

    public Token(TokenType type) {
        this(type, null, -1, -1);
    }

    public Token(TokenType type, Object data) {
        this(type, data, -1, -1);
    }

    public Token(TokenType type, int offset, int length) {
        this(type, null, offset, length);
    }

    public Token(TokenType type, Object data, int offset, int length) {
        this.type = Objects.requireNonNull(type);
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    public TokenType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public Token setPosition(int offset, int length) {
        this.offset = offset;
        this.length = length;
        return this;
    }

    public boolean noOffset() {
        return offset < 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('<').append(type);
        if (data != null) {
            result.append(":").append(StringEscapeUtils.escapeJava(data.toString()));
            result.append(':').append(data.getClass().getSimpleName());
        }
        result.append('>');
        if (offset >= 0) result.append("@").append(offset).append("-").append(offset + length);
        return result.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(data, token.data) &&
                (noOffset() || token.noOffset() || offset == token.offset && length == token.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data, offset, length);
    }
}
