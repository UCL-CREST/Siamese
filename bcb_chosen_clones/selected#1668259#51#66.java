    @Override
    protected RequestLogHandler createRequestLogHandler() {
        try {
            File logbackConf = File.createTempFile("logback-access", ".xml");
            IOUtils.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("logback-access.xml"), new FileOutputStream(logbackConf));
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            RequestLogImpl requestLog = new RequestLogImpl();
            requestLog.setFileName(logbackConf.getPath());
            requestLogHandler.setRequestLog(requestLog);
        } catch (FileNotFoundException e) {
            log.error("Could not create request log handler", e);
        } catch (IOException e) {
            log.error("Could not create request log handler", e);
        }
        return null;
    }
