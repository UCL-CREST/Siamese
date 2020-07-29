    public MultiArray copyout(int[] origin, int[] shape) {
        if (origin.length != lengths.length || shape.length != lengths.length) throw new IllegalArgumentException("Rank Mismatch");
        int ji = lengths.length - 1;
        for (; ji >= 0; ji--) {
            if (origin[ji] != 0 || shape[ji] != lengths[ji]) break;
        }
        if (ji < 0) {
            return (MultiArrayImpl) this.clone();
        }
        final int[] shp = (int[]) shape.clone();
        final int[] pducts = new int[shp.length];
        final int product = numberOfElements(shp, pducts);
        final Object dst = Array.newInstance(getComponentType(), product);
        int src_pos = indexMap(origin);
        if (ji == 0) {
            System.arraycopy(storage, src_pos, dst, 0, product);
        } else {
            ji--;
            final int step = products[ji];
            final int contig = pducts[ji];
            for (int dst_pos = 0; dst_pos < product; dst_pos += contig) {
                System.arraycopy(storage, src_pos, dst, dst_pos, contig);
                src_pos += step;
            }
        }
        return new MultiArrayImpl(shp, pducts, dst);
    }
