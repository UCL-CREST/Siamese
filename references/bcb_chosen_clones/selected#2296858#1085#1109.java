    private static DataSource reflectMDS(String cname, Object pds) {
        Class cls;
        Constructor cc;
        Class[] paramTypes = new Class[1];
        Object[] arg = new Object[1];
        try {
            cls = Class.forName(cname);
            paramTypes[0] = pds.getClass();
            cc = cls.getConstructor(paramTypes);
            if (cname.indexOf("PullDataSource") >= 0) {
                arg[0] = (PullDataSource[]) pds;
            } else if (cname.indexOf("PushDataSource") >= 0) {
                arg[0] = (PushDataSource[]) pds;
            } else if (cname.indexOf("PullBufferDataSource") >= 0) {
                arg[0] = (PullBufferDataSource[]) pds;
            } else if (cname.indexOf("PushBufferDataSource") >= 0) {
                arg[0] = (PushBufferDataSource[]) pds;
            } else if (cname.indexOf("CDPushBDS") >= 0) {
                arg[0] = (PushBufferDataSource[]) pds;
            }
            return (DataSource) (cc.newInstance(arg));
        } catch (Exception ex) {
        }
        return null;
    }
