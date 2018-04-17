    public boolean instantiate(String className, String filename, String binding) throws Exception {
        ModuleLoader ml = ModuleLoader.getInstance();
        if (!this.equals(ml)) return ml.instantiate(className, filename, binding); else {
            Class c = Class.forName(className);
            Class[] carr = new Class[] { String.class };
            Constructor con = c.getConstructor(carr);
            Object[] oarr = new Object[] { filename };
            Object o = con.newInstance(oarr);
            Config.addHandler(binding, o);
            addModule((Module) o);
            return true;
        }
    }
