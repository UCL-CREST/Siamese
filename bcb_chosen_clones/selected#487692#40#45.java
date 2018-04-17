    public static <A> A[] composeArray(A[] base, A postfix) {
        @SuppressWarnings("unchecked") final A[] result = (A[]) Array.newInstance(base.getClass().getComponentType(), base.length + 1);
        System.arraycopy(base, 0, result, 0, base.length);
        result[base.length] = postfix;
        return result;
    }
