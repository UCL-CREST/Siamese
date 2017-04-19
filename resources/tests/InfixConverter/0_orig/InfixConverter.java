import java.lang.*;
import java.util.*;
import java.io.*;
class InfixConverter {
    public static String ReadString() {
        try {
            InputStreamReader input = new InputStreamReader ( System.in );
            BufferedReader reader = new BufferedReader ( input );
            return reader.readLine();
        } catch ( Exception e ) {
            e.printStackTrace();
            return "";
        }
    }
    public static int ReadInteger() {
        try {
            InputStreamReader input = new InputStreamReader ( System.in );
            BufferedReader reader = new BufferedReader ( input );
            return Integer.parseInt ( reader.readLine() );
        } catch ( Exception e ) {
            e.printStackTrace();
            return 0;
        }
    }
    public static String InfixToPostfixConvert ( String infixBuffer ) {
        int priority = 0;
        String postfixBuffer = "";
        Stack s1 = new Stack();
        for ( int i = 0; i < infixBuffer.length(); i++ ) {
            char ch = infixBuffer.charAt ( i );
            if ( ch == '+' || ch == '-' || ch == '*' || ch == '/' ) {
                if ( s1.size() <= 0 ) {
                    s1.push ( ch );
                } else {
                    Character chTop = ( Character ) s1.peek();
                    if ( chTop == '*' || chTop == '/' ) {
                        priority = 1;
                    } else {
                        priority = 0;
                    }
                    if ( priority == 1 ) {
                        if ( ch == '+' || ch == '-' ) {
                            postfixBuffer += s1.pop();
                            i--;
                        } else {
                            postfixBuffer += s1.pop();
                            i--;
                        }
                    } else {
                        if ( ch == '+' || ch == '-' ) {
                            postfixBuffer += s1.pop();
                            s1.push ( ch );
                        } else {
                            s1.push ( ch );
                        }
                    }
                }
            } else {
                postfixBuffer += ch;
            }
        }
        int len = s1.size();
        for ( int j = 0; j < len; j++ ) {
            postfixBuffer += s1.pop();
        }
        return postfixBuffer;
    }
    public static void main ( String[] args ) {
        String infixBuffer = "";
        String postfixBuffer = "";
        if ( args.length == 1 ) {
            infixBuffer = args[0];
            postfixBuffer = InfixToPostfixConvert ( infixBuffer );
            System.out.println ( "InFix  :\t" + infixBuffer );
            System.out.println ( "PostFix:\t" + postfixBuffer );
            System.out.println();
        } else {
            infixBuffer = "a+b*c";
            postfixBuffer = InfixToPostfixConvert ( infixBuffer );
            System.out.println ( "InFix  :\t" + infixBuffer );
            System.out.println ( "PostFix:\t" + postfixBuffer );
            System.out.println();
            infixBuffer = "a+b*c/d-e";
            postfixBuffer = InfixToPostfixConvert ( infixBuffer );
            System.out.println ( "InFix  :\t" + infixBuffer );
            System.out.println ( "PostFix:\t" + postfixBuffer );
            System.out.println();
            infixBuffer = "a+b*c/d-e+f*h/i+j-k";
            postfixBuffer = InfixToPostfixConvert ( infixBuffer );
            System.out.println ( "InFix  :\t" + infixBuffer );
            System.out.println ( "PostFix:\t" + postfixBuffer );
            System.out.println();
        }
    }
}
