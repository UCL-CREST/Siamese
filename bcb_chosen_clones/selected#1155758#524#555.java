    private static <T> T multidimensionalize_internal(double[] linearArray, Class<T> outputArrayType, int[] lengths, int indexIntoLengths, int[] currIndices) {
        Class<?> arrayType = outputArrayType.getComponentType();
        int arrayLength = lengths[indexIntoLengths];
        T array = (T) Array.newInstance(arrayType, arrayLength);
        if (arrayType.isArray()) {
            if (arrayType.equals(double[].class)) {
                int[] primitiveArrayIndices = new int[currIndices.length + 2];
                System.arraycopy(currIndices, 0, primitiveArrayIndices, 0, currIndices.length);
                for (int i = 0; i < arrayLength; i++) {
                    primitiveArrayIndices[primitiveArrayIndices.length - 2] = i;
                    double[] primitiveArray = new double[lengths[lengths.length - 1]];
                    for (int j = 0; j < primitiveArray.length; j++) {
                        primitiveArrayIndices[primitiveArrayIndices.length - 1] = j;
                        int linearIndex = multidimensionalIndicesToLinearIndex(lengths, primitiveArrayIndices);
                        primitiveArray[j] = linearArray[linearIndex];
                    }
                    Array.set(array, i, primitiveArray);
                }
            } else {
                for (int i = 0; i < arrayLength; i++) {
                    int[] nextIndices = new int[currIndices.length + 1];
                    System.arraycopy(currIndices, 0, nextIndices, 0, currIndices.length);
                    nextIndices[nextIndices.length - 1] = i;
                    Object innerArray = multidimensionalize_internal(linearArray, arrayType, lengths, indexIntoLengths + 1, nextIndices);
                    Array.set(array, i, innerArray);
                }
            }
        } else {
            System.arraycopy(linearArray, 0, array, 0, arrayLength);
        }
        return array;
    }
