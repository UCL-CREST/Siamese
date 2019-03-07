package crest.siamese;

/**
 * Created by Chaiyong on 4/5/17.
 */

import crest.siamese.document.DocumentTest;
import crest.siamese.helpers.nGramGeneratorTest;
import crest.siamese.language.java.JavaMethodParserTest;
import crest.siamese.language.java.JavaNormalizerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DocumentTest.class, JavaNormalizerTest.class,
JavaMethodParserTest.class, nGramGeneratorTest.class})
public class TestSuite {
    //nothing
}