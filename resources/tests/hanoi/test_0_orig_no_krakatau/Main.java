class Main {
    static int movecount;
    public static java.util.Stack A;
    public static java.util.Stack B;
    public static java.util.Stack C;
    static int countA;
    static int countB;
    static int countC;
    Main() {
        super();
    }
    public static void Solve2DiscsTOH ( java.util.Stack a, java.util.Stack a0, java.util.Stack a1 ) {
        a0.push ( a.pop() );
        movecount = movecount + 1;
        Main.PrintStacks();
        a1.push ( a.pop() );
        movecount = movecount + 1;
        Main.PrintStacks();
        a1.push ( a0.pop() );
        movecount = movecount + 1;
        Main.PrintStacks();
    }
    public static int SolveTOH ( int i, java.util.Stack a, java.util.Stack a0, java.util.Stack a1 ) {
        if ( i > 4 ) {
            Main.SolveTOH ( i - 2, a, a0, a1 );
            a0.push ( a.pop() );
            movecount = movecount + 1;
            Main.PrintStacks();
            Main.SolveTOH ( i - 2, a1, a, a0 );
            a1.push ( a.pop() );
            movecount = movecount + 1;
            Main.PrintStacks();
            Main.SolveTOH ( i - 1, a0, a, a1 );
            return 1;
        } else {
            if ( i % 2 != 0 ) {
                if ( i == 1 ) {
                    return -1;
                }
                Main.Solve2DiscsTOH ( a, a1, a0 );
                a1.push ( a.pop() );
                movecount = movecount + 1;
                Main.PrintStacks();
                Main.Solve2DiscsTOH ( a0, a, a1 );
            } else {
                Main.Solve2DiscsTOH ( a, a0, a1 );
                int i0 = i - 1;
                if ( i0 == 1 ) {
                    return 1;
                }
                a0.push ( a.pop() );
                movecount = movecount + 1;
                Main.PrintStacks();
                Main.Solve2DiscsTOH ( a1, a, a0 );
                a1.push ( a.pop() );
                movecount = movecount + 1;
                Main.PrintStacks();
                Main.SolveTOH ( i0, a0, a, a1 );
            }
            return 1;
        }
    }
    public static void PrintStacks() {
        int i = countA;
        int i0 = A.size();
        label0: {
            label1: {
                if ( i != i0 ) {
                    break label1;
                }
                if ( countB != B.size() ) {
                    break label1;
                }
                if ( countC == C.size() ) {
                    break label0;
                }
            }
            int i1 = A.size() - countA;
            int i2 = B.size() - countB;
            C.size();
            int dummy = countC;
            if ( i1 != 1 ) {
                if ( i2 != 1 ) {
                    if ( i1 != -1 ) {
                        System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( C.peek() ).append ( " From B To C" ).toString() );
                    } else {
                        System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( C.peek() ).append ( " From A To C" ).toString() );
                    }
                } else if ( i1 != -1 ) {
                    System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( B.peek() ).append ( " From C To B" ).toString() );
                } else {
                    System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( B.peek() ).append ( " From A To B" ).toString() );
                }
            } else if ( i2 != -1 ) {
                System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( A.peek() ).append ( " From C To A" ).toString() );
            } else {
                System.out.print ( new StringBuilder().append ( "Move Disc " ).append ( A.peek() ).append ( " From B To A" ).toString() );
            }
            countA = A.size();
            countB = B.size();
            countC = C.size();
            System.out.println();
        }
        Main.PrintStack ( A );
        System.out.print ( " , " );
        Main.PrintStack ( B );
        System.out.print ( " , " );
        Main.PrintStack ( C );
        System.out.print ( " , " );
    }
    public static void PrintStack ( java.util.Stack a ) {
        System.out.print ( a.toString() );
    }
    public static void main ( String[] a ) {
        try {
            while ( true ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                String s = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine();
                movecount = 0;
                int i = Integer.parseInt ( s );
                if ( i != -1 ) {
                    if ( i > 1 && i < 10 ) {
                        int i0 = i;
                        while ( i0 >= 1 ) {
                            A.push ( ( Object ) Integer.valueOf ( i0 ) );
                            i0 = i0 + -1;
                        }
                        countA = A.size();
                        countB = B.size();
                        countC = C.size();
                        Main.PrintStacks();
                        Main.SolveTOH ( i, A, B, C );
                        System.out.println ( new StringBuilder().append ( "Total Moves = " ).append ( movecount ).toString() );
                        while ( C.size() > 0 ) {
                            C.pop();
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
        movecount = 0;
        A = new java.util.Stack();
        B = new java.util.Stack();
        C = new java.util.Stack();
        countA = 0;
        countB = 0;
        countC = 0;
    }
}
