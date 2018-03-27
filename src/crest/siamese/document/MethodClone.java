package crest.siamese.document;

import java.util.List;

/**
 * Created by chaiyong on 14/03/2017.
 */
public class MethodClone extends Method {
    private String cluster;

    public MethodClone(String file, String methodPackage, String name, String className, String src, int startLine, int endLine, List<Parameter> params, String fullHeader, String cluster) {
        super(file, methodPackage, className, name, "", src, startLine, endLine, params, fullHeader);
        this.cluster = cluster;
    }

    public MethodClone(String file, String cluster, String header) {
        this.setFile(file);
        this.cluster = cluster;
        this.setHeader(header);
    }

    public String getCluster() {
        return cluster;
    }
}
