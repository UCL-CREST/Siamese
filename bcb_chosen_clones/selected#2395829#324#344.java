    public Database databaseForName(String database_Type, Instances instances) {
        Object o = null;
        Constructor co = null;
        try {
            co = (Class.forName(database_Type)).getConstructor(new Class[] { Instances.class });
            o = co.newInstance(new Object[] { instances });
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
        return (Database) o;
    }
