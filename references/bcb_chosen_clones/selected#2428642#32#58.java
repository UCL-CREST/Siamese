    public static IRunner newRunner(ICommand cmd, File cwd) throws Exception {
        logger.debug("Creating runner for Command: " + cmd.getID());
        if (cmd.getType().equals(CommandType.HTC) || cmd.getType().equals(CommandType.INTERNAL)) {
            String className = HTCConfig.getProperty("grid.runner");
            logger.debug("Read grid runner class name: " + className);
            Class[] paramTypes = { Command.class, File.class };
            Class runnerClass = Class.forName(className);
            logger.debug("Obtained class: " + runnerClass.getName());
            Constructor constructor = runnerClass.getConstructor(paramTypes);
            logger.debug("Obtained constructor: " + constructor);
            Object[] params = { (Command) cmd, cwd };
            IRunner runner = (IRunner) constructor.newInstance(params);
            return runner;
        } else if (cmd.getType().equals(CommandType.SSH)) {
            return (IRunner) new SSHRunner((Command) cmd, cwd);
        } else if (cmd.getType().equals(CommandType.SGE)) {
            return (IRunner) new SgeRunner((Command) cmd, cwd);
        } else if (cmd.getType().equals(CommandType.CONDOR)) {
            return (IRunner) new CondorRunner((Command) cmd, cwd);
        } else if (cmd.getType().equals(CommandType.COMPOSITE)) {
            return (IRunner) new CompositeRunner((CommandSet) cmd, cwd);
        } else if (cmd.getType().equals(CommandType.MW)) {
            return (IRunner) new Runner((Command) cmd, cwd);
        } else {
            throw new Exception("unknown request/command type " + cmd.getType());
        }
    }
