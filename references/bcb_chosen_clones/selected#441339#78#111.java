    public void createPlugin() {
        try {
            XmlElement editors = getPluginEditors();
            String[] keys = new String[editors.getElementCount()];
            Hashtable editorsMap = new Hashtable();
            for (int loop = 0; loop < editors.getElementCount(); loop++) {
                XmlElement editor = editors.getElementByIndex(loop);
                keys[loop] = Class.forName(editor.getAttributeValueByName("className")).getName();
                editorsMap.put(keys[loop], editor);
            }
            String pluginClassName = XappRootApplication.askForSelection("New Plugin type", "Select a Plugin type", keys, keys[0]);
            if (pluginClassName == null) {
                return;
            }
            XmlElement editor = ((XmlElement) editorsMap.get(pluginClassName));
            pluginClassName = editor.getAttributeValueByName("className");
            String pluginDefinitionFile = File.createTempFile("untitled", ".plugin").getAbsolutePath();
            XmlElement xDefinition = new XmlElement("plugin");
            xDefinition.addAttribute("class", pluginClassName);
            xDefinition.addAttribute("name", "untitled");
            XappPlugin plugin = (XappPlugin) Class.forName(pluginClassName).getConstructor(new Class[] { String.class, XmlElement.class }).newInstance(new Object[] { pluginDefinitionFile, xDefinition });
            if (editor == null) {
                XappRootApplication.displayMessage("No editor found for plugin of type " + plugin.getClass().getName());
                return;
            }
            String editorClassName = editor.getValue();
            String editorFormsName = editor.getAttributeValueByName("formsName");
            XmlPathSearch search = new XmlPathSearch(XmlCloner.getClone(getDefinition()));
            XmlElement forms = search.findElement("plugin/forms[@name='" + editorFormsName + "']");
            Class.forName(editorClassName).getConstructor(new Class[] { XmlElement.class, XappPlugin.class }).newInstance(new Object[] { forms, plugin });
        } catch (Exception e) {
            XappRootApplication.displayException(e);
        }
    }
