package crest.siamese.document;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class JavaTerm implements Comparable<JavaTerm> {

    private String term;
    private long freq;

    public JavaTerm() {
        this.term = "";
        this.freq = 0;
    }

    public JavaTerm(String term, long freq) {
        this.term = term;
        this.freq = freq;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(term)
                .append(freq)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof JavaTerm) {
            JavaTerm jt = (JavaTerm) o;
            return new EqualsBuilder()
                    .append(getTerm(), jt.getTerm())
                    .isEquals();
        } else {
            return false;
        }
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public long getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public String toString() {
        return term + " (" + freq + ")";
    }

    @Override
    public int compareTo(JavaTerm o) {
        JavaTerm jt = (JavaTerm) o;
        return new CompareToBuilder()
                .append(this.getFreq(), jt.getFreq())
                .toComparison();
    }
}
