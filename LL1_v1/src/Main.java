import plc.ll1.Tokenizer.*;

import java.util.LinkedList;


public class Main {

    public static void main(String[] args) {
        Tokenizer tk = new Tokenizer();
        go(tk);
        LinkedList<Tokenizer.Token> tokens;

        System.out.println("let's test out some assignments...");
        try {
            Thread.sleep(650);
            //tk.tokenize("x = 1 ; y = 2; z= x+y;"); //works
            tk.tokenize("u=3*2; v=2*u ; w=3*v+1;");
           // tk.tokenize("r =3; s= 2*(r-1);t =3*r+(s-2*r);");
         //   tk.tokenize("s=1+2;");
            tokens=tk.getTokens();
            tokens.add(new Tokenizer.Token(10, "$","eoi"));
            tk.interpret();

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * A helper method to populate tokenizer with rules, without altering its blueprint
     * @param t
     */
    public static void go(Tokenizer t){
        t.add("[a-zA-Z_][a-zA-Z_0-9]*", 1, "Identifier") ; // Identifier
        t.add("\\=" , 2, "Equals");     //assignment operator
        t.add( "^0[^0-9]|^[1-9]\\d*",3,"Literal"); //from start of string 0 not followed by any digit or any digit w/non leading zero
        t.add("\\+" , 4,"Plus"); //plus
        t.add("\\-" , 5, "Minus"); //minus
        t.add("\\*" ,6, "Mul"); //mul
        t.add("\\(",  7,"L_Par"); //L_Par
        t.add("\\)", 8,"R_Par"); //R_Par
        t.add("\\;",  9,"Semi"); //semicolon
        t.add("\\s", 0,"WS"); //match skips this using trim, need it here to avoid errors
    }
}

