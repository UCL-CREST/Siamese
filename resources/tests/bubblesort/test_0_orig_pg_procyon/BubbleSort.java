import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
class BubbleSort {
    public static void main ( final String[] array ) {
        final BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) );
        try {
            System.out.print ( "Enter a Number Elements for BUBBLE SORT:" );
            final long long1 = Long.parseLong ( bufferedReader.readLine() );
            final long[] array2 = new long[100];
            for ( int n = 0; n < long1; ++n ) {
                System.out.print ( "Enter [" + ( n + 1 ) + "] Element: " );
                array2[n] = Long.parseLong ( bufferedReader.readLine() );
            }
            for ( int n2 = 1; n2 < long1; ++n2 ) {
                for ( int n3 = 0; n3 < long1 - n2; ++n3 ) {
                    if ( array2[n3] > array2[n3 + 1] ) {
                        final long n4 = array2[n3];
                        array2[n3] = array2[n3 + 1];
                        array2[n3 + 1] = n4;
                    }
                }
                System.out.print ( "After iteration " + n2 + ": " );
                for ( int n5 = 0; n5 < long1; ++n5 ) {
                    System.out.print ( array2[n5] + " " );
                }
                System.out.println ( "/*** " + n2 + " biggest number(s) is(are) pushed to the end of the array ***/" );
            }
            System.out.println ( "The numbers in ascending orders are given below:" );
            for ( int n6 = 0; n6 < long1; ++n6 ) {
                System.out.println ( array2[n6] );
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
}
