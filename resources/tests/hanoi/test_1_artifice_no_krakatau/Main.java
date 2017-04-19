class Main {
    static int f00;
    public static java.util.Stack f10;
    public static java.util.Stack f20;
    public static java.util.Stack f30;
    static int f40;
    static int f50;
    static int f60;
    Main() {
        super();
    }
    public static void m00 ( java.util.Stack a, java.util.Stack a0, java.util.Stack a1 ) {
        a0.push ( a.pop() );
        f00 = f00 + 1;
        Main.m20();
        a1.push ( a.pop() );
        f00 = f00 + 1;
        Main.m20();
        a1.push ( a0.pop() );
        f00 = f00 + 1;
        Main.m20();
    }
    public static int m10 ( int i, java.util.Stack a, java.util.Stack a0, java.util.Stack a1 ) {
        if ( i > 4 ) {
            Main.m10 ( i - 2, a, a0, a1 );
            a0.push ( a.pop() );
            f00 = f00 + 1;
            Main.m20();
            Main.m10 ( i - 2, a1, a, a0 );
            a1.push ( a.pop() );
            f00 = f00 + 1;
            Main.m20();
            Main.m10 ( i - 1, a0, a, a1 );
            return 1;
        } else {
            if ( i % 2 != 0 ) {
                if ( i == 1 ) {
                    return -1;
                }
                Main.m00 ( a, a1, a0 );
                a1.push ( a.pop() );
                f00 = f00 + 1;
                Main.m20();
                Main.m00 ( a0, a, a1 );
            } else {
                Main.m00 ( a, a0, a1 );
                int i0 = i - 1;
                if ( i0 == 1 ) {
                    return 1;
                }
                a0.push ( a.pop() );
                f00 = f00 + 1;
                Main.m20();
                Main.m00 ( a1, a, a0 );
                a1.push ( a.pop() );
                f00 = f00 + 1;
                Main.m20();
                Main.m10 ( i0, a0, a, a1 );
            }
            return 1;
        }
    }
    public static void m20() {
        int i = f40;
        int i0 = f10.size();
        label0: {
            label1: {
                if ( i != i0 ) {
                    break label1;
                }
                if ( f50 != f20.size() ) {
                    break label1;
                }
                if ( f60 == f30.size() ) {
                    break label0;
                }
            }
            int i1 = f10.size() - f40;
            int i2 = f20.size() - f50;
            f30.size();
            int dummy = f60;
            if ( i1 != 1 ) {
                if ( i2 != 1 ) {
                    if ( i1 != -1 ) {
                        System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f30.peek() ).append ( " From B To C" ).toString() );
                    } else {
                        System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f30.peek() ).append ( " From A To C" ).toString() );
                    }
                } else if ( i1 != -1 ) {
                    System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f20.peek() ).append ( " From C To B" ).toString() );
                } else {
                    System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f20.peek() ).append ( " From A To B" ).toString() );
                }
            } else if ( i2 != -1 ) {
                System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f10.peek() ).append ( " From C To A" ).toString() );
            } else {
                System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( f10.peek() ).append ( " From B To A" ).toString() );
            }
            f40 = f10.size();
            f50 = f20.size();
            f60 = f30.size();
            System.out.println();
        }
        Main.m30 ( f10 );
        System.out.print ( " , " );
        Main.m30 ( f20 );
        System.out.print ( " , " );
        Main.m30 ( f30 );
        System.out.print ( " , " );
    }
    public static void m30 ( java.util.Stack a ) {
        System.out.print ( a.toString() );
    }
    public static void main ( String[] a ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                String s = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine();
                f00 = 0;
                int i = Integer.parseInt ( s );
                if ( i != -1 ) {
                    if ( i > 1 && i < 10 ) {
                        int i0 = i;
                        while ( i0 >= 1 ) {
                            f10.push ( ( Object ) Integer.valueOf ( i0 ) );
                            i0 = i0 - 1;
                        }
                        f40 = f10.size();
                        f50 = f20.size();
                        f60 = f30.size();
                        Main.m20();
                        Main.m10 ( i, f10, f20, f30 );
                        System.out.println ( new StringBuilder().append ( "Total Moves = " ).append ( f00 ).toString() );
                        while ( f30.size() > 0 ) {
                            f30.pop();
                        }
                        continue;
                    }
                    System.out.println ( "Enter between 2 - 9" );
                } else {
                    break;
                }
            }
            System.out.println ( "Good Bye!" );
        } catch ( Exception a0 ) {
            a0.printStackTrace();
            return;
        }
    }
    static {
        f00 = 0;
        f10 = new java.util.Stack();
        f20 = new java.util.Stack();
        f30 = new java.util.Stack();
        f40 = 0;
        f50 = 0;
        f60 = 0;
    }
}
