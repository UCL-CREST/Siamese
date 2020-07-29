    @SuppressWarnings("unchecked")
    public Component getComponent(Object bean, String wicketId, ElementMetaData propertyMeta) {
        boolean viewOnly = propertyMeta.isViewOnly();
        String componentClassName = propertyMeta.getFieldType();
        if (componentClassName == null) {
            Class type = propertyMeta.getPropertyType();
            if (type.isArray()) {
                type = Object[].class;
            }
            Class elementType = propertyMeta.getElementType(null);
            for (; type != null && componentClassName == null; type = type.getSuperclass()) {
                componentClassName = getComponentClassName(type, elementType);
                Class[] intfs = type.getInterfaces();
                for (int i = 0; componentClassName == null && i < intfs.length; i++) {
                    componentClassName = getComponentClassName(intfs[i], elementType);
                }
            }
        }
        if (componentClassName != null) {
            try {
                Class componentClass = Class.forName(componentClassName, true, Thread.currentThread().getContextClassLoader());
                Constructor xtor = componentClass.getConstructor(constructorArgs);
                IModel model = new BeanPropertyModel(bean, propertyMeta);
                Component component = (Component) xtor.newInstance(new Object[] { wicketId, model, propertyMeta, viewOnly });
                associateLabelToFormComponents(propertyMeta, component);
                return component;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Component class not found", e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Component class does not implement constructor (String wicketId, IModel model)", e);
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating component " + componentClassName, e);
            }
        }
        return new Label(wicketId, "<No Field for " + propertyMeta.getPropertyType().getName() + ">");
    }
