package elasticsearch.document;

import com.github.javaparser.ast.body.Parameter;
import java.util.List;

/**
 * Created by chaiyong on 14/03/2017.
 */
public class MethodClone extends Method {
    private String cluster;

    public MethodClone(String file, String name, String src, int startLine, int endLine, List<Parameter> params, String fullHeader, String cluster) {
        super(file, name, src, startLine, endLine, params, fullHeader);
        this.cluster = cluster;
    }

    public MethodClone(String file, String cluster, String header) {
        this.setFile(file);
        this.cluster = cluster;
        this.setFullHeader(header);
    }

    public String getCluster() {
        return cluster;
    }
}
