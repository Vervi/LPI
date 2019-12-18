package proj.plc.f19;

import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        LinkedList<Lexer.Token> tokens;
        Parser parser =new Parser();
        System.out.println("let's test out some assignments...");

      //  System.out.println("Please enter a string of assignments to test");
      //  Scanner sc= new Scanner(System.in);
      //  String test=sc.nextLine();

        try {
            Thread.sleep(650);
            //lexer.tokenize("x =1; y=2; z= x+y;"); //works
            //lexer.tokenize("u=3*1; v=2*u ; w=(v+1)*1+2*2;");
             lexer.tokenize("r =3; s=(r-1)*2; t =3*r+(s-2*r); y=r*2+(s-3*-t);");
             //lexer.tokenize("s=1; y=2; x=3;");
        //    lexer.tokenize(test);
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

