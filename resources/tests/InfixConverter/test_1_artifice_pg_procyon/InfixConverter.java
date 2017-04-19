import java.util.*;
class InfixConverter {
    private static String a ( final String s ) {
        String s2 = "";
        final Stack<Character> stack = new Stack<Character>();
        for ( int i = 0; i < s.length(); ++i ) {
            final char char1;
            if ( ( char1 = s.charAt ( i ) ) == '+' || char1 == '-' || char1 == '*' || char1 == '/' ) {
                if ( stack.size() <= 0 ) {
                    stack.push ( char1 );
                } else {
                    final Character c;
                    if ( ( c = stack.peek() ) == '*' || c == '/' ) {
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
            final String s;
            final String a = a ( s = array[0] );
            System.out.println ( "InFix  :\t" + s );
            System.out.println ( "PostFix:\t" + a );
            System.out.println();
            return;
        }
        final String s2;
        final String a2 = a ( s2 = "a+b*c" );
        System.out.println ( "InFix  :\t" + s2 );
        System.out.println ( "PostFix:\t" + a2 );
        System.out.println();
        final String s3;
        final String a3 = a ( s3 = "a+b*c/d-e" );
        System.out.println ( "InFix  :\t" + s3 );
        System.out.println ( "PostFix:\t" + a3 );
        System.out.println();
        final String s4;
        final String a4 = a ( s4 = "a+b*c/d-e+f*h/i+j-k" );
        System.out.println ( "InFix  :\t" + s4 );
        System.out.println ( "PostFix:\t" + a4 );
        System.out.println();
    }
}
