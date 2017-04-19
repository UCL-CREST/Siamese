public class EightQueens {
    private int[] f00;
    private boolean[] f10;
    private int f20;
    public static void main ( String[] a ) {
        EightQueens a0 = new EightQueens ( 5 );
        a0.m00();
        a0.m40();
    }
    public EightQueens ( int i ) {
        super();
        this.f00 = new int[i];
        this.f10 = new boolean[i];
        this.f20 = 0;
        int i0 = 0;
        while ( i0 < i ) {
            this.f00[i0] = -1;
            this.f10[i0] = false;
            i0 = i0 + 1;
        }
    }
    public void m00() {
        this.m10 ( 0 );
    }
    public void m10 ( int i ) {
        if ( i == this.f00.length ) {
            this.m30();
            this.f20 = this.f20 + 1;
        }
        int i0 = 0;
        while ( i0 < this.f00.length ) {
            if ( !this.f10[i0] && !this.m20 ( i, i0 ) ) {
                this.f00[i] = i0;
                this.f10[i0] = true;
                this.m10 ( i + 1 );
                this.f10[i0] = false;
            }
            i0 = i0 + 1;
        }
    }
    private boolean m20 ( int i, int i0 ) {
        int i1 = 0;
        while ( i1 < i ) {
            if ( Math.abs ( i - i1 ) != Math.abs ( this.f00[i1] - i0 ) ) {
                i1 = i1 + 1;
            } else {
                return true;
            }
        }
        return false;
    }
    public void m30() {
        System.out.println ( "Here is a solution:\n" );
        int i = 0;
        while ( i < this.f00.length ) {
            int i0 = 0;
            while ( i0 < this.f00.length ) {
                if ( this.f00[i0] != i ) {
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
    public void m40() {
        System.out.println ( new StringBuilder().append ( "There were " ).append ( this.f20 ).append ( " solutions." ).toString() );
    }
}
