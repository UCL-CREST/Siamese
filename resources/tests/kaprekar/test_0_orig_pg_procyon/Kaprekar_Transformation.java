import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class Kaprekar_Transformation {
    private static int a() {
        try {
            return Integer.parseInt ( new BufferedReader ( new InputStreamReader ( System.in ) ).readLine() );
        } catch ( Exception ex ) {
            ex.printStackTrace();
            return 0;
        }
    }
    public static void main ( final String[] array ) {
        System.out.print ( "Enter a 3 digit number for Kaprekar Tranformation: " );
        int a;
        if ( ( a = a() ) < 0 || a > 999 ) {
            System.out.println ( "Your input is not correct." );
        }
        final int n = a / 100;
        final int n2 = ( a - n * 100 ) / 10;
        final int n3 = a % 10;
        if ( n == n2 && n == n3 ) {
            System.out.println ( "All digits should not be equal." );
            return;
        }
        while ( true ) {
            final int n5;
            final int n4 = ( n5 = a ) / 100;
            final int n6 = ( n5 - n4 * 100 ) / 10;
            final int n7 = n5 % 10;
            int n8;
            int n9;
            int n10;
            if ( n4 > n6 ) {
                if ( n4 > n7 ) {
                    n8 = n4;
                    if ( n6 > n7 ) {
                        n9 = n6;
                        n10 = n7;
                    } else {
                        n10 = n6;
                        n9 = n7;
                    }
                } else {
                    n8 = n7;
                    n9 = n4;
                    n10 = n6;
                }
            } else if ( n6 > n7 ) {
                n8 = n6;
                if ( n4 > n7 ) {
                    n9 = n4;
                    n10 = n7;
                } else {
                    n10 = n4;
                    n9 = n7;
                }
            } else {
                n8 = n7;
                n9 = n6;
                n10 = n4;
            }
            int n11;
            int n12;
            int n13;
            if ( n4 < n6 ) {
                if ( n4 < n7 ) {
                    n11 = n4;
                    if ( n6 < n7 ) {
                        n12 = n6;
                        n13 = n7;
                    } else {
                        n13 = n6;
                        n12 = n7;
                    }
                } else {
                    n11 = n7;
                    n12 = n4;
                    n13 = n6;
                }
            } else if ( n6 < n7 ) {
                n11 = n6;
                if ( n4 < n7 ) {
                    n12 = n4;
                    n13 = n7;
                } else {
                    n13 = n4;
                    n12 = n7;
                }
            } else {
                n11 = n7;
                n12 = n6;
                n13 = n4;
            }
            final int i = n8 * 100 + n9 * 10 + n10;
            final int j = n11 * 100 + n12 * 10 + n13;
            final int k = i - j;
            System.out.format ( "%d - %d = %d\n", i, j, k );
            final int n14 = k;
            if ( a == n14 ) {
                break;
            }
            a = n14;
        }
    }
}
