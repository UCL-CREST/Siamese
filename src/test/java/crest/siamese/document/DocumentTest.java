/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.document;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
/**
 * Created by Chaiyong on 7/27/16.
 */
public class DocumentTest {

    @Test
    public void TestDocumentCreation() {
        Document d = new Document();
        d.setFile("/my/test/file");
        d.setId(0);
        d.setSource("import java.io.*; class BubbleSort { public static void main ( String[] args )");

        Document d2 = new Document(
                0,
                "/my/test/file",
                1,
                1,
                "import java.io.*; class BubbleSort { public static void main ( String[] args )",
                "import java.io.*; class BubbleSort { public static void main ( String[] args )",
                "import java.io.*; class BubbleSort { public static void main ( String[] args )",
                "import java.io.*; class BubbleSort { public static void main ( String[] args )",
                "import java.io.*; class BubbleSort { public static void main ( String[] args )",
                "",
                ""
        );

        assertEquals(d, d2);
    }

    @Test
    public void TestDifferentDocuments() {
        Document d = new Document();
        d.setFile("/my/test/file");
        d.setId(0);
        d.setSource("import java.io.*; class BubbleSort { public static void main ( String[] args )");

        Document d3 = new Document(
                1,
                "/my/file",
                1,
                1,
                "Hello world!",
                "Hello world!",
                "Hello world!",
                "Hello world!",
                "Hello world!",
                "MIT",
                "https://hello.world");

        assertNotEquals(d, d3);
    }
}
