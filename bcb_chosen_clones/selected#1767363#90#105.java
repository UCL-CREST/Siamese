    public void testLogWritableNonExistent() throws Exception {
        File logDir = new File("testLogWritableNonExistent");
        if (logDir.exists()) {
            for (File f : logDir.listFiles()) {
                f.delete();
            }
            logDir.delete();
        }
        assertFalse("Log dir does not exist", logDir.exists());
        DiskLog log = new DiskLog();
        log.setReadOnly(false);
        log.setLogDir(logDir.getAbsolutePath());
        log.prepare();
        log.release();
        assertTrue("Log dir exists", logDir.exists() && logDir.isDirectory());
    }
