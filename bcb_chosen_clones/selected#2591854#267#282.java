    public Object[] toArray(Object[] result) {
        int n = size();
        if (result.length < n) {
            result = (Object[]) Array.newInstance(result.getClass().getComponentType(), n);
        }
        l1.toArray(result);
        n = l1.size();
        if (USE_ARRAYCOPY) {
            System.arraycopy(l2.toArray(), 0, result, n, l2.size());
        } else {
            for (Iterator i = l2.iterator(); i.hasNext(); ) {
                result[n++] = i.next();
            }
        }
        return result;
    }
