    void createCanvas(int maxWidth, int maxHeight) throws IOException {
        vc = null;
        try {
            Class cl = Class.forName("java.awt.Graphics2D");
            cl = Class.forName("VncCanvas2");
            Class[] argClasses = { this.getClass(), Integer.TYPE, Integer.TYPE };
            java.lang.reflect.Constructor cstr = cl.getConstructor(argClasses);
            Object[] argObjects = { this, new Integer(maxWidth), new Integer(maxHeight) };
            vc = (VncCanvas) cstr.newInstance(argObjects);
        } catch (Exception e) {
            System.out.println("Warning: Java 2D API is not available");
        }
        if (vc == null) vc = new VncCanvas(this, maxWidth, maxHeight);
    }
