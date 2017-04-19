public class MagicSquare {
    private int[][] a;
    private boolean[] b;
    private int c;
    private int d;
    private int e;
    public static void main ( final String[] array ) {
        final MagicSquare magicSquare;
        ( magicSquare = new MagicSquare ( 3 ) ).a ( 0, 0 );
        System.out.println ( "There were " + magicSquare.e + " number of solutions." );
    }
    private MagicSquare ( int i ) {
        super();
        this.a = new int[3][3];
        for ( i = 0; i < 3; ++i ) {
            for ( int j = 0; j < 3; ++j ) {
                this.a[i][j] = 0;
            }
        }
        this.c = 9;
        this.b = new boolean[this.c];
        for ( int k = 0; k < this.c; ++k ) {
            this.b[k] = true;
        }
        this.d = 15;
        this.e = 0;
    }
    private void a ( final int n, final int n2 ) {
        while ( true ) {
            for ( int i = 0; i < this.a.length; ++i ) {
                int n3 = 0;
                boolean b = false;
                for ( int j = 0; j < this.a[i].length; ++j ) {
                    n3 += this.a[i][j];
                    if ( this.a[i][j] == 0 ) {
                        b = true;
                    }
                }
                if ( !b && n3 != this.d ) {
                    final boolean b2 = false;
                    if ( b2 ) {
                        int k = 0;
                        while ( true ) {
                            while ( k < this.a[0].length ) {
                                int n4 = 0;
                                boolean b3 = false;
                                for ( int l = 0; l < this.a.length; ++l ) {
                                    n4 += this.a[l][k];
                                    if ( this.a[l][k] == 0 ) {
                                        b3 = true;
                                    }
                                }
                                if ( !b3 && n4 != this.d ) {
                                    final boolean b4 = false;
                                    if ( !b4 ) {
                                        return;
                                    }
                                    int n5 = 0;
                                    boolean b5 = false;
                                    for ( int n6 = 0; n6 < this.a.length; ++n6 ) {
                                        n5 += this.a[n6][n6];
                                        if ( this.a[n6][n6] == 0 ) {
                                            b5 = true;
                                        }
                                    }
                                    boolean b6;
                                    if ( !b5 && n5 != this.d ) {
                                        b6 = false;
                                    } else {
                                        int n7 = 0;
                                        boolean b7 = false;
                                        for ( int n8 = 0; n8 < this.a.length; ++n8 ) {
                                            n7 += this.a[n8][this.a.length - 1 - n8];
                                            if ( this.a[n8][this.a.length - 1 - n8] == 0 ) {
                                                b7 = true;
                                            }
                                        }
                                        b6 = ( b7 || n7 == this.d );
                                    }
                                    if ( !b6 ) {
                                        return;
                                    }
                                    if ( n == this.a.length ) {
                                        System.out.println ( this );
                                        ++this.e;
                                        return;
                                    }
                                    for ( int n9 = 0; n9 < this.c; ++n9 ) {
                                        if ( this.b[n9] ) {
                                            this.a[n][n2] = n9 + 1;
                                            this.b[n9] = false;
                                            int n10 = n2 + 1;
                                            int n11 = n;
                                            if ( n10 == this.a.length ) {
                                                n11 = n + 1;
                                                n10 = 0;
                                            }
                                            this.a ( n11, n10 );
                                            this.a[n][n2] = 0;
                                            this.b[n9] = true;
                                        }
                                    }
                                    return;
                                } else {
                                    ++k;
                                }
                            }
                            final boolean b4 = true;
                            continue;
                        }
                    }
                    return;
                }
            }
            final boolean b2 = true;
            continue;
        }
    }
    public String toString() {
        String s = "";
        for ( int i = 0; i < this.a.length; ++i ) {
            for ( int j = 0; j < this.a[i].length; ++j ) {
                s = s + ( 0 + this.a[i][j] ) + "\t";
            }
            s += "0\n";
        }
        return s;
    }
}
