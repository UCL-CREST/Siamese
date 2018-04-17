    public JDialog getPDCEditorWidow() throws KExceptionClass {
        try {
            log.log(this, "Openning editor " + pdcEditorClass.getName());
            Class[] editorConstructorRequiredParam = new Class[] { KConfigurationClass.class, KLogClass.class, java.awt.Window.class };
            Object[] editorConstructorActualArguments = new Object[] { configuration, log, parentWindow };
            Constructor editorConstructor;
            try {
                editorConstructor = pdcEditorClass.getConstructor(editorConstructorRequiredParam);
            } catch (NoSuchMethodException error) {
                throw new KExceptionClass("Object Editor " + pdcEditorClass.getName() + " does not provide the required constructor (KConfigurationClass, KLogClass, java.awt.Frame) ", error);
            }
            return ((JDialog) editorConstructor.newInstance(editorConstructorActualArguments));
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            throw new KExceptionClass("Error openning window " + pdcEditorClass.getName() + " :" + error.toString(), error);
        }
    }
