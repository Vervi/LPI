public class Main {

    public static void main(String[] args) {
        Tokenizer tk = new Tokenizer();
        go(tk);

        System.out.println("let's test out some assignments...");
        try {
            Thread.sleep(650);
            tk.tokenize("x =1;");

            for (Tokenizer.Token tok : tk.getTokens()) {
                System.out.println("Current token is:" + tok.token + " of type " + tok.name);
                Thread.sleep(650);

                //Interpret.program();


            }
        }//2 secs
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
        t.add("\\;",  9,"Semicolon"); //semicolon
        t.add("\\s", 0,"Whitespace"); //match skips this using trim, need it here to avoid errors

    }



}

