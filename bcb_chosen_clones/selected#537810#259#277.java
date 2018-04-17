    public static boolean clearFiles(File srcDir, Boolean isHidden) {
        if (null == srcDir || !srcDir.exists() || !srcDir.isDirectory()) {
            log.error("srcDir(" + srcDir + ") is invalid !");
            return false;
        }
        for (File f : listFiles(srcDir, isHidden)) {
            if (!f.delete()) {
                log.error("Failed in deteting file(" + f + ") !");
                return false;
            }
            if (DEBUG) {
                log.debug("Succeeded in deteting file(" + f + ") !");
            }
        }
        if (DEBUG) {
            log.debug("succeeded: dir=" + srcDir + "; isHidden=" + isHidden);
        }
        return true;
    }
