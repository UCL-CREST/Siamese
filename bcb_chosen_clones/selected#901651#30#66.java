    private FileLog(LOG_LEVEL displayLogLevel, LOG_LEVEL logLevel, String logPath) {
        this.logLevel = logLevel;
        this.displayLogLevel = displayLogLevel;
        if (null != logPath) {
            logFile = new File(logPath, "current.log");
            log(LOG_LEVEL.DEBUG, "FileLog", "Initialising logfile " + logFile.getAbsolutePath() + " .");
            try {
                if (logFile.exists()) {
                    if (!logFile.renameTo(new File(logPath, System.currentTimeMillis() + ".log"))) {
                        File newFile = new File(logPath, System.currentTimeMillis() + ".log");
                        if (newFile.exists()) {
                            log(LOG_LEVEL.WARN, "FileLog", "The file (" + newFile.getAbsolutePath() + newFile.getName() + ") already exists, will overwrite it.");
                            newFile.delete();
                        }
                        newFile.createNewFile();
                        FileInputStream inStream = new FileInputStream(logFile);
                        FileOutputStream outStream = new FileOutputStream(newFile);
                        byte buffer[] = null;
                        int offSet = 0;
                        while (inStream.read(buffer, offSet, 2048) != -1) {
                            outStream.write(buffer);
                            offSet += 2048;
                        }
                        inStream.close();
                        outStream.close();
                        logFile.delete();
                        logFile = new File(logPath, "current.log");
                    }
                }
                logFile.createNewFile();
            } catch (IOException e) {
                logFile = null;
            }
        } else {
            logFile = null;
        }
    }
