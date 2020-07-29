    public synchronized ArrayList<Tracker> getTrackers() {
        ArrayList<Tracker> trackers = new ArrayList<Tracker>();
        File f = new File("trackers/");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(mainWindow, "Tracker-directory not found at " + f.getAbsolutePath());
            return null;
        }
        String[] descriptorNames = f.list();
        try {
            for (int i = 0; i < descriptorNames.length; i++) {
                if (descriptorNames[i].endsWith(".class")) {
                    String name = "trackers." + (descriptorNames[i].substring(0, descriptorNames[i].length() - 6));
                    if (!name.contains("$")) {
                        ClassLoader loader = getClass().getClassLoader().getSystemClassLoader();
                        Constructor c = loader.loadClass(name).getConstructors()[0];
                        Object[] args = new Object[1];
                        args[0] = this;
                        trackers.add((Tracker) c.newInstance(args));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackers;
    }
