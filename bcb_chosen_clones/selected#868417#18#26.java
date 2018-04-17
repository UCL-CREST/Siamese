    public IOutputer get_Outputer() throws Exception {
        if (_loaded == null) {
            URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new File("").toURI().toURL() });
            Class<?> outClass = Class.forName(_packageName + "." + _outputerName + "." + _outputerName + "Outputer", false, loader);
            Constructor<?> cons = outClass.getConstructor(String.class);
            _loaded = (IOutputer) cons.newInstance(_fileName);
        }
        return _loaded;
    }
