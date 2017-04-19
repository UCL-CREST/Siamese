class GuessWord {
    GuessWord() {
        super();
    }
    public static int m04 ( String[] a ) {
        java.io.FileNotFoundException a0 = null;
        try {
            try {
                java.io.FileReader a1 = new java.io.FileReader ( "words_input.txt" );
                java.io.BufferedReader a2 = new java.io.BufferedReader ( ( java.io.Reader ) a1 );
                int i = 0;
                int i0 = 0;
                while ( true ) {
                    if ( i0 < 100 ) {
                        String s = a2.readLine();
                        if ( s != null ) {
                            a[i] = s;
                            i = i + 1;
                            i0 = i0 + 1;
                            continue;
                        }
                    }
                    a1.close();
                    return i;
                }
            } catch ( java.io.FileNotFoundException a3 ) {
                a0 = a3;
            }
        } catch ( java.io.IOException a4 ) {
            System.out.println ( ( Object ) a4.getStackTrace() );
            return -1;
        }
        System.out.println ( a0.getMessage() );
        return -1;
    }
    public static String m14() {
        String s = null;
        try {
            s = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine();
        } catch ( Exception a ) {
            a.printStackTrace();
            return "";
        }
        return s;
    }
    public static void main ( String[] a ) {
        System.out.println ( "Welcome to Guess a Word\n" );
        String[] a0 = new String[100];
        int i = GuessWord.m04 ( a0 );
        if ( i < 0 ) {
            System.out.println ( "No words found in the file" );
            return;
        }
        String s = a0[ ( int ) ( Math.random() * 100.0 ) % i];
        int i0 = s.length();
        System.out.print ( "Your secret word is: " );
        int i1 = 0;
        while ( i1 < i0 ) {
            System.out.print ( "*" );
            i1 = i1 + 1;
        }
        System.out.println();
        System.out.println ( "Guess now  (To stop the program, enter #) : " );
        String s0 = "";
        while ( true ) {
            boolean b = false;
            String s1 = GuessWord.m14();
            if ( s1.compareTo ( "#" ) != 0 ) {
                s0 = new StringBuilder().append ( s0 ).append ( s1 ).toString();
                if ( s0.length() > 10 ) {
                    s0 = s0.substring ( 0, 9 );
                }
                boolean b0 = true;
                boolean b1 = false;
                String s2 = "";
                int i2 = 0;
                while ( i2 < i0 ) {
                    int i3 = 0;
                    while ( true ) {
                        boolean b2 = false;
                        if ( i3 >= s0.length() ) {
                            b2 = false;
                        } else {
                            int i4 = s.charAt ( i2 );
                            if ( s1.indexOf ( i4 ) >= 0 ) {
                                b1 = true;
                            }
                            int i5 = s.charAt ( i2 );
                            int i6 = s0.charAt ( i3 );
                            if ( i5 != i6 ) {
                                i3 = i3 + 1;
                                continue;
                            }
                            StringBuilder a1 = new StringBuilder().append ( s2 );
                            int i7 = s.charAt ( i2 );
                            s2 = a1.append ( ( char ) i7 ).toString();
                            b2 = true;
                        }
                        if ( !b2 ) {
                            s2 = new StringBuilder().append ( s2 ).append ( "*" ).toString();
                            b0 = false;
                        }
                        i2 = i2 + 1;
                        break;
                    }
                }
                if ( b1 ) {
                    java.io.PrintStream a2 = System.out;
                    Object[] a3 = new Object[1];
                    a3[0] = s2;
                    a2.format ( "Correct: %s", a3 );
                } else {
                    java.io.PrintStream a4 = System.out;
                    Object[] a5 = new Object[1];
                    a5[0] = s2;
                    a4.format ( "Wrong: %s", a5 );
                }
                if ( b0 ) {
                    b = true;
                } else {
                    System.out.println();
                    System.out.print ( "Guesses : " );
                    int i8 = 0;
                    while ( i8 < s0.length() ) {
                        java.io.PrintStream a6 = System.out;
                        Object[] a7 = new Object[1];
                        int i9 = s0.charAt ( i8 );
                        a7[0] = Character.valueOf ( ( char ) i9 );
                        a6.format ( "%c ", a7 );
                        i8 = i8 + 1;
                    }
                    System.out.println();
                    System.out.println();
                    if ( s0.length() < 10 ) {
                        continue;
                    }
                    System.out.print ( "You have reached maximum amount of guesses." );
                    b = false;
                }
            } else {
                b = false;
            }
            if ( b ) {
                System.out.println ( "\nCongrats! You have guessed it correctly" );
            } else {
                System.out.println ( new StringBuilder().append ( "\nUnfortunately you did not guess it correctly. The secret word is: " ).append ( s ).toString() );
            }
            return;
        }
    }
}
