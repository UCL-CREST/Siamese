class SLinkedList {
    private int f02;
    private SLinkedList f12;
    public SLinkedList() {
        super();
        this.f02 = 0;
        this.f12 = null;
    }
    public SLinkedList ( int i ) {
        super();
        this.f02 = i;
        this.f12 = null;
    }
    public SLinkedList m02 ( int i ) {
        SLinkedList a = new SLinkedList ( i );
        if ( this.f12 != null ) {
            a.f12 = this.f12;
            this.f12 = a;
        } else {
            a.f12 = null;
            this.f12 = a;
        }
        return a;
    }
    public int m12() {
        if ( this.f12 == null ) {
            return 0;
        }
        SLinkedList dummy = this.f12;
        this.f12 = this.f12.f12;
        return 1;
    }
    public void m22 ( SLinkedList a ) {
        if ( a == null ) {
            a = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( a != null ) {
            System.out.println ( a.f02 );
            a = a.f12;
        }
    }
    public SLinkedList m32 ( SLinkedList a ) {
        SLinkedList a0 = null;
        if ( a == null ) {
            SLinkedList a1 = null;
            return a1;
        }
        while ( a.f12 != a0 ) {
            SLinkedList a2 = a;
            while ( true ) {
                int i = a2.f02;
                a2.f02 = a2.f12.f02;
                a2.f12.f02 = i;
                a2 = a2.f12;
                if ( a2.f12 == a0 ) {
                    a0 = a2;
                    break;
                }
            }
        }
        return a;
    }
    public SLinkedList m42 ( SLinkedList a ) {
        if ( a == null ) {
            SLinkedList a0 = null;
            return a0;
        }
        SLinkedList a1 = null;
        while ( a != null ) {
            SLinkedList a2 = a.f12;
            a.f12 = a1;
            a = a2;
            a1 = a;
        }
        return a1;
    }
    public SLinkedList m52 ( SLinkedList a ) {
        SLinkedList a0 = null;
        while ( a != null ) {
            SLinkedList a1 = new SLinkedList ( a.f02 );
            a1.f12 = a0;
            a = a.f12;
            a0 = a1;
        }
        return a0;
    }
    public static void main ( String[] a ) {
        SLinkedList a0 = new SLinkedList ( 1 );
        a0.m02 ( 2 ).m02 ( 3 ).m02 ( 4 ).m02 ( 5 ).m02 ( 6 ).m02 ( 7 ).m02 ( 8 );
        a0.m22 ( ( SLinkedList ) null );
        a0.m42 ( a0 ).m22 ( ( SLinkedList ) null );
    }
}
