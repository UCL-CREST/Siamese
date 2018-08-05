    private static PZKSAssignmentPlanner createAssignmentPlanner(String className, Queue<PZKSQueueElement> queue, PZKSGraph systemGraph, PZKSAssignmentStrategy strategy) {
        PZKSAssignmentPlanner planner = null;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Queue.class, PZKSGraph.class, PZKSAssignmentStrategy.class);
            planner = (PZKSAssignmentPlanner) constructor.newInstance(queue, systemGraph, strategy);
        } catch (Exception e) {
            System.out.println("Error during creating assignment planner " + className + ". Default planner will be used");
            e.printStackTrace();
        }
        return planner;
    }
