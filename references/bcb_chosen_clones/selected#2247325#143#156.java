    public static PZKSAssignmentStrategy createAssignmentStrategy(String className) {
        PZKSAssignmentStrategy strategy = null;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            strategy = (PZKSAssignmentStrategy) constructor.newInstance();
        } catch (Exception e) {
            System.out.println("Error during creating assignment strategy " + className + ". Default strategy will be used");
            e.printStackTrace();
            strategy = new PZKSPreactStrategy();
        }
        return strategy;
    }
