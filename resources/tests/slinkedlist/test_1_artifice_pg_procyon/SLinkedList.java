class SLinkedList {
    private int a;
    private SLinkedList b;
    public SLinkedList() {
        this.a = 0;
        this.b = null;
    }
    private SLinkedList ( final int a ) {
        this.a = a;
        this.b = null;
    }
    private SLinkedList a ( final int n ) {
        final SLinkedList list = new SLinkedList ( n );
        if ( this.b == null ) {
            list.b = null;
            this.b = list;
        } else {
            list.b = this.b;
            this.b = list;
        }
        return list;
    }
    private void a ( SLinkedList b ) {
        b = this;
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( b != null ) {
            System.out.println ( b.a );
            b = b.b;
        }
    }
    public static void main ( final String[] array ) {
        final SLinkedList list;
        ( list = new SLinkedList ( 1 ) ).a ( 2 ).a ( 3 ).a ( 4 ).a ( 5 ).a ( 6 ).a ( 7 ).a ( 8 );
        list.a ( null );
        SLinkedList list2 = list;
        SLinkedList b = null;
        while ( list2 != null ) {
            final SLinkedList b2 = list2.b;
            list2.b = b;
            b = list2;
            list2 = b2;
        }
        b.a ( null );
    }
}
