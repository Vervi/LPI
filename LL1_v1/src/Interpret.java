import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;


public class Interpret {
    /*create a method to handle the parsing/interpretation
     */
    HashMap<String, Integer> memory;

    void program(){
        assignment();
    }


/*see if next token is an identifier if its not throw an error
if it is a valid id consume token
and look at the next one, match the '=' token
if it fails throw an error else consume and look at next token
call expr (it will return what it should or throw an error thats dealt with elsewhere)
match the ";" token, if it fails throw an error
*/

void assignment(){


    }

    void expr(){



    }

    void expr_pr(){




    }

    void term(){



    }

    void term_pr(){


    }

    void fact(){


    }



}
