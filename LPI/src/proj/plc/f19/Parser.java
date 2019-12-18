
package proj.plc.f19;

import java.util.*;
/**
 Program:
 Assignment*

 Assignment:
 Identifier = Exp;

 Exp:
 Exp + Term | Exp - Term | Term

 Term:
 Term * Fact | Fact

 Fact:
 ( Exp ) | - Fact | + Fact | Literal | Identifier

 Identifier:
 Letter [Letter | Digit]*

 Comparison of original and left recursion free grammars

 Program  ->  {Assignment}
 Assignment -> ID = Expr ;
 Expr -> Term EP
 EP -> + Term EP | - Term EP | Epsilon
 //	Expr ->Term + Term | Term - Term | Term
 //this tells us that expr resolves to a term
 // + & - are binary operators here
 Term -> Fact TP
 TP -> * Fact TP | Epsilon
 // Term -> Term * Fact | Fact
 //this tells us that a call to Term resolves to Fact
 Fact -> ( Expr ) | + Fact | - Fact | ID |Literal
 //+ & - are unary operators here

 */
public class Parser {
    private HashMap<String, Integer> memory;    //holds actual completed assignments

    private Lexer.Token input;
    private Iterator<Lexer.Token> itr;
    private LinkedList<Lexer.Token> tokens;

    private String current;
    private String var;
    private String t_type;

    /**
     * Constructor of parser
     */
     Parser() {  }

    /**
     * Takes a string that represents the token expected to appear at that point in the token
     * stream. If the match is successful, the token is consumed and the iterator points to
     * the next token in the stream.
     * @param expected the token the parser is expecting to see
     */
    private void match(String expected) {
        if (!(input.name.equals(expected))) {
            System.out.println("match has failed");
            parseError();
        } else
            next();
    }

    /**
     * An exception generated by an error in parsing. Method is called when an unexpected token appears in the stream.
     */
    private void parseError() {
        throw new ParseException("we weren't expecting to see " + input.token +
                " here.");
    }

    /**
     * An exception generated by an error in interpretation. Method is called when operations are done on variables
     * that cannot be resolved, usually because they have not been initialized yet.
     */
    private void variableError() {
        throw new ParseException("unable to interpret variable '" + input.token +
                "', has it been defined?");
    }

    /**
     * A simple function that creates an iterator for the list of tokens to be interpreted and
     * a hashmap to store intermediary computations. It initiates a call to the parser's start rule.
     */

    void parse(LinkedList<Lexer.Token> tkns) {
        this.tokens = tkns;
        itr = tokens.iterator();

        next();            //set iterator to first token
        memory = new HashMap<>();
        program();            //call start rule
    }

    /**
     * A helper function that iterates to the next token in the list if it exists.
     */
    private void next() {
        try {
            if (itr.hasNext()) {
                input = itr.next();
                current = input.token;   //the actual token string
                t_type = input.name;     //the type of token
            }
        } catch (NoSuchElementException e) {
            input = null;
        }
    }

    /**
     * Start rule of the defined grammar.
     */
    private void program() {
        while (!(t_type.equals("eoi"))) { //as long as $ has not been reached keep looping
            switch (t_type) {
                case "Identifier":
                    assignment();
                    break;
                default:
                    parseError();
            }
        } //end while loop
        if (memory.containsValue(null))
            variableError();
        else {
           // prints out the successfully completed assignments
            memory.entrySet().forEach(entry ->
            {
                System.out.println(entry.getKey() + " = " +
                     entry.getValue());
            }
            );
        }
    }//end program rule

    private void assignment() {
        switch (t_type) {
            case "Identifier":
                var = current;
                memory.put(var, null);    //add variable name to memory
                match("Identifier");
                match("Equals");
                int x;            //variable for calc to pass back
                x = expr();        //start parsing/interpreting the RHS of our assingment fn
                match("Semi");        //ensure that it's a valid assignment ending in a ;
                memory.replace(var, x);    //store val passed to RHS of assignment into hashmap for output
                break;
            default:
                parseError();
        }
    }

    //start of interpreting functions
    private int expr() {
        int x = term();
        while (true) { //allows for consecutive sum/diff operations
            switch (t_type) {
                case "Plus": {
                    match("Plus");
                    x += term();
                    break;
                }
                case "Minus": {
                    match("Minus");
                    x -= term();
                    break;
                }
                default:
                    return x;
            }
        }
    } //end expr


    private int term(){
        int x = fact();
        while (true) {
            if (t_type.equals("Mul")) {
                match("Mul");
                x *= fact();
            } else
                return x;
        }
    }

    private int fact() {
        int x = 0;
         switch (t_type) {
            case "L_Par":
                match("L_Par");
                x = expr();
                match("R_Par");
                break;

            case "Minus":        //unary-
                match("Minus");
                x = -fact();
                break;

            case "Plus":        //unary+
                match("Plus");
                x = fact();
                break;

            case "Literal":
                x = Integer.parseInt(current);
                match("Literal");
                break;

            case "Identifier":
                if (memory.containsKey(current) && (memory.get(current) != null)) {
                   x = memory.get(current);
                } else {
                   variableError();
                }
                match("Identifier");
                break;

            default: {
                x = -1;
                parseError();

            }
        }
        return x;
    }//end fact
}//end parser class
