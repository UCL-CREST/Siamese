    public static final Object duplicate(Object ary, int jb, int je) {
        int len = Array.getLength(ary);
        if (jb < 0 || je > len || jb > je) throw new IndexOutOfBoundsException(jb + " or " + je + " exceeds " + len);
        len = je - jb;
        Object dst = Array.newInstance(ary.getClass().getComponentType(), len);
        System.arraycopy(ary, jb, dst, 0, len);
        return dst;
    }
