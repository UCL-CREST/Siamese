import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
class RailRoadCar {
    public static LinkedList Rearrange ( final LinkedList list ) {
        System.out.println ( "Rearranging Cars..." );
        final LinkedList list2 = new LinkedList();
        final LinkedList<Object> list3 = new LinkedList<Object>();
        final LinkedList<Object> list4 = new LinkedList<Object>();
        final LinkedList<Object> list5 = new LinkedList<Object>();
        while ( true ) {
            final int size = list.size();
            if ( size == 0 ) {
                break;
            }
            for ( int i = 0; i < size; ++i ) {
                if ( i == 0 ) {
                    list4.add ( list.element() );
                    list.remove();
                } else if ( Integer.parseInt ( list4.element().toString() ) < Integer.parseInt ( list.element().toString() ) ) {
                    list3.add ( list4.element() );
                    list4.remove();
                    list4.add ( list.element() );
                    list.remove();
                } else {
                    list3.add ( list.element() );
                    list.remove();
                }
            }
            for ( int size2 = list3.size(), j = 0; j < size2; ++j ) {
                list.add ( list3.element() );
                list3.remove();
            }
            list5.add ( list4.element() );
            list4.remove();
            System.out.print ( list.toString() );
            System.out.print ( " , " );
            System.out.print ( list3.toString() );
            System.out.print ( " , " );
            System.out.print ( list4.toString() );
            System.out.print ( " , " );
            System.out.println ( list5.toString() );
        }
        return list5;
    }
    public static void main ( final String[] array ) {
        final BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) );
        final LinkedList<Character> list = new LinkedList<Character>();
        try {
            System.out.println ( "Input: " );
            final String line = bufferedReader.readLine();
            for ( int i = 0; i < line.length(); ++i ) {
                list.add ( line.charAt ( i ) );
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        System.out.println ( Rearrange ( list ).toString() );
    }
}
