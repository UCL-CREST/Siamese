    public void setDisplayMethod(String classname) {
        try {
            Class col = ClassLoader.getSystemClassLoader().loadClass(classname);
            Constructor colcon = col.getConstructor(new Class[] {});
            output = (rOutput) colcon.newInstance(new Object[] {});
        } catch (Exception e) {
            System.err.println("Error: The specified class does not implement rOutput interface.");
            System.exit(1);
        }
    }
