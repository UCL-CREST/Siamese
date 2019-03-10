package crest.siamese.language;

public interface NormalizerMode {
    public void configure(char[] normOptions);
    public void reset();
}
