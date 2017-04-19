import java.io.*;
import java.util.*;
class InfixConverter {
    public static String ReadString() {
        try {
            return new BufferedReader ( new InputStreamReader ( System.in ) ).readLine();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            return "";
        }
    }
    public static int ReadInteger() {
        try {
            return Integer.parseInt ( new BufferedReader ( new InputStreamReader ( System.in ) ).readLine() );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            return 0;
        }
    }
    public static String InfixToPostfixConvert ( final String s ) {
        String s2 = "";
        final Stack<Character> stack = new Stack<Character>();
        for ( int i = 0; i < s.length(); ++i ) {
            final char char1 = s.charAt ( i );
            if ( char1 == '+' || char1 == '-' || char1 == '*' || char1 == '/' ) {
                if ( stack.size() <= 0 ) {
                    stack.push ( char1 );
                } else {
                    final Character c = stack.peek();
                    if ( c == '*' || c == '/' ) {
                        if ( char1 == '+' || char1 == '-' ) {
                            s2 += stack.pop();
                            --i;
                        } else {
                            s2 += stack.pop();
                            --i;
                        }
                    } else if ( char1 == '+' || char1 == '-' ) {
                        s2 += stack.pop();
                        stack.push ( char1 );
                    } else {
                        stack.push ( char1 );
                    }
                }
            } else {
                s2 += char1;
            }
        }
        for ( int size = stack.size(), j = 0; j < size; ++j ) {
            s2 += stack.pop();
        }
        return s2;
    }
    public static void main ( final String[] array ) {
        if ( array.length == 1 ) {
            final String s = array[0];
            final String infixToPostfixConvert = InfixToPostfixConvert ( s );
            System.out.println ( "InFix  :\t" + s );
            System.out.println ( "PostFix:\t" + infixToPostfixConvert );
            System.out.println();
        } else {
            final String s2 = "a+b*c";
            final String infixToPostfixConvert2 = InfixToPostfixConvert ( s2 );
            System.out.println ( "InFix  :\t" + s2 );
            System.out.println ( "PostFix:\t" + infixToPostfixConvert2 );
            System.out.println();
            final String s3 = "a+b*c/d-e";
            final String infixToPostfixConvert3 = InfixToPostfixConvert ( s3 );
            System.out.println ( "InFix  :\t" + s3 );
            System.out.println ( "PostFix:\t" + infixToPostfixConvert3 );
            System.out.println();
            final String s4 = "a+b*c/d-e+f*h/i+j-k";
            final String infixToPostfixConvert4 = InfixToPostfixConvert ( s4 );
            System.out.println ( "InFix  :\t" + s4 );
            System.out.println ( "PostFix:\t" + infixToPostfixConvert4 );
            System.out.println();
        }
    }
}
