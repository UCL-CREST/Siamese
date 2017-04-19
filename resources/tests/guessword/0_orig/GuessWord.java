import java.io.*;
import java.lang.*;
import java.util.*;
class GuessWord {
    public static int ReadWordsFromFile ( String[] words ) {
        try {
            FileReader fr = new FileReader ( "words_input.txt" );
            BufferedReader br = new BufferedReader ( fr );
            int count = 0;
            for ( int i = 0; i < 100; i++ ) {
                String s = br.readLine();
                if ( s == null ) {
                    break;
                }
                words[count++] = s;
            }
            fr.close();
            return count;
        } catch ( FileNotFoundException e ) {
            System.out.println ( e.getMessage() );
            return -1;
        } catch ( IOException err ) {
            System.out.println ( err.getStackTrace() );
            return -1;
        }
    }
    static public String ReadString() {
        try {
            String inpString = "";
            InputStreamReader input = new InputStreamReader ( System.in );
            BufferedReader reader = new BufferedReader ( input );
            return reader.readLine();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main ( String[] args ) {
        System.out.println ( "Welcome to Guess a Word\n" );
        String[] words = new String[100];
        int count = ReadWordsFromFile ( words );
        if ( count < 0 ) {
            System.out.println ( "No words found in the file" );
            return;
        }
        int x = ( int ) ( Math.random() * 100 );
        int guessX = ( x % count );
        String secretWord = words[guessX];
        int numChars = secretWord.length();
        System.out.print ( "Your secret word is: " );
        for ( int i = 0; i < numChars; i++ ) {
            System.out.print ( "*" );
        }
        System.out.println();
        boolean bGuessedCorrectly = true;
        System.out.println ( "Guess now  (To stop the program, enter #) : " );
        String choicelist = "";
        while ( true ) {
            bGuessedCorrectly = true;
            String choice = ReadString();
            if ( choice.compareTo ( "#" ) == 0 ) {
                bGuessedCorrectly = false;
                break;
            }
            choicelist += choice;
            if ( choicelist.length() > 10 ) {
                choicelist = choicelist.substring ( 0, 9 );
            }
            boolean bResult = false;
            String dispstr = "";
            for ( int i = 0; i < numChars; i++ ) {
                boolean bCorrect = false;
                for ( int j = 0; j < choicelist.length(); j++ ) {
                    if ( choice.indexOf ( secretWord.charAt ( i ) ) >= 0 ) {
                        bResult = true;
                    }
                    if ( secretWord.charAt ( i ) == choicelist.charAt ( j ) ) {
                        dispstr += secretWord.charAt ( i );
                        bCorrect = true;
                        break;
                    }
                }
                if ( bCorrect == false ) {
                    dispstr += "*";
                    bGuessedCorrectly = false;
                }
            }
            if ( bResult == true ) {
                System.out.format ( "Correct: %s", dispstr );
            } else {
                System.out.format ( "Wrong: %s", dispstr );
            }
            if ( bGuessedCorrectly == true ) {
                break;
            }
            System.out.println();
            System.out.print ( "Guesses : " );
            for ( int i = 0; i < choicelist.length(); i++ ) {
                System.out.format ( "%c ", choicelist.charAt ( i ) );
            }
            System.out.println();
            System.out.println();
            if ( choicelist.length() >= 10 ) {
                System.out.print ( "You have reached maximum amount of guesses." );
                bGuessedCorrectly = false;
                break;
            }
        }
        if ( bGuessedCorrectly == false ) {
            System.out.println ( "\nUnfortunately you did not guess it correctly. The secret word is: " + secretWord );
        } else {
            System.out.println ( "\nCongrats! You have guessed it correctly" );
        }
    }
}
