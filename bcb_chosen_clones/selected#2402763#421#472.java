    public static Object arraySubsref(Object obj, int[][] idx) throws Exception {
        if (!obj.getClass().isArray()) {
            throw new IllegalArgumentException("not a Java array");
        }
        if (idx.length == 1) {
            if (idx[0].length == 1) {
                return Array.get(obj, idx[0][0]);
            } else {
                Object retval = Array.newInstance(obj.getClass().getComponentType(), idx[0].length);
                for (int i = 0; i < idx[0].length; i++) {
                    Array.set(retval, i, Array.get(obj, idx[0][i]));
                }
                return retval;
            }
        } else {
            int[] dims = new int[idx.length];
            for (int i = 0; i < idx.length; i++) {
                dims[i] = idx[i].length;
            }
            if (dims.length != getArrayClassNDims(obj.getClass())) {
                throw new IllegalArgumentException("index size mismatch");
            }
            Object theObj = obj;
            int offset = 0;
            while (dims[offset] == 1) {
                theObj = Array.get(theObj, idx[offset][0]);
                offset = offset + 1;
                if (offset >= dims.length) {
                    return theObj;
                }
            }
            if (offset > 0) {
                int[][] new_idx = new int[idx.length - offset][];
                System.arraycopy(idx, offset, new_idx, 0, idx.length - offset);
                return arraySubsref(theObj, new_idx);
            }
            int ndims = dims.length;
            while (ndims > 1 && dims[ndims - 1] == 1) {
                ndims--;
            }
            Class elemClass = theObj.getClass();
            for (int i = 0; i <= (dims.length - ndims); i++) {
                elemClass = elemClass.getComponentType();
            }
            Object retval = Array.newInstance(elemClass, dims[0]);
            for (int i = 0; i < idx[0].length; i++) {
                Object elem = getArrayElements(Array.get(theObj, idx[0][i]), idx, 1, ndims, elemClass);
                Array.set(retval, i, elem);
            }
            return retval;
        }
    }
