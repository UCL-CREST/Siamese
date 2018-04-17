    private void registerPlugins() {
        name2plugin = new HashMap<String, AbstractContentDisplayPlugin>();
        name2activation = new HashMap<String, Boolean>();
        pluginOrder = new Vector<String>();
        Set<String> attsAccountedFor = new HashSet<String>();
        String pluginsProp = Config.config.getProperty("ContentsDisplay.Plugins");
        String[] pluginNames = pluginsProp.split(";");
        for (String pluginName : pluginNames) {
            try {
                String classpath = "net.sourceforge.ondex.ovtk2.ui.contentsdisplay.plugins." + pluginName;
                Class<?>[] args = new Class<?>[] { ONDEXGraph.class };
                Constructor<?> constr = AbstractContentDisplayPlugin.class.getClassLoader().loadClass(classpath).getConstructor(args);
                AbstractContentDisplayPlugin plugin = (AbstractContentDisplayPlugin) constr.newInstance(aog);
                if (plugin instanceof GDSAttributePlugin) {
                    String[] atts = ((GDSAttributePlugin) plugin).getAttributeNames();
                    for (String att : atts) {
                        attsAccountedFor.add(att);
                    }
                }
                String name = plugin.getName();
                name2plugin.put(name, plugin);
                pluginOrder.add(name);
                name2activation.put(name, true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
        SortedSet<String> sort = new TreeSet<String>();
        for (AttributeName att : aog.getMetaData().getAttributeNames()) {
            if (!attsAccountedFor.contains(att.getId())) {
                if (Number.class.isAssignableFrom(att.getDataType())) {
                    name2plugin.put(att.getId(), new NumberPlugin(aog, att));
                    name2activation.put(att.getId(), true);
                    sort.add(att.getId());
                } else if (String.class.isAssignableFrom(att.getDataType())) {
                    name2plugin.put(att.getId(), new StringPlugin(aog, att));
                    name2activation.put(att.getId(), true);
                    sort.add(att.getId());
                } else if (Boolean.class.isAssignableFrom(att.getDataType())) {
                    name2plugin.put(att.getId(), new BooleanPlugin(aog, att));
                    name2activation.put(att.getId(), true);
                    sort.add(att.getId());
                }
            }
        }
        pluginOrder.addAll(sort);
    }
