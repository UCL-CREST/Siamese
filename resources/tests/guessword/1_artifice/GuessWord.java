import java.io.*;
import java.lang.*;
import java.util.*;
class GuessWord {
    public static int m04 ( String[] v0 ) {
        try {
            FileReader v1;
            v1 = new FileReader ( "words_input.txt" );
            BufferedReader v2;
            v2 = new BufferedReader ( v1 );
            int v3;
            v3 = 0;
            int v4;
            v4 = 0;
            while ( v4 < 100 ) {
                String v5;
                v5 = v2.readLine();
                if ( v5 == null ) {
                    break;
                }
                v0[+v3] = v5;
                v3 = v3 + 1;
                v4 = v4 + 1;
            }
            v1.close();
            return v3;
        } catch ( FileNotFoundException v6 ) {
            System.out.println ( v6.getMessage() );
            return -1;
        } catch ( IOException v7 ) {
            System.out.println ( v7.getStackTrace() );
            return -1;
        }
    }
    static public String m14() {
        try {
            String v8;
            v8 = "";
            InputStreamReader v9;
            v9 = new InputStreamReader ( System.in );
            BufferedReader v10;
            v10 = new BufferedReader ( v9 );
            return v10.readLine();
        } catch ( Exception v11 ) {
            v11.printStackTrace();
        }
        return "";
    }
    public static void main ( String[] v12 ) {
        System.out.println ( "Welcome to Guess a Word\n" );
        String[] v13;
        v13 = new String[100];
        int v14;
        v14 = m04 ( v13 );
        if ( v14 < 0 ) {
            System.out.println ( "No words found in the file" );
            return;
        }
        int v15;
        v15 = ( int ) ( Math.random() * 100 );
        int v16;
        v16 = ( v15 % v14 );
        String v17;
        v17 = v13[v16];
        int v18;
        v18 = v17.length();
        System.out.print ( "Your secret word is: " );
        int v19;
        v19 = 0;
        while ( v19 < v18 ) {
            System.out.print ( "*" );
            v19 = v19 + 1;
        }
        System.out.println();
        boolean v20;
        v20 = true;
        System.out.println ( "Guess now  (To stop the program, enter #) : " );
        String v21;
        v21 = "";
        for ( ; true; ) {
            v20 = true;
            String v22;
            v22 = m14();
            if ( v22.compareTo ( "#" ) == 0 ) {
                v20 = false;
                break;
            }
            v21 = v21 + ( v22 );
            if ( v21.length() > 10 ) {
                v21 = v21.substring ( 0, 9 );
            }
            boolean v23;
            v23 = false;
            String v24;
            v24 = "";
            int v25;
            v25 = 0;
            while ( v25 < v18 ) {
                boolean v26;
                v26 = false;
                int v27;
                v27 = 0;
                while ( v27 < v21.length() ) {
                    if ( v22.indexOf ( v17.charAt ( v25 ) ) >= 0 ) {
                        v23 = true;
                    }
                    if ( v17.charAt ( v25 ) == v21.charAt ( v27 ) ) {
                        v24 = v24 + ( v17.charAt ( v25 ) );
                        v26 = true;
                        break;
                    }
                    v27 = v27 + 1;
                }
                if ( v26 == false ) {
                    v24 = v24 + ( "*" );
                    v20 = false;
                }
                v25 = v25 + 1;
            }
            if ( v23 == true ) {
                System.out.format ( "Correct: %s", v24 );
            } else {
                System.out.format ( "Wrong: %s", v24 );
            }
            if ( v20 == true ) {
                break;
            }
            System.out.println();
            System.out.print ( "Guesses : " );
            int v28;
            v28 = 0;
            while ( v28 < v21.length() ) {
                System.out.format ( "%c ", v21.charAt ( v28 ) );
                v28 = v28 + 1;
            }
            System.out.println();
            System.out.println();
            if ( v21.length() >= 10 ) {
                System.out
                .print ( "You have reached maximum amount of guesses." );
                v20 = false;
                break;
            }
        }
        if ( v20 == false ) {
            System.out.println ( "\nUnfortunately you did not guess it correctly. The secret word is: " + v17 );
        } else {
            System.out.println ( "\nCongrats! You have guessed it correctly" );
        }
    }
}
