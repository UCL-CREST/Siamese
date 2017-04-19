import java.util.*;
public class EightQueens {
    private int[] perm;
    private boolean[] used;
    private int numsols;
    public static void main ( String[] args ) {
        EightQueens obj = new EightQueens ( 5 );
        obj.solveIt();
        obj.printNumSols();
    }
    public EightQueens ( int n ) {
        perm = new int[n];
        used = new boolean[n];
        numsols = 0;
        for ( int i = 0; i < n; i++ ) {
            perm[i] = -1;
            used[i] = false;
        }
    }
    public void solveIt() {
        solveItRec ( 0 );
    }
    public void solveItRec ( int location ) {
        int i;
        if ( location == perm.length ) {
            printSol();
            numsols++;
        }
        for ( i = 0; i < perm.length; i++ ) {
            if ( used[i] == false ) {
                if ( !conflict ( location, i ) ) {
                    perm[location] = i;
                    used[i] = true;
                    solveItRec ( location + 1 );
                    used[i] = false;
                }
            }
        }
    }
    private boolean conflict ( int location, int row ) {
        int i;
        for ( i = 0; i < location; i++ )
            if ( Math.abs ( location - i ) == Math.abs ( perm[i] - row ) ) {
                return true;
            }
        return false;
    }
    public void printSol() {
        int i, j;
        System.out.println ( "Here is a solution:\n" );
        for ( i = 0; i < perm.length; i++ ) {
            for ( j = 0; j < perm.length; j++ ) {
                if ( perm[j] == i ) {
                    System.out.print ( "Q " );
                } else {
                    System.out.print ( "_ " );
                }
            }
            System.out.println ( "\n" );
        }
    }
    public void printNumSols() {
        System.out.println ( "There were " + numsols + " solutions." );
    }
}
