    public static final java.lang.Object[] trunc(java.lang.Object[] src, int src_ofs, int src_len, int trunc_ofs, int trunc_len) {
        if (null == src || 0 > src_ofs || 0 > src_len) throw new java.lang.IllegalArgumentException("src"); else if (0 > trunc_ofs || 1 > trunc_len || trunc_ofs < src_ofs || trunc_len > src_len) throw new java.lang.IllegalArgumentException("trunc"); else if (trunc_ofs == src_ofs) {
            if (trunc_len == src_len) {
                if (src.length == src_len) return src; else if (0 == src_len) return null; else {
                    java.lang.Class comp = src.getClass().getComponentType();
                    java.lang.Object[] re = (java.lang.Object[]) java.lang.reflect.Array.newInstance(comp, src_len);
                    System.arraycopy(src, src_ofs, re, 0, src_len);
                    return re;
                }
            } else {
                int re_len = (src_len - trunc_len);
                if (0 == re_len) return null; else {
                    java.lang.Class comp = src.getClass().getComponentType();
                    java.lang.Object[] re = (java.lang.Object[]) java.lang.reflect.Array.newInstance(comp, re_len);
                    System.arraycopy(src, (src_ofs + trunc_len), re, 0, re_len);
                    return re;
                }
            }
        } else if (trunc_len == src_len) throw new java.lang.IllegalArgumentException("trunc"); else {
            int a_ofs = src_ofs;
            int a_len = (trunc_ofs - src_ofs);
            java.lang.Object[] a = sublist(src, a_ofs, a_len);
            int b_ofs = trunc_ofs + trunc_len;
            int b_len = (src_len - (b_ofs + 1));
            java.lang.Object[] b = sublist(src, b_ofs, b_len);
            return cat(a, b);
        }
    }
