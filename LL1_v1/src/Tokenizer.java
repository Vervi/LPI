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
    public static class Token {

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
        HashMap<String, Integer> memory; //no duplicate keys but will allow dup null values
     private Token input;
     private Iterator <Token> itr;
     private String current;
     private String t_type;

         //to make life easier, refer to tokens by their type(number) not name
        void match(String expected){
            if ( !(input.name.equals(expected)) ){
                error();
            }
            else
                next();
        }
        void error(){
            throw new ParseException("we weren't expecting to see "+ input.token + " here.");
        }

        void interpret(){
            itr = tokens.iterator();
            next();
            System.out.println("itr initially points to to: "+ current );
            program();
               // System.out.println("program ended successfully");
        }

        void next(){
            try{
                if(itr.hasNext()) {
                    input = itr.next();
                    current = input.token.intern();
                    t_type = input.name;
                }
            }
            catch(NoSuchElementException e)
            {
            input=null;
            }
        }

        void program(){
            System.out.println("entering start rule: 'program'...");
            switch(t_type) {
                case "Identifier":
                    assignment();
                    break;
                default:
                    error();
            }
            System.out.println("program ended successfully");
         //   match(10);
        }
    //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
        void assignment(){
            System.out.println("enter assignment...");
           switch(t_type){
                case "Identifier":
                match("Identifier"); //passes
              //      System.out.println("assignment case "Identifier" match yields: "+ current );
                match("Equals"); //passes
                //    System.out.println("assignment case "Equals" match yields: "+ current );
                expr();
                //    System.out.println("assignment ->match -> expr yields "+current);
                match("Semi");
                 //   System.out.println("assignment ->match -> expr -> match 9 yields "+current);
                    System.out.println("assignment ended successfully");
                //add identifier and value of EXPR to hashmap
                 break;
                default:
                error();
            }
        }

    //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
        void expr(){
            System.out.println("enter expr...");
           switch (t_type) {  //may need to split this up to deal with inserting values into the hashmap
               case "L_Par":
               case "Minus":
               case "Plus":
               case "Literal":
               case "Identifier":
                  term();
                    //System.out.println("expr -> term returned " + current);
                  expr_pr();
                    //System.out.println("expr -> term -> expr_pr returned " + current);
                   System.out.println("expr ended successfully");
                   break;
               default:
                   error();
            }
          }

        void expr_pr(){
            System.out.println("enter expr_pr...");
               switch (t_type) {
                    case "Plus":
                        match("Plus");
                        //    System.out.println("expr_pr case "Plus" match yields next token: " + current);
                        term();
                          //  System.out.println("expr_pr 4 match->term yields next token: " + current);
                        expr_pr();
                            //System.out.println("expr_pr 4 match->term-> expr_pr yields next token: " + current);

                             System.out.println("expr_pr 4 ended successfully");
                        break;
                    case "Minus":
                        match("Minus");
                            //System.out.println("just matched type 5 in expr_pr call next up is: " + current);
                        term();
                           // System.out.println("expr_pr 5 match->term yields next token: " + current);
                        expr_pr();
                           // System.out.println("expr_pr 5 match->term-> expr_pr yields next token: " + current);
                        System.out.println("expr_pr 5 ended successfully");
                        break;
                   case "R_Par":
                   case "Semi":
                   case "WS":
                       //  System.out.println("expr_pr -> case 8/9/10 returned " + current);
                       System.out.println("expr_pr 8/9/10 ended successfully");
                       break;
                    default:
                        error();
                }
        }

        void term(){
            System.out.println("enter term....");
         switch(t_type) {
            case "Plus": //+
            case "Minus": //-
            case "L_Par": // (
            case "Literal": // lit
            case "Identifier": //id
                fact();
                    //System.out.println("term -> fact returned " + current);//exits successfully
                term_pr();
                   // System.out.println("term -> fact -> term_pr returned " + current); //exits successfully
                System.out.println("term ended successfully");

               break;
            default:
                error();
           }
        }

        void term_pr(){ //double check the first/follow/predict set for this one
            System.out.println("enter term_pr...  ");
                switch (t_type) {
                    case "Mul": //6
                        match("Mul");
                            // System.out.println("just matched type 6 in term_pr call next up is: " + current);
                        fact();
                            //System.out.println("term_pr -> fact returned " + current);
                        term_pr();
                            //System.out.println("term_pr -> fact -> term_pr returned " + current);
                        System.out.println("term_pr ended successfully");
                        break; //may need to remove this one
                    case "Plus": //+
                    case "Minus": //-
                    case "R_Par": //)
                    case "Semi": //;
                //    case "Identifier"0: //$
                        System.out.println("term_pr ended successfully");
                        break;
                    default:
                        error();
                }
        }

        void fact() {
            System.out.println("enter fact...");
            switch (t_type) {
                case "L_Par": //(
                    match("L_Par");
                        //System.out.println("fact case 7 match yields next token: " + current);
                    expr();
                        //System.out.println("expr 7 match->expr yields next token: " + current);
                    match("R_Par");
                        //System.out.println("expr 7 match->expr -> match 8 yields next token: " + current);
                    System.out.println("fact 7 ended successfully");
                    break;
                case "Minus": //-
                    match("Minus");
                        //System.out.println("fact case 5 match yields next token: " + current);
                    fact();
                        //System.out.println("fact case 5 match -> fact yields next token: " + current);

                    System.out.println("fact 5 ended successfully");
                    break;
                case "Plus": //+
                    match("Plus");
                        //System.out.println("fact case "Plus" match yields next token: " + current);
                    fact();
                        //System.out.println("fact case "Plus" match->fact yields next token: " + current);
                    System.out.println("fact 4 ended successfully");
                    break;
                case "Literal": //lit
                    match("Literal");
                        //System.out.println("fact case "Literal" match yields next token: " + current);
                    System.out.println("fact 3 ended successfully");
                    break;
                case "Identifier": //id
                    match("Identifier"); //see if it is already declared/in map then return int value}
                        //System.out.println("fact case "Identifier" match yields next token: " + current);
                    System.out.println("fact 1 ended successfully");
                    break;
                default:
                    error();
            }
        }
}
