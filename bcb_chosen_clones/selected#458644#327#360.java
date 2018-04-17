    public static JComponent findEditor(Attribute attribute) {
        JComponent editor = null;
        AttributeName an = attribute.getOfType();
        String className = an.getDataTypeAsString();
        String editorName = Config.config.getProperty("Dialog.Attribute.Editor." + attribute.getOfType().getId());
        if (editorName == null) editorName = Config.config.getProperty("Dialog.Attribute." + className);
        if (editorName == null) editorName = "DefaultEditor";
        try {
            Class<?> editorClass = null;
            try {
                editorClass = Class.forName("net.sourceforge.ondex.ovtk2.ui.gds." + editorName);
            } catch (ClassNotFoundException cnfe) {
            }
            if (editorClass == null) {
                editor = (JComponent) OVTK2PluginLoader.getInstance().loadAttributeEditor(editorName, attribute);
            } else {
                Class<?>[] args = new Class<?>[] { Attribute.class };
                Constructor<?> constr = editorClass.getConstructor(args);
                editor = (JComponent) constr.newInstance(attribute);
            }
            if (!(editor instanceof GDSEditor)) {
                throw new RuntimeException(editor.getClass().getName() + " does not implement required " + GDSEditor.class + " interface");
            }
            editor.setMinimumSize(new Dimension(100, 100));
            editor.setPreferredSize(new Dimension(100, 100));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editor;
    }
