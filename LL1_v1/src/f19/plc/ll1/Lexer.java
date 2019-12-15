package f19.plc.ll1;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Blueprint of f19.plc.ll1.Lexer class/object
 */
public class Lexer {


    private class TokenDatum {

        public final Pattern regex;
        public final int type;
        //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
        public final String name;

        /**
         * Constructor for TokenDatum class/object
         *
         * @param regex The Regular Expression that describes a given lexer rule
         * @param type  An integer used to refer to a given lexer rule
         * @param name  A name used to describe the strings that fall within the scope of a given lexer rule
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
         * Constructor for Token object.
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
     * Constructor for Lexer object
     */
    public Lexer() {
        tokenData = new LinkedList<>();
        tokens = new LinkedList<>();

        add("[a-zA-Z_][a-zA-Z_0-9]*", 1, "Identifier"); // Identifier
        add("\\=", 2, "Equals");     //assignment operator
        add("^0[^0-9]|^[1-9]\\d*", 3, "Literal"); //from start of string 0 not followed by any digit or any digit w/non leading zero
        add("\\(", 7, "L_Par"); //L_Par
        add("\\)", 8, "R_Par"); //R_Par
        add("\\*", 6, "Mul"); //mul
        add("\\+", 4, "Plus"); //plus
        add("\\-", 5, "Minus"); //minus
        add("\\;", 9, "Semi"); //semicolon
        add("\\s", 0, "WS");
    }

    /**
     * A helper method for adding lexical rules to our tokenizer object
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
     * @return tokens a LinkedList of Tokens
     */
    public LinkedList<Token> getTokens() {
        return tokens;
    }


    private HashMap<String, Integer> memory;
    private Lexer.Token input;
    private Iterator<Lexer.Token> itr;
    private String current; //points to the token currently being looked at by the parser
    private String var; //the variable currently being worked on by the interpreter (ID on left side of assignment fn)
    private String current_op;
    private Boolean parCheck = false; //check to see if parser is interpreting tokens inside a parenthentical expression
    private int v1;
    private int v2;
    private String t_type;

    private int t = 0; //the value associated with temp in memory map
    private String temp = "temp";
    private int p = 0; //the value associated with what's inside current parenthitecal operation in memory map
    private String par = "par";

    LinkedList<String> ops;
    Iterator<String> ditr;

    //for readability referring to tokens by type name
    private void match(String expected) {
        if (!(input.name.equals(expected))) {
            parseError();
        } else
            next();
    }

    /**
     * An exception generated by an error in parsing. Method is called when an unexpected token appears in the stream.
     */
    private void parseError() {
        throw new ParseException("we weren't expecting to see " + input.token + " here.");
    }

    /**
     * An exception generated by an error in interpretation. Method is called when operations are done on variables that cannot be resolved.
     */
    private void interpretorError() {
        throw new ParseException("unable to interpret variable '" + input.token + "', it is undefined.");
    }

    /**
     * A simple function that creates an iterator for the list of tokens to be interpreted and
     * a hashmap to store intermediary computations. It initiates a call to the parser's start rule.
     */
    void interpret() {
        itr = tokens.iterator();
        ops =new LinkedList<String>();
        ditr = ops.descendingIterator();
        next();
        memory = new HashMap<String, Integer>();
        memory.put(temp, t);
        memory.put(par, p);
        program();
    }

    void next() {
        try {
            if (itr.hasNext()) {
                input = itr.next();
                current = input.token.intern();
                t_type = input.name;
            }
        } catch (NoSuchElementException e) {
            input = null;
        }
    }

    /**
     * Start rule of the defined grammar.
     */
    void program() {
        // System.out.println("entering start rule: 'program'...");

        while (!(t_type.equals("eoi"))) { //as long as $ has not been reached keep looping
            switch (t_type) {
                case "Identifier":
                    assignment();
                    break;
                default:
                    parseError();
            }
        }

        //    if any key in the map is set to null then we need to throw an error and not print anything
        if(memory.containsValue(null))
            interpretorError();
        else {
          //  memory.remove(temp);
          //  memory.remove(par);
            memory.entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            });
        }
    }

    private void assignment() {
        //  System.out.println("enter assignment...");
        switch (t_type) {
            case "Identifier":
                memory.put(current, null); //add variable name to memory
                var = current;
                match("Identifier");
                ops.add("id");
                match("Equals");
                current_op = "eq";
                ops.add("=");
                expr();
                match("Semi");
                ops.add(";");
                break;
            default:
                parseError();
        }
        //      System.out.println("assignment ended");
    }

    private void expr() {
        //    System.out.println("enter expr...");
        switch (t_type) {
            case "L_Par":
            case "Minus":
            case "Plus":
            case "Literal":
            case "Identifier":
                term();
                expr_pr();
                memory.put(var, memory.get(temp));
                break;
            default:
                parseError();
        }
        //System.out.println("expr ended");
    }

    private void expr_pr() {
        // System.out.println("enter expr_pr...");
        switch (t_type) {
            case "Plus":
                match("Plus");
                current_op = "Plus";
                term();

                expr_pr();
                break;
            case "Minus":
                match("Minus");
                current_op = "Plus";
                term();
                expr_pr();

                break;
            case "R_Par":
            case "Semi":
            case "WS":
                break;
            default:
                parseError();
        }
        //System.out.println("expr_pr ended");
    }

    private void term() {
        //System.out.println("enter term....");
        switch (t_type) {
            case "Plus":
                fact();
                term_pr();
                break;
            case "Minus":
                fact();
                term_pr();
                break;

            case "L_Par":
            case "Literal":
            case "Identifier":
                fact();
                term_pr();
                break;

            default:
                parseError();

        }
    }

    private void term_pr() {
        // System.out.println("enter term_pr...");
        switch (t_type) {
            case "Mul": //6
                current_op = "Mul";
                ops.add("Mul");
                match("Mul");
                fact();
                last_op = current_op;
                term_pr();
                break; //may need to remove this one
            case "Plus": //+
            case "Minus": //-
            case "R_Par": //)
            case "Semi": //;
                break;
            default:
                parseError();
        }
        //System.out.println("term_pr ended");
    }

    String last_op = "";

    private void fact() {
        // System.out.println("enter fact...");
        switch (t_type) {
            case "L_Par": //(
                last_op = current_op;
                current_op = "L_Par";
                ops.add("(");
                parCheck = true;
                match("L_Par");
                expr();
                parCheck = false;
                ops.add(")");
                match("R_Par"); //)
                 //if prev match succeeded, no longer inside (exp)
                last_op = "R_Par";
                break;
            case "Minus": //-
                current_op = "Minus";
                ops.add("-");
                match("Minus");
                last_op = current_op;
                fact();
                break;
            case "Plus": //+
                current_op = "Plus";
                ops.add("+");
                match("Plus");
                last_op = current_op;
                fact();
                break;
            case "Literal": //lit
                v1 = Integer.parseInt(current);
                update();
                ops.add("lit");
                match("Literal");
                break;
            case "Identifier":
            //  System.out.println("fact id input token is : "+ current +'\n' + " value: "+ memory.get(current));
                if (memory.containsKey(current) && (memory.get(current) != null)) {
                    v1 = memory.get(current);
                  update();
                }
                else
                {
                    interpretorError();
                }
                ops.add("id");
                match("Identifier");
                break;
            default:
                parseError();
        }
        //System.out.println("fact ended");
    }


    private void signCheck(){

        if (current_op.equals("Minus")){
            String ch=""; //the id or lit that is in scope of interpreter when this is called
            int flip=0;
            String first=""; //the last token we check after looking at consec minus signs

            //this loop never actually gets entered, need to know why
            while(ditr.hasNext() ) { //&& ch=="-"){
                ch = ditr.next();
                System.out.println("ch = ");

                if (ch.equals("-")) {
                    flip += 1;
                    //first = ch;

                }
                else {
                    first = ch;//last_op=ch;
                    System.out.println("first= " + first);
                    if (ch.equals("*")) {
                        current_op = "Mul";
                        System.out.println("first mul reached");
                    } else if (ch.equals("+"))
                        current_op = "Plus";
                    else if (ch.equals("-"))
                        current_op = "Minus";
                    else if (ch.equals(")"))
                        last_op = "R_Par";
                    break;
                }
                System.out.println("val of first: "+first  );
            }
            if(flip==1){
                if ((first.equals("lit")||first.equals("id")))
                    System.out.println("first when flips is 1: "+first );
                    current_op=ch;


            }
            else if (flip == 2) {
                current_op="Plus";
             }
             else if (flip >2 && flip % 2 != 0)
             {
               v1 *= -1;
               System.out.println("flip 2+ reached");
             }
        }
        }

    private void update(){
        signCheck();
        if (parCheck) //if inside a parenthetical exp
        {
            updatePar();
        }
        else if (last_op.equals("R_Par")){
            t = p;
            memory.replace(temp, t);
            updateTemp();
        }
        else
            updateTemp();
    }

    private void updateTemp() {
        if (current_op.equals("eq")) {
            t = v1;
            memory.replace(temp, t);
        } else if (current_op.equals("Plus")) {
            t += v1;
            memory.replace(temp, t);
        } else if (current_op.equals("Minus")) {
            t -= v1;
            memory.replace(temp, t);
        } else if (current_op.equals("Mul")) {
            t *= v1;
            memory.replace(temp, t);
        }
    }

    private void updatePar() {
        if (current_op.equals("L_Par")) { //if it's the first number after the ( set it to p
            p = v1;
            memory.replace(par, p);
        }
        else if (current_op.equals("Plus")) {
            p += v1;
            memory.replace(par, p);
        } else if (current_op.equals("Minus")) {
            p -= v1;
            memory.replace(par, p);
        } else if (current_op.equals("Mul")) {
            p *= v1;
            memory.replace(par, p);
        }

    }


}