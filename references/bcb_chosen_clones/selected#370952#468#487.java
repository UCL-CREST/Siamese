    public SModule newModule(String moduletype, String modulename) {
        moduletype = moduletype.trim();
        String config = "";
        if (modulename == null) modulename = "";
        SModule newmodule = null;
        String className = getModuleAbsName(moduletype);
        try {
            Constructor moduleConst = Class.forName(className).getConstructor(new Class[] { String.class, String.class });
            newmodule = (SModule) moduleConst.newInstance(new Object[] { modulename, config });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exception: " + e, "Could not instantiate module", JOptionPane.ERROR_MESSAGE);
            return newmodule;
        }
        try {
            cm.add(newmodule);
        } catch (SDuplicateElementException e) {
            JOptionPane.showMessageDialog(null, "There is already a module with that name", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        return newmodule;
    }
