class SLinkedList {
    private int data;
    private SLinkedList next;
    public SLinkedList() {
        super();
        this.data = 0;
        this.next = null;
    }
    public SLinkedList ( int i ) {
        super();
        this.data = i;
        this.next = null;
    }
    public SLinkedList InsertNext ( int i ) {
        SLinkedList a = new SLinkedList ( i );
        if ( this.next != null ) {
            a.next = this.next;
            this.next = a;
        } else {
            a.next = null;
            this.next = a;
        }
        return a;
    }
    public int DeleteNext() {
        if ( this.next == null ) {
            return 0;
        }
        SLinkedList dummy = this.next;
        this.next = this.next.next;
        return 1;
    }
    public void Traverse ( SLinkedList a ) {
        if ( a == null ) {
            a = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( a != null ) {
            System.out.println ( a.data );
            a = a.next;
        }
    }
    public SLinkedList Reverse_With_Bubble ( SLinkedList a ) {
        SLinkedList a0 = null;
        if ( a == null ) {
            SLinkedList a1 = null;
            return a1;
        }
        while ( a.next != a0 ) {
            SLinkedList a2 = a;
            while ( true ) {
                int i = a2.data;
                a2.data = a2.next.data;
                a2.next.data = i;
                a2 = a2.next;
                if ( a2.next == a0 ) {
                    a0 = a2;
                    break;
                }
            }
        }
        return a;
    }
    public SLinkedList Reverse ( SLinkedList a ) {
        if ( a == null ) {
            SLinkedList a0 = null;
            return a0;
        }
        SLinkedList a1 = null;
        while ( a != null ) {
            SLinkedList a2 = a.next;
            a.next = a1;
            a = a2;
            a1 = a;
        }
        return a1;
    }
    public SLinkedList Reverse_with_copy ( SLinkedList a ) {
        SLinkedList a0 = null;
        while ( a != null ) {
            SLinkedList a1 = new SLinkedList ( a.data );
            a1.next = a0;
            a = a.next;
            a0 = a1;
        }
        return a0;
    }
    public static void main ( String[] a ) {
        SLinkedList a0 = new SLinkedList ( 1 );
        a0.InsertNext ( 2 ).InsertNext ( 3 ).InsertNext ( 4 ).InsertNext ( 5 ).InsertNext ( 6 ).InsertNext ( 7 ).InsertNext ( 8 );
        a0.Traverse ( ( SLinkedList ) null );
        a0.Reverse ( a0 ).Traverse ( ( SLinkedList ) null );
    }
}
