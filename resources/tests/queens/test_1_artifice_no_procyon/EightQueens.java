public class EightQueens {
    private int[] f00;
    private boolean[] f10;
    private int f20;
    public static void main ( final String[] array ) {
        final EightQueens eightQueens = new EightQueens ( 5 );
        eightQueens.m00();
        eightQueens.m40();
    }
    public EightQueens ( final int n ) {
        super();
        this.f00 = new int[n];
        this.f10 = new boolean[n];
        this.f20 = 0;
        for ( int i = 0; i < n; ++i ) {
            this.f00[i] = -1;
            this.f10[i] = false;
        }
    }
    public void m00() {
        this.m10 ( 0 );
    }
    public void m10 ( final int n ) {
        if ( n == this.f00.length ) {
            this.m30();
            ++this.f20;
        }
        for ( int i = 0; i < this.f00.length; ++i ) {
            if ( !this.f10[i] && !this.m20 ( n, i ) ) {
                this.f00[n] = i;
                this.f10[i] = true;
                this.m10 ( n + 1 );
                this.f10[i] = false;
            }
        }
    }
    private boolean m20 ( final int n, final int n2 ) {
        for ( int i = 0; i < n; ++i ) {
            if ( Math.abs ( n - i ) == Math.abs ( this.f00[i] - n2 ) ) {
                return true;
            }
        }
        return false;
    }
    public void m30() {
        System.out.println ( "Here is a solution:\n" );
        for ( int i = 0; i < this.f00.length; ++i ) {
            for ( int j = 0; j < this.f00.length; ++j ) {
                if ( this.f00[j] == i ) {
                    System.out.print ( "Q " );
                } else {
                    System.out.print ( "_ " );
                }
            }
            System.out.println ( "\n" );
        }
    }
    public void m40() {
        System.out.println ( "There were " + this.f20 + " solutions." );
    }
}
