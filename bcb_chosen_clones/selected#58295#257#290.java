    void shutdown(final boolean unexpected) {
        if (unexpected) {
            log.warn("S H U T D O W N   ---   received unexpected shutdown request.");
        } else {
            log.info("S H U T D O W N   ---   start regular shutdown.");
        }
        if (this.uncaughtException != null) {
            log.warn("Shutdown probably caused by the following Exception.", this.uncaughtException);
        }
        log.error("check if we need the controler listener infrastructure");
        if (this.dumpDataAtEnd) {
            new PopulationWriter(this.population, this.network).write(this.controlerIO.getOutputFilename(FILENAME_POPULATION));
            new NetworkWriter(this.network).write(this.controlerIO.getOutputFilename(FILENAME_NETWORK));
            new ConfigWriter(this.config).write(this.controlerIO.getOutputFilename(FILENAME_CONFIG));
            if (!unexpected && this.getConfig().vspExperimental().isWritingOutputEvents()) {
                File toFile = new File(this.controlerIO.getOutputFilename("output_events.xml.gz"));
                File fromFile = new File(this.controlerIO.getIterationFilename(this.getLastIteration(), "events.xml.gz"));
                IOUtils.copyFile(fromFile, toFile);
            }
        }
        if (unexpected) {
            log.info("S H U T D O W N   ---   unexpected shutdown request completed.");
        } else {
            log.info("S H U T D O W N   ---   regular shutdown completed.");
        }
        try {
            Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
        } catch (IllegalStateException e) {
            log.info("Cannot remove shutdown hook. " + e.getMessage());
        }
        this.shutdownHook = null;
        this.collectLogMessagesAppender = null;
        IOUtils.closeOutputDirLogging();
    }
