public class MagicSquare {
    private int[][] f00;
    private boolean[] f10;
    private int f20;
    private int f30;
    private int f40;
    public static void main ( String[] v0 ) {
        MagicSquare v1;
        v1 = new MagicSquare ( 3 );
        v1.m00 ( 0, 0 );
        System.out.println ( "There were " + v1.m40() + " number of solutions." );
    }
    public MagicSquare ( int v2 ) {
        f00 = new int[v2][v2];
        int v3;
        v3 = 0;
        while ( v3 < v2 ) {
            int v4;
            v4 = 0;
            while ( v4 < v2 ) {
                f00[v3][v4] = 0;
                v4 = v4 + 1;
            }
            v3 = v3 + 1;
        }
        f20 = v2 * v2;
        f10 = new boolean[f20];
        int v5;
        v5 = 0;
        while ( v5 < f20 ) {
            f10[v5] = true;
            v5 = v5 + 1;
        }
        f30 = v2 * ( v2 * v2 + 1 ) / 2;
        f40 = 0;
    }
    public void m00 ( int v6, int v7 ) {
        if ( !m10() || !m20() || !m30() ) {
            return;
        }
        if ( v6 == f00.length ) {
            System.out.println ( this );
            f40 = f40 + 1;
            return;
        }
        int v8;
        v8 = 0;
        while ( v8 < f20 ) {
            if ( f10[v8] ) {
                f00[v6][v7] = v8 + 1;
                f10[v8] = false;
                int v9;
                v9 = v7 + 1;
                int v10;
                v10 = v6;
                if ( v9 == f00.length ) {
                    v10 = v10 + 1;
                    v9 = 0;
                }
                m00 ( v10, v9 );
                f00[v6][v7] = 0;
                f10[v8] = true;
            }
            v8 = v8 + 1;
        }
    }
    public boolean m10() {
        int v11;
        v11 = 0;
        while ( v11 < f00.length ) {
            int v12;
            v12 = 0;
            boolean v13;
            v13 = false;
            int v14;
            v14 = 0;
            while ( v14 < f00[v11].length ) {
                v12 = v12 + ( f00[v11][v14] );
                if ( f00[v11][v14] == 0 ) {
                    v13 = true;
                }
                v14 = v14 + 1;
            }
            if ( !v13 && v12 != f30 ) {
                return false;
            }
            v11 = v11 + 1;
        }
        return true;
    }
    public boolean m20() {
        int v15;
        v15 = 0;
        while ( v15 < f00[0].length ) {
            int v16;
            v16 = 0;
            boolean v17;
            v17 = false;
            int v18;
            v18 = 0;
            while ( v18 < f00.length ) {
                v16 = v16 + ( f00[v18][v15] );
                if ( f00[v18][v15] == 0 ) {
                    v17 = true;
                }
                v18 = v18 + 1;
            }
            if ( !v17 && v16 != f30 ) {
                return false;
            }
            v15 = v15 + 1;
        }
        return true;
    }
    public boolean m30() {
        int v19;
        v19 = 0;
        boolean v20;
        v20 = false;
        int v21;
        v21 = 0;
        while ( v21 < f00.length ) {
            v19 = v19 + ( f00[v21][v21] );
            if ( f00[v21][v21] == 0 ) {
                v20 = true;
            }
            v21 = v21 + 1;
        }
        if ( !v20 && v19 != f30 ) {
            return false;
        }
        v19 = 0;
        v20 = false;
        int v22;
        v22 = 0;
        while ( v22 < f00.length ) {
            v19 = v19 + ( f00[v22][f00.length - 1 - v22] );
            if ( f00[v22][f00.length - 1 - v22] == 0 ) {
                v20 = true;
            }
            v22 = v22 + 1;
        }
        if ( !v20 && v19 != f30 ) {
            return false;
        }
        return true;
    }
    public int m40() {
        return f40;
    }
    public String toString() {
        String v23;
        v23 = "";
        int v24;
        v24 = 0;
        while ( v24 < f00.length ) {
            int v25;
            v25 = 0;
            while ( v25 < f00[v24].length ) {
                v23 += 0 + f00[v24][v25] + "\t";
                v25 = v25 + 1;
            }
            v23 += 0 + "\n";
            v24 = v24 + 1;
        }
        return v23;
    }
}
