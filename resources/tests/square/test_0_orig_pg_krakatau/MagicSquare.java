public class MagicSquare {
    private int[][] a;
    private boolean[] b;
    private int c;
    private int d;
    private int e;
    public static void main ( String[] a0 ) {
        MagicSquare a1 = new MagicSquare ( 3 );
        a1.a ( 0, 0 );
        System.out.println ( new StringBuilder ( "There were " ).append ( a1.e ).append ( " number of solutions." ).toString() );
    }
    private MagicSquare ( int i ) {
        super();
        this.a = new int[3][3];
        int i0 = 0;
        while ( i0 < 3 ) {
            int i1 = 0;
            while ( i1 < 3 ) {
                this.a[i0][i1] = 0;
                i1 = i1 + 1;
            }
            i0 = i0 + 1;
        }
        this.c = 9;
        this.b = new boolean[this.c];
        int i2 = 0;
        while ( i2 < this.c ) {
            this.b[i2] = true;
            i2 = i2 + 1;
        }
        this.d = 15;
        this.e = 0;
    }
    private void a ( int i, int i0 ) {
        int i1 = 0;
        while ( true ) {
            int i2 = 0;
            if ( i1 >= this.a.length ) {
                i2 = 1;
            } else {
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                while ( i5 < this.a[i1].length ) {
                    int i6 = i3 + this.a[i1][i5];
                    int i7 = ( this.a[i1][i5] != 0 ) ? i4 : 1;
                    int i8 = i5 + 1;
                    i3 = i6;
                    i4 = i7;
                    i5 = i8;
                }
                label6: {
                    label7: {
                        if ( i4 != 0 ) {
                            break label7;
                        }
                        if ( i3 != this.d ) {
                            break label6;
                        }
                    }
                    i1 = i1 + 1;
                    continue;
                }
                i2 = 0;
            }
            if ( i2 != 0 ) {
                int i9 = 0;
                int i10 = 0;
                while ( true ) {
                    if ( i10 >= this.a[0].length ) {
                        i9 = 1;
                        break;
                    } else {
                        int i11 = 0;
                        int i12 = 0;
                        int i13 = 0;
                        while ( i13 < this.a.length ) {
                            int i14 = i11 + this.a[i13][i10];
                            int i15 = ( this.a[i13][i10] != 0 ) ? i12 : 1;
                            int i16 = i13 + 1;
                            i11 = i14;
                            i12 = i15;
                            i13 = i16;
                        }
                        label4: {
                            label5: {
                                if ( i12 != 0 ) {
                                    break label5;
                                }
                                if ( i11 != this.d ) {
                                    break label4;
                                }
                            }
                            i10 = i10 + 1;
                            continue;
                        }
                        i9 = 0;
                        break;
                    }
                }
                if ( i9 != 0 ) {
                    int i17 = 0;
                    int i18 = 0;
                    int i19 = 0;
                    int i20 = 0;
                    while ( i20 < this.a.length ) {
                        int i21 = i18 + this.a[i20][i20];
                        int i22 = ( this.a[i20][i20] != 0 ) ? i19 : 1;
                        int i23 = i20 + 1;
                        i18 = i21;
                        i19 = i22;
                        i20 = i23;
                    }
                    label0: {
                        label3: {
                            if ( i19 != 0 ) {
                                break label3;
                            }
                            if ( i18 == this.d ) {
                                break label3;
                            }
                            i17 = 0;
                            break label0;
                        }
                        int i24 = 0;
                        int i25 = 0;
                        int i26 = 0;
                        while ( i26 < this.a.length ) {
                            int i27 = i24 + this.a[i26][this.a.length - 1 - i26];
                            int i28 = ( this.a[i26][this.a.length - 1 - i26] != 0 ) ? i25 : 1;
                            int i29 = i26 + 1;
                            i24 = i27;
                            i25 = i28;
                            i26 = i29;
                        }
                        label1: {
                            label2: {
                                if ( i25 != 0 ) {
                                    break label2;
                                }
                                if ( i24 != this.d ) {
                                    break label1;
                                }
                            }
                            i17 = 1;
                            break label0;
                        }
                        i17 = 0;
                    }
                    if ( i17 != 0 ) {
                        if ( i != this.a.length ) {
                            int i30 = 0;
                            while ( i30 < this.c ) {
                                if ( this.b[i30] ) {
                                    int i31 = 0;
                                    int i32 = 0;
                                    this.a[i][i0] = i30 + 1;
                                    this.b[i30] = false;
                                    int i33 = i0 + 1;
                                    if ( i33 != this.a.length ) {
                                        i31 = i33;
                                        i32 = i;
                                    } else {
                                        int i34 = i + 1;
                                        i31 = 0;
                                        i32 = i34;
                                    }
                                    this.a ( i32, i31 );
                                    this.a[i][i0] = 0;
                                    this.b[i30] = true;
                                }
                                i30 = i30 + 1;
                            }
                            return;
                        } else {
                            System.out.println ( ( Object ) this );
                            this.e = this.e + 1;
                            return;
                        }
                    }
                }
            }
            return;
        }
    }
    public String toString() {
        String s = "";
        int i = 0;
        while ( i < this.a.length ) {
            String s0 = s;
            int i0 = 0;
            while ( i0 < this.a[i].length ) {
                String s1 = new StringBuilder().append ( s0 ).append ( this.a[i][i0] ).append ( "\t" ).toString();
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
