import java.io.*;
import java.lang.*;
class SqrtAlgorithm {
    public static double m00 ( double v0 ) {
        long v1;
        v1 = ( long ) v0;
        long v2;
        v2 = v1;
        long v3;
        v3 = ( long ) ( ( v0 - v1 ) * 1000000 );
        long v4;
        v4 = v3;
        int v5;
        v5 = 0;
        int v6;
        v6 = 0;
        int v7;
        v7 = 0;
        int v8;
        v8 = 0;
        int v9;
        v9 = 0;
        int v10;
        v10 = 0;
        int v11;
        v11 = 1;
        int v12;
        v12 = 0;
        double v13;
        v13 = 0;
        int v14, v15, v16, v17, v18, v19, v20;
        v5 = 0;
        while ( v2 >= 10 ) {
            v2 = ( v2 / 10 );
            v5 = v5 + 1;
        }
        v5 = v5 + 1;
        v6 = 0;
        while ( v4 >= 10 ) {
            v4 = ( v4 / 10 );
            v6 = v6 + 1;
        }
        v6 = v6 + 1;
        if ( ( v5 % 2 ) == 1 ) {
            v9 = 1;
        }
        for ( ; true; ) {
            v11 = 1;
            if ( ( v9 == 1 ) ) {
                v7 = ( v5 - 1 );
            } else {
                v7 = ( v5 - 2 );
            }
            v14 = 0;
            while ( v14 < v7 ) {
                v11 = v11 * ( 10 );
                v14 = v14 + 1;
            }
            v10 = ( int ) v1 / v11;
            v18 = v10;
            v20 = v8 * 2;
            v17 = 0;
            v19 = 0;
            v17 = 1;
            while ( true ) {
                if ( v20 == 0 ) {
                    if ( v18 - v17 * v17 < 0 ) {
                        v8 = ( v17 - 1 );
                        break;
                    }
                } else {
                    v19 = v20 * 10 + v17;
                    if ( v18 - v17 * v19 < 0 ) {
                        v8 = v20 / 2 * 10 + v17 - 1;
                        break;
                    }
                }
                v17 = v17 + 1;
            }
            v15 = v8 / 10;
            v16 = v8 % 10;
            if ( v15 == 0 ) {
                v1 -= 0 - ( v8 * v8 * v11 );
            } else {
                v1 -= 0 - ( ( v15 * 2 * 10 + v16 ) * v16 * v11 );
            }
            if ( v1 == 0 && v3 == 0 ) {
                if ( v7 > 0 ) {
                    v11 = 1;
                    v7 = v7 / 2;
                    v14 = 0;
                    while ( v14 < v7 ) {
                        v11 = v11 * ( 10 );
                        v14 = v14 + 1;
                    }
                    v8 = v8 * ( v11 );
                }
                break;
            }
            if ( v9 == 1 ) {
                v5 = v5 - ( 1 );
                v9 = 0;
            } else {
                v5 = v5 - ( 2 );
            }
            if ( v5 <= 0 ) {
                if ( v1 > 0 || v3 > 0 ) {
                    if ( v12 >= 5 ) {
                        break;
                    }
                    v12 = v12 + 1;
                    v1 = v1 * ( 100 );
                    if ( v3 > 0 ) {
                        v6 = v6 - ( 2 );
                        v11 = 1;
                        v14 = 0;
                        while ( v14 < v6 ) {
                            v11 = v11 * ( 10 );
                            v14 = v14 + 1;
                        }
                        v1 = v1 + ( v3 / v11 );
                        v3 = v3 % v11;
                    }
                    v5 = v5 + ( 2 );
                } else {
                    break;
                }
            }
        }
        if ( v12 == 0 ) {
            v13 = v8;
        } else {
            v11 = 1;
            v14 = 0;
            while ( v14 < v12 ) {
                v11 = v11 * ( 10 );
                v14 = v14 + 1;
            }
            v13 = ( double ) v8 / v11;
        }
        return v13;
    }
    public static void main ( String[] v21 ) {
        double v22;
        v22 = 0;
        while ( v22 <= 10000 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( v22 );
            System.out.print ( ") = " );
            System.out.print ( m00 ( v22 ) );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( v22 ) );
            v22 = v22 + ( 50 );
        }
    }
}
