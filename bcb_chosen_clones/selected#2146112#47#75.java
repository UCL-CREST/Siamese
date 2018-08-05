    @Override
    public ITaskResult call() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(commandLine).directory(workingDirectory).redirectErrorStream(true);
        pb.environment().putAll(additionalEnvironment);
        System.out.println(pb.environment());
        Process p = pb.start();
        try {
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        int status = p.waitFor();
        if (status != 0) {
            throw new RuntimeException("Task finished with non-zero exit status: " + status + "\n Commandline: " + commandLine + ", working directory: " + workingDirectory);
        }
        for (IPostProcessor postProcessor : postProcessors) {
            postProcessor.process(this);
        }
        if (outputDirectory.exists()) {
            return new MaltcmsTaskResult(outputDirectory);
        }
        Logger.getLogger(Task.class.getName()).log(Level.INFO, "Output directory for task " + getTaskId() + " was removed by post processor!");
        return DefaultTaskResult.EMPTY;
    }
