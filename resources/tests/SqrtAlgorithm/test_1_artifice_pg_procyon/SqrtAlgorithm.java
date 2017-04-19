import java.io.*;
class SqrtAlgorithm {
    public static void main ( final String[] array ) {
        for ( double n = 0.0; n <= 10000.0; n += 50.0 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( n );
            System.out.print ( ") = " );
            final PrintStream out = System.out;
            final double n4;
            long n3;
            long n2 = n3 = ( long ) ( n4 = n );
            long n6;
            long n5 = n6 = ( long ) ( ( n4 - n2 ) * 1000000.0 );
            int n7 = 0;
            int n8 = 0;
            int n9 = 0;
            int n10;
            for ( n10 = 0; n3 >= 10L; n3 /= 10L, ++n10 ) {}
            int n11 = n10 + 1;
            int n12;
            for ( n12 = 0; n6 >= 10L; n6 /= 10L, ++n12 ) {}
            int n13 = n12 + 1;
            if ( n11 % 2 == 1 ) {
                n8 = 1;
            }
            while ( true ) {
                int n14 = 1;
                int n15;
                if ( n8 == 1 ) {
                    n15 = n11 - 1;
                } else {
                    n15 = n11 - 2;
                }
                for ( int i = 0; i < n15; ++i ) {
                    n14 *= 10;
                }
                final int n16 = ( int ) n2 / n14;
                final int n17 = n7 << 1;
                int n18 = 1;
                while ( true ) {
                    if ( n17 == 0 ) {
                        if ( n16 - n18 * n18 < 0 ) {
                            n7 = n18 - 1;
                            break;
                        }
                    } else if ( n16 - n18 * ( n17 * 10 + n18 ) < 0 ) {
                        n7 = n17 / 2 * 10 + n18 - 1;
                        break;
                    }
                    ++n18;
                }
                final int n19 = n7 / 10;
                final int n20 = n7 % 10;
                if ( n19 == 0 ) {
                    n2 -= 0 - n7 * n7 * n14;
                } else {
                    n2 -= 0 - ( ( n19 << 1 ) * 10 + n20 ) * n20 * n14;
                }
                if ( n2 == 0L && n5 == 0L ) {
                    if ( n15 > 0 ) {
                        int n21 = 1;
                        for ( int n22 = n15 / 2, j = 0; j < n22; ++j ) {
                            n21 *= 10;
                        }
                        n7 *= n21;
                        break;
                    }
                    break;
                } else {
                    if ( n8 == 1 ) {
                        --n11;
                        n8 = 0;
                    } else {
                        n11 -= 2;
                    }
                    if ( n11 > 0 ) {
                        continue;
                    }
                    if ( ( n2 <= 0L && n5 <= 0L ) || n9 >= 5 ) {
                        break;
                    }
                    ++n9;
                    n2 *= 100L;
                    if ( n5 > 0L ) {
                        n13 -= 2;
                        int n23 = 1;
                        for ( int k = 0; k < n13; ++k ) {
                            n23 *= 10;
                        }
                        n2 += n5 / n23;
                        n5 %= n23;
                    }
                    n11 += 2;
                }
            }
            double n24;
            if ( n9 == 0 ) {
                n24 = n7;
            } else {
                int n25 = 1;
                for ( int l = 0; l < n9; ++l ) {
                    n25 *= 10;
                }
                n24 = n7 / n25;
            }
            out.print ( n24 );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( n ) );
        }
    }
}
