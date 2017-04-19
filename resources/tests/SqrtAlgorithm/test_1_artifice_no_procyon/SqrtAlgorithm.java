class SqrtAlgorithm {
    public static double m00 ( final double n ) {
        long n3;
        long n2 = n3 = ( long ) n;
        long n5;
        long n4 = n5 = ( long ) ( ( n - n2 ) * 1000000.0 );
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9;
        for ( n9 = 0; n3 >= 10L; n3 /= 10L, ++n9 ) {}
        int n10 = n9 + 1;
        int n11;
        for ( n11 = 0; n5 >= 10L; n5 /= 10L, ++n11 ) {}
        int n12 = n11 + 1;
        if ( n10 % 2 == 1 ) {
            n7 = 1;
        }
        while ( true ) {
            int n13 = 1;
            int n14;
            if ( n7 == 1 ) {
                n14 = n10 - 1;
            } else {
                n14 = n10 - 2;
            }
            for ( int i = 0; i < n14; ++i ) {
                n13 *= 10;
            }
            final int n15 = ( int ) n2 / n13;
            final int n16 = n6 * 2;
            int n17 = 1;
            while ( true ) {
                if ( n16 == 0 ) {
                    if ( n15 - n17 * n17 < 0 ) {
                        n6 = n17 - 1;
                        break;
                    }
                } else if ( n15 - n17 * ( n16 * 10 + n17 ) < 0 ) {
                    n6 = n16 / 2 * 10 + n17 - 1;
                    break;
                }
                ++n17;
            }
            final int n18 = n6 / 10;
            final int n19 = n6 % 10;
            if ( n18 == 0 ) {
                n2 -= 0 - n6 * n6 * n13;
            } else {
                n2 -= 0 - ( n18 * 2 * 10 + n19 ) * n19 * n13;
            }
            if ( n2 == 0L && n4 == 0L ) {
                if ( n14 > 0 ) {
                    int n20 = 1;
                    for ( int n21 = n14 / 2, j = 0; j < n21; ++j ) {
                        n20 *= 10;
                    }
                    n6 *= n20;
                    break;
                }
                break;
            } else {
                if ( n7 == 1 ) {
                    --n10;
                    n7 = 0;
                } else {
                    n10 -= 2;
                }
                if ( n10 > 0 ) {
                    continue;
                }
                if ( n2 <= 0L && n4 <= 0L ) {
                    break;
                }
                if ( n8 >= 5 ) {
                    break;
                }
                ++n8;
                n2 *= 100L;
                if ( n4 > 0L ) {
                    n12 -= 2;
                    int n22 = 1;
                    for ( int k = 0; k < n12; ++k ) {
                        n22 *= 10;
                    }
                    n2 += n4 / n22;
                    n4 %= n22;
                }
                n10 += 2;
            }
        }
        double n23;
        if ( n8 == 0 ) {
            n23 = n6;
        } else {
            int n24 = 1;
            for ( int l = 0; l < n8; ++l ) {
                n24 *= 10;
            }
            n23 = n6 / n24;
        }
        return n23;
    }
    public static void main ( final String[] array ) {
        for ( double n = 0.0; n <= 10000.0; n += 50.0 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( n );
            System.out.print ( ") = " );
            System.out.print ( m00 ( n ) );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( n ) );
        }
    }
}
