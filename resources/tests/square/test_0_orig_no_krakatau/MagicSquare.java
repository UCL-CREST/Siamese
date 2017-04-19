public class MagicSquare {
    private int[][] square;
    private boolean[] possible;
    private int totalSqs;
    private int sum;
    private int numsquares;
    public static void main ( String[] a ) {
        MagicSquare a0 = new MagicSquare ( 3 );
        a0.fill ( 0, 0 );
        System.out.println ( new StringBuilder().append ( "There were " ).append ( a0.getTotalSolutions() ).append ( " number of solutions." ).toString() );
    }
    public MagicSquare ( int i ) {
        super();
        this.square = new int[i][i];
        int i0 = 0;
        while ( i0 < i ) {
            int i1 = 0;
            while ( i1 < i ) {
                this.square[i0][i1] = 0;
                i1 = i1 + 1;
            }
            i0 = i0 + 1;
        }
        this.totalSqs = i * i;
        this.possible = new boolean[this.totalSqs];
        int i2 = 0;
        while ( i2 < this.totalSqs ) {
            this.possible[i2] = true;
            i2 = i2 + 1;
        }
        this.sum = i * ( i * i + 1 ) / 2;
        this.numsquares = 0;
    }
    public void fill ( int i, int i0 ) {
        if ( this.checkRows() && this.checkCols() && this.checkDiags() ) {
            if ( i != this.square.length ) {
                int i1 = 0;
                while ( i1 < this.totalSqs ) {
                    if ( this.possible[i1] ) {
                        int i2 = 0;
                        int i3 = 0;
                        this.square[i][i0] = i1 + 1;
                        this.possible[i1] = false;
                        int i4 = i0 + 1;
                        if ( i4 != this.square.length ) {
                            i2 = i4;
                            i3 = i;
                        } else {
                            int i5 = i + 1;
                            i2 = 0;
                            i3 = i5;
                        }
                        this.fill ( i3, i2 );
                        this.square[i][i0] = 0;
                        this.possible[i1] = true;
                    }
                    i1 = i1 + 1;
                }
                return;
            } else {
                System.out.println ( ( Object ) this );
                this.numsquares = this.numsquares + 1;
                return;
            }
        }
    }
    public boolean checkRows() {
        int i = 0;
        while ( i < this.square.length ) {
            int i0 = 0;
            int i1 = 0;
            int i2 = 0;
            while ( i2 < this.square[i].length ) {
                int i3 = i0 + this.square[i][i2];
                int i4 = ( this.square[i][i2] != 0 ) ? i1 : 1;
                int i5 = i2 + 1;
                i0 = i3;
                i1 = i4;
                i2 = i5;
            }
            if ( i1 == 0 && i0 != this.sum ) {
                return false;
            }
            i = i + 1;
        }
        return true;
    }
    public boolean checkCols() {
        int i = 0;
        while ( i < this.square[0].length ) {
            int i0 = 0;
            int i1 = 0;
            int i2 = 0;
            while ( i2 < this.square.length ) {
                int i3 = i0 + this.square[i2][i];
                int i4 = ( this.square[i2][i] != 0 ) ? i1 : 1;
                int i5 = i2 + 1;
                i0 = i3;
                i1 = i4;
                i2 = i5;
            }
            if ( i1 == 0 && i0 != this.sum ) {
                return false;
            }
            i = i + 1;
        }
        return true;
    }
    public boolean checkDiags() {
        int i = 0;
        int i0 = 0;
        int i1 = 0;
        while ( i1 < this.square.length ) {
            int i2 = i + this.square[i1][i1];
            int i3 = ( this.square[i1][i1] != 0 ) ? i0 : 1;
            int i4 = i1 + 1;
            i = i2;
            i0 = i3;
            i1 = i4;
        }
        if ( i0 == 0 && i != this.sum ) {
            return false;
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while ( i7 < this.square.length ) {
            int i8 = i5 + this.square[i7][this.square.length - 1 - i7];
            int i9 = ( this.square[i7][this.square.length - 1 - i7] != 0 ) ? i6 : 1;
            int i10 = i7 + 1;
            i5 = i8;
            i6 = i9;
            i7 = i10;
        }
        if ( i6 == 0 && i5 != this.sum ) {
            return false;
        }
        return true;
    }
    public int getTotalSolutions() {
        return this.numsquares;
    }
    public String toString() {
        String s = "";
        int i = 0;
        while ( i < this.square.length ) {
            String s0 = s;
            int i0 = 0;
            while ( i0 < this.square[i].length ) {
                String s1 = new StringBuilder().append ( s0 ).append ( this.square[i][i0] ).append ( "\t" ).toString();
                int i1 = i0 + 1;
                s0 = s1;
                i0 = i1;
            }
            String s2 = new StringBuilder().append ( s0 ).append ( "\n" ).toString();
            int i2 = i + 1;
            s = s2;
            i = i2;
        }
        return s;
    }
}
