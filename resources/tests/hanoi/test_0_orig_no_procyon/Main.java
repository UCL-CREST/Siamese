import java.util.*;
import java.io.*;
class Main {
    static int movecount;
    public static Stack A;
    public static Stack B;
    public static Stack C;
    static int countA;
    static int countB;
    static int countC;
    public static void Solve2DiscsTOH ( final Stack stack, final Stack stack2, final Stack stack3 ) {
        stack2.push ( stack.pop() );
        ++Main.movecount;
        PrintStacks();
        stack3.push ( stack.pop() );
        ++Main.movecount;
        PrintStacks();
        stack3.push ( stack2.pop() );
        ++Main.movecount;
        PrintStacks();
    }
    public static int SolveTOH ( int n, final Stack stack, final Stack stack2, final Stack stack3 ) {
        if ( n <= 4 ) {
            if ( n % 2 == 0 ) {
                Solve2DiscsTOH ( stack, stack2, stack3 );
                --n;
                if ( n == 1 ) {
                    return 1;
                }
                stack2.push ( stack.pop() );
                ++Main.movecount;
                PrintStacks();
                Solve2DiscsTOH ( stack3, stack, stack2 );
                stack3.push ( stack.pop() );
                ++Main.movecount;
                PrintStacks();
                SolveTOH ( n, stack2, stack, stack3 );
            } else {
                if ( n == 1 ) {
                    return -1;
                }
                Solve2DiscsTOH ( stack, stack3, stack2 );
                --n;
                stack3.push ( stack.pop() );
                ++Main.movecount;
                PrintStacks();
                Solve2DiscsTOH ( stack2, stack, stack3 );
            }
            return 1;
        }
        if ( n >= 5 ) {
            SolveTOH ( n - 2, stack, stack2, stack3 );
            stack2.push ( stack.pop() );
            ++Main.movecount;
            PrintStacks();
            SolveTOH ( n - 2, stack3, stack, stack2 );
            stack3.push ( stack.pop() );
            ++Main.movecount;
            PrintStacks();
            SolveTOH ( n - 1, stack2, stack, stack3 );
        }
        return 1;
    }
    public static void PrintStacks() {
        if ( Main.countA != Main.A.size() || Main.countB != Main.B.size() || Main.countC != Main.C.size() ) {
            final int n = Main.A.size() - Main.countA;
            final int n2 = Main.B.size() - Main.countB;
            final int n3 = Main.C.size() - Main.countC;
            if ( n == 1 ) {
                if ( n2 == -1 ) {
                    System.out.print ( "Move Disc " + Main.A.peek() + " From B To A" );
                } else {
                    System.out.print ( "Move Disc " + Main.A.peek() + " From C To A" );
                }
            } else if ( n2 == 1 ) {
                if ( n == -1 ) {
                    System.out.print ( "Move Disc " + Main.B.peek() + " From A To B" );
                } else {
                    System.out.print ( "Move Disc " + Main.B.peek() + " From C To B" );
                }
            } else if ( n == -1 ) {
                System.out.print ( "Move Disc " + Main.C.peek() + " From A To C" );
            } else {
                System.out.print ( "Move Disc " + Main.C.peek() + " From B To C" );
            }
            Main.countA = Main.A.size();
            Main.countB = Main.B.size();
            Main.countC = Main.C.size();
            System.out.println();
        }
        PrintStack ( Main.A );
        System.out.print ( " , " );
        PrintStack ( Main.B );
        System.out.print ( " , " );
        PrintStack ( Main.C );
        System.out.print ( " , " );
    }
    public static void PrintStack ( final Stack stack ) {
        System.out.print ( stack.toString() );
    }
    public static void main ( final String[] array ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                final String line = new BufferedReader ( new InputStreamReader ( System.in ) ).readLine();
                Main.movecount = 0;
                final int int1 = Integer.parseInt ( line );
                if ( int1 == -1 ) {
                    break;
                }
                if ( int1 <= 1 || int1 >= 10 ) {
                    System.out.println ( "Enter between 2 - 9" );
                } else {
                    for ( int i = int1; i >= 1; --i ) {
                        Main.A.push ( i );
                    }
                    Main.countA = Main.A.size();
                    Main.countB = Main.B.size();
                    Main.countC = Main.C.size();
                    PrintStacks();
                    SolveTOH ( int1, Main.A, Main.B, Main.C );
                    System.out.println ( "Total Moves = " + Main.movecount );
                    while ( Main.C.size() > 0 ) {
                        Main.C.pop();
                    }
                }
            }
            System.out.println ( "Good Bye!" );
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
    static {
        Main.movecount = 0;
        Main.A = new Stack();
        Main.B = new Stack();
        Main.C = new Stack();
        Main.countA = 0;
        Main.countB = 0;
        Main.countC = 0;
    }
}
