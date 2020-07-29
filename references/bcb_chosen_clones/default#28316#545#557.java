    void loadDemo(String classname) {
        setStatus(getString("Status.loading") + getString(classname + ".name"));
        DemoModule demo = null;
        try {
            Class demoClass = Class.forName(classname);
            Constructor demoConstructor = demoClass.getConstructor(new Class[] { SwingSet2.class });
            demo = (DemoModule) demoConstructor.newInstance(new Object[] { this });
            addDemo(demo);
        } catch (Exception e) {
            System.err.println("Error occurred loading demo: " + classname);
            e.printStackTrace();
        }
    }
