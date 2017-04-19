import java.util.*;
import java.io.*;
class Main {
    static int f00;
    public static Stack f10;
    public static Stack f20;
    public static Stack f30;
    static int f40;
    static int f50;
    static int f60;
    public static void m00 ( final Stack stack, final Stack stack2, final Stack stack3 ) {
        stack2.push ( stack.pop() );
        ++Main.f00;
        m20();
        stack3.push ( stack.pop() );
        ++Main.f00;
        m20();
        stack3.push ( stack2.pop() );
        ++Main.f00;
        m20();
    }
    public static int m10 ( int n, final Stack stack, final Stack stack2, final Stack stack3 ) {
        if ( n <= 4 ) {
            if ( n % 2 == 0 ) {
                m00 ( stack, stack2, stack3 );
                --n;
                if ( n == 1 ) {
                    return 1;
                }
                stack2.push ( stack.pop() );
                ++Main.f00;
                m20();
                m00 ( stack3, stack, stack2 );
                stack3.push ( stack.pop() );
                ++Main.f00;
                m20();
                m10 ( n, stack2, stack, stack3 );
            } else {
                if ( n == 1 ) {
                    return -1;
                }
                m00 ( stack, stack3, stack2 );
                --n;
                stack3.push ( stack.pop() );
                ++Main.f00;
                m20();
                m00 ( stack2, stack, stack3 );
            }
            return 1;
        }
        if ( n >= 5 ) {
            m10 ( n - 2, stack, stack2, stack3 );
            stack2.push ( stack.pop() );
            ++Main.f00;
            m20();
            m10 ( n - 2, stack3, stack, stack2 );
            stack3.push ( stack.pop() );
            ++Main.f00;
            m20();
            m10 ( n - 1, stack2, stack, stack3 );
        }
        return 1;
    }
    public static void m20() {
        if ( Main.f40 != Main.f10.size() || Main.f50 != Main.f20.size() || Main.f60 != Main.f30.size() ) {
            final int n = Main.f10.size() - Main.f40;
            final int n2 = Main.f20.size() - Main.f50;
            final int n3 = Main.f30.size() - Main.f60;
            if ( n == 1 ) {
                if ( n2 == -1 ) {
                    System.out.print ( "Move Disc " + Main.f10.peek() + " From B To A" );
                } else {
                    System.out.print ( "Move Disc " + Main.f10.peek() + " From C To A" );
                }
            } else if ( n2 == 1 ) {
                if ( n == -1 ) {
                    System.out.print ( "Move Disc " + Main.f20.peek() + " From A To B" );
                } else {
                    System.out.print ( "Move Disc " + Main.f20.peek() + " From C To B" );
                }
            } else if ( n == -1 ) {
                System.out.print ( "Move Disc " + Main.f30.peek() + " From A To C" );
            } else {
                System.out.print ( "Move Disc " + Main.f30.peek() + " From B To C" );
            }
            Main.f40 = Main.f10.size();
            Main.f50 = Main.f20.size();
            Main.f60 = Main.f30.size();
            System.out.println();
        }
        m30 ( Main.f10 );
        System.out.print ( " , " );
        m30 ( Main.f20 );
        System.out.print ( " , " );
        m30 ( Main.f30 );
        System.out.print ( " , " );
    }
    public static void m30 ( final Stack stack ) {
        System.out.print ( stack.toString() );
    }
    public static void main ( final String[] array ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                final String line = new BufferedReader ( new InputStreamReader ( System.in ) ).readLine();
                Main.f00 = 0;
                final int int1 = Integer.parseInt ( line );
                if ( int1 == -1 ) {
                    break;
                }
                if ( int1 <= 1 || int1 >= 10 ) {
                    System.out.println ( "Enter between 2 - 9" );
                } else {
                    for ( int i = int1; i >= 1; --i ) {
                        Main.f10.push ( i );
                    }
                    Main.f40 = Main.f10.size();
                    Main.f50 = Main.f20.size();
                    Main.f60 = Main.f30.size();
                    m20();
                    m10 ( int1, Main.f10, Main.f20, Main.f30 );
                    System.out.println ( "Total Moves = " + Main.f00 );
                    while ( Main.f30.size() > 0 ) {
                        Main.f30.pop();
                    }
                }
            }
            System.out.println ( "Good Bye!" );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    static {
        Main.f00 = 0;
        Main.f10 = new Stack();
        Main.f20 = new Stack();
        Main.f30 = new Stack();
        Main.f40 = 0;
        Main.f50 = 0;
        Main.f60 = 0;
    }
}
