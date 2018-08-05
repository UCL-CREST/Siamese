    public static final java.lang.Object[] sublist(java.lang.Object[] oary, int from_idx, int to_idx) {
        if (null == oary || 0 >= to_idx || 0 > from_idx) return null; else {
            java.lang.Class comp = oary.getClass().getComponentType();
            java.lang.Object[] copier = (java.lang.Object[]) java.lang.reflect.Array.newInstance(comp, (to_idx - from_idx));
            System.arraycopy(oary, from_idx, copier, 0, copier.length);
            return copier;
        }
    }
