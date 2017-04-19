import java.lang.*;
import java.util.*;
import java.io.*;
class InfixConverter {
    public static String m00() {
        try {
            InputStreamReader v0;
            v0 = new InputStreamReader ( System.in );
            BufferedReader v1;
            v1 = new BufferedReader ( v0 );
            return v1.readLine();
        } catch ( Exception v2 ) {
            v2.printStackTrace();
            return "";
        }
    }
    public static int m10() {
        try {
            InputStreamReader v3;
            v3 = new InputStreamReader ( System.in );
            BufferedReader v4;
            v4 = new BufferedReader ( v3 );
            return Integer.parseInt ( v4.readLine() );
        } catch ( Exception v5 ) {
            v5.printStackTrace();
            return 0;
        }
    }
    public static String m20 ( String v6 ) {
        int v7;
        v7 = 0;
        String v8;
        v8 = "";
        Stack v9;
        v9 = new Stack();
        int v10;
        v10 = 0;
        while ( v10 < v6.length() ) {
            char v11;
            v11 = v6.charAt ( v10 );
            if ( v11 == '+' || v11 == '-' || v11 == '*' || v11 == '/' ) {
                if ( v9.size() <= 0 ) {
                    v9.push ( v11 );
                } else {
                    Character v12;
                    v12 = ( Character ) v9.peek();
                    if ( v12 == '*' || v12 == '/' ) {
                        v7 = 1;
                    } else {
                        v7 = 0;
                    }
                    if ( v7 == 1 ) {
                        if ( v11 == '+' || v11 == '-' ) {
                            v8 = v8 + ( v9.pop() );
                            v10 = v10 - 1;
                        } else {
                            v8 = v8 + ( v9.pop() );
                            v10 = v10 - 1;
                        }
                    } else {
                        if ( v11 == '+' || v11 == '-' ) {
                            v8 = v8 + ( v9.pop() );
                            v9.push ( v11 );
                        } else {
                            v9.push ( v11 );
                        }
                    }
                }
            } else {
                v8 = v8 + ( v11 );
            }
            v10 = v10 + 1;
        }
        int v13;
        v13 = v9.size();
        int v14;
        v14 = 0;
        while ( v14 < v13 ) {
            v8 = v8 + ( v9.pop() );
            v14 = v14 + 1;
        }
        return v8;
    }
    public static void main ( String[] v15 ) {
        String v16;
        v16 = "";
        String v17;
        v17 = "";
        if ( v15.length == 1 ) {
            v16 = v15[0];
            v17 = m20 ( v16 );
            System.out.println ( "InFix  :\t" + v16 );
            System.out.println ( "PostFix:\t" + v17 );
            System.out.println();
        } else {
            v16 = "a+b*c";
            v17 = m20 ( v16 );
            System.out.println ( "InFix  :\t" + v16 );
            System.out.println ( "PostFix:\t" + v17 );
            System.out.println();
            v16 = "a+b*c/d-e";
            v17 = m20 ( v16 );
            System.out.println ( "InFix  :\t" + v16 );
            System.out.println ( "PostFix:\t" + v17 );
            System.out.println();
            v16 = "a+b*c/d-e+f*h/i+j-k";
            v17 = m20 ( v16 );
            System.out.println ( "InFix  :\t" + v16 );
            System.out.println ( "PostFix:\t" + v17 );
            System.out.println();
        }
    }
}
