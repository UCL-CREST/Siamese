public class MagicSquare {
    private int[][] f00;
    private boolean[] f10;
    private int f20;
    private int f30;
    private int f40;
    public static void main ( final String[] array ) {
        final MagicSquare magicSquare = new MagicSquare ( 3 );
        magicSquare.m00 ( 0, 0 );
        System.out.println ( "There were " + magicSquare.m40() + " number of solutions." );
    }
    public MagicSquare ( final int n ) {
        super();
        this.f00 = new int[n][n];
        for ( int i = 0; i < n; ++i ) {
            for ( int j = 0; j < n; ++j ) {
                this.f00[i][j] = 0;
            }
        }
        this.f20 = n * n;
        this.f10 = new boolean[this.f20];
        for ( int k = 0; k < this.f20; ++k ) {
            this.f10[k] = true;
        }
        this.f30 = n * ( n * n + 1 ) / 2;
        this.f40 = 0;
    }
    public void m00 ( final int n, final int n2 ) {
        if ( !this.m10() || !this.m20() || !this.m30() ) {
            return;
        }
        if ( n == this.f00.length ) {
            System.out.println ( this );
            ++this.f40;
            return;
        }
        for ( int i = 0; i < this.f20; ++i ) {
            if ( this.f10[i] ) {
                this.f00[n][n2] = i + 1;
                this.f10[i] = false;
                int n3 = n2 + 1;
                int n4 = n;
                if ( n3 == this.f00.length ) {
                    ++n4;
                    n3 = 0;
                }
                this.m00 ( n4, n3 );
                this.f00[n][n2] = 0;
                this.f10[i] = true;
            }
        }
    }
    public boolean m10() {
        for ( int i = 0; i < this.f00.length; ++i ) {
            int n = 0;
            boolean b = false;
            for ( int j = 0; j < this.f00[i].length; ++j ) {
                n += this.f00[i][j];
                if ( this.f00[i][j] == 0 ) {
                    b = true;
                }
            }
            if ( !b && n != this.f30 ) {
                return false;
            }
        }
        return true;
    }
    public boolean m20() {
        for ( int i = 0; i < this.f00[0].length; ++i ) {
            int n = 0;
            boolean b = false;
            for ( int j = 0; j < this.f00.length; ++j ) {
                n += this.f00[j][i];
                if ( this.f00[j][i] == 0 ) {
                    b = true;
                }
            }
            if ( !b && n != this.f30 ) {
                return false;
            }
        }
        return true;
    }
    public boolean m30() {
        int n = 0;
        boolean b = false;
        for ( int i = 0; i < this.f00.length; ++i ) {
            n += this.f00[i][i];
            if ( this.f00[i][i] == 0 ) {
                b = true;
            }
        }
        if ( !b && n != this.f30 ) {
            return false;
        }
        int n2 = 0;
        boolean b2 = false;
        for ( int j = 0; j < this.f00.length; ++j ) {
            n2 += this.f00[j][this.f00.length - 1 - j];
            if ( this.f00[j][this.f00.length - 1 - j] == 0 ) {
                b2 = true;
            }
        }
        return b2 || n2 == this.f30;
    }
    public int m40() {
        return this.f40;
    }
    public String toString() {
        String s = "";
        for ( int i = 0; i < this.f00.length; ++i ) {
            for ( int j = 0; j < this.f00[i].length; ++j ) {
                s = s + ( 0 + this.f00[i][j] ) + "\t";
            }
            s += "0\n";
        }
        return s;
    }
}
