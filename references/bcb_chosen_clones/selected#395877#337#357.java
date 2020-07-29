    public MultiArray copyout(int[] origin, int[] shape) {
        if (origin.length != rank || shape.length != rank) throw new IllegalArgumentException("Rank Mismatch");
        final int[] shp = (int[]) shape.clone();
        final int[] pducts = new int[shp.length];
        final int product = MultiArrayImpl.numberOfElements(shp, pducts);
        final Object dst = Array.newInstance(getComponentType(), product);
        int ji = rank - 1;
        int src_pos = origin[ji];
        if (ji == 0) {
            System.arraycopy(jla, src_pos, dst, 0, product);
        } else {
            ji--;
            final int contig = pducts[ji];
            final OffsetIndexIterator odo = new OffsetIndexIterator(truncCopy(origin), getTruncLengths());
            for (int dst_pos = 0; dst_pos < product; dst_pos += contig) {
                System.arraycopy(getLeaf(odo.value()), src_pos, dst, dst_pos, contig);
                odo.incr();
            }
        }
        return new MultiArrayImpl(shp, pducts, dst);
    }
