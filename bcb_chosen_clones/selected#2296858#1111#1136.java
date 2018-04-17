    private static DataSource reflectDS(String cname, DataSource source) {
        Class cls;
        Constructor cc;
        Class[] paramTypes = new Class[1];
        Object[] arg = new Object[1];
        try {
            cls = Class.forName(cname);
            if (cname.indexOf("PullDataSource") >= 0) {
                paramTypes[0] = PullDataSource.class;
                arg[0] = (PullDataSource) source;
            } else if (cname.indexOf("PushDataSource") >= 0) {
                paramTypes[0] = PushDataSource.class;
                arg[0] = (PushDataSource) source;
            } else if (cname.indexOf("PullBufferDataSource") >= 0) {
                paramTypes[0] = PullBufferDataSource.class;
                arg[0] = (PullBufferDataSource) source;
            } else if (cname.indexOf("PushBufferDataSource") >= 0) {
                paramTypes[0] = PushBufferDataSource.class;
                arg[0] = (PushBufferDataSource) source;
            }
            cc = cls.getConstructor(paramTypes);
            return (DataSource) (cc.newInstance(arg));
        } catch (Exception ex) {
        }
        return null;
    }
