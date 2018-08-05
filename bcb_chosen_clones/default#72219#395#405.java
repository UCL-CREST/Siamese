    protected Extendable createMAGEobject(String className) {
        Extendable MAGEobj = null;
        try {
            java.lang.Class MAGEclass = java.lang.Class.forName(className);
            java.lang.reflect.Constructor constructor = MAGEclass.getConstructor(null);
            MAGEobj = (Extendable) constructor.newInstance(null);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return MAGEobj;
    }
