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