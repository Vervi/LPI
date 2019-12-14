package f19.plc.ll1;

public class ParseException extends RuntimeException {
    public ParseException(String msg) {
        super("\n"+msg);
        printStackTrace();


    }

  }