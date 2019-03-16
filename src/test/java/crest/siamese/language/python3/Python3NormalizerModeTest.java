package crest.siamese.language.python3;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Python3NormalizerModeTest {

    @Test
    public void newInstanceOfPython3NormalizerModeHasAllOptionsDefaultToFalse() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();

        // then
        assertFalse(python3NormalizerMode.isKeywordToBeNormalised());
        assertFalse(python3NormalizerMode.isValueToBeNormalised());
        assertFalse(python3NormalizerMode.isStringToBeNormalised());
        assertFalse(python3NormalizerMode.isOperatorToBeNormalised());
        assertFalse(python3NormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void configurationCharArraySetOptionsToTrue() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();

        // when
        char[] normOptions = {'k', 'v', 'w'};
        python3NormalizerMode.configure(normOptions);

        // then
        assertTrue(python3NormalizerMode.isKeywordToBeNormalised());
        assertTrue(python3NormalizerMode.isValueToBeNormalised());
        assertFalse(python3NormalizerMode.isStringToBeNormalised());
        assertFalse(python3NormalizerMode.isOperatorToBeNormalised());
        assertTrue(python3NormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void normalizationOptionsMustBeLowerCaseToSetOptionsToTrue() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();

        // when
        char[] normOptions = {'k', 'V'};
        python3NormalizerMode.configure(normOptions);

        // then
        assertTrue(python3NormalizerMode.isKeywordToBeNormalised());
        assertFalse(python3NormalizerMode.isValueToBeNormalised());
    }

    @Test
    public void charactersWhichAreNotInNormalizationOptionsAreIgnored() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();

        // when
        char[] normOptions = {'x', 'v', 's', 'y', 'o', 'w', 'z'};
        python3NormalizerMode.configure(normOptions);

        // then
        assertFalse(python3NormalizerMode.isKeywordToBeNormalised());
        assertTrue(python3NormalizerMode.isValueToBeNormalised());
        assertTrue(python3NormalizerMode.isStringToBeNormalised());
        assertTrue(python3NormalizerMode.isOperatorToBeNormalised());
        assertTrue(python3NormalizerMode.isNameToBeNormalised());
    }

    @Test
    public void resetMethodSetsAllNormalizationOptionsToFalse() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();

        // when
        char[] normOptions = {'k', 'v', 'w'};
        python3NormalizerMode.configure(normOptions);
        python3NormalizerMode.reset();

        // then
        assertFalse(python3NormalizerMode.isKeywordToBeNormalised());
        assertFalse(python3NormalizerMode.isValueToBeNormalised());
        assertFalse(python3NormalizerMode.isStringToBeNormalised());
        assertFalse(python3NormalizerMode.isOperatorToBeNormalised());
        assertFalse(python3NormalizerMode.isNameToBeNormalised());
    }
}