        private <T> T[] increaseArray(T[] a, int len) {
            T[] tmp = a;
            if (a.length > len) {
                return a;
            }
            int newLen = a.length * 2;
            if (newLen < len) {
                newLen = len;
            }
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), newLen);
            System.arraycopy(tmp, 0, a, 0, tmp.length);
            return a;
        }
