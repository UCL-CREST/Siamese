import java.io.*;
import java.util.*;
import java.lang.*;
class RailRoadCar {
    public static LinkedList Rearrange ( LinkedList list ) {
        System.out.println ( "Rearranging Cars..." );
        LinkedList track1 = new LinkedList();
        LinkedList track2 = new LinkedList();
        LinkedList track3 = new LinkedList();
        LinkedList Output = new LinkedList();
        track1 = list;
        while ( true ) {
            int len1 = track1.size();
            if ( len1 == 0 ) {
                break;
            }
            for ( int i = 0; i < len1; i++ ) {
                if ( i == 0 ) {
                    track3.add ( track1.element() );
                    track1.remove();
                } else {
                    int lvalue = Integer.parseInt ( track3.element().toString() );
                    int rvalue = Integer.parseInt ( track1.element().toString() );
                    if ( lvalue < rvalue ) {
                        track2.add ( track3.element() );
                        track3.remove();
                        track3.add ( track1.element() );
                        track1.remove();
                    } else {
                        track2.add ( track1.element() );
                        track1.remove();
                    }
                }
            }
            int len2 = track2.size();
            for ( int j = 0; j < len2; j++ ) {
                track1.add ( track2.element() );
                track2.remove();
            }
            Output.add ( track3.element() );
            track3.remove();
            System.out.print ( track1.toString() );
            System.out.print ( " , " );
            System.out.print ( track2.toString() );
            System.out.print ( " , " );
            System.out.print ( track3.toString() );
            System.out.print ( " , " );
            System.out.println ( Output.toString() );
        }
        return Output;
    }
    public static void main ( String[] args ) {
        String inpstring = "";
        InputStreamReader input = new InputStreamReader ( System.in );
        BufferedReader reader = new BufferedReader ( input );
        LinkedList q = new LinkedList();
        try {
            System.out.println ( "Input: " );
            inpstring = reader.readLine();
            int i = 0;
            for ( i = 0; i < inpstring.length(); i++ ) {
                Object o = inpstring.charAt ( i );
                boolean bRet = q.add ( o );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        LinkedList result = Rearrange ( q );
        System.out.println ( result.toString() );
    }
}
