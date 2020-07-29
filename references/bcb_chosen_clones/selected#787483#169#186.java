    public static int findInVector(Vector v, Object objp, Orderable op, Method getter) throws IllegalArgumentException, InvocationTargetException {
        if (v == null || v.isEmpty()) return -1;
        if (v == null || objp == null || op == null) throw new IllegalArgumentException("null arg in Utils.findInVector(Vector v, Object objp, Orderable op)");
        int low = 0;
        int high = v.size() - 1;
        try {
            while (low <= high) {
                int mid = (low + high) / 2;
                int c = op.compareTo(objp, getter.invoke(v.elementAt(mid), null));
                if (c < 0) high = mid - 1; else if (c > 0) low = mid + 1; else return mid;
            }
        } catch (InvocationTargetException e) {
            throw new InvocationTargetException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("IllegalArgumentException in Utils.findInVector");
        }
        return -1;
    }
