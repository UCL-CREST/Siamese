/*
   Copyright 2018 Wayne Tsui and Jens Krinke

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

package crest.siamese.language.python3;

import crest.siamese.document.Method;
import crest.siamese.document.Parameter;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Python3MethodParserTest {

    @Test
    public void parseMethodsAtFileLevelReturnsExpectedResults() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();
        URL url = getClass().getClassLoader().getResource("crest/siamese/language/python3/renderer_human.py");
        if (url == null) {
            fail("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        python3MethodParser.configure(filePath, "", "FILE-LEVEL", false);

        // when
        ArrayList<Method> methods = python3MethodParser.parseMethods();
        Method fileMethod = methods.get(0);
        int expectedStartLine = 14;
        int expectedEndLine = 1246;

        // then
        assertThat(methods, hasSize(1));
        assertThat(fileMethod.getFile(), is(filePath));
        assertThat(fileMethod.getMethodPackage(), is(emptyString()));
        assertThat(fileMethod.getClassName(), is(emptyString()));
        assertThat(fileMethod.getName(), is(emptyString()));
        assertThat(fileMethod.getComment(), is(emptyString()));
        assertThat(fileMethod.getStartLine(), is(expectedStartLine));
        assertThat(fileMethod.getEndLine(), is(expectedEndLine));
        assertThat(fileMethod.getParams(), is(empty()));
        assertThat(fileMethod.getHeader(), is(emptyString()));
    }

    @Test
    public void parseMethodsAtFileLevelExtractMethodAsExpected() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();
        URL url = getClass().getClassLoader().getResource("crest/siamese/language/python3/reverse_list.py");
        if (url == null) {
            fail("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        python3MethodParser.configure(filePath, "", "FILE-LEVEL", false);

        // when
        ArrayList<Method> methods = python3MethodParser.parseMethods();
        Method fileMethod = methods.get(0);
        String expectedSourceCode = "" +
                "input = [ 1 , 2 , 3 , 4 , 5 ]\n" +
                "def reverse_list ( a ) :\n" +
                    "\ta . reverse ( )\n" +
                    "\treturn a\n" +
                "print ( reverse_list ( input ) ) " +
                "<EOF>";
        int expectedStartLine = 1;
        int expectedEndLine = 7;

        // then
        assertThat(fileMethod.getFile(), is(filePath));
        assertThat(fileMethod.getMethodPackage(), is(emptyString()));
        assertThat(fileMethod.getClassName(), is(emptyString()));
        assertThat(fileMethod.getName(), is(emptyString()));
        assertThat(fileMethod.getComment(), is(emptyString()));
        assertThat(fileMethod.getSrc(), is(expectedSourceCode));
        assertThat(fileMethod.getStartLine(), is(expectedStartLine));
        assertThat(fileMethod.getEndLine(), is(expectedEndLine));
        assertThat(fileMethod.getParams(), is(empty()));
        assertThat(fileMethod.getHeader(), is(emptyString()));
    }

    @Test
    public void parseMethodsAtMethodLevelReturnsExpectedResults() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();
        URL url = getClass().getClassLoader().getResource("crest/siamese/language/python3/renderer_human.py");
        if (url == null) {
            fail("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        python3MethodParser.configure(filePath, "", "METHOD-LEVEL", false);

        // when
        ArrayList<Method> methods = python3MethodParser.parseMethods();

        // then
        assertThat(methods, hasSize(55));
    }

    /**
     * Note that for the parameter list, the pre-existing {@link Parameter} class is used, where it was originally
     * defined for Java methods, therefore here we have empty string for the type.
     *
     * Also, the parameters in the header do not have spaces between the comma, which can differ from the original
     * source code, but has no semantic difference. However, there is a space between def keyword and the method name.
     */
    @Test
    public void parseMethodsAtMethodLevelExtractMethodAsExpected() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();
        URL url = getClass().getClassLoader().getResource("crest/siamese/language/python3/renderer_human.py");
        if (url == null) {
            fail("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        python3MethodParser.configure(filePath, "", "METHOD-LEVEL", false);

        // when
        ArrayList<Method> methods = python3MethodParser.parseMethods();
        Method method = methods.get(6);
        String expectedClassName = "_Surface";
        String expectedName = "draw_circle";
        String expectedSourceCode = "def draw_circle ( self , color , world_loc , world_radius , thickness = 0 ) :\n" +
                "\t\"\"\"Draw a circle using world coordinates and radius.\"\"\"\n" +
                "\tif world_radius > 0 :\n" +
                "\t\tcenter = self . world_to_surf . fwd_pt ( world_loc ) . round ( )\n" +
                "\t\tradius = max ( 1 , int ( self . world_to_surf . fwd_dist ( world_radius ) ) )\n" +
                "\t\tpygame . draw . circle ( self . surf , color , center , radius , thickness if thickness < radius else 0 )\n";
        int expectedStartLine = 126;
        int expectedEndLine = 132;
        List<Parameter> expectedParams = new ArrayList<>(Arrays.asList(
                new Parameter(StringUtils.EMPTY, "self"),
                new Parameter(StringUtils.EMPTY, "world_loc"),
                new Parameter(StringUtils.EMPTY, "color"),
                new Parameter(StringUtils.EMPTY, "world_radius"),
                new Parameter(StringUtils.EMPTY, "thickness")
        ));
        String expectedHeader = "def draw_circle(self,color,world_loc,world_radius,thickness=0):";

        // then
        assertThat(method.getFile(), is(filePath));
        assertThat(method.getMethodPackage(), is(emptyString()));
        assertThat(method.getClassName(), is(expectedClassName));
        assertThat(method.getName(), is(expectedName));
        assertThat(method.getComment(), is(emptyString()));
        assertThat(method.getSrc(), is(expectedSourceCode));
        assertThat(method.getStartLine(), is(expectedStartLine));
        assertThat(method.getEndLine(), is(expectedEndLine));
        assertThat(method.getParams(), is(expectedParams));
        assertThat(method.getHeader(), is(expectedHeader));
    }

    /**
     * Note that while extracting a method with inner methods, the inner methods are appended to the methods ArrayList
     * before the outer method.
     *
     * Also, methods in the same "depth" in the source code are processed by ascending line number.
     */
    @Test
    public void parseMethodsAtMethodLevelExtractInnerMethodAsSeparateMethod() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();
        URL url = getClass().getClassLoader().getResource("crest/siamese/language/python3/renderer_human.py");
        if (url == null) {
            fail("Resource not found, please check input to getResource().");
        }
        String filePath = url.getPath();
        python3MethodParser.configure(filePath, "", "METHOD-LEVEL", false);

        // when
        ArrayList<Method> methods = python3MethodParser.parseMethods();
        Method method = methods.get(1);
        String expectedName = "decorator";
        String expectedSourceCode = "def decorator ( func ) :\n" +
                                        "\treturn _with_lock\n";
        int expectedStartLine = 58;
        int expectedEndLine = 63;
        List<Parameter> expectedParams = Collections.singletonList(new Parameter(StringUtils.EMPTY, "func"));
        String expectedHeader = "def decorator(func):";

        // then
        assertThat(method.getFile(), is(filePath));
        assertThat(method.getMethodPackage(), is(emptyString()));
        assertThat(method.getClassName(), is(emptyString()));
        assertThat(method.getName(), is(expectedName));
        assertThat(method.getComment(), is(emptyString()));
        assertThat(method.getSrc(), is(expectedSourceCode));
        assertThat(method.getStartLine(), is(expectedStartLine));
        assertThat(method.getEndLine(), is(expectedEndLine));
        assertThat(method.getParams(), is(expectedParams));
        assertThat(method.getHeader(), is(expectedHeader));
    }

    @Test
    public void getLicenseShouldReturnNullAsItIsNotImplementedForPython3CloneSearch() {
        // given
        Python3MethodParser python3MethodParser = new Python3MethodParser();

        // when
        String license = python3MethodParser.getLicense();

        // then
        assertNull(license);
    }
}