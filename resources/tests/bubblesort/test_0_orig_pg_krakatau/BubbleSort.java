class BubbleSort {
    BubbleSort() {
        super();
    }
    public static void main ( String[] a ) {
        java.io.BufferedReader a0 = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) );
        try {
            System.out.print ( "Enter a Number Elements for BUBBLE SORT:" );
            long j = Long.parseLong ( a0.readLine() );
            long[] a1 = new long[100];
            int i = 0;
            while ( ( long ) i < j ) {
                System.out.print ( new StringBuilder ( "Enter [" ).append ( i + 1 ).append ( "] Element: " ).toString() );
                a1[i] = Long.parseLong ( a0.readLine() );
                i = i + 1;
            }
            int i0 = 1;
            while ( ( long ) i0 < j ) {
                int i1 = 0;
                while ( ( long ) i1 < j - ( long ) i0 ) {
                    {
                        if ( a1[i1] > a1[i1 + 1] ) {
                            long j0 = a1[i1];
                            a1[i1] = a1[i1 + 1];
                            a1[i1 + 1] = j0;
                        }
                        i1 = i1 + 1;
                    }
                }
                System.out.print ( new StringBuilder ( "After iteration " ).append ( i0 ).append ( ": " ).toString() );
                int i2 = 0;
                while ( ( long ) i2 < j ) {
                    {
                        System.out.print ( new StringBuilder().append ( a1[i2] ).append ( " " ).toString() );
                        i2 = i2 + 1;
                    }
                }
                System.out.println ( new StringBuilder ( "/*** " ).append ( i0 ).append ( " biggest number(s) is(are) pushed to the end of the array ***/" ).toString() );
                i0 = i0 + 1;
            }
            System.out.println ( "The numbers in ascending orders are given below:" );
            int i3 = 0;
            while ( ( long ) i3 < j ) {
                System.out.println ( a1[i3] );
                i3 = i3 + 1;
            }
        } catch ( Exception a2 ) {
            a2.printStackTrace();
            return;
        }
    }
}
