package crest.siamese.language.javascript;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JSNormalizerModeTest {

    @Test
    public void configureWithBlankOptionsTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        // then
        assertFalse(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertFalse(jsNormalizerMode.isOperatorToBeNormalised());
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithKeyWordsAndOperatorNameOptionsTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'o', 'w'};
        jsNormalizerMode.configure(normOptions);

        // then
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithLowerCaseAndUpperCaseOptionsTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'W'};
        jsNormalizerMode.configure(normOptions);
        // then
        // expected true as the option 'k' is lower case according to the requirement
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        // expected false as the option 'W' is Upper case that does not comply with the requirement
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configureWithIrrelevantOptionsTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'a', 'b', 'c', 'd', 'e', 'k', 'o', 'w'};
        jsNormalizerMode.configure(normOptions);
        // then
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());
    }


    @Test
    public void resetTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'o', 'w'};
        // when
        jsNormalizerMode.configure(normOptions);
        // then
        assertTrue(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertTrue(jsNormalizerMode.isOperatorToBeNormalised());
        assertTrue(jsNormalizerMode.isNameToBeNormalised());
        // when
        jsNormalizerMode.reset();
        // then
        assertFalse(jsNormalizerMode.isKeywordToBeNormalised());
        assertFalse(jsNormalizerMode.isValueToBeNormalised());
        assertFalse(jsNormalizerMode.isStringToBeNormalised());
        assertFalse(jsNormalizerMode.isOperatorToBeNormalised());
        assertFalse(jsNormalizerMode.isNameToBeNormalised());
    }


}
