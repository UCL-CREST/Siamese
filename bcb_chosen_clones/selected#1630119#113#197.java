    @Override
    protected boolean executeJob(ETLJob jCurrentJob) {
        boolean bSuccess = true;
        this.monitor = new HadoopJobMonitor(jCurrentJob, Thread.currentThread());
        try {
            stage = Stage.Preparing;
            cmd = null;
            this.monitor.start();
            ETLStatus jsJobStatus;
            String strWorkingDirectory;
            Process pProcess = null;
            File fWorkingDirectory = null;
            long start = (System.currentTimeMillis() - 1);
            if ((jCurrentJob instanceof OSJob) == false) {
                return false;
            }
            this.ojJob = (OSJob) jCurrentJob;
            jsJobStatus = ojJob.getStatus();
            if ((strWorkingDirectory = ojJob.getWorkingDirectory()) != null) {
                fWorkingDirectory = new File(strWorkingDirectory);
            }
            try {
                String osName = System.getProperty("os.name");
                String strExecStmt;
                cmd = ojJob.getCommandLine();
                if (osName.startsWith("Windows")) {
                    strExecStmt = "cmd.exe /c " + cmd;
                } else {
                    strExecStmt = cmd;
                }
                if (ojJob.isDebug()) ResourcePool.LogMessage(this, ResourcePool.DEBUG_MESSAGE, "Executing os command: " + strExecStmt);
                stage = Stage.About_To_Execute;
                if (fWorkingDirectory != null) {
                    pProcess = Runtime.getRuntime().exec(strExecStmt, null, fWorkingDirectory);
                } else {
                    pProcess = Runtime.getRuntime().exec(strExecStmt);
                }
                stage = Stage.Connecting_STDOUT;
                this.monitor.process = pProcess;
            } catch (Throwable e) {
                jsJobStatus.setErrorCode(1);
                jsJobStatus.setErrorMessage("Error running exec(): " + e.getMessage());
                return false;
            }
            try {
                StringBuilder inBuffer = new StringBuilder();
                InputStream inStream = pProcess.getInputStream();
                new InputStreamHandler(inBuffer, inStream);
                stage = Stage.Connecting_STDIN;
                StringBuilder errBuffer = new StringBuilder();
                InputStream errStream = pProcess.getErrorStream();
                new InputStreamHandler(errBuffer, errStream);
                stage = Stage.Executing;
                int iReturnValue = pProcess.waitFor();
                stage = Stage.Completed;
                if (inBuffer.length() > 0) {
                    jsJobStatus.setExtendedMessage(inBuffer.toString());
                }
                try {
                    this.fireJobTriggers(ojJob.iLoadID, ojJob.getJobTriggers(), Integer.toString(iReturnValue));
                } catch (Exception e) {
                    ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.ERROR_MESSAGE, "Error firing triggers, check format <EXITCODE>=<VALUE>=(exec|setStatus)(..);... : " + e.getMessage());
                }
                jsJobStatus.setErrorCode(iReturnValue);
                if (iReturnValue != 0) {
                    jsJobStatus.setErrorMessage("STDERROR:" + errBuffer.toString());
                    jsJobStatus.setExtendedMessage("STDOUT:" + inBuffer.toString());
                    if (iReturnValue == ETLJobStatus.CRITICAL_FAILURE_ERROR_CODE) {
                        jsJobStatus.setErrorMessage("Server has been paused\n" + jsJobStatus.getErrorMessage());
                        jsJobStatus.setStatusCode(ETLJobStatus.CRITICAL_FAILURE_PAUSE_LOAD);
                    }
                    bSuccess = false;
                } else jsJobStatus.setStats(-1, System.currentTimeMillis() - start);
            } catch (Exception e) {
                jsJobStatus.setErrorCode(2);
                jsJobStatus.setErrorMessage("Error in process: " + e.getMessage());
                return false;
            }
        } finally {
            this.stage = Stage.Completed;
            this.monitor.alive = false;
            this.ojJob = null;
        }
        return bSuccess;
    }
