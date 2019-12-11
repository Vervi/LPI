package f19.plc.ll1;

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
            tk.tokenize("u=3*2; v=2*u ; w=3*(v+1);");
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
     * @param tz a tokenizer object
     */
    public static void go(Tokenizer tz){
        tz.add("[a-zA-Z_][a-zA-Z_0-9]*", 1, "Identifier") ; // Identifier
        tz.add("\\=" , 2, "Equals");     //assignment operator
        tz.add( "^0[^0-9]|^[1-9]\\d*",3,"Literal"); //from start of string 0 not followed by any digit or any digit w/non leading zero
        tz.add("\\+" , 4,"Plus"); //plus
        tz.add("\\-" , 5, "Minus"); //minus
        tz.add("\\*" ,6, "Mul"); //mul
        tz.add("\\(",  7,"L_Par"); //L_Par
        tz.add("\\)", 8,"R_Par"); //R_Par
        tz.add("\\;",  9,"Semi"); //semicolon
        tz.add("\\s", 0,"WS"); //match skips this using trim, need it here to avoid errors
    }
}

