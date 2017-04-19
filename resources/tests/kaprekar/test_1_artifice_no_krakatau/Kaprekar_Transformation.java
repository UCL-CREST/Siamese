class Kaprekar_Transformation {
    Kaprekar_Transformation() {
        super();
    }
    public static int m01() {
        int i = 0;
        try {
            i = Integer.parseInt ( new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine() );
        } catch ( Exception a ) {
            a.printStackTrace();
            return 0;
        }
        return i;
    }
    static int m11 ( int i ) {
        int i0 = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = i / 100;
        int i6 = ( i - i5 * 100 ) / 10;
        int i7 = i % 10;
        if ( i5 <= i6 ) {
            if ( i6 <= i7 ) {
                i0 = i7;
                i1 = i6;
                i2 = i5;
            } else if ( i5 <= i7 ) {
                i0 = i6;
                i1 = i7;
                i2 = i5;
            } else {
                i0 = i6;
                i1 = i5;
                i2 = i7;
            }
        } else if ( i5 <= i7 ) {
            i0 = i7;
            i1 = i5;
            i2 = i6;
        } else if ( i6 <= i7 ) {
            i0 = i5;
            i1 = i7;
            i2 = i6;
        } else {
            i0 = i5;
            i1 = i6;
            i2 = i7;
        }
        if ( i5 >= i6 ) {
            if ( i6 >= i7 ) {
                i3 = i7;
                i4 = i6;
            } else if ( i5 >= i7 ) {
                i3 = i6;
                i4 = i7;
            } else {
                i3 = i6;
                i4 = i5;
                i5 = i7;
            }
        } else if ( i5 >= i7 ) {
            i3 = i7;
            i4 = i5;
            i5 = i6;
        } else if ( i6 >= i7 ) {
            i3 = i5;
            i4 = i7;
            i5 = i6;
        } else {
            i3 = i5;
            i4 = i6;
            i5 = i7;
        }
        int i8 = i0 * 100 + i1 * 10 + i2;
        int i9 = i3 * 100 + i4 * 10 + i5;
        int i10 = i8 - i9;
        java.io.PrintStream a = System.out;
        Object[] a0 = new Object[3];
        a0[0] = Integer.valueOf ( i8 );
        a0[1] = Integer.valueOf ( i9 );
        a0[2] = Integer.valueOf ( i10 );
        a.format ( "%d - %d = %d\n", a0 );
        return i10;
    }
    public static void main ( String[] a ) {
        System.out.print ( "Enter a 3 digit number for Kaprekar Tranformation: " );
        int i = Kaprekar_Transformation.m01();
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
            int i3 = Kaprekar_Transformation.m11 ( i );
            if ( i == i3 ) {
                return;
            }
            i = i3;
        }
    }
}
