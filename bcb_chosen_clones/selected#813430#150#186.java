    public static void zipLogFile(String logFilePath) {
        try {
            File logFile = new File(logFilePath);
            if (logFile.exists() == false) return;
            String zipFilePath = logFilePath + ".zip";
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                LogHelper.writeErrorFormat("logZipFileExistsError", zipFilePath);
                if (zipFile.delete() == false) {
                    LogHelper.writeErrorFormat("logZipFileDeleteError", zipFilePath);
                    return;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(out);
            FileInputStream fileInputStream = new FileInputStream(logFile);
            BufferedInputStream in = new BufferedInputStream(fileInputStream);
            ZipEntry entry = new ZipEntry(logFile.getName());
            zipOutputStream.putNextEntry(entry);
            int bufferSize = 1024;
            if (logFile.length() < Integer.MAX_VALUE) bufferSize = (int) logFile.length(); else bufferSize = Integer.MAX_VALUE;
            byte[] buffer = new byte[bufferSize];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, length);
            }
            in.close();
            zipOutputStream.close();
            if (logFile.delete() == false) {
                LogHelper.writeErrorFormat("fileNotRemoved", logFilePath);
            }
        } catch (Exception e) {
            LogHelper.writeErrorFormat("logZippingError", logFilePath, e.getLocalizedMessage());
            LogHelper.writeStackTrace(e.getStackTrace());
        }
    }
