import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
class GuessWord {
    private static int a ( final String[] array ) {
        try {
            final FileReader in = new FileReader ( "words_input.txt" );
            final BufferedReader bufferedReader = new BufferedReader ( in );
            int n = 0;
            String line;
            for ( int n2 = 0; n2 < 100 && ( line = bufferedReader.readLine() ) != null; ++n2 ) {
                array[n] = line;
                ++n;
            }
            in.close();
            return n;
        } catch ( FileNotFoundException ex ) {
            System.out.println ( ex.getMessage() );
            return -1;
        } catch ( IOException ex2 ) {
            System.out.println ( ex2.getStackTrace() );
            return -1;
        }
    }
    private static String a() {
        try {
            return new BufferedReader ( new InputStreamReader ( System.in ) ).readLine();
        } catch ( Exception ex ) {
            ex.printStackTrace();
            return "";
        }
    }
    public static void main ( String[] array ) {
        System.out.println ( "Welcome to Guess a Word\n" );
        final int a;
        if ( ( a = a ( array = new String[100] ) ) < 0 ) {
            System.out.println ( "No words found in the file" );
            return;
        }
        final String str;
        final int length = ( str = array[ ( int ) ( Math.random() * 100.0 ) % a] ).length();
        System.out.print ( "Your secret word is: " );
        for ( int i = 0; i < length; ++i ) {
            System.out.print ( "*" );
        }
        System.out.println();
        System.out.println ( "Guess now  (To stop the program, enter #) : " );
        String str2 = "";
        int n;
        while ( true ) {
            n = 1;
            final String a2;
            if ( ( a2 = a() ).compareTo ( "#" ) == 0 ) {
                n = 0;
                break;
            }
            if ( ( str2 += a2 ).length() > 10 ) {
                str2 = str2.substring ( 0, 9 );
            }
            int n2 = 0;
            String s = "";
            for ( int j = 0; j < length; ++j ) {
                boolean b = false;
                for ( int k = 0; k < str2.length(); ++k ) {
                    if ( a2.indexOf ( str.charAt ( j ) ) >= 0 ) {
                        n2 = 1;
                    }
                    if ( str.charAt ( j ) == str2.charAt ( k ) ) {
                        s += str.charAt ( j );
                        b = true;
                        break;
                    }
                }
                if ( !b ) {
                    s += "*";
                    n = 0;
                }
            }
            if ( n2 == 1 ) {
                System.out.format ( "Correct: %s", s );
            } else {
                System.out.format ( "Wrong: %s", s );
            }
            if ( n == 1 ) {
                break;
            }
            System.out.println();
            System.out.print ( "Guesses : " );
            for ( int l = 0; l < str2.length(); ++l ) {
                System.out.format ( "%c ", str2.charAt ( l ) );
            }
            System.out.println();
            System.out.println();
            if ( str2.length() >= 10 ) {
                System.out.print ( "You have reached maximum amount of guesses." );
                n = 0;
                break;
            }
        }
        if ( n == 0 ) {
            System.out.println ( "\nUnfortunately you did not guess it correctly. The secret word is: " + str );
            return;
        }
        System.out.println ( "\nCongrats! You have guessed it correctly" );
    }
}
