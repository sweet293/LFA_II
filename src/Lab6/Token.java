package Lab6;

import Lab6.TokenType;

//class to initialize a token
class Token {
    public TokenType type; //type of the token, identifier, operator, separator, real, keyword, none
    public String match; //matches patters to their tokens. When a match is found,
    // it is extracted and analysed. Each character will be analysed. if there is a character after it,
    // it is also analysed and is made a conclusion : is it still an identifier or actually an operator?
    public Token(TokenType type, String match) {
        this.type = type;
        this.match = match;
    }
}