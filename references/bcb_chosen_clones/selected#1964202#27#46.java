    public String getPrimitiveDB(String cls, String render, Object oParam[]) {
        String getPrimitiveDB = "";
        try {
            cObject = null;
            Object o = null;
            Class c = getSwingServicesClass("com.swing.model.dal." + cls);
            Constructor cx = c.getConstructor(DbConnection.class);
            o = cx.newInstance(DbSource.getConnection());
            cObject = invokeMethod(o, oParam, render);
            if (cObject != null) {
                getPrimitiveDB = cObject.toString();
            }
            cObject = null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            release();
        }
        return getPrimitiveDB;
    }
