    public static Agent createAgentFromClass(String agentName, String team, String agentClass) {
        ClassLoader classLoader = Agent.class.getClassLoader();
        Class<?> aClass = null;
        try {
            aClass = classLoader.loadClass(agentClass);
            System.out.println("instance of \"" + aClass.getName() + "\" created");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor<?> c = null;
        Agent ret = null;
        try {
            c = aClass.getConstructor(new Class[] { String.class, String.class });
            ret = (Agent) (c.newInstance(agentName, team));
        } catch (Exception e) {
            System.out.println(e);
            assert false : e;
            return null;
        }
        return ret;
    }
