    private static PZKSQueuePlanner createQueuePlanner(String className, PZKSGraph graph) {
        PZKSQueuePlanner planner = null;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(PZKSGraph.class);
            planner = (PZKSQueuePlanner) constructor.newInstance(graph);
        } catch (Exception e) {
            System.out.println("Error during creating queue planner " + className + ". Default planner will be used");
        }
        return planner;
    }
