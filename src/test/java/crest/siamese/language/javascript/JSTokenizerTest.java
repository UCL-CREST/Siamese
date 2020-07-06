package crest.siamese.language.javascript;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JSTokenizerTest {


    @Test
    public void getTokensFromStringTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        JSNormalizer jsNormalizer = new JSNormalizer();
        jsNormalizer.configure(jsNormalizerMode);
        JSTokenizer jsTokenizer = new JSTokenizer();
        jsTokenizer.configure(jsNormalizer);
        // when
        String input = "function (a,b){ return a+b;} ";
        ArrayList<String> actual = jsTokenizer.getTokensFromString(input);
        ArrayList<String> expected = new ArrayList<>(Arrays.asList(
                "function", "(", "a", ",", "b", ")", "{", "return", "a", "+", "b", ";", "}"
        ));
        // then
        assertThat(actual, is(expected));

    }
}
