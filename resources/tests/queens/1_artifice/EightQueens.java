import java.util.*;
public class EightQueens {
    private int[] f00;
    private boolean[] f10;
    private int f20;
    public static void main ( String[] v0 ) {
        EightQueens v1;
        v1 = new EightQueens ( 5 );
        v1.m00();
        v1.m40();
    }
    public EightQueens ( int v2 ) {
        f00 = new int[v2];
        f10 = new boolean[v2];
        f20 = 0;
        int v3;
        v3 = 0;
        while ( v3 < v2 ) {
            f00[v3] = -1;
            f10[v3] = false;
            v3 = v3 + 1;
        }
    }
    public void m00() {
        m10 ( 0 );
    }
    public void m10 ( int v4 ) {
        int v5;
        if ( v4 == f00.length ) {
            m30();
            f20 = f20 + 1;
        }
        v5 = 0;
        while ( v5 < f00.length ) {
            if ( f10[v5] == false ) {
                if ( !m20 ( v4, v5 ) ) {
                    f00[v4] = v5;
                    f10[v5] = true;
                    m10 ( v4 + 1 );
                    f10[v5] = false;
                }
            }
            v5 = v5 + 1;
        }
    }
    private boolean m20 ( int v6, int v7 ) {
        int v8;
        v8 = 0;
        while ( v8 < v6 ) {
            if ( Math.abs ( v6 - v8 ) == Math.abs ( f00[v8] - v7 ) ) {
                return true;
            }
            v8 = v8 + 1;
        }
        return false;
    }
    public void m30() {
        int v9, v10;
        System.out.println ( "Here is a solution:\n" );
        v9 = 0;
        while ( v9 < f00.length ) {
            v10 = 0;
            while ( v10 < f00.length ) {
                if ( f00[v10] == v9 ) {
                    System.out.print ( "Q " );
                } else {
                    System.out.print ( "_ " );
                }
                v10 = v10 + 1;
            }
            System.out.println ( "\n" );
            v9 = v9 + 1;
        }
    }
    public void m40() {
        System.out.println ( "There were " + f20 + " solutions." );
    }
}
