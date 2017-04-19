public class MagicSquare {
    private int[][] f00;
    private boolean[] f10;
    private int f20;
    private int f30;
    private int f40;
    public static void main ( String[] a ) {
        MagicSquare a0 = new MagicSquare ( 3 );
        a0.m00 ( 0, 0 );
        System.out.println ( new StringBuilder().append ( "There were " ).append ( a0.m40() ).append ( " number of solutions." ).toString() );
    }
    public MagicSquare ( int i ) {
        super();
        this.f00 = new int[i][i];
        int i0 = 0;
        while ( i0 < i ) {
            int i1 = 0;
            while ( i1 < i ) {
                this.f00[i0][i1] = 0;
                i1 = i1 + 1;
            }
            i0 = i0 + 1;
        }
        this.f20 = i * i;
        this.f10 = new boolean[this.f20];
        int i2 = 0;
        while ( i2 < this.f20 ) {
            this.f10[i2] = true;
            i2 = i2 + 1;
        }
        this.f30 = i * ( i * i + 1 ) / 2;
        this.f40 = 0;
    }
    public void m00 ( int i, int i0 ) {
        if ( this.m10() && this.m20() && this.m30() ) {
            if ( i != this.f00.length ) {
                int i1 = 0;
                while ( i1 < this.f20 ) {
                    if ( this.f10[i1] ) {
                        int i2 = 0;
                        int i3 = 0;
                        this.f00[i][i0] = i1 + 1;
                        this.f10[i1] = false;
                        int i4 = i0 + 1;
                        if ( i4 != this.f00.length ) {
                            i2 = i4;
                            i3 = i;
                        } else {
                            int i5 = i + 1;
                            i2 = 0;
                            i3 = i5;
                        }
                        this.m00 ( i3, i2 );
                        this.f00[i][i0] = 0;
                        this.f10[i1] = true;
                    }
                    i1 = i1 + 1;
                }
                return;
            } else {
                System.out.println ( ( Object ) this );
                this.f40 = this.f40 + 1;
                return;
            }
        }
    }
    public boolean m10() {
        int i = 0;
        while ( i < this.f00.length ) {
            int i0 = 0;
            int i1 = 0;
            int i2 = 0;
            while ( i2 < this.f00[i].length ) {
                int i3 = i0 + this.f00[i][i2];
                int i4 = ( this.f00[i][i2] != 0 ) ? i1 : 1;
                int i5 = i2 + 1;
                i0 = i3;
                i1 = i4;
                i2 = i5;
            }
            if ( i1 == 0 && i0 != this.f30 ) {
                return false;
            }
            i = i + 1;
        }
        return true;
    }
    public boolean m20() {
        int i = 0;
        while ( i < this.f00[0].length ) {
            int i0 = 0;
            int i1 = 0;
            int i2 = 0;
            while ( i2 < this.f00.length ) {
                int i3 = i0 + this.f00[i2][i];
                int i4 = ( this.f00[i2][i] != 0 ) ? i1 : 1;
                int i5 = i2 + 1;
                i0 = i3;
                i1 = i4;
                i2 = i5;
            }
            if ( i1 == 0 && i0 != this.f30 ) {
                return false;
            }
            i = i + 1;
        }
        return true;
    }
    public boolean m30() {
        int i = 0;
        int i0 = 0;
        int i1 = 0;
        while ( i1 < this.f00.length ) {
            int i2 = i + this.f00[i1][i1];
            int i3 = ( this.f00[i1][i1] != 0 ) ? i0 : 1;
            int i4 = i1 + 1;
            i = i2;
            i0 = i3;
            i1 = i4;
        }
        if ( i0 == 0 && i != this.f30 ) {
            return false;
        }
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while ( i7 < this.f00.length ) {
            int i8 = i5 + this.f00[i7][this.f00.length - 1 - i7];
            int i9 = ( this.f00[i7][this.f00.length - 1 - i7] != 0 ) ? i6 : 1;
            int i10 = i7 + 1;
            i5 = i8;
            i6 = i9;
            i7 = i10;
        }
        if ( i6 == 0 && i5 != this.f30 ) {
            return false;
        }
        return true;
    }
    public int m40() {
        return this.f40;
    }
    public String toString() {
        String s = "";
        int i = 0;
        while ( i < this.f00.length ) {
            String s0 = s;
            int i0 = 0;
            while ( i0 < this.f00[i].length ) {
                String s1 = new StringBuilder().append ( s0 ).append ( 0 + this.f00[i][i0] ).append ( "\t" ).toString();
                int i1 = i0 + 1;
                s0 = s1;
                i0 = i1;
            }
            String s2 = new StringBuilder().append ( s0 ).append ( "0\n" ).toString();
            int i2 = i + 1;
            s = s2;
            i = i2;
        }
        return s;
    }
}
