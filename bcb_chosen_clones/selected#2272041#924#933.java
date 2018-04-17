    public static final java.lang.Object[] cat(java.lang.Object[] aoary, java.lang.Object[] boary) {
        if (null == aoary) return boary; else if (null == boary) return aoary; else {
            int aclen = aoary.length, bclen = boary.length;
            java.lang.Class comp = aoary.getClass().getComponentType();
            java.lang.Object[] roary = (java.lang.Object[]) java.lang.reflect.Array.newInstance(comp, (aclen + bclen));
            System.arraycopy(aoary, 0, roary, 0, aclen);
            System.arraycopy(boary, 0, roary, aclen, bclen);
            return roary;
        }
    }
