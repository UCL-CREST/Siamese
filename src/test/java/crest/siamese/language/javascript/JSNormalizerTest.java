package crest.siamese.language.javascript;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

public class JSNormalizerTest {

    Map<String, String> keywordTokenAndType;
    Map<String, String> operatorTokenAndType;

    @Before
    public void init() {
        this.keywordTokenAndType = new HashMap<>();
        keywordTokenAndType.put("function", "K");// keyword
        keywordTokenAndType.put("if", "K");// keyword
        keywordTokenAndType.put("else", "K"); // keyword
        this.operatorTokenAndType = new HashMap<>();
        operatorTokenAndType.put("and", "O");// operator
        operatorTokenAndType.put("or", "O");// operator
        operatorTokenAndType.put("arrow", "O");// operator
    }


    @Test
    public void configureTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        char[] normOptions = {'k', 'o'};// normalizer options set as keyword  and operator
        jsNormalizerMode.configure(normOptions);
        JSNormalizer jsNormalizer = new JSNormalizer();
        jsNormalizer.configure(jsNormalizerMode);
        // when
        for (Map.Entry<String, String> element : this.keywordTokenAndType.entrySet()) {
            String token = element.getKey();
            String normalizedToken = jsNormalizer.normalizeAToken(token, token.toUpperCase());
            // then
            assertThat(element.getValue(), is(normalizedToken));
        }
        // when
        for (Map.Entry<String, String> element : this.operatorTokenAndType.entrySet()) {
            String token = element.getKey();
            String normalizedToken = jsNormalizer.normalizeAToken(token, token.toUpperCase());
            // then
            assertThat(element.getValue(), is(normalizedToken));
        }

    }

    @Test
    public void normalizeATokenTest() {
        // given
        JSNormalizerMode jsNormalizerMode = new JSNormalizerMode();
        JSNormalizer jsNormalizer = new JSNormalizer();
        jsNormalizer.configure(jsNormalizerMode);// blank normalizer options
        // when
        for (Map.Entry<String, String> element : this.keywordTokenAndType.entrySet()) {
            String token = element.getKey();
            String type = token.toUpperCase();
            String normalizedToken = jsNormalizer.normalizeAToken(token, type);
            // then
            assertThat(token, is(normalizedToken));
        }
    }


    @Test
    public void noNormalizeATokenTest() {
        // given
        JSNormalizer jsNormalizer = new JSNormalizer();
        // when
        for (Map.Entry<String, String> element : this.keywordTokenAndType.entrySet()) {
            String token = element.getKey();
            // then
            assertNull(jsNormalizer.noNormalizeAToken(token));
        }
    }



}
