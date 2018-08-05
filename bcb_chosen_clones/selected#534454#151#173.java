    private static boolean maybeBackupFile(File f, ZipOutputStream zip, Map origFileTimes, Map origFileSizes, Map fileTimes, Map fileSizes) {
        String name = f.getName();
        Long fileTime = new Long(f.lastModified());
        Long fileSize = new Long(f.length());
        fileTimes.put(name, fileTime);
        fileSizes.put(name, fileSize);
        Long prevTime = (Long) origFileTimes.get(name);
        Long prevSize = (Long) origFileSizes.get(name);
        if (prevTime != null && prevTime.equals(fileTime) && prevSize != null && prevSize.equals(fileSize)) return false;
        try {
            ZipEntry e = new ZipEntry(name);
            e.setTime(fileTime.longValue());
            zip.putNextEntry(e);
            byte[] buffer = new byte[4096];
            FileInputStream in = new FileInputStream(f);
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) zip.write(buffer, 0, bytesRead);
            in.close();
        } catch (IOException ioe) {
            printError("Error while backing up " + name, ioe);
        }
        return true;
    }
