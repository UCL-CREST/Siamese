    protected ModuleBuilder initModuleBuilder(String className) throws Exception {
        Data lab = getDocParent();
        String file = lab == null ? null : lab.getString(DataTypes.LAB_builderFile);
        if (file == null) return super.initModuleBuilder(className);
        Class cls = Class.forName(className);
        Object arg = new MetaDataModulesDefinition(DefaultEnvironment.getTop().getModuleDirectory() + Const.fileSep + lab.getString(DataTypes.LAB_id) + Const.fileSep + file);
        try {
            return (ModuleBuilder) cls.getConstructor(new Class[] { ModulesDefinition.class }).newInstance(new Object[] { arg });
        } catch (Exception e) {
            return super.initModuleBuilder(className);
        }
    }
