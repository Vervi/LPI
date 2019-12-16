package proj.plc.f19;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        LinkedList<Lexer.Token> tokens;
        Parser parser =new Parser();
        System.out.println("let's test out some assignments...");
        try {
            Thread.sleep(650);
            //lexer.tokenize("x = 1 ; y = 2; z= x+y;"); //works
            //lexer.tokenize("u=3*1; v=2*u ; w=(v+1)*1+2*2;");
            // lexer.tokenize("r =3; s=(r-1)*2; t =3*r+(s-2*r);");
            lexer.tokenize("s=3*-2;");
            tokens=lexer.getTokens();
            tokens.add(new Lexer.Token(10, "$","eoi")); //add end of input token so parser knows when to terminate

            parser.parse(tokens);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}

