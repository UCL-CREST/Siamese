class SqrtAlgorithm {
    public static double SqrtByAlogorithm ( final double n ) {
        long n3;
        long n2 = n3 = ( long ) n;
        long n5;
        long n4 = n5 = ( long ) ( ( n - n2 ) * 1000000.0 );
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9;
        for ( n9 = 0; n3 >= 10L; n3 /= 10L, ++n9 ) {}
        ++n9;
        int n10;
        for ( n10 = 0; n5 >= 10L; n5 /= 10L, ++n10 ) {}
        ++n10;
        if ( n9 % 2 == 1 ) {
            n7 = 1;
        }
        while ( true ) {
            int n11 = 1;
            final int n12 = ( n7 == 1 ) ? ( n9 - 1 ) : ( n9 - 2 );
            for ( int i = 0; i < n12; ++i ) {
                n11 *= 10;
            }
            final int n13 = ( int ) n2 / n11;
            final int n14 = n6 * 2;
            int n15 = 1;
            while ( true ) {
                if ( n14 == 0 ) {
                    if ( n13 - n15 * n15 < 0 ) {
                        n6 = n15 - 1;
                        break;
                    }
                } else if ( n13 - n15 * ( n14 * 10 + n15 ) < 0 ) {
                    n6 = n14 / 2 * 10 + n15 - 1;
                    break;
                }
                ++n15;
            }
            final int n16 = n6 / 10;
            final int n17 = n6 % 10;
            if ( n16 == 0 ) {
                n2 -= n6 * n6 * n11;
            } else {
                n2 -= ( n16 * 2 * 10 + n17 ) * n17 * n11;
            }
            if ( n2 == 0L && n4 == 0L ) {
                if ( n12 > 0 ) {
                    int n18 = 1;
                    for ( int n19 = n12 / 2, j = 0; j < n19; ++j ) {
                        n18 *= 10;
                    }
                    n6 *= n18;
                    break;
                }
                break;
            } else {
                if ( n7 == 1 ) {
                    --n9;
                    n7 = 0;
                } else {
                    n9 -= 2;
                }
                if ( n9 > 0 ) {
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
                    n10 -= 2;
                    int n20 = 1;
                    for ( int k = 0; k < n10; ++k ) {
                        n20 *= 10;
                    }
                    n2 += n4 / n20;
                    n4 %= n20;
                }
                n9 += 2;
            }
        }
        double n21;
        if ( n8 == 0 ) {
            n21 = n6;
        } else {
            int n22 = 1;
            for ( int l = 0; l < n8; ++l ) {
                n22 *= 10;
            }
            n21 = n6 / n22;
        }
        return n21;
    }
    public static void main ( final String[] array ) {
        for ( double n = 0.0; n <= 10000.0; n += 50.0 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( n );
            System.out.print ( ") = " );
            System.out.print ( SqrtByAlogorithm ( n ) );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( n ) );
        }
    }
}
