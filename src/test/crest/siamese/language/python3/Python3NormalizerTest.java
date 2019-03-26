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