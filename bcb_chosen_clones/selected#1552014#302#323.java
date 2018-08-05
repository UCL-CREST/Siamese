    public static PropertyValueCellEditor instantiateValueCellEditor(final ParameterType type, Operator operator) {
        PropertyValueCellEditor editor;
        Class<?> typeClass = type.getClass();
        do {
            Class<? extends PropertyValueCellEditor> editorClass = knownValueEditors.get(typeClass);
            if (editorClass != null) {
                try {
                    Constructor<? extends PropertyValueCellEditor> constructor = editorClass.getConstructor(new Class[] { typeClass });
                    editor = constructor.newInstance(new Object[] { type });
                } catch (Exception e) {
                    LogService.getRoot().log(Level.WARNING, "Cannot construct property editor: " + e, e);
                    editor = new DefaultPropertyValueCellEditor(type);
                }
                break;
            } else {
                typeClass = typeClass.getSuperclass();
                editor = new DefaultPropertyValueCellEditor(type);
            }
        } while (typeClass != null);
        editor.setOperator(operator);
        return editor;
    }
