    @SuppressWarnings({ "unchecked" })
    @Override
    public <T> T[] toArray(T[] a) {
        a = blobsForRevision.toArray(a);
        T[] other = (T[]) Array.newInstance(a.getClass().getComponentType(), blobsForRevised.size());
        other = blobsForRevised.toArray(other);
        System.arraycopy(other, 0, a, blobsForRevision.size(), other.length);
        return other;
    }
