    private void copy(File fromFile, File toFile) throws IOException {
        String fromFileName = fromFile.getName();
        File tmpFile = new File(fromFileName);
        String toFileName = toFile.getName();
        if (!tmpFile.exists()) throw new IOException("FileCopy: " + "no such source file: " + fromFileName);
        if (!tmpFile.isFile()) throw new IOException("FileCopy: " + "can't copy directory: " + fromFileName);
        if (!tmpFile.canRead()) throw new IOException("FileCopy: " + "source file is unreadable: " + fromFileName);
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(tmpFile);
            File toF = new File(toFile.getCanonicalPath());
            if (!toF.exists()) ;
            toF.createNewFile();
            if (!SBCMain.DEBUG_MODE) to = new FileOutputStream(toFile); else to = new FileOutputStream(toF);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = from.read(buffer)) != -1) to.write(buffer, 0, bytesRead);
        } finally {
            if (from != null) try {
                from.close();
            } catch (IOException e) {
                ;
            }
            if (to != null) try {
                to.close();
            } catch (IOException e) {
                ;
            }
        }
    }
