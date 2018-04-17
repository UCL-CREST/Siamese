    public static final Object[] addToArray(Object[] table, Object newObject) throws TechniqueException {
        if (table == null) throw new TechniqueException("Impossible d'ajouter un �l�ment � un tableau qui n'existe pas");
        if (newObject == null) throw new TechniqueException("Impossible d'ajouter un �l�ment null � un tableau");
        Class c = table.getClass();
        Object newArray = java.lang.reflect.Array.newInstance(c.getComponentType(), table.length + 1);
        System.arraycopy(table, 0, newArray, 0, table.length);
        java.lang.reflect.Array.set(newArray, table.length, newObject);
        return (Object[]) newArray;
    }
