import java.lang.*;
import java.util.*;
import java.io.*;
class SLinkedList {
    private int f02;
    private SLinkedList f12;
    public SLinkedList() {
        f02 = 0;
        f12 = null;
    }
    public SLinkedList ( int v0 ) {
        f02 = v0;
        f12 = null;
    }
    public SLinkedList m02 ( int v1 ) {
        SLinkedList v2;
        v2 = new SLinkedList ( v1 );
        if ( this.f12 == null ) {
            v2.f12 = null; 
            this.f12 = v2;
        } else {
            SLinkedList v3;
            v3 = this.f12;
            v2.f12 = v3;
            this.f12 = v2;
        }
        return v2;
    }
    public int m12() {
        if ( f12 == null ) {
            return 0;
        }
        SLinkedList v4;
        v4 = this.f12;
        this.f12 = this.f12.f12;  
        v4 = null;
        return 1;
    }
    public void m22 ( SLinkedList v5 ) {
        if ( v5 == null ) {
            v5 = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        for ( ; v5 != null; ) {
            System.out.println ( v5.f02 );
            v5 = v5.f12;
        }
    }
    public SLinkedList m32 ( SLinkedList v6 ) {
        SLinkedList v7;
        v7 = null;
        if ( v6 == null ) {
            return v6;
        }
        for ( ; true; ) {
            SLinkedList v8;
            v8 = v6;
            if ( v8.f12 == v7 ) {
                break;
            }
            for ( ; true; ) {
                int v9;
                v9 = v8.f02;
                v8.f02 = v8.f12.f02;
                v8.f12.f02 = v9;
                v8 = v8.f12;
                if ( v8.f12 == v7 ) {
                    v7 = v8;
                    break;
                }
            }
        }
        return v6;
    }
    public SLinkedList m42 ( SLinkedList v10 ) {
        if ( v10 == null ) {
            return v10;
        }
        SLinkedList v11;
        v11 = v10;
        SLinkedList v12;
        v12 = null;
        for ( ; v11 != null; ) {
            SLinkedList v13;
            v13 = v11.f12;
            v11.f12 = v12;
            v12 = v11;
            v11 = v13;
        }
        v10 = v12;
        return v10;
    }
    public SLinkedList m52 ( SLinkedList v14 ) {
        SLinkedList v15;
        v15 = v14;
        SLinkedList v16;
        v16 = null;
        for ( ; true; ) {
            if ( v15 == null ) {
                return v16;
            }
            SLinkedList v17;
            v17 = new SLinkedList ( v15.f02 );
            v17.f12 = v16;
            v16 = v17;
            v15 = v15.f12;
        }
    }
    public static void main ( String[] v18 ) {
        SLinkedList v19;
        v19 = new SLinkedList ( 1 );
        SLinkedList v20;
        v20 = v19.m02 ( 2 );
        v20 = v20.m02 ( 3 );
        v20 = v20.m02 ( 4 );
        v20 = v20.m02 ( 5 );
        v20 = v20.m02 ( 6 );
        v20 = v20.m02 ( 7 );
        v20 = v20.m02 ( 8 );
        v19.m22 ( null );
        SLinkedList v21;
        v21 = v19.m42 ( v19 );
        v21.m22 ( null );
    }
}
