package elasticsearch.test;

import elasticsearch.main.Document;
import static org.junit.Assert.*;
/**
 * Created by Chaiyong on 7/27/16.
 */
public class DocumentTest {

    @org.junit.Test
    public void TestDocumentCreation() {
        Document d = new Document();
        d.setFile("/my/test/file");
        d.setId("0");
        d.setSource("import java.io.*; class BubbleSort { public static void main ( String[] args )");

        Document d2 = new Document("0", "/my/test/file", "import java.io.*; class BubbleSort { public static void main ( String[] args )");

        assertEquals(d, d2);
    }

    @org.junit.Test
    public void TestDifferentDocuments() {
        Document d = new Document();
        d.setFile("/my/test/file");
        d.setId("0");
        d.setSource("import java.io.*; class BubbleSort { public static void main ( String[] args )");

        Document d3 = new Document("1", "/my/file", "Hello world!");

        assertNotEquals(d, d3);
    }
}
