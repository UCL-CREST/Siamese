package crest.siamese.test;
import crest.siamese.main.Siamese;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

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
