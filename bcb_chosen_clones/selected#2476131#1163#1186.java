    public synchronized ArrayList<Plugin> getPlugins() {
        ArrayList<Plugin> plugins = new ArrayList<Plugin>();
        File f = new File("plugins/");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(mainWindow, "Plugin-directory not found at " + f.getAbsolutePath());
            return null;
        }
        String[] descriptorNames = f.list();
        try {
            for (int i = 0; i < descriptorNames.length; i++) {
                if (descriptorNames[i].endsWith(".class")) {
                    String name = "plugins." + (descriptorNames[i].substring(0, descriptorNames[i].length() - 6));
                    if (!name.contains("$")) {
                        ClassLoader loader = getClass().getClassLoader().getSystemClassLoader();
                        Constructor c = loader.loadClass(name).getConstructors()[0];
                        plugins.add((Plugin) c.newInstance());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plugins;
    }
