    public static PZKSGantRenderer createGantRenderer(Queue<List<PZKSAssignmentElement>> elements) {
        PZKSGantRenderer renderer = null;
        String className = System.getProperty("gant-renderer");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Queue.class);
            renderer = (PZKSGantRenderer) constructor.newInstance(elements);
        } catch (Exception e) {
            System.out.println("Error during creating gant renderer " + className + ". Default renderer will be used");
            System.out.println(e.getMessage());
            renderer = new PZKSLamaoGantRenderer(elements);
        }
        return renderer;
    }
