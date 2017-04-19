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
            ++n10;
            int n11;
            for ( n11 = 0; n6 >= 10L; n6 /= 10L, ++n11 ) {}
            ++n11;
            if ( n10 % 2 == 1 ) {
                n8 = 1;
            }
            while ( true ) {
                int n12 = 1;
                final int n13 = ( n8 == 1 ) ? ( n10 - 1 ) : ( n10 - 2 );
                for ( int i = 0; i < n13; ++i ) {
                    n12 *= 10;
                }
                final int n14 = ( int ) n2 / n12;
                final int n15 = n7 << 1;
                int n16 = 1;
                while ( true ) {
                    if ( n15 == 0 ) {
                        if ( n14 - n16 * n16 < 0 ) {
                            n7 = n16 - 1;
                            break;
                        }
                    } else if ( n14 - n16 * ( n15 * 10 + n16 ) < 0 ) {
                        n7 = n15 / 2 * 10 + n16 - 1;
                        break;
                    }
                    ++n16;
                }
                final int n17 = n7 / 10;
                final int n18 = n7 % 10;
                if ( n17 == 0 ) {
                    n2 -= n7 * n7 * n12;
                } else {
                    n2 -= ( ( n17 << 1 ) * 10 + n18 ) * n18 * n12;
                }
                if ( n2 == 0L && n5 == 0L ) {
                    if ( n13 > 0 ) {
                        int n19 = 1;
                        for ( int n20 = n13 / 2, j = 0; j < n20; ++j ) {
                            n19 *= 10;
                        }
                        n7 *= n19;
                        break;
                    }
                    break;
                } else {
                    if ( n8 == 1 ) {
                        --n10;
                        n8 = 0;
                    } else {
                        n10 -= 2;
                    }
                    if ( n10 > 0 ) {
                        continue;
                    }
                    if ( ( n2 <= 0L && n5 <= 0L ) || n9 >= 5 ) {
                        break;
                    }
                    ++n9;
                    n2 *= 100L;
                    if ( n5 > 0L ) {
                        n11 -= 2;
                        int n21 = 1;
                        for ( int k = 0; k < n11; ++k ) {
                            n21 *= 10;
                        }
                        n2 += n5 / n21;
                        n5 %= n21;
                    }
                    n10 += 2;
                }
            }
            double n22;
            if ( n9 == 0 ) {
                n22 = n7;
            } else {
                int n23 = 1;
                for ( int l = 0; l < n9; ++l ) {
                    n23 *= 10;
                }
                n22 = n7 / n23;
            }
            out.print ( n22 );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( n ) );
        }
    }
}
