package Lab3.AgainLab3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    //array of keywords
    private static final String[] KEYWORDS = {"while", "if", "else", "float", "double", "int", "string"};

    public static Token greedilyGetNextToken(String input) {
        String largestMatch = ""; //stores text that represents the longest match found so far
        TokenType largestMatchType = TokenType.NONE; //at starting oint we have no matches,
        // no token has been identified yet

        //we start matching all strings or characters from the input from the file we provide

        // This next part recognises the keywords.
        // To recognise a keyword, we delimit the keywords by "|". When the code executes,
        // it will compile the regular expression pattern that matches one/any of the keywords

        Matcher matcher = Pattern.compile("^(" + String.join("|", KEYWORDS) + ")").matcher(input);
        //the "^" prevents the code to read the same word multiple times.
        if (matcher.find()) {
            String match = matcher.group();
            //even if we have the keywords identified,
            // we still need to make sure they are not a substring but a full word
            if (match.length() > largestMatch.length()) {
                largestMatch = match;
                largestMatchType = TokenType.KEYWORD;
            }
        }

        // This next part recognises the operators.
        // To recognise an operator, we specify which are the operators

        matcher = Pattern.compile("^[=<>]").matcher(input);
        if (matcher.find()) {        //find match in input string
            String match = matcher.group();   //extract the match and if the current match is longer
            // than previous match, it updates largestMatch with the current match
            //if it is the longest, it will be an OPERATOR
            if (match.length() > largestMatch.length()) {
                largestMatch = match;
                largestMatchType = TokenType.OPERATOR;
            }
        }

        // This next part recognises literal type - in our case,numbers
        // To recognise them, we specify which are they
        matcher = Pattern.compile("^\"([^\"]*)\"|^(true|false)|^[-+]?[0-9]*\\.?[0-9]+").matcher(input);
        if (matcher.find()) {
            String match = matcher.group();  //extract the match and if the current match is longer
            //then previous match, it updates largestMatch with the current match
            //if it is the longest, it will be of type LIRETAL
            if (match.length() > largestMatch.length()) {
                largestMatch = match;
                largestMatchType = TokenType.LITERAL;
            }
        }

        // Here we define which are the separators, scan through them from input, find match,
        // extract it, check the length, and decide if its a SEPARATOR
        matcher = Pattern.compile("^[(;)]").matcher(input);
        if (matcher.find()) {
            String match = matcher.group();
            if (match.length() > largestMatch.length()) {
                largestMatch = match;
                largestMatchType = TokenType.SEPARATOR;
            }
        }

        // Here we define which are the separators, scan through them from input, find match,
        // extract it, check the length, and decide if its an IDENTIFIER
        matcher = Pattern.compile("^[_A-Za-z]+").matcher(input);
        if (matcher.find()) {
            String match = matcher.group();
            if (match.length() > largestMatch.length()) {
                largestMatch = match;
                largestMatchType = TokenType.IDENTIFIER;
            }
        }
        //if we scanned, and none were identified, the NONE type will show
        if (largestMatchType == TokenType.NONE || largestMatch.isEmpty())
            return new Token(TokenType.NONE, "Error");

        return new Token(largestMatchType, largestMatch);
    }

    public static String typeToString(TokenType type) {
        switch (type) {
            case NONE:
                return "NONE";
            case KEYWORD:
                return "keyword";
            case SEPARATOR:
                return "separator";
            case IDENTIFIER:
                return "identifier";
            case OPERATOR:
                return "operator";
            case LITERAL:
                return "literal";
            default:
                return "UNKNOWN";
        }
    }


    public static void lexer() {
        List<Token> tokens = new ArrayList<>();

        try {
            File inputFile = new File("C:\\...\\Lab3\\Adder.java"); //file path for adder.java
            Scanner scanner = new Scanner(inputFile);

            //read input file
            while (scanner.hasNext()) {
                String input = scanner.next();
                String runningInput = input;

                //tokenize the input
                while (!runningInput.isEmpty()) {
                    //get next token, if no token found,print error
                    Token token = greedilyGetNextToken(runningInput);
                    if (token.type == TokenType.NONE || token.match.isEmpty()) {
                        System.out.println("Error when getting next token.\n\n");
                        break;
                    }

                    //add token to list
                    tokens.add(token);

                    //remove matched text from input
                    int numToRemoveFromFront = token.match.length();
                    runningInput = runningInput.substring(numToRemoveFromFront);
                }
            }

            scanner.close();

            //print all tokens
            for (Token token : tokens) {
                System.out.println(typeToString(token.type) + "         '" + token.match + "'");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found.");
            e.printStackTrace();
        }
    }
    //main method
    public static void main(String[] args) {
        lexer();
    }
}


