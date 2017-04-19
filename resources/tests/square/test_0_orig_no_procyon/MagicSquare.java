public class MagicSquare {
    private int[][] square;
    private boolean[] possible;
    private int totalSqs;
    private int sum;
    private int numsquares;
    public static void main ( final String[] array ) {
        final MagicSquare magicSquare = new MagicSquare ( 3 );
        magicSquare.fill ( 0, 0 );
        System.out.println ( "There were " + magicSquare.getTotalSolutions() + " number of solutions." );
    }
    public MagicSquare ( final int n ) {
        super();
        this.square = new int[n][n];
        for ( int i = 0; i < n; ++i ) {
            for ( int j = 0; j < n; ++j ) {
                this.square[i][j] = 0;
            }
        }
        this.totalSqs = n * n;
        this.possible = new boolean[this.totalSqs];
        for ( int k = 0; k < this.totalSqs; ++k ) {
            this.possible[k] = true;
        }
        this.sum = n * ( n * n + 1 ) / 2;
        this.numsquares = 0;
    }
    public void fill ( final int n, final int n2 ) {
        if ( !this.checkRows() || !this.checkCols() || !this.checkDiags() ) {
            return;
        }
        if ( n == this.square.length ) {
            System.out.println ( this );
            ++this.numsquares;
            return;
        }
        for ( int i = 0; i < this.totalSqs; ++i ) {
            if ( this.possible[i] ) {
                this.square[n][n2] = i + 1;
                this.possible[i] = false;
                int n3 = n2 + 1;
                int n4 = n;
                if ( n3 == this.square.length ) {
                    ++n4;
                    n3 = 0;
                }
                this.fill ( n4, n3 );
                this.square[n][n2] = 0;
                this.possible[i] = true;
            }
        }
    }
    public boolean checkRows() {
        for ( int i = 0; i < this.square.length; ++i ) {
            int n = 0;
            boolean b = false;
            for ( int j = 0; j < this.square[i].length; ++j ) {
                n += this.square[i][j];
                if ( this.square[i][j] == 0 ) {
                    b = true;
                }
            }
            if ( !b && n != this.sum ) {
                return false;
            }
        }
        return true;
    }
    public boolean checkCols() {
        for ( int i = 0; i < this.square[0].length; ++i ) {
            int n = 0;
            boolean b = false;
            for ( int j = 0; j < this.square.length; ++j ) {
                n += this.square[j][i];
                if ( this.square[j][i] == 0 ) {
                    b = true;
                }
            }
            if ( !b && n != this.sum ) {
                return false;
            }
        }
        return true;
    }
    public boolean checkDiags() {
        int n = 0;
        boolean b = false;
        for ( int i = 0; i < this.square.length; ++i ) {
            n += this.square[i][i];
            if ( this.square[i][i] == 0 ) {
                b = true;
            }
        }
        if ( !b && n != this.sum ) {
            return false;
        }
        int n2 = 0;
        boolean b2 = false;
        for ( int j = 0; j < this.square.length; ++j ) {
            n2 += this.square[j][this.square.length - 1 - j];
            if ( this.square[j][this.square.length - 1 - j] == 0 ) {
                b2 = true;
            }
        }
        return b2 || n2 == this.sum;
    }
    public int getTotalSolutions() {
        return this.numsquares;
    }
    public String toString() {
        String s = "";
        for ( int i = 0; i < this.square.length; ++i ) {
            for ( int j = 0; j < this.square[i].length; ++j ) {
                s = s + this.square[i][j] + "\t";
            }
            s += "\n";
        }
        return s;
    }
}
