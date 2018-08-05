    private Object[] allocateNewArray(int capacity) {
        Class arrayClass = this.array.getClass();
        Object[] newArray = (Object[]) Array.newInstance(arrayClass.getComponentType(), capacity);
        System.arraycopy(this.array, 0, newArray, 0, this.length);
        return newArray;
    }
