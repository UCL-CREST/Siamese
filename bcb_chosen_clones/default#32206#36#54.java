    public void testEmbedded() throws Exception {
        String whichOneToRun = askWhichOneToRun();
        String className = "org.mortbay.jetty.example." + whichOneToRun;
        Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        Object o = clazz.newInstance();
        Method main = clazz.getMethod("main", new Class[] { stringArrayType.getClass() });
        String[] prompts = (String[]) argMap.get(whichOneToRun);
        String[] args = new String[0];
        if (prompts != null) {
            args = new String[prompts.length];
            for (int i = 0; i < prompts.length; i++) {
                System.err.print("Enter arg " + i + ": " + prompts[i] + " > ");
                args[i] = reader.readLine();
            }
        }
        Object[] methodArgs = new Object[1];
        methodArgs[0] = args;
        main.invoke(null, methodArgs);
    }
