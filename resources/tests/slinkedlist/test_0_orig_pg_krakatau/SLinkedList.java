class SLinkedList {
    private int a;
    private SLinkedList b;
    public SLinkedList() {
        super();
        this.a = 0;
        this.b = null;
    }
    private SLinkedList ( int i ) {
        super();
        this.a = i;
        this.b = null;
    }
    private SLinkedList a ( int i ) {
        SLinkedList a0 = new SLinkedList ( i );
        if ( this.b != null ) {
            a0.b = this.b;
            this.b = a0;
        } else {
            a0.b = null;
            this.b = a0;
        }
        return a0;
    }
    private void a ( SLinkedList a0 ) {
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        SLinkedList a1 = this;
        while ( a1 != null ) {
            System.out.println ( a1.a );
            a1 = a1.b;
        }
    }
    public static void main ( String[] a0 ) {
        SLinkedList a1 = new SLinkedList ( 1 );
        a1.a ( 2 ).a ( 3 ).a ( 4 ).a ( 5 ).a ( 6 ).a ( 7 ).a ( 8 );
        a1.a ( ( SLinkedList ) null );
        SLinkedList a2 = null;
        while ( a1 != null ) {
            SLinkedList a3 = a1.b;
            a1.b = a2;
            a1 = a3;
            a2 = a1;
        }
        a2.a ( ( SLinkedList ) null );
    }
}
