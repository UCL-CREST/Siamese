    public static Object[] remove(Object[] array, Object item) {
        Object[] newArray = array;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(item)) {
                int newLength = array.length - 1;
                newArray = (Object[]) Array.newInstance(array.getClass().getComponentType(), newLength);
                System.arraycopy(array, 0, newArray, 0, i);
                if (i < newLength) {
                    System.arraycopy(array, i + 1, newArray, i, newLength - i);
                }
                break;
            }
        }
        return newArray;
    }
