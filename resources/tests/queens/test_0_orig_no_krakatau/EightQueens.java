public class EightQueens {
    private int[] perm;
    private boolean[] used;
    private int numsols;
    public static void main ( String[] a ) {
        EightQueens a0 = new EightQueens ( 5 );
        a0.solveIt();
        a0.printNumSols();
    }
    public EightQueens ( int i ) {
        super();
        this.perm = new int[i];
        this.used = new boolean[i];
        this.numsols = 0;
        int i0 = 0;
        while ( i0 < i ) {
            this.perm[i0] = -1;
            this.used[i0] = false;
            i0 = i0 + 1;
        }
    }
    public void solveIt() {
        this.solveItRec ( 0 );
    }
    public void solveItRec ( int i ) {
        if ( i == this.perm.length ) {
            this.printSol();
            this.numsols = this.numsols + 1;
        }
        int i0 = 0;
        while ( i0 < this.perm.length ) {
            if ( !this.used[i0] && !this.conflict ( i, i0 ) ) {
                this.perm[i] = i0;
                this.used[i0] = true;
                this.solveItRec ( i + 1 );
                this.used[i0] = false;
            }
            i0 = i0 + 1;
        }
    }
    private boolean conflict ( int i, int i0 ) {
        int i1 = 0;
        while ( i1 < i ) {
            if ( Math.abs ( i - i1 ) != Math.abs ( this.perm[i1] - i0 ) ) {
                i1 = i1 + 1;
            } else {
                return true;
            }
        }
        return false;
    }
    public void printSol() {
        System.out.println ( "Here is a solution:\n" );
        int i = 0;
        while ( i < this.perm.length ) {
            int i0 = 0;
            while ( i0 < this.perm.length ) {
                if ( this.perm[i0] != i ) {
                    System.out.print ( "_ " );
                } else {
                    System.out.print ( "Q " );
                }
                i0 = i0 + 1;
            }
            System.out.println ( "\n" );
            i = i + 1;
        }
    }
    public void printNumSols() {
        System.out.println ( new StringBuilder().append ( "There were " ).append ( this.numsols ).append ( " solutions." ).toString() );
    }
}
