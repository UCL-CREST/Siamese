    public void run(final File tmpdir, final ISimulationDataProvider inputProvider, final ISimulationResultEater resultEater, final ISimulationMonitor monitor) throws SimulationException {
        ConsoleHelper.writeLine(m_outputStream, String.format(Messages.SimulationPi2SobekWorker_0));
        final File directory = new File(tmpdir, ISobekCalculationJobConstants.PATH_SOBEK_BATCH_DIR);
        final String[] command = new String[3];
        command[0] = "cmd.exe";
        command[1] = "/C";
        command[2] = "1_PI2sobek.bat";
        Process exec;
        try {
            exec = Runtime.getRuntime().exec(command, null, directory);
        } catch (final IOException e1) {
            e1.printStackTrace();
            throw new SimulationException(e1.getMessage());
        }
        final InputStream errorStream = exec.getErrorStream();
        final InputStream inputStream = exec.getInputStream();
        final StreamGobbler error = new StreamGobbler(errorStream, "Report: ERROR_STREAM", false, m_sobekStream);
        final StreamGobbler input = new StreamGobbler(inputStream, "Report: INPUT_STREAM", false, m_sobekStream);
        error.start();
        input.start();
        int timeRunning = 0;
        while (true) {
            try {
                exec.exitValue();
                if (monitor.isCanceled()) throw new SimulationException("Computation Canceld");
                break;
            } catch (final RuntimeException e) {
            }
            try {
                Thread.sleep(100);
                timeRunning = timeRunning + 100;
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
        ConsoleHelper.writeLine(m_outputStream, String.format(Messages.SimulationPi2SobekWorker_4));
        ConsoleHelper.writeLine(m_outputStream, "");
        final File logFile = new File(tmpdir, ISobekCalculationJobConstants.LOG_PI2SOBEK_PATH);
        if (!logFile.exists()) throw new SimulationException(Messages.SimulationPi2SobekWorker_6);
        resultEater.addResult(ISobekCalculationJobConstants.LOG_PI2SOBEK, logFile);
        if (!checkLogFile(logFile)) throw new SimulationException(Messages.SimulationPi2SobekWorker_7);
    }
