    private void copyFromZip(File zipFile) throws GLMRessourceManagerException {
        if (zipFile == null) throw new GLMRessourceZIPException(1);
        if (!zipFile.exists()) throw new GLMRessourceZIPException(2);
        int len = 0;
        byte[] buffer = ContentManager.getDefaultBuffer();
        try {
            ZipInputStream zip_in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry zipEntry;
            File rootDir = null;
            while ((zipEntry = zip_in.getNextEntry()) != null) {
                File destFile = new File(tempDirectory, zipEntry.getName());
                if (rootDir == null) rootDir = destFile.getParentFile();
                if (!zipEntry.isDirectory() && destFile.getParentFile().equals(rootDir)) {
                    if (!zipEntry.getName().equals(ContentManager.IMS_MANIFEST_FILENAME)) {
                        FileOutputStream file_out = new FileOutputStream(new File(tempDirectory, zipEntry.getName()));
                        while ((len = zip_in.read(buffer)) > 0) file_out.write(buffer, 0, len);
                        file_out.flush();
                        file_out.close();
                    }
                }
            }
            zip_in.close();
        } catch (Exception e) {
            throw new GLMRessourceZIPException(3);
        }
    }
