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
        private TokenDatum(Pattern regex, int type, String name) {
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
        tokenData = new LinkedList<>();
        tokens = new LinkedList<>();
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

     private HashMap<String, Integer> memory; //no duplicate keys but will allow dup null values
     private Token input;
     private Iterator <Token> itr;
     private String current; //points to the token currently being looked at by the parser
     private String var; //the variable currently being worked on by the interpreter (on left side of assignment fn)
     private String last_op;
     private int v1;
     private int v2;
     private String temp="temp";


     private String t_type;

         //for readability refer to tokens by type name
    private void match(String expected){
            if ( !(input.name.equals(expected)) ){
                p_error();
            }
            else
                next();
        }

    /**
     *
     */
    private void p_error(){
            throw new ParseException("we weren't expecting to see "+ input.token + " here.");
        }
    private void i_error(){
        throw new ParseException("unable to interpret "+ input.token + ", it is undefined.");
    }
    /**
     *
     */
    void interpret(){
            itr = tokens.iterator();
            next();
          //  System.out.println("itr initially points to to: "+ current );
            memory = new HashMap<String,Integer>();
            memory.put(temp,0);
            program();
               // System.out.println("program ended successfully");
        }

    private void next(){
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

    /**
     * Start rule of the defined grammar.
     */
    private void program(){
            System.out.println("entering start rule: 'program'...");

        while (!(t_type.equals("eoi"))) { //as long as $ has not been reached keep looping
            switch(t_type) {
                  case "Identifier":
                        assignment();

                    /*if assignments succeed then after control is reverted to
                      program all of the assignments performed (entries in the map should be printed
                      key = val
                     */

                        break;
                    default:
                        p_error();
                }
            }
        /*
            if any key in the map is set to null then we need to throw an error and not print anything
         */
        //remove "temp"kv pair from map then print entries

            memory.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        });

       //    System.out.println("v1 = " +v1);

        }
    //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
       private void assignment(){
        System.out.println("enter assignment...");
           switch(t_type){
                case "Identifier":

                memory.put(current,null); //add variable name to memory
                var = input.token;
         //       System.out.println("current variable under assignment's scope is " + var);

                match("Identifier");
                match("Equals");
                last_op ="eq";
                expr();
                match("Semi");
                break;
                default:
                p_error();
            }
           System.out.println("assignment ended");
        }

    //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
       private void expr(){
           System.out.println("enter expr...");
           switch (t_type) {  //may need to split this up to deal with inserting values into the hashmap
               case "L_Par":
               case "Minus":
               case "Plus":
               case "Literal":
               case "Identifier":
                  term();
                  expr_pr();

                  memory.put(var,v1);
 //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                  break;
               default:
                   p_error();
            }
           System.out.println("expr ended");
          }

       private void expr_pr(){
            System.out.println("enter expr_pr...");
               switch (t_type) {
                    case "Plus":
                        match("Plus");
                        last_op="Plus";
                        term();

                        expr_pr();

                        if(input.name.equals("Literal") || input.name.equals("Identifier")){
                            v2=v1;
                            fact();
                            term_pr();
                            v1+=v2;
                        }

                        break;
                    case "Minus":
                        match("Minus");
                        last_op="Plus";
                        term();
                        expr_pr();
                        break;
                   case "R_Par":
                   case "Semi":
                   case "WS":
                        break;
                    default:
                //        p_error();
                }
           System.out.println("expr_pr ended");
        }

       private void term(){
            System.out.println("enter term....");
         switch(t_type) {
            case "Plus":
                fact();
                term_pr();
                break;
            case "Minus":
                fact();
                term_pr();
                break;

            case "L_Par": // (
            case "Literal": // lit
            case "Identifier": //id
                fact();
                term_pr();
                break;

            default:
                p_error();

           }
           System.out.println("term ended");
        }

      private void term_pr(){
        System.out.println("enter term_pr...");
                switch (t_type) {
                    case "Mul": //6

                       System.out.println("just matched mul and moved to next token");
                        match("Mul");
                        //if we know the next token will give us a second term for mul, find it
                        //and perform the operation
                        if(input.name.equals("Literal") || input.name.equals("Identifier")){
                            v2=v1;
                            fact();
                            term_pr();
                            v1*=v2;
                        }
                        else{
                            fact();
                            term_pr();
                        }

                        break; //may need to remove this one
                    case "Plus": //+
                    case "Minus": //-
                    case "R_Par": //)
                    case "Semi": //;
                         break;
                    default:
                        p_error();
                }
          System.out.println("term_pr ended");
        }

       private void fact() {
        System.out.println("enter fact...");
            switch (t_type) {
                case "L_Par": //(
                    match("L_Par");
                    expr();
                    match("R_Par");
                    break;
                case "Minus": //-
                    last_op="Minus";
                    match("Minus");
                    fact();
                    break;
                case "Plus": //+
                    last_op="Plus";
                    match("Plus");
                    v1=Integer.parseInt(input.token);
                    fact();
                    break;
                case "Literal": //lit
                    memory.replace(var,Integer.parseInt(input.token));
                    v1=Integer.parseInt(input.token);
                    match("Literal");
                System.out.println("fact: literal matched successfully");
                    break;
                case "Identifier": //id
                    if (memory.containsKey(input.token)){
                        v1=memory.get(input.token);

                    }
                    match("Identifier");
                    //see if it is already declared/in map then return int value

                    break;
                default:
                    p_error();
            }
        }
}
