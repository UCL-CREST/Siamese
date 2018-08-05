    public Object toArray(Object dst, int[] origin, int[] shape) {
        if (origin.length != lengths.length || shape.length != lengths.length) throw new IllegalArgumentException("Rank Mismatch");
        int ji = lengths.length - 1;
        for (; ji >= 0; ji--) {
            if (origin[ji] != 0 || shape[ji] != lengths[ji]) break;
        }
        if (ji < 0) {
            final int length = Array.getLength(storage);
            dst = fixDest(dst, length, getComponentType());
            System.arraycopy(storage, 0, dst, 0, length);
            return dst;
        }
        final int[] shp = (int[]) shape.clone();
        final int[] pducts = new int[shp.length];
        final int product = numberOfElements(shp, pducts);
        dst = fixDest(dst, product, getComponentType());
        int src_pos = indexMap(origin);
        if (ji == 0) {
            System.arraycopy(storage, src_pos, dst, 0, product);
            return dst;
        }
        ji--;
        final int step = products[ji];
        final int contig = pducts[ji];
        for (int dst_pos = 0; dst_pos < product; dst_pos += contig) {
            System.arraycopy(storage, src_pos, dst, dst_pos, contig);
            src_pos += step;
        }
        return dst;
    }
