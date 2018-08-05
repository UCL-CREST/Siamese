    public Object[] toArray(Object[] arObjects) {
        Object[] arReturn = (arObjects.length >= size) ? arObjects : (Object[]) Array.newInstance(arObjects.getClass().getComponentType(), size);
        System.arraycopy(_arData, 0, arReturn, 0, size);
        if (arReturn.length > size) arReturn[size] = null;
        return arReturn;
    }
