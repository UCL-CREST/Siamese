    private void createValue(Object key, Class createClass) {
        if (createClass == null || _destination == null) {
            return;
        }
        Object addValue = null;
        try {
            Class[] classArgs = null;
            Constructor constructor = createClass.getConstructor(classArgs);
            Object[] objectArgs = null;
            addValue = constructor.newInstance(objectArgs);
            _destination.addProperty(key, null, addValue);
        } catch (Exception e) {
            AssertUtility.exception(e);
        }
    }
