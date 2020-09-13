package crest.siamese.language.javascript;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSNormalizerModeTest {

    @Test
    public void configureWithBlankOptionsTest() {

        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        assertFalse(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertFalse(jsNormalizerMode.isOperatorToBeNormalised());
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithKeyWordsAndOperatorNameOptionsTest() {

        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'o', 'w'};
        jsNormalizerMode.configure(normOptions);
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithLowerCaseAndUpperCaseOptionsTest() {
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'W'};
        jsNormalizerMode.configure(normOptions);
        // expected true as the option 'k' is lower case according to the requirement
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        // expected false as the option 'W' is Upper case that does not comply with the requirement
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithIrrelevantOptionsTest() {

        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'a', 'b', 'c', 'd', 'e', 'k', 'o', 'w'};
        jsNormalizerMode.configure(normOptions);
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());
    }


    @Test
    public void resetTest() {

        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'o', 'w'};
        jsNormalizerMode.configure(normOptions);

        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());

        jsNormalizerMode.reset();
        assertFalse(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertFalse(jsNormalizerMode.isOperatorToBeNormalised());
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }


}
