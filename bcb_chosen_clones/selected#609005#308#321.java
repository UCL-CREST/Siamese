    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(T arry[], int ofs, int len) {
        if (arry != null) {
            int alen = arry.length;
            ofs = _constrainOffset(ofs, alen);
            len = _constrainLength(ofs, len, alen);
            Class type = arry.getClass().getComponentType();
            T newArry[] = (T[]) Array.newInstance(type, len);
            System.arraycopy(arry, ofs, newArry, 0, len);
            return newArry;
        } else {
            return arry;
        }
    }
