import java.io.*;
class BubbleSort {
    public static void main ( String[] v0 ) {
        String v1;
        v1 = "";
        InputStreamReader v2;
        v2 = new InputStreamReader ( System.in );
        BufferedReader v3;
        v3 = new BufferedReader ( v2 );
        try {
            System.out.print ( "Enter a Number Elements for BUBBLE SORT:" );
            v1 = v3.readLine();
            long v4;
            v4 = Long.parseLong ( v1 );
            long[] v5;
            v5 = new long[100];
            int v6;
            v6 = 0;
            while ( v6 < v4 ) {
                System.out.print ( "Enter [" + ( v6 + 1 ) + "] Element: " );
                v1 = v3.readLine();
                v5[v6] = Long.parseLong ( v1 );
                v6 = v6 + 1;
            }
            int v7;
            v7 = 1;
            while ( v7 < v4 ) {
                int v8;
                v8 = 0;
                while ( v8 < v4 - v7 ) {
                    if ( v5[v8] > v5[v8 + 1] ) {
                        long v9;
                        v9 = v5[v8];
                        v5[v8] = v5[v8 + 1];
                        v5[v8 + 1] = v9;
                    }
                    v8 = v8 + 1;
                }
                System.out.print ( "After iteration " + v7 + ": " );
                int v10;
                v10 = 0;
                while ( v10 < v4 ) {
                    System.out.print ( v5[v10] + " " );
                    v10 = v10 + 1;
                }
                System.out
                .println ( "/*** "
                           + v7
                           + " biggest number(s) is(are) pushed to the end of the array ***/" );
                v7 = v7 + 1;
            }
            System.out.println ( "The numbers in ascending orders are given below:" );
            int v11;
            v11 = 0;
            while ( v11 < v4 ) {
                System.out.println ( v5[v11] );
                v11 = v11 + 1;
            }
        } catch ( Exception v12 ) {
            v12.printStackTrace();
        }
    }
}
