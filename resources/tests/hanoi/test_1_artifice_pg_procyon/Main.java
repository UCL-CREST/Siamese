import java.util.*;
import java.io.*;
class Main {
    private static int a;
    private static Stack b;
    private static Stack c;
    private static Stack d;
    private static int e;
    private static int f;
    private static int g;
    private static void a ( final Stack stack, final Stack stack2, final Stack stack3 ) {
        stack2.push ( stack.pop() );
        ++Main.a;
        a();
        stack3.push ( stack.pop() );
        ++Main.a;
        a();
        stack3.push ( stack2.pop() );
        ++Main.a;
        a();
    }
    private static int a ( int n, final Stack stack, final Stack stack2, final Stack stack3 ) {
        if ( n <= 4 ) {
            if ( n % 2 == 0 ) {
                a ( stack, stack2, stack3 );
                if ( --n == 1 ) {
                    return 1;
                }
                stack2.push ( stack.pop() );
                ++Main.a;
                a();
                a ( stack3, stack, stack2 );
                stack3.push ( stack.pop() );
                ++Main.a;
                a();
                a ( n, stack2, stack, stack3 );
            } else {
                if ( n == 1 ) {
                    return -1;
                }
                a ( stack, stack3, stack2 );
                stack3.push ( stack.pop() );
                ++Main.a;
                a();
                a ( stack2, stack, stack3 );
            }
            return 1;
        }
        if ( n >= 5 ) {
            a ( n - 2, stack, stack2, stack3 );
            stack2.push ( stack.pop() );
            ++Main.a;
            a();
            a ( n - 2, stack3, stack, stack2 );
            stack3.push ( stack.pop() );
            ++Main.a;
            a();
            a ( n - 1, stack2, stack, stack3 );
        }
        return 1;
    }
    private static void a() {
        if ( Main.e != Main.b.size() || Main.f != Main.c.size() || Main.g != Main.d.size() ) {
            final int n = Main.b.size() - Main.e;
            final int n2 = Main.c.size() - Main.f;
            Main.d.size();
            final int g = Main.g;
            if ( n == 1 ) {
                if ( n2 == -1 ) {
                    System.out.print ( "Move Disc " + Main.b.peek() + " From B To A" );
                } else {
                    System.out.print ( "Move Disc " + Main.b.peek() + " From C To A" );
                }
            } else if ( n2 == 1 ) {
                if ( n == -1 ) {
                    System.out.print ( "Move Disc " + Main.c.peek() + " From A To B" );
                } else {
                    System.out.print ( "Move Disc " + Main.c.peek() + " From C To B" );
                }
            } else if ( n == -1 ) {
                System.out.print ( "Move Disc " + Main.d.peek() + " From A To C" );
            } else {
                System.out.print ( "Move Disc " + Main.d.peek() + " From B To C" );
            }
            Main.e = Main.b.size();
            Main.f = Main.c.size();
            Main.g = Main.d.size();
            System.out.println();
        }
        a ( Main.b );
        System.out.print ( " , " );
        a ( Main.c );
        System.out.print ( " , " );
        a ( Main.d );
        System.out.print ( " , " );
    }
    private static void a ( final Stack stack ) {
        System.out.print ( stack.toString() );
    }
    public static void main ( final String[] array ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                final String line = new BufferedReader ( new InputStreamReader ( System.in ) ).readLine();
                Main.a = 0;
                final int int1;
                if ( ( int1 = Integer.parseInt ( line ) ) == -1 ) {
                    break;
                }
                if ( int1 <= 1 || int1 >= 10 ) {
                    System.out.println ( "Enter between 2 - 9" );
                } else {
                    for ( int i = int1; i > 0; --i ) {
                        Main.b.push ( i );
                    }
                    Main.e = Main.b.size();
                    Main.f = Main.c.size();
                    Main.g = Main.d.size();
                    a();
                    a ( int1, Main.b, Main.c, Main.d );
                    System.out.println ( "Total Moves = " + Main.a );
                    while ( Main.d.size() > 0 ) {
                        Main.d.pop();
                    }
                }
            }
            System.out.println ( "Good Bye!" );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    static {
        Main.a = 0;
        Main.b = new Stack();
        Main.c = new Stack();
        Main.d = new Stack();
        Main.e = 0;
        Main.f = 0;
        Main.g = 0;
    }
}
