    private Object[] convertPrimitiveToObjectArray(Class arrayType, Object originalArray) {
        Object[] convertedArray;
        try {
            Class wrapperClass = (Class) wrapperClasses.get(arrayType);
            Constructor constructor = wrapperClass.getConstructor(new Class[] { String.class });
            int len = Array.getLength(originalArray);
            convertedArray = (Object[]) Array.newInstance(wrapperClass, len);
            for (int i = 0; i < len; i++) {
                convertedArray[i] = constructor.newInstance(new Object[] { Array.get(originalArray, i).toString() });
            }
        } catch (NoSuchMethodException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (InstantiationException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new NakedObjectRuntimeException(e);
        }
        return convertedArray;
    }
