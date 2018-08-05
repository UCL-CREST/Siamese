    public static void editTemplate(View view) {
        JFileChooser chooser = new JFileChooser(jEdit.getProperty("plugin.TemplatesPlugin.templateDir.0", "."));
        int retVal = chooser.showOpenDialog(view);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    jEdit.openFile(view, file.getCanonicalPath());
                } catch (IOException e) {
                }
            }
        }
    }
