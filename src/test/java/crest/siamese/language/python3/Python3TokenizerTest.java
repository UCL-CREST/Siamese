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

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class Python3TokenizerTest {

    @Test
    public void tokensAreNotNormalisedWhenTheTokenizerHasADefaultCompositeNormalizer() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();
        Python3Normalizer python3Normalizer = new Python3Normalizer();
        python3Normalizer.configure(python3NormalizerMode);
        Python3Tokenizer python3Tokenizer = new Python3Tokenizer();
        python3Tokenizer.configure(python3Normalizer);

        // when
        String input = "def sum(a, b): return a + b";
        ArrayList<String> actual = python3Tokenizer.getTokensFromString(input);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "def", "sum", "(", "a", ",", "b", ")", ":", "return", "a", "+", "b"
        ));

        // then
        assertThat(actual, is(expected));
    }

    /**
     * Note that the input String tested does not have newlines and tabs to properly denote the structure of the method.
     * Here, we do not have to be concerned about newlines, indents and dedents since those symbols are not normalized.
     *
     * The reformatted String source code from {@link Python3MethodParser#reformat(List)} would be in such a way that
     * calling {@link Python3Tokenizer#getTokensFromString(String)} would return the sequence of tokens (text) that are
     * the same sequence of tokens (within list of {@link org.antlr.v4.runtime.tree.TerminalNodeImpl} that are passed
     * into {@link Python3MethodParser#reformat(List)}.
     */
    @Test
    public void tokensAreNormalisedAccordingToTheCompositeNormalizer() {
        // given
        Python3NormalizerMode python3NormalizerMode = new Python3NormalizerMode();
        char[] normOptions = {'k', 'w'};
        python3NormalizerMode.configure(normOptions);
        Python3Normalizer python3Normalizer = new Python3Normalizer();
        python3Normalizer.configure(python3NormalizerMode);
        Python3Tokenizer python3Tokenizer = new Python3Tokenizer();
        python3Tokenizer.configure(python3Normalizer);

        // when
        String input = "def sum(a, b): return a + b";
        ArrayList<String> actual = python3Tokenizer.getTokensFromString(input);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "K", "W", "(", "W", ",", "W", ")", ":", "K", "W", "+", "W"
        ));

        // then
        assertThat(actual, is(expected));
    }

    @Test
    public void unusedLegacyMethodsShouldReturnNullAsExpected() throws Exception {
        // given
        Python3Tokenizer python3Tokenizer = new Python3Tokenizer();
        String s = "string";
        Reader r = new StringReader(s);
        File f = new File("pathNameDoesNotMatter");

        // when
        List<ArrayList<String>> outputs = new ArrayList<>();
        outputs.add(python3Tokenizer.tokenize(s));
        outputs.add(python3Tokenizer.tokenize(r));
        outputs.add(python3Tokenizer.tokenizeLine(r));
        outputs.add(python3Tokenizer.tokenize(f));
        outputs.add(python3Tokenizer.getTokensFromFile(s));

        // then
        assertThat(outputs, everyItem(is(nullValue())));
    }
}