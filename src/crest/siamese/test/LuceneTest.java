package crest.siamese.test;

import crest.siamese.helpers.JavaLexer;
import crest.siamese.main.Siamese;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.IndexReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LuceneTest {

    @org.junit.Test
    public void testSearch() {
        String elasticsearchLoc = "/Users/Chaiyong/IdeasProjects/Siamese/elasticsearch-2.2.0";
        String index = "bigclonebench_utf8";
        Siamese siamese = new Siamese("config_query_bcb.properties");
        IndexReader ir = siamese.readIndex(elasticsearchLoc + "/data/stackoverflow/nodes/0/indices/"
                + index + "/0/index");
        try {
            siamese.luceneSearch(ir, "public");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
