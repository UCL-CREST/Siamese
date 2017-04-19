import java.io.*;
import java.lang.*;
import java.util.*;
class Main {
    static int movecount = 0;
    static public void Solve2DiscsTOH ( Stack source, Stack temp, Stack dest ) {
        temp.push ( source.pop() );
        movecount++;
        PrintStacks();
        dest.push ( source.pop() );
        movecount++;
        PrintStacks();
        dest.push ( temp.pop() );
        movecount++;
        PrintStacks();
    }
    static public int SolveTOH ( int nDiscs, Stack source, Stack temp, Stack dest ) {
        if ( nDiscs <= 4 ) {
            if ( ( nDiscs % 2 ) == 0 ) {
                Solve2DiscsTOH ( source, temp, dest );
                nDiscs = nDiscs - 1;
                if ( nDiscs == 1 ) {
                    return 1;
                }
                temp.push ( source.pop() );
                movecount++;
                PrintStacks();
                Solve2DiscsTOH ( dest, source, temp );
                dest.push ( source.pop() );
                movecount++;
                PrintStacks();
                SolveTOH ( nDiscs, temp, source, dest );
            } else {
                if ( nDiscs == 1 ) {
                    return -1;
                }
                Solve2DiscsTOH ( source, dest, temp );
                nDiscs = nDiscs - 1;
                dest.push ( source.pop() );
                movecount++;
                PrintStacks();
                Solve2DiscsTOH ( temp, source, dest );
            }
            return 1;
        } else if ( nDiscs >= 5 ) {
            SolveTOH ( nDiscs - 2, source, temp, dest );
            temp.push ( source.pop() );
            movecount++;
            PrintStacks();
            SolveTOH ( nDiscs - 2, dest, source, temp );
            dest.push ( source.pop() );
            movecount++;
            PrintStacks();
            SolveTOH ( nDiscs - 1, temp, source, dest );
        }
        return 1;
    }
    static public Stack A = new Stack();
    static public Stack B = new Stack();
    static public Stack C = new Stack();
    static public void PrintStacks() {
        if ( countA != A.size() ||
                countB != B.size() ||
                countC != C.size() ) {
            int diffA = A.size() - countA;
            int diffB = B.size() - countB;
            int diffC = C.size() - countC;
            if ( diffA == 1 ) {
                if ( diffB == -1 ) {
                    System.out.print ( "Move Disc " + A.peek() + " From B To A" );
                } else {
                    System.out.print ( "Move Disc " + A.peek() + " From C To A" );
                }
            } else if ( diffB == 1 ) {
                if ( diffA == -1 ) {
                    System.out.print ( "Move Disc " + B.peek() + " From A To B" );
                } else {
                    System.out.print ( "Move Disc " + B.peek() + " From C To B" );
                }
            } else {
                if ( diffA == -1 ) {
                    System.out.print ( "Move Disc " + C.peek() + " From A To C" );
                } else {
                    System.out.print ( "Move Disc " + C.peek() + " From B To C" );
                }
            }
            countA = A.size();
            countB = B.size();
            countC = C.size();
            System.out.println();
        }
        PrintStack ( A );
        System.out.print ( " , " );
        PrintStack ( B );
        System.out.print ( " , " );
        PrintStack ( C );
        System.out.print ( " , " );
    }
    static int countA = 0;
    static int countB = 0;
    static int countC = 0;
    static public void PrintStack ( Stack s ) {
        System.out.print ( s.toString() );
    }
    public static void main ( String[] args ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                int maxdisc = 0;
                String inpstring = "";
                InputStreamReader input = new InputStreamReader ( System.in );
                BufferedReader reader = new BufferedReader ( input );
                inpstring = reader.readLine();
                movecount = 0;
                maxdisc = Integer.parseInt ( inpstring );
                if ( maxdisc == -1 ) {
                    System.out.println ( "Good Bye!" );
                    return;
                }
                if ( maxdisc <= 1 || maxdisc >= 10 ) {
                    System.out.println ( "Enter between 2 - 9" );
                    continue;
                }
                for ( int i = maxdisc; i >= 1; i-- ) {
                    A.push ( i );
                }
                countA = A.size();
                countB = B.size();
                countC = C.size();
                PrintStacks();
                SolveTOH ( maxdisc, A, B, C );
                System.out.println ( "Total Moves = " + movecount );
                while ( C.size() > 0 ) {
                    C.pop();
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
