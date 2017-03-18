package elasticsearch.document;

import com.github.javaparser.ast.body.Parameter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Method {
    private String file;
    private String name;
    private String fullHeader;
    private String src;
    private int startLine;
    private int endLine;
    private List<Parameter> params;

    public Method() {

    }

    public Method(String name, String src) {
        this.name = name;
        this.src = src;
    }

    public Method(String file, String name, String src, int startLine, int endLine, List<Parameter> params, String fullHeader) {
        this.file = file;
        this.name = name;
        this.src = src;
        this.startLine = startLine;
        this.endLine = endLine;
        this.params = params;
        this.fullHeader = fullHeader;
    }

    public String getFullHeader() {
        return fullHeader;
    }

    public void setFullHeader(String fullHeader) {
        this.fullHeader = fullHeader;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public String getHeader() {
        return name;
    }

    public String toString() {
        return file + ": " + fullHeader;
    }

    public boolean equals(Object o) {
        Method m = (Method) o;
        return ((file.equals(m.getFile())) && (fullHeader.equals(m.fullHeader)));
    }
}
