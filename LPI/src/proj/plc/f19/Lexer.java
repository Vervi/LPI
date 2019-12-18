package proj.plc.f19;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    /**
     * Lexer blueprint
     */
    public class Lexer {


        private class TokenDatum {

            public final Pattern regex;
            public final int type;
            //1 ID, 2 =, 3 Lit, 4 +, 5 -, 6 *, 7 (, 8 ), 9 ;
            public final String name;

            /**
             * Constructor for TokenDatum class/object
             *
             * @param regex The Regular Expression that describes a given lexer rule
             * @param type  An integer used to refer to a given lexer rule
             * @param name  A name used to describe the strings that fall within the scope of a given lexer rule
             */
            private TokenDatum(Pattern regex, int type, String name) {
                super();
                this.regex = regex;
                this.type = type;
                this.name = name;
            }
        }

        /**
         * Blueprint of inner Token Class
         */
        public static class Token {

            public final int type;
            public final String token;
            public final String name;

            /**
             * Constructor for Token object.
             *
             * @param type  the type
             * @param token the token
             * @param name  the name
             */
            public Token(int type, String token, String name) {
                super();
                this.type = type;
                this.token = token;
                this.name = name;
            }

        }

        private LinkedList<TokenDatum> tokenData;
        private LinkedList<Token> tokens;

        /**
         * Constructor for Lexer object
         */
        public Lexer() {
            tokenData = new LinkedList<>();
            tokens = new LinkedList<>();

            add("[a-zA-Z_][a-zA-Z_0-9]*", 1, "Identifier"); // Identifier
            add("\\=", 2, "Equals");     //assignment operator
            add("^0[^0-9]|^[1-9]\\d*", 3, "Literal"); //from start of string 0 not followed by any digit or any digit w/non leading zero
            add("\\(", 7, "L_Par"); //L_Par
            add("\\)", 8, "R_Par"); //R_Par
            add("\\*", 6, "Mul"); //mul
            add("\\+", 4, "Plus"); //plus
            add("\\-", 5, "Minus"); //minus
            add("\\;", 9, "Semi"); //semicolon
            add("\\s", 0, "WS");
        }

        /**
         * A helper method for adding lexical rules to our tokenizer object
         *
         * @param regex the regex
         * @param type  the type
         * @param name  the name
         */
        public void add(String regex, int type, String name) {
            tokenData.add(new TokenDatum(Pattern.compile("^(" + regex + ")"), type, name));
        }

        /**
         * Tokenize(str) takes in a string to be turned into tokens.
         * It sets a default flag value (match) to false then for each
         * possible token in the string, it sees if the token currently being looked at is a match
         * to any of the regex's stored in the tokenizer if it is, the flag is switched to true
         * the token is added to the token LinkedList, it's is replaced by the "" character,leading whitespace is trimmed
         * and the next token is evaluated
         * if it doesn't match, an exception is raised and tells the console what unexpected value appeared
         *
         * @param str the str
         */
        public void tokenize(String str) {
            String s = str.trim();
            tokens.clear();
            while (!s.equals("")) {
                boolean match = false;
                for (TokenDatum td : tokenData) {
                    Matcher m = td.regex.matcher(s);
                    if (m.find()) {
                        match = true;
                        String tkn = m.group().trim();
                        s = m.replaceFirst("").trim();
                        tokens.add(new Token(td.type, tkn, td.name));
                        break;
                    }
                }
                if (!match) throw new ParseException("Uh-oh! I wasn't expecting to see a: " + s + " here.");
            }
        }

        /**
         * @return tokens a LinkedList of Tokens
         */
        public LinkedList<Token> getTokens() {
            return tokens;
        }


}
