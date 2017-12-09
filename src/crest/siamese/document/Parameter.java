package crest.siamese.document;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created by Chaiyong on 4/29/17.
 */
public class Parameter {
    private String type;
    private String id;

    public Parameter(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(type)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parameter) {
            Parameter p = (Parameter) o;
            return new EqualsBuilder()
                    .append(type, p.getType())
                    .isEquals();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return type + " " + id;
    }
}
