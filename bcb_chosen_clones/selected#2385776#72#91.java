    private ITestObjectPool GetITestObjectPool() {
        ITestObjectPool objPool = null;
        String projectType = _config.GetProjectType();
        String dll = _config.GetRelateObjectByType(projectType);
        try {
            if (dll.indexOf(".") < 0) {
                dll = "Script.UAT.Framework." + dll;
            }
            Class cls = Class.forName(dll);
            Class partypes[] = new Class[1];
            partypes[0] = Object.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = TestJob.GetRootObject();
            objPool = (ITestObjectPool) (ct.newInstance(arglist));
        } catch (Exception e) {
            throw new RuntimeException("Class not found.");
        }
        return objPool;
    }
