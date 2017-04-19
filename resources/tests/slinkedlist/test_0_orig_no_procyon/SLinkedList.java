class SLinkedList {
    private int data;
    private SLinkedList next;
    public SLinkedList() {
        this.data = 0;
        this.next = null;
    }
    public SLinkedList ( final int data ) {
        this.data = data;
        this.next = null;
    }
    public SLinkedList InsertNext ( final int n ) {
        final SLinkedList list = new SLinkedList ( n );
        if ( this.next == null ) {
            list.next = null;
            this.next = list;
        } else {
            list.next = this.next;
            this.next = list;
        }
        return list;
    }
    public int DeleteNext() {
        if ( this.next == null ) {
            return 0;
        }
        final SLinkedList next = this.next;
        this.next = this.next.next;
        return 1;
    }
    public void Traverse ( SLinkedList next ) {
        if ( next == null ) {
            next = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( next != null ) {
            System.out.println ( next.data );
            next = next.next;
        }
    }
    public SLinkedList Reverse_With_Bubble ( final SLinkedList list ) {
        SLinkedList list2 = null;
        if ( list == null ) {
            return list;
        }
        while ( true ) {
            SLinkedList next = list;
            if ( next.next == list2 ) {
                break;
            }
            do {
                final int data = next.data;
                next.data = next.next.data;
                next.next.data = data;
                next = next.next;
            } while ( next.next != list2 );
            list2 = next;
        }
        return list;
    }
    public SLinkedList Reverse ( SLinkedList list ) {
        if ( list == null ) {
            return list;
        }
        SLinkedList list2 = list;
        SLinkedList next = null;
        while ( list2 != null ) {
            final SLinkedList next2 = list2.next;
            list2.next = next;
            next = list2;
            list2 = next2;
        }
        list = next;
        return list;
    }
    public SLinkedList Reverse_with_copy ( final SLinkedList list ) {
        SLinkedList next = list;
        SLinkedList next2 = null;
        while ( next != null ) {
            final SLinkedList list2 = new SLinkedList ( next.data );
            list2.next = next2;
            next2 = list2;
            next = next.next;
        }
        return next2;
    }
    public static void main ( final String[] array ) {
        final SLinkedList list = new SLinkedList ( 1 );
        list.InsertNext ( 2 ).InsertNext ( 3 ).InsertNext ( 4 ).InsertNext ( 5 ).InsertNext ( 6 ).InsertNext ( 7 ).InsertNext ( 8 );
        list.Traverse ( null );
        list.Reverse ( list ).Traverse ( null );
    }
}
