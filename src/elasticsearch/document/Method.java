package elasticsearch.document;

/**
 * Created by Chaiyong on 7/27/16.
 */
public class Method {
    private String name;
    private String src;

    public Method(String name, String src) {
        this.name = name;
        this.src = src;
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

    public String toString() {
        return name + ": " + src;
    }
}
