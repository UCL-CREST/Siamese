import java.lang.*;
import java.util.*;
import java.io.*;
class SLinkedList {
    private int data;
    private SLinkedList next;
    public SLinkedList() {
        data = 0;
        next = null;
    }
    public SLinkedList ( int value ) {
        data = value;
        next = null;
    }
    public SLinkedList InsertNext ( int value ) {
        SLinkedList node = new SLinkedList ( value );
        if ( this.next == null ) {
            node.next = null; 
            this.next = node;
        } else {
            SLinkedList temp = this.next;
            node.next = temp;
            this.next = node;
        }
        return node;
    }
    public int DeleteNext() {
        if ( next == null ) {
            return 0;
        }
        SLinkedList node = this.next;
        this.next = this.next.next;  
        node = null;
        return 1;
    }
    public void Traverse ( SLinkedList node ) {
        if ( node == null ) {
            node = this;
        }
        System.out.println ( "\n\nTraversing in Forward Direction\n\n" );
        while ( node != null ) {
            System.out.println ( node.data );
            node = node.next;
        }
    }
    public SLinkedList Reverse_With_Bubble ( SLinkedList head ) {
        SLinkedList testnode = null;
        if ( head == null ) {
            return head;
        }
        while ( true ) {
            SLinkedList p = head;
            if ( p.next == testnode ) {
                break;
            }
            while ( true ) {
                int temp = p.data;
                p.data = p.next.data;
                p.next.data = temp;
                p = p.next;
                if ( p.next == testnode ) {
                    testnode = p;
                    break;
                }
            }
        }
        return head;
    }
    public SLinkedList Reverse ( SLinkedList head ) {
        if ( head == null ) {
            return head;
        }
        SLinkedList cp = head;
        SLinkedList prev = null;
        while ( cp != null ) {
            SLinkedList next = cp.next;
            cp.next = prev;
            prev = cp;
            cp = next;
        }
        head = prev;
        return head;
    }
    public SLinkedList Reverse_with_copy ( SLinkedList head ) {
        SLinkedList p = head;
        SLinkedList newhead = null;
        while ( true ) {
            if ( p == null ) {
                return newhead;
            }
            SLinkedList newnode = new SLinkedList ( p.data );
            newnode.next = newhead;
            newhead = newnode;
            p = p.next;
        }
    }
    public static void main ( String[] args ) {
        SLinkedList head = new SLinkedList ( 1 );
        SLinkedList p = head.InsertNext ( 2 );
        p = p.InsertNext ( 3 );
        p = p.InsertNext ( 4 );
        p = p.InsertNext ( 5 );
        p = p.InsertNext ( 6 );
        p = p.InsertNext ( 7 );
        p = p.InsertNext ( 8 );
        head.Traverse ( null );
        SLinkedList newhead = head.Reverse ( head );
        newhead.Traverse ( null );
    }
}
