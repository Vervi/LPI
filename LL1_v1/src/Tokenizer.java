import plc.ll1.Tokenizer.ParseException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Blueprint of Tokenizer class/object
 */
public class Tokenizer {

    private class TokenDatum {

        public final Pattern regex;
        public final int type;
        public final String name;

        /**
         * Constructor for TokenDatum class/object
         *
         * @param regex the regex
         * @param type  the type
         * @param name  the name
         */
        public TokenDatum(Pattern regex, int type, String name) {
            super();
            this.regex = regex;
            this.type = type;
            this.name = name;
        }
    }

    /**
     * Blueprint of inner Token Class
     */
    public class Token {

        public final int type;
        public final String token;
        public final String name;

        /**
         * Constructor for Token class/object.
         *
         * @param type  the type
         * @param token the token
         * @param name  the name
         */
        public Token(int type, String token, String name) {
            super();
            this.type = type;
            this.token = token;
            this.name = name;
        }

    }
    private LinkedList<TokenDatum> tokenData;
    private LinkedList<Token> tokens;

    /**
     * Constructor for Tokenizer objects
     */
    public Tokenizer() {
        tokenData = new LinkedList<TokenDatum>();
        tokens = new LinkedList<Token>();
    }

    /**
     * A helper method for adding tokenization rules to our tokenizer object
     *
     * @param regex the regex
     * @param type  the type
     * @param name  the name
     */
    public void add(String regex, int type, String name) {
        tokenData.add(new TokenDatum(Pattern.compile("^(" + regex + ")"), type, name));
    }

    /**
     * Tokenize(str) takes in a string to be turned into tokens.
     * It sets a default flag value (match) to false then for each
     * possible token in the string, it sees if the token currently being looked at is a match
     * to any of the regex's stored in the tokenizer if it is, the flag is switched to true
     * the token is added to the token LinkedList, it's is replaced by the "" character,leading whitespace is trimmed
     * and the next token is evaluated
     * if it doesn't match, an exception is raised and tells the console what unexpected value appeared
     *
     * @param str the str
     */
    public void tokenize(String str) {
        String s = str.trim();
        tokens.clear();
        while (!s.equals("")) {
            boolean match = false;
            for (TokenDatum td : tokenData) {
                Matcher m = td.regex.matcher(s);
                if (m.find()) {
                    match = true;
                    String tkn = m.group().trim();
                    s = m.replaceFirst("").trim();
                    tokens.add(new Token(td.type, tkn, td.name));
                    break;
                }
            }
            if (!match) throw new ParseException("Uh-oh! I wasn't expecting to see a: " + s + " here.");
        }
    }

    /**
     * Returns a LinkedList of type Token
     *
     * @return tokens a LinkedList of Tokens
     */
    public LinkedList<Token> getTokens() {
        return tokens;
    }

    //code above this point works properly

    /**
     *
     */
    //public class Interpreter {
        /*create a method to handle the parsing/interpretation
         */
        HashMap<String, Integer> memory;
        Token input;
        Iterator <Token> itr;
        String current;

        //to make life easier, refer to tokens by their type(number) not name
        void match(int expected){
            if (input.type != expected ){
                throw new ParseException("we weren't expecting to see "+ input.token + " here.");
            }
        }

        //this gets called first
        void interpret(){
            //input = tokens.element(); //set the token stream to the first token
            itr = tokens.iterator();
            program();

        }

        void next(){
            try{
                input=itr.next();
                current= input.token;

            }
            catch(NoSuchElementException e)
            {
            input=null;
            }

        }

        void program(){
            assignment();
        }


/*see if next token is an identifier if its not throw an error
if it is a valid id consume token
and look at the next one, match the '=' token
if it fails throw an error else consume and look at next token
call expr (it will return what it should or throw an error thats dealt with elsewhere)
match the ";" token, if it fails throw an error
*/

        void assignment(){


        }

        /*currently debating what the return type for each method should be
         maybe program and assignment should be void
         and the rest should return a token?
        */
        void expr(){



        }

        void expr_pr(){




        }

        void term(){



        }

        void term_pr(){


        }

        void fact(){


        }



  //  } took the interpreter stuff out of the interpret subclass to nix static vs nonstatic issue




}
