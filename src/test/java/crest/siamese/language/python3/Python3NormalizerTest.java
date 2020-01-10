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

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

public class Python3NormalizerTest {

    @Test
    public void tokenIsNormalisedIfTheNormOptionIsConfiguredToTheNormalizerMode() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();
        char[] normOptions = {'k'};
        python3NormalizerMode.configure(normOptions);
        Python3Normalizer python3Normalizer = new Python3Normalizer();
        python3Normalizer.configure(python3NormalizerMode);

        String token = "if";
        String type = "IF";
        String normalizedSymbol = "K";

        // when
        String output = python3Normalizer.normalizeAToken(token, type);

        // then
        assertThat(normalizedSymbol, is(output));
    }

    @Test
    public void tokenIsNotNormalisedIfTheNormOptionIsNotConfiguredToTheNormalizerMode() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();
        Python3Normalizer python3Normalizer = new Python3Normalizer();
        python3Normalizer.configure(python3NormalizerMode);

        String token = "if";
        String type = "IF";

        // when
        String output = python3Normalizer.normalizeAToken(token, type);

        // then
        assertThat(token, is(output));
    }

    @Test
    public void noNormalizeATokenMethodShouldReturnNullAsItIsNotImplemented() {
        // given
        Python3Normalizer python3Normalizer = new Python3Normalizer();
        String token = "if";

        // when
        ArrayList<String> output = python3Normalizer.noNormalizeAToken(token);

        // then
        assertNull(output);
    }
}