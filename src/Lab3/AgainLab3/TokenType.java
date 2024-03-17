package Lab3.AgainLab3;


//enumeration of types of tokens
enum TokenType {
    NONE, //when the code doesnt recognise any combination of characters or characters alone
    KEYWORD, //when the code recognises a set of characters that make up a word like : if, while, else, int, float
    SEPARATOR, //when the code recognises the end on a statement ";"
    IDENTIFIER, //when the code recognises a class name, variable, package, method, interface
    OPERATOR,  //when the code recognises an operator like <>=^
    LITERAL, //when the code recognises a number that has a +-nr.nr
}