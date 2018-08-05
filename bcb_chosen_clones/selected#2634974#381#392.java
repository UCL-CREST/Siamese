    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        try {
            SortableList<E> clone = (SortableList<E>) super.clone();
            clone.array = (E[]) Array.newInstance(array.getClass().getComponentType(), array.length);
            System.arraycopy(array, 0, clone.array, 0, array.length);
            return (clone);
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
