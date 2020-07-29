    public Object convertSourceToTargetClass(Object source, Class targetClass) throws Exception {
        if (targetClass.equals(DataModel.class)) {
            targetClass = OneSelectionTrackingListDataModel.class;
        }
        Constructor emptyConstructor = ClassUtils.getConstructorIfAvailable(targetClass, new Class[] {});
        DataModel model = (DataModel) emptyConstructor.newInstance(new Object[] {});
        model.setWrappedData(source);
        return model;
    }
