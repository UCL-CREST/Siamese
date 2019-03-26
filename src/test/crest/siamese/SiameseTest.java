package crest.siamese;

import org.junit.Ignore;
import org.junit.Test;

/**
* TODO: @Chaiyong
* This test class is ignored until resolved.
 */
@Ignore
public class SiameseTest {
//    TODO: check why this doesn't work

    @Test
    public void testDelete() {
        Siamese siamese = new Siamese("config_test.properties");
        siamese.startup();

        try {
            siamese.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        siamese.shutdown();
    }
}
