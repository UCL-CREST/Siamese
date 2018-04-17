    public static Object[] convertPrimitiveToObjectArray(final Class arrayType, final Object originalArray) {
        Object[] convertedArray;
        try {
            final Class wrapperClass = (Class) wrapperClasses.get(arrayType);
            final Constructor constructor = wrapperClass.getConstructor(new Class[] { String.class });
            final int len = Array.getLength(originalArray);
            convertedArray = (Object[]) Array.newInstance(wrapperClass, len);
            for (int i = 0; i < len; i++) {
                convertedArray[i] = constructor.newInstance(new Object[] { Array.get(originalArray, i).toString() });
            }
        } catch (final NoSuchMethodException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final InstantiationException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new NakedObjectRuntimeException(e);
        }
        return convertedArray;
    }
