    public static <T> T insert(T array, T additionArray, int idx) {
        int additionLength = Array.getLength(additionArray);
        if (additionLength == 0) return array;
        Class componentType = array.getClass().getComponentType();
        int originalLength = Array.getLength(array);
        if (idx == -1) idx = originalLength;
        T newarray = (T) Array.newInstance(componentType, originalLength + additionLength);
        if (idx > 0) System.arraycopy(array, 0, newarray, 0, idx == originalLength ? idx : idx + 1);
        if (componentType.isPrimitive()) {
            for (int a = 0; a < additionLength; a++) {
                Array.set(newarray, idx + a, Array.get(additionArray, a));
            }
        } else {
            System.arraycopy(additionArray, 0, newarray, idx, additionLength);
        }
        if (idx < Array.getLength(array)) System.arraycopy(array, idx, newarray, idx + additionLength, originalLength - idx);
        return newarray;
    }
