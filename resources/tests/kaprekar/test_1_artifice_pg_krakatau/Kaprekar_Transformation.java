class Kaprekar_Transformation {
    Kaprekar_Transformation() {
        super();
    }
    private static int a() {
        int i = 0;
        try {
            i = Integer.parseInt ( new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine() );
        } catch ( Exception a ) {
            a.printStackTrace();
            return 0;
        }
        return i;
    }
    public static void main ( String[] a ) {
        System.out.print ( "Enter a 3 digit number for Kaprekar Tranformation: " );
        int i = Kaprekar_Transformation.a();
        label0: {
            label1: {
                if ( i < 0 ) {
                    break label1;
                }
                if ( i <= 999 ) {
                    break label0;
                }
            }
            System.out.println ( "Your input is not correct." );
        }
        int i0 = i / 100;
        int i1 = ( i - i0 * 100 ) / 10;
        int i2 = i % 10;
        if ( i0 == i1 && i0 == i2 ) {
            System.out.println ( "All digits should not be equal." );
            return;
        }
        while ( true ) {
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            int i8 = i / 100;
            int i9 = ( i - i8 * 100 ) / 10;
            int i10 = i % 10;
            if ( i8 <= i9 ) {
                if ( i9 <= i10 ) {
                    i3 = i10;
                    i4 = i9;
                    i5 = i8;
                } else if ( i8 <= i10 ) {
                    i3 = i9;
                    i4 = i10;
                    i5 = i8;
                } else {
                    i3 = i9;
                    i4 = i8;
                    i5 = i10;
                }
            } else if ( i8 <= i10 ) {
                i3 = i10;
                i4 = i8;
                i5 = i9;
            } else if ( i9 <= i10 ) {
                i3 = i8;
                i4 = i10;
                i5 = i9;
            } else {
                i3 = i8;
                i4 = i9;
                i5 = i10;
            }
            if ( i8 >= i9 ) {
                if ( i9 >= i10 ) {
                    i6 = i10;
                    i7 = i9;
                } else if ( i8 >= i10 ) {
                    i6 = i9;
                    i7 = i10;
                } else {
                    i6 = i9;
                    i7 = i8;
                    i8 = i10;
                }
            } else if ( i8 >= i10 ) {
                i6 = i10;
                i7 = i8;
                i8 = i9;
            } else if ( i9 >= i10 ) {
                i6 = i8;
                i7 = i10;
                i8 = i9;
            } else {
                i6 = i8;
                i7 = i9;
                i8 = i10;
            }
            int i11 = i3 * 100 + i4 * 10 + i5;
            int i12 = i6 * 100 + i7 * 10 + i8;
            int i13 = i11 - i12;
            java.io.PrintStream a0 = System.out;
            Object[] a1 = new Object[3];
            a1[0] = Integer.valueOf ( i11 );
            a1[1] = Integer.valueOf ( i12 );
            a1[2] = Integer.valueOf ( i13 );
            a0.format ( "%d - %d = %d\n", a1 );
            if ( i == i13 ) {
                return;
            }
            i = i13;
        }
    }
}
