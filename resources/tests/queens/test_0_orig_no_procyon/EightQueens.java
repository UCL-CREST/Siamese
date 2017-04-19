public class EightQueens {
    private int[] perm;
    private boolean[] used;
    private int numsols;
    public static void main ( final String[] array ) {
        final EightQueens eightQueens = new EightQueens ( 5 );
        eightQueens.solveIt();
        eightQueens.printNumSols();
    }
    public EightQueens ( final int n ) {
        super();
        this.perm = new int[n];
        this.used = new boolean[n];
        this.numsols = 0;
        for ( int i = 0; i < n; ++i ) {
            this.perm[i] = -1;
            this.used[i] = false;
        }
    }
    public void solveIt() {
        this.solveItRec ( 0 );
    }
    public void solveItRec ( final int n ) {
        if ( n == this.perm.length ) {
            this.printSol();
            ++this.numsols;
        }
        for ( int i = 0; i < this.perm.length; ++i ) {
            if ( !this.used[i] && !this.conflict ( n, i ) ) {
                this.perm[n] = i;
                this.used[i] = true;
                this.solveItRec ( n + 1 );
                this.used[i] = false;
            }
        }
    }
    private boolean conflict ( final int n, final int n2 ) {
        for ( int i = 0; i < n; ++i ) {
            if ( Math.abs ( n - i ) == Math.abs ( this.perm[i] - n2 ) ) {
                return true;
            }
        }
        return false;
    }
    public void printSol() {
        System.out.println ( "Here is a solution:\n" );
        for ( int i = 0; i < this.perm.length; ++i ) {
            for ( int j = 0; j < this.perm.length; ++j ) {
                if ( this.perm[j] == i ) {
                    System.out.print ( "Q " );
                } else {
                    System.out.print ( "_ " );
                }
            }
            System.out.println ( "\n" );
        }
    }
    public void printNumSols() {
        System.out.println ( "There were " + this.numsols + " solutions." );
    }
}
