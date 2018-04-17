    private final void saveCopy(String source, String destination) {
        BufferedInputStream from = null;
        BufferedOutputStream to = null;
        try {
            from = new BufferedInputStream(new FileInputStream(source));
            to = new BufferedOutputStream(new FileOutputStream(destination));
            byte[] buffer = new byte[65535];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) to.write(buffer, 0, bytes_read);
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " copying file");
        }
        try {
            to.close();
            from.close();
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " closing files");
        }
        to = null;
        from = null;
    }
