    public static void saveTemplate(View view) {
        JFileChooser chooser = new JFileChooser(jEdit.getProperty("plugin.TemplatesPlugin.templateDir.0", "."));
        int retVal = chooser.showSaveDialog(view);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                try {
                    view.getBuffer().save(view, file.getCanonicalPath());
                } catch (IOException e) {
                }
            }
        }
    }
