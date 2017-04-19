import java.lang.*;
import java.util.*;
import java.io.*;
class Kaprekar_Transformation {
    public static int m01() {
        try {
            InputStreamReader v0;
            v0 = new InputStreamReader ( System.in );
            BufferedReader v1;
            v1 = new BufferedReader ( v0 );
            return Integer.parseInt ( v1.readLine() );
        } catch ( Exception v2 ) {
            v2.printStackTrace();
            return 0;
        }
    }
    static int m11 ( int v3 ) {
        int v4, v5, v6;
        int v7, v8, v9;
        int v10, v11, v12;
        int v13;
        v13 = v3 / 100;
        int v14;
        v14 = ( v3 - v13 * 100 ) / 10;
        int v15;
        v15 = ( v3 % 10 );
        if ( v13 > v14 ) {
            if ( v13 > v15 ) {
                v7 = v13;
                if ( v14 > v15 ) {
                    v8 = v14;
                    v9 = v15;
                } else {
                    v9 = v14;
                    v8 = v15;
                }
            } else {
                v7 = v15;
                v8 = v13;
                v9 = v14;
            }
        } else {
            if ( v14 > v15 ) {
                v7 = v14;
                if ( v13 > v15 ) {
                    v8 = v13;
                    v9 = v15;
                } else {
                    v9 = v13;
                    v8 = v15;
                }
            } else {
                v7 = v15;
                v8 = v14;
                v9 = v13;
            }
        }
        if ( v13 < v14 ) {
            if ( v13 < v15 ) {
                v4 = v13;
                if ( v14 < v15 ) {
                    v5 = v14;
                    v6 = v15;
                } else {
                    v6 = v14;
                    v5 = v15;
                }
            } else {
                v4 = v15;
                v5 = v13;
                v6 = v14;
            }
        } else {
            if ( v14 < v15 ) {
                v4 = v14;
                if ( v13 < v15 ) {
                    v5 = v13;
                    v6 = v15;
                } else {
                    v6 = v13;
                    v5 = v15;
                }
            } else {
                v4 = v15;
                v5 = v14;
                v6 = v13;
            }
        }
        v10 = v7 * 100 + v8 * 10 + v9;
        v11 = v4 * 100 + v5 * 10 + v6;
        v12 = v10 - v11;
        System.out.format ( "%d - %d = %d\n", v10, v11, v12 );
        return v12;
    }
    public static void main ( String[] v16 ) {
        System.out.print ( "Enter a 3 digit number for Kaprekar Tranformation: " );
        int v17;
        v17 = m01();
        if ( v17 < 0 || v17 > 999 ) {
            System.out.println ( "Your input is not correct." );
        }
        int v18;
        v18 = v17 / 100;
        int v19;
        v19 = ( v17 - v18 * 100 ) / 10;
        int v20;
        v20 = ( v17 % 10 );
        if ( v18 == v19 && v18 == v20 ) {
            System.out.println ( "All digits should not be equal." );
            return;
        }
        for ( ; true; ) {
            int v21;
            v21 = m11 ( v17 );
            if ( v17 == v21 ) {
                break;
            }
            v17 = v21;
        }
    }
}
