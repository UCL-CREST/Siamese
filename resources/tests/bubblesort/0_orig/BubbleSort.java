import java.io.*;
class BubbleSort {
    public static void main ( String[] args ) {
        String inpstring = "";
        InputStreamReader input = new InputStreamReader ( System.in );
        BufferedReader reader = new BufferedReader ( input );
        try {
            System.out.print ( "Enter a Number Elements for BUBBLE SORT:" );
            inpstring = reader.readLine();
            long max = Long.parseLong ( inpstring );
            long[] arrElements = new long[100];
            for ( int i = 0; i < max; i++ ) {
                System.out.print ( "Enter [" + ( i + 1 ) + "] Element: " );
                inpstring = reader.readLine();
                arrElements[i] = Long.parseLong ( inpstring );
            }
            for ( int i = 1; i < max; i++ ) {
                for ( int j = 0; j < max - i; j++ ) {
                    if ( arrElements[j] > arrElements[j + 1] ) {
                        long temp = arrElements[j];
                        arrElements[j] = arrElements[j + 1];
                        arrElements[j + 1] = temp;
                    }
                }
                System.out.print ( "After iteration " + i + ": " );
                for ( int k = 0; k < max; k++ ) {
                    System.out.print ( arrElements[k] + " " );
                }
                System.out.println ( "/*** " + i  + " biggest number(s) is(are) pushed to the end of the array ***/" );
            }
            System.out.println ( "The numbers in ascending orders are given below:" );
            for ( int i = 0; i < max; i++ ) {
                System.out.println ( arrElements[i] );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
