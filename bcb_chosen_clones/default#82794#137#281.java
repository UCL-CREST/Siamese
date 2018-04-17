    public void run() {
        Class c;
        Class[] actionTypes, parameterTypes;
        Constructor cc;
        int parameterCount;
        Method method;
        Object[] parameterObjects;
        String actionName, taskName, logText;
        String[] stringParameters;
        StringBuffer logBuffer;
        Task task;
        Assignment asgn = null;
        Document doc = null;
        try {
            asgn = new Assignment(username, assignmentName);
        } catch (IOException ex) {
            Throwable cause = ex.getCause();
            log("   There was a problem accessing your files.");
            log("   Assignment: " + ex.toString());
            if (cause != null) log("      Cause: " + cause.toString());
            return;
        }
        Actions acts = new Actions(this);
        Accounts.setRunStatus(username, "Assignment parsed");
        int taskCount = asgn.getTaskCount();
        for (int tasknum = 1; tasknum <= taskCount; tasknum++) {
            if (Thread.interrupted() == true) return;
            taskName = asgn.getTaskName(tasknum);
            logBuffer = new StringBuffer(taskName + "( ");
            parameterObjects = asgn.getParameters(tasknum, -1);
            parameterCount = parameterObjects.length;
            for (int p = 0; p < parameterCount; p++) {
                logBuffer.append(parameterObjects[p].toString() + ", ");
            }
            logBuffer.append(")");
            log("");
            log(new String(logBuffer));
            if (localRun) System.out.println("   " + logBuffer.toString());
            if (taskName.compareTo("Execute") == 0) {
                parameterCount++;
                parameterObjects = exec(parameterObjects);
            }
            parameterTypes = new Class[parameterCount];
            for (int j = 0; j < parameterCount; j++) parameterTypes[j] = String.class;
            try {
                logText = null;
                c = Class.forName(taskName);
                cc = c.getConstructor(parameterTypes);
                task = (Task) cc.newInstance(parameterObjects);
                doc = task.getResult();
                if (task.getSuccessFlag() == false) logText = "Last status: " + task.getStatus();
            } catch (ClassNotFoundException ex) {
                logText = "Could not find the task";
                log(logText);
                log(ex.toString());
            } catch (InstantiationException ex) {
                logText = "The task couldn't be created";
                log(logText);
                log(ex.toString());
            } catch (NoSuchMethodException ex) {
                logText = "The task tried to call a non-existent method";
                log(logText);
                log(ex.toString());
            } catch (SecurityException ex) {
                logText = "The task tried to do something it isn't allowed to";
                log(logText);
                log(ex.toString());
            } catch (IllegalAccessException ex) {
                logText = "The server is incorrectly configured";
                log(logText);
                log(ex.toString());
            } catch (InvocationTargetException ex) {
                logText = "The task caused a target error";
                log(logText);
                log(ex.toString());
            } catch (RuntimeException ex) {
                logText = "The task generated a runtime exception";
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                log(logText);
                log(ex.toString());
                log("");
                log(sw.toString());
            }
            if (logText != null) {
                try {
                    ResultHelper xml = new ResultHelper("Failed: " + logText, taskName, "text/plain", "Colander Error", "Colander");
                    doc = xml.getDocument();
                } catch (ParserConfigurationException ex) {
                    continue;
                }
                log(logText);
            }
            try {
                acts.setDocument(doc);
                c = acts.getClass();
                if (ServerProperties.isDebugModeSet()) {
                    if (Thread.interrupted() == true) return;
                    String[] sArray = { "debug_" + tasknum + "_result.log" };
                    Class[] cArray = new Class[1];
                    cArray[0] = String.class;
                    method = c.getMethod("toFile", cArray);
                    method.invoke(acts, (Object[]) sArray);
                }
                int actionCount = asgn.getActionCount(tasknum);
                for (int action = 1; action <= actionCount; action++) {
                    if (Thread.interrupted() == true) return;
                    actionName = asgn.getActionName(tasknum, action);
                    stringParameters = asgn.getParameters(tasknum, action);
                    actionTypes = new Class[stringParameters.length];
                    logBuffer = new StringBuffer("   " + actionName + "( ");
                    for (int apc = 0; apc < stringParameters.length; apc++) {
                        actionTypes[apc] = String.class;
                        logBuffer.append(stringParameters[apc] + ", ");
                    }
                    logBuffer.append(")");
                    log(new String(logBuffer));
                    method = c.getMethod(actionName, actionTypes);
                    method.invoke(acts, (Object[]) stringParameters);
                }
            } catch (NoSuchMethodException ex) {
                log("The assignment tried to call a non-existent action");
                log(ex.toString());
            } catch (SecurityException ex) {
                log("The action tried to do something it isn't allowed to");
                log(ex.toString());
            } catch (IllegalAccessException ex) {
                log("The server is incorrectly configured");
                log(ex.toString());
            } catch (InvocationTargetException ex) {
                log("The action caused the following error:");
                log("thrown by: " + ex.getCause().toString());
            } catch (RuntimeException ex) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                log("The action generated a runtime exception");
                log(ex.toString());
                log("");
                log(sw.toString());
            }
            Accounts.setRunStatus(username, "Completed " + tasknum + " of " + taskCount + " tasks");
        }
        log("");
        log("All tasks have been run.");
    }
