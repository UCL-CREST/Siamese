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

package crest.siamese.language.java;

import crest.siamese.document.Method;
import crest.siamese.language.MethodParser;
import crest.siamese.settings.Settings;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class JavaMethodParserTest {

    @Test
    public void checkMethodParser() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("crest.siamese.language.java.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(3, methods.size());
        assertEquals("ReadWordsFromFile", methods.get(0).getName());
        assertEquals("ReadString", methods.get(1).getName());
        assertEquals("main", methods.get(2).getName());
    }

    @Test
    public void checkMethodParserFileLevel() throws Exception {
        String srcStr = "resources/tests/guessword/0_orig/GuessWord.java";
        Class cl = Class.forName("crest.siamese.language.java.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.FILE, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("method", methods.get(0).getName());
    }

    @Test
    public void checkMethodParser2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("crest.siamese.language.java.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();

        assertEquals(1, methods.size());
        assertEquals("main", methods.get(0).getName());
    }

    @Test
    public void testExtractingComment() throws Exception {
        String srcStr = "resources/tests/WritableComparable.java";
        Class cl = Class.forName("crest.siamese.language.java.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }

    @Test
    public void testExtractingComment2() throws Exception {
        String srcStr = "resources/tests/bubblesort/0_orig/BubbleSort.java";
        Class cl = Class.forName("crest.siamese.language.java.JavaMethodParser");
        Constructor con = cl.getConstructor(String.class, String.class, String.class, boolean.class);
        MethodParser mParser = (MethodParser) con.newInstance(srcStr, "", Settings.MethodParserType.METHOD, false);
        ArrayList<Method> methods = mParser.parseMethods();
    }
}
