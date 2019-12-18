package proj.plc.f19;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        LinkedList<Lexer.Token> tokens;
        Parser parser =new Parser();
        Scanner sc= new Scanner(System.in);
        try {
            System.out.println("A quick note on formatting...");
            Thread.sleep(750);
            System.out.println("if assigning 0 to a variable...");
            Thread.sleep(750);
            System.out.println("please enter a space between 0 and the semicolon.");
            Thread.sleep(750);
            System.out.println("\n\n\n\n");
            Thread.sleep(350);

            System.out.println("Now, let's have some fun with parsing...");
            Thread.sleep(6000);
            System.out.println("please enter a series of assignments and hit ''return' key when done.");

            String test=sc.nextLine();
            lexer.tokenize(test);
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

