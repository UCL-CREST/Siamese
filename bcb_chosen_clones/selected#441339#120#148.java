    public void editPlugin() {
        String[] plugins = XappRootApplication.getPluginStore().getPluginNames();
        Vector editablePlugins = new Vector();
        for (int loop = 0; loop < plugins.length; loop++) {
            if (getEditor(plugins[loop]) != null) {
                editablePlugins.addElement(plugins[loop]);
            }
        }
        plugins = StringArrayHelper.vectorToStringArray(editablePlugins);
        String pluginName = XappRootApplication.askForSelection("Plugin to edit", "Select a Plugin", plugins, plugins[0]);
        if (pluginName == null) {
            return;
        }
        XmlElement editor = getEditor(pluginName);
        if (editor == null) {
            XappRootApplication.displayMessage("No editor found for plugin " + pluginName);
            return;
        }
        String editorClassName = editor.getValue();
        String editorFormsName = editor.getAttributeValueByName("formsName");
        XmlPathSearch search = new XmlPathSearch(XmlCloner.getClone(getDefinition()));
        XmlElement forms = search.findElement("plugin/forms[@name='" + editorFormsName + "']");
        XappPlugin plugin = XappRootApplication.getPluginStore().getPlugin(pluginName);
        try {
            Class.forName(editorClassName).getConstructor(new Class[] { XmlElement.class, XappPlugin.class }).newInstance(new Object[] { forms, plugin });
        } catch (Exception e) {
            XappRootApplication.displayException(e);
        }
    }
