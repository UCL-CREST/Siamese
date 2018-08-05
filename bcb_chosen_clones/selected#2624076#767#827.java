    public Object launch(String javaCmd, Properties props, String[] opts, String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No class name in args[0]");
        }
        String taskName = args[0];
        try {
            Class taskClass = Class.forName(taskName, true, pluginLoader);
            if (!SubVMTask.class.isAssignableFrom(taskClass)) {
                throw new IllegalArgumentException(taskName + " does not implement " + "SubVMTask");
            }
            if (taskClass.getDeclaringClass() != null) {
                if ((taskClass.getModifiers() & Modifier.STATIC) == 0) {
                    throw new IllegalArgumentException(taskName + " must be a static class");
                }
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + taskName);
        }
        if (javaCmd == null) {
            javaCmd = System.getProperty("java.home");
            javaCmd += File.separator + "bin" + File.separator + "java";
        }
        ArrayList cmdList = new ArrayList();
        cmdList.add(javaCmd);
        boolean gotClasspath = false;
        if (opts != null) {
            for (int i = 0; i < opts.length; i++) {
                if (opts[i].equals("-cp") || opts[i].equals("-classpath")) if (i + 1 == opts.length) {
                    throw new IllegalArgumentException("classpath option " + "does not have " + "a value");
                }
                gotClasspath = true;
                cmdList.add(opts[i]);
            }
        }
        if (!gotClasspath) {
            throw new IllegalArgumentException("no classpath defined");
        }
        if (props != null) {
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                String value = props.getProperty(key);
                cmdList.add("-D" + key + "=" + value);
            }
        }
        cmdList.add("com.sun.jini.tool.envcheck.SubVM");
        for (int i = 0; i < args.length; i++) {
            cmdList.add(args[i]);
        }
        try {
            String[] argList = (String[]) cmdList.toArray(new String[cmdList.size()]);
            Process p = Runtime.getRuntime().exec(argList);
            Pipe pipe = new Pipe("errorPipe", p.getErrorStream(), System.err, "Child VM: ");
            ObjectInputStream s = new ObjectInputStream(p.getInputStream());
            Object o = s.readObject();
            pipe.waitTillEmpty(5000);
            return o;
        } catch (Exception e) {
            return e;
        }
    }
