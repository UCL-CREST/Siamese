import java.io.*;
import java.lang.*;
import java.util.*;
class Main {
    static int f00 = 0;
    static public void m00 ( Stack v0, Stack v1, Stack v2 ) {
        v1.push ( v0.pop() );
        f00 = f00 + 1;
        m20();
        v2.push ( v0.pop() );
        f00 = f00 + 1;
        m20();
        v2.push ( v1.pop() );
        f00 = f00 + 1;
        m20();
    }
    static public int m10 ( int v3, Stack v4, Stack v5, Stack v6 ) {
        if ( v3 <= 4 ) {
            if ( ( v3 % 2 ) == 0 ) {
                m00 ( v4, v5, v6 );
                v3 = v3 - 1;
                if ( v3 == 1 ) {
                    return 1;
                }
                v5.push ( v4.pop() );
                f00 = f00 + 1;
                m20();
                m00 ( v6, v4, v5 );
                v6.push ( v4.pop() );
                f00 = f00 + 1;
                m20();
                m10 ( v3, v5, v4, v6 );
            } else {
                if ( v3 == 1 ) {
                    return -1;
                }
                m00 ( v4, v6, v5 );
                v3 = v3 - 1;
                v6.push ( v4.pop() );
                f00 = f00 + 1;
                m20();
                m00 ( v5, v4, v6 );
            }
            return 1;
        } else if ( v3 >= 5 ) {
            m10 ( v3 - 2, v4, v5, v6 );
            v5.push ( v4.pop() );
            f00 = f00 + 1;
            m20();
            m10 ( v3 - 2, v6, v4, v5 );
            v6.push ( v4.pop() );
            f00 = f00 + 1;
            m20();
            m10 ( v3 - 1, v5, v4, v6 );
        }
        return 1;
    }
    static public Stack f10 = new Stack();
    static public Stack f20 = new Stack();
    static public Stack f30 = new Stack();
    static public void m20() {
        if ( f40 != f10.size() ||
                f50 != f20.size() ||
                f60 != f30.size() ) {
            int v7;
            v7 = f10.size() - f40;
            int v8;
            v8 = f20.size() - f50;
            int v9;
            v9 = f30.size() - f60;
            if ( v7 == 1 ) {
                if ( v8 == -1 ) {
                    System.out.print ( "Move Disc " + f10.peek() + " From B To A" );
                } else {
                    System.out.print ( "Move Disc " + f10.peek() + " From C To A" );
                }
            } else if ( v8 == 1 ) {
                if ( v7 == -1 ) {
                    System.out.print ( "Move Disc " + f20.peek() + " From A To B" );
                } else {
                    System.out.print ( "Move Disc " + f20.peek() + " From C To B" );
                }
            } else {
                if ( v7 == -1 ) {
                    System.out.print ( "Move Disc " + f30.peek() + " From A To C" );
                } else {
                    System.out.print ( "Move Disc " + f30.peek() + " From B To C" );
                }
            }
            f40 = f10.size();
            f50 = f20.size();
            f60 = f30.size();
            System.out.println();
        }
        m30 ( f10 );
        System.out.print ( " , " );
        m30 ( f20 );
        System.out.print ( " , " );
        m30 ( f30 );
        System.out.print ( " , " );
    }
    static int f40 = 0;
    static int f50 = 0;
    static int f60 = 0;
    static public void m30 ( Stack v10 ) {
        System.out.print ( v10.toString() );
    }
    public static void main ( String[] v11 ) {
        try {
            for ( ; true; ) {
                System.out.print ( "\nEnter the number of discs (-1 to exit): " );
                int v12;
                v12 = 0;
                String v13;
                v13 = "";
                InputStreamReader v14;
                v14 = new InputStreamReader ( System.in );
                BufferedReader v15;
                v15 = new BufferedReader ( v14 );
                v13 = v15.readLine();
                f00 = 0;
                v12 = Integer.parseInt ( v13 );
                if ( v12 == -1 ) {
                    System.out.println ( "Good Bye!" );
                    return;
                }
                if ( v12 <= 1 || v12 >= 10 ) {
                    System.out.println ( "Enter between 2 - 9" );
                    continue;
                }
                int v16;
                v16 = v12;
                while ( v16 >= 1 ) {
                    f10.push ( v16 );
                    v16 = v16 - 1;
                }
                f40 = f10.size();
                f50 = f20.size();
                f60 = f30.size();
                m20();
                m10 ( v12, f10, f20, f30 );
                System.out.println ( "Total Moves = " + f00 );
                for ( ; f30.size() > 0; ) {
                    f30.pop();
                }
            }
        } catch ( Exception v17 ) {
            v17.printStackTrace();
        }
    }
}
