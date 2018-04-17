    @SuppressWarnings("unchecked")
    public ArrayList<Tracker> loadTrackers() {
        ArrayList<Tracker> trackers = new ArrayList<Tracker>();
        ArrayList<String> classes = getPluginClassNames("trackers");
        for (String classname : classes) {
            try {
                Class c = loadClass(classname);
                Constructor cs = c.getConstructors()[0];
                Object[] args = new Object[1];
                args[0] = gui;
                Object o = cs.newInstance(args);
                trackers.add((Tracker) o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return trackers;
    }
