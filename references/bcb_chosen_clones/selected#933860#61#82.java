    public static final DAVEditor getEditor(DAVFile file) {
        String filename = file.getFileName();
        int lastDot = filename.lastIndexOf(".") + 1;
        if (lastDot > 0 && (lastDot) < filename.length()) {
            String extension = filename.substring(lastDot).toLowerCase().trim();
            if (extensionHash.containsKey(extension)) {
                String editorName = extensionHash.get(extension).toString();
                try {
                    Class editorClass = Class.forName(editorName);
                    Constructor editorConstructor = editorClass.getConstructor(new Class[] { DAVFile.class });
                    Object editorObject = editorConstructor.newInstance(new Object[] { file });
                    if (editorObject instanceof DAVEditor) {
                        configurator.configure(editorObject);
                        return (DAVEditor) editorObject;
                    }
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
        }
        return getDefaultEditor(file);
    }
