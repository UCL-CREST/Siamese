package crest.siamese.document;

import java.util.Comparator;

public class JavaTermComparator implements Comparator<JavaTerm> {

    @Override
    public int compare(JavaTerm jt1, JavaTerm jt2) {
        return jt1.compareTo(jt2);
    }
}
