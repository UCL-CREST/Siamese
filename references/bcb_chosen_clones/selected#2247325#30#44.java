    public static PZKSRenderer createRenderer(PZKSGraph graph) {
        PZKSRenderer renderer = null;
        String className = System.getProperty("renderer");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(PZKSGraph.class);
            renderer = (PZKSRenderer) constructor.newInstance(graph);
        } catch (Exception e) {
            System.out.println("Error during creating renderer " + className + ". Default renderer will be used");
            System.out.println(e.getMessage());
            renderer = new PZKSLamaoRenderer(graph);
        }
        return renderer;
    }
