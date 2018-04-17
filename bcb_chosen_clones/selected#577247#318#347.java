    private boolean addEntry(File theFile) {
        boolean ok = true;
        String fileName = theFile.getAbsolutePath();
        if (IO.IS_WINDOWS) {
            if (!fileName.toUpperCase().startsWith(myDirectoryName.toUpperCase() + File.separator)) {
                ok = false;
            }
        } else if (!fileName.startsWith(myDirectoryName + File.separator)) {
            ok = false;
        }
        if (!ok) {
            error("can't add entry for " + fileName + " for directory " + myDirectoryName);
        } else {
            String entryName = fileName.substring(myDirectoryNameLen).replace(File.separatorChar, '/');
            try {
                ZipEntry entry = new ZipEntry(entryName);
                myOutput.putNextEntry(entry);
                FileInputStream in = new FileInputStream(theFile);
                int numRead = 0;
                while ((numRead = in.read(myBuffer, 0, BUFFER_SIZE)) > 0) {
                    myOutput.write(myBuffer, 0, numRead);
                }
                in.close();
                myOutput.closeEntry();
            } catch (Exception e) {
                error("can't add entry: " + entryName + ", e=" + e.getMessage());
            }
        }
        return ok;
    }
