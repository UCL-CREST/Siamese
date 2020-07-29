    public DataObject dataObjectForName(String database_distanceType, Instance instance, String key, Database database) {
        Object o = null;
        Constructor co = null;
        try {
            co = (Class.forName(database_distanceType)).getConstructor(new Class[] { Instance.class, String.class, Database.class });
            o = co.newInstance(new Object[] { instance, key, database });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (DataObject) o;
    }
