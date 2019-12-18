package proj.plc.f19;

public class ParseException extends RuntimeException {
    public ParseException(String msg) {
        super("\n"+msg);
       // printStackTrace();


    }

}