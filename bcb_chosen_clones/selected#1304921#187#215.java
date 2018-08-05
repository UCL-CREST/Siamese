    public void applyTo(Object bean, int method, String... properties) {
        List<String> allProperties = Arrays.asList(DynaBeanUtils.getPropertyNames(bean));
        List<String> props = Arrays.asList(properties == null ? new String[0] : properties);
        if (!allProperties.containsAll(props)) {
            List<String> hlp = new ArrayList<String>(props);
            hlp.removeAll(allProperties);
            log.debug("Class " + bean.getClass() + " does not contain the specified properties " + hlp);
            return;
        }
        if (method == Form.EXCLUDE_PROPERTIES) {
            List<String> hlp = new ArrayList<String>(allProperties);
            hlp.removeAll(props);
            props = hlp;
        }
        for (String propertyName : props) {
            try {
                Class<?> propertyClass = DynaBeanUtils.getPropertyClass(bean, propertyName);
                if (propertyClass.isArray()) {
                    Object[] values = getValues(propertyName);
                    Object arr = Array.newInstance(propertyClass.getComponentType(), values.length);
                    System.arraycopy(values, 0, arr, 0, values.length);
                    DynaBeanUtils.setProperty(bean, propertyName, arr);
                } else {
                    DynaBeanUtils.setProperty(bean, propertyName, getValue(propertyName));
                }
            } catch (Exception e) {
            }
        }
    }
