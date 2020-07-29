    public final GUIObject getGUIObject() {
        if (guiObject == null) {
            try {
                Constructor<?> con = null;
                if (this instanceof Module) {
                    Class<?> c = Class.forName("org.jdmp.gui.module.ModuleGUIObject");
                    con = c.getConstructor(new Class<?>[] { Module.class });
                } else if (this instanceof Variable) {
                    Class<?> c = Class.forName("org.jdmp.gui.variable.VariableGUIObject");
                    con = c.getConstructor(new Class<?>[] { Variable.class });
                } else if (this instanceof Sample) {
                    Class<?> c = Class.forName("org.jdmp.gui.sample.SampleGUIObject");
                    con = c.getConstructor(new Class<?>[] { Sample.class });
                } else if (this instanceof DataSet) {
                    Class<?> c = Class.forName("org.jdmp.gui.dataset.DataSetGUIObject");
                    con = c.getConstructor(new Class<?>[] { DataSet.class });
                } else if (this instanceof Algorithm) {
                    Class<?> c = Class.forName("org.jdmp.gui.algorithm.AlgorithmGUIObject");
                    con = c.getConstructor(new Class<?>[] { Algorithm.class });
                } else {
                    System.err.println("unknown object type: " + this.getClass());
                }
                guiObject = (GUIObject) con.newInstance(new Object[] { this });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return guiObject;
    }
