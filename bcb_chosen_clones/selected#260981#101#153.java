    @Override
    public void run() throws CErrorException {
        final String vProject = projectConfig.getOption(EProjectOptions.PROJECTNAME);
        final IBlackboard vBlackboard = AbstractBlackboardFactory.getDefaultFactory().getBlackboard();
        HashMap<String, String> vParams = new HashMap<String, String>();
        vParams.put("workroot", new File(projectConfig.getWorkingPath(this)).getParentFile().toURI().toString());
        vParams.put("sourcefolder", new File(projectConfig.getOption(EProjectOptions.SOURCEFOLDER)).toURI().toString());
        vParams.put("name", vProject);
        vParams.put("outfolder", new File(getOutputFile()).getParentFile().toURI().toString());
        String vOutFile = getOutputFile();
        File vTargetFile = new File(vOutFile);
        List<Thread> vThreads = new LinkedList<Thread>();
        CTransformationContext vJob = transformationContexts.pollFirst();
        while (vJob != null) {
            while (vJobsRunning >= config.getIntOption(EConfigurationOptions.MAXTHREADS)) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            logger.info("Creating report part '" + vJob.getOutputFile().getAbsolutePath() + "'");
            InputStream vStream = new BufferedInputStream(vBlackboard.getAllInformationForAsStream(vProject));
            Thread t = new Thread(new CJobRunner(vStream, vJob, config.getOption(EConfigurationOptions.TRANSFORMATIONSTRATEGY), config.getOption(EConfigurationOptions.ARCHIVEDIR) + File.separator + projectConfig.getOption(EProjectOptions.PROJECTNAME) + File.separator, this, vParams));
            t.setName("Generating " + vJob.getOutputFile());
            t.start();
            vThreads.add(t);
            vJobsRunning++;
            vJob = transformationContexts.pollFirst();
        }
        for (Thread t : vThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            File vReportDefinition = new File(config.getOption(EConfigurationOptions.REPORTDEFINITION));
            logger.info("Integrating report parts");
            integrationContext.runXSLT(new FileInputStream(vReportDefinition), vParams);
        } catch (FileNotFoundException e1) {
            throw new CErrorException(EErrorMessages.COULDNOTOPEN, e1, config.getOption(EConfigurationOptions.REPORTDEFINITION));
        }
        if (config.getBoolOption(EConfigurationOptions.OPENREPORT)) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(vTargetFile);
                } catch (IOException e) {
                    throw new CErrorException(EErrorMessages.COULDNOTOPEN, e, vTargetFile.getAbsolutePath());
                }
            }
        }
    }
