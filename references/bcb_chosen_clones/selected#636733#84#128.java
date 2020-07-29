    private SearchEngine makeEngine(NavigatorView view) {
        Hashtable params = view.getParameters();
        if (params == null || (params != null && !params.containsKey("data"))) {
            return null;
        }
        String engineName = (String) params.get("engine");
        HelpSet hs = view.getHelpSet();
        URL base = hs.getHelpSetURL();
        ClassLoader loader = hs.getLoader();
        if (engineName == null) {
            engineName = HelpUtilities.getDefaultQueryEngine();
            params.put("engine", engineName);
        }
        SearchEngine back = null;
        Constructor konstructor;
        Class types[] = { URL.class, Hashtable.class };
        Object args[] = { base, params };
        Class klass;
        debug("makeEngine");
        debug("  base: " + base);
        debug("  params: " + params);
        try {
            if (loader == null) {
                klass = Class.forName(engineName);
            } else {
                klass = loader.loadClass(engineName);
            }
        } catch (Throwable t) {
            throw new Error("Could not load engine named " + engineName + " for view: " + view);
        }
        try {
            konstructor = klass.getConstructor(types);
        } catch (Throwable t) {
            throw new Error("Could not find constructor for " + engineName + ". For view: " + view);
        }
        try {
            back = (SearchEngine) konstructor.newInstance(args);
        } catch (InvocationTargetException e) {
            System.err.println("Exception while creating engine named " + engineName + " for view: " + view);
            e.printStackTrace();
        } catch (Throwable t) {
            throw new Error("Could not create engine named " + engineName + " for view: " + view);
        }
        return back;
    }
