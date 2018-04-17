    public CGDemoModule loadDemo(String resourceName) {
        CGTutorial tutorial = getTutorial();
        CGDemoModule demo = null;
        try {
            String demoName = tutorial.getString(resourceName + ".class");
            Class demoClass = Class.forName(demoName);
            Constructor demoConstructor = demoClass.getConstructor(new Class[] { tutorial.getClass() });
            Object[] args = new Object[] { tutorial };
            demo = (CGDemoModule) demoConstructor.newInstance(args);
        } catch (Exception ex) {
            ex.printStackTrace();
            tutorial.setStatus("Cannot load demo: " + ex);
            System.err.println("Hint: check if each line in manifest ends with a space, and that computational.jar is updated!");
        }
        return demo;
    }
