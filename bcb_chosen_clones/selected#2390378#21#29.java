    public StringCharAdapter(MultiArray delegate, char fillValue) {
        if (delegate.getComponentType() != Character.TYPE) throw new IllegalArgumentException("Not a Character Array");
        delegate_ = delegate;
        fillValue_ = fillValue;
        lengths_ = new int[delegate_.getRank() - 1];
        final int[] dlengths = delegate_.getLengths();
        System.arraycopy(dlengths, 0, lengths_, 0, lengths_.length);
        maxStringLen_ = dlengths[lengths_.length];
    }
