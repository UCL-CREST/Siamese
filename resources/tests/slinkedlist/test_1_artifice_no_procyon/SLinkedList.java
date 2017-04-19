class SLinkedList {
    private int f02;
    private SLinkedList f12;
    public SLinkedList() {
        this.f02 = 0;
        this.f12 = null;
    }
    public SLinkedList ( final int f02 ) {
        this.f02 = f02;
        this.f12 = null;
    }
    public SLinkedList m02 ( final int n ) {
        final SLinkedList list = new SLinkedList ( n );
        if ( this.f12 == null ) {
            list.f12 = null;
            this.f12 = list;
        } else {
            list.f12 = this.f12;
            this.f12 = list;
        }
        return list;
    }
    public int m12() {
        if ( this.f12 == null ) {
            return 0;
        }
        final SLinkedList f12 = this.f12;
        this.f12 = this.f12.f12;
        return 1;
    }
    public void m22 ( SLinkedList f12 ) {
        if ( f12 == null ) {
            f12 = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( f12 != null ) {
            System.out.println ( f12.f02 );
            f12 = f12.f12;
        }
    }
    public SLinkedList m32 ( final SLinkedList list ) {
        SLinkedList list2 = null;
        if ( list == null ) {
            return list;
        }
        while ( true ) {
            SLinkedList f12 = list;
            if ( f12.f12 == list2 ) {
                break;
            }
            do {
                final int f2 = f12.f02;
                f12.f02 = f12.f12.f02;
                f12.f12.f02 = f2;
                f12 = f12.f12;
            } while ( f12.f12 != list2 );
            list2 = f12;
        }
        return list;
    }
    public SLinkedList m42 ( SLinkedList list ) {
        if ( list == null ) {
            return list;
        }
        SLinkedList list2 = list;
        SLinkedList f12 = null;
        while ( list2 != null ) {
            final SLinkedList f2 = list2.f12;
            list2.f12 = f12;
            f12 = list2;
            list2 = f2;
        }
        list = f12;
        return list;
    }
    public SLinkedList m52 ( final SLinkedList list ) {
        SLinkedList f12 = list;
        SLinkedList f2 = null;
        while ( f12 != null ) {
            final SLinkedList list2 = new SLinkedList ( f12.f02 );
            list2.f12 = f2;
            f2 = list2;
            f12 = f12.f12;
        }
        return f2;
    }
    public static void main ( final String[] array ) {
        final SLinkedList list = new SLinkedList ( 1 );
        list.m02 ( 2 ).m02 ( 3 ).m02 ( 4 ).m02 ( 5 ).m02 ( 6 ).m02 ( 7 ).m02 ( 8 );
        list.m22 ( null );
        list.m42 ( list ).m22 ( null );
    }
}
