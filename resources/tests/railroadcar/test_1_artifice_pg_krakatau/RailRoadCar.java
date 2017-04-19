class RailRoadCar {
    RailRoadCar() {
        super();
    }
    private static java.util.LinkedList a ( java.util.LinkedList a ) {
        System.out.println ( "Rearranging Cars..." );
        java.util.LinkedList a0 = new java.util.LinkedList();
        java.util.LinkedList a1 = new java.util.LinkedList();
        java.util.LinkedList a2 = new java.util.LinkedList();
        java.util.LinkedList a3 = new java.util.LinkedList();
        while ( true ) {
            int i = a.size();
            if ( i == 0 ) {
                return a3;
            }
            int i0 = 0;
            while ( i0 < i ) {
                if ( i0 != 0 ) {
                    if ( Integer.parseInt ( a2.element().toString() ) >= Integer.parseInt ( a.element().toString() ) ) {
                        a1.add ( a.element() );
                        a.remove();
                    } else {
                        a1.add ( a2.element() );
                        a2.remove();
                        a2.add ( a.element() );
                        a.remove();
                    }
                } else {
                    a2.add ( a.element() );
                    a.remove();
                    i0 = 0;
                }
                i0 = i0 + 1;
            }
            int i1 = a1.size();
            int i2 = 0;
            while ( i2 < i1 ) {
                a.add ( a1.element() );
                a1.remove();
                i2 = i2 + 1;
            }
            a3.add ( a2.element() );
            a2.remove();
            System.out.print ( a.toString() );
            System.out.print ( " , " );
            System.out.print ( a1.toString() );
            System.out.print ( " , " );
            System.out.print ( a2.toString() );
            System.out.print ( " , " );
            System.out.println ( a3.toString() );
        }
    }
    public static void main ( String[] a ) {
        java.io.BufferedReader a0 = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) );
        java.util.LinkedList a1 = new java.util.LinkedList();
        try {
            System.out.println ( "Input: " );
            String s = a0.readLine();
            int i = 0;
            while ( i < s.length() ) {
                int i0 = s.charAt ( i );
                a1.add ( ( Object ) Character.valueOf ( ( char ) i0 ) );
                i = i + 1;
            }
        } catch ( Exception a2 ) {
            a2.printStackTrace();
        }
        java.util.LinkedList a3 = RailRoadCar.a ( a1 );
        System.out.println ( a3.toString() );
    }
}
