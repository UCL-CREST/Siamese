    public void createCachePackage(OutputStream output) {
        int bytesRead = 0;
        byte[] transferBuffer = new byte[BUFFER_SIZE];
        try {
            ZipOutputStream zos = new ZipOutputStream(output);
            for (File file : cacheFolder.listFiles()) {
                if (file.getName().contains("DS_Store") || file.getName().endsWith(".svn")) continue;
                ZipEntry fileEntry = new ZipEntry(file.getName());
                zos.putNextEntry(fileEntry);
                InputStream bis = new FileInputStream(file);
                while ((bytesRead = bis.read(transferBuffer)) != -1) {
                    zos.write(transferBuffer, 0, bytesRead);
                }
                bis.close();
                zos.closeEntry();
                logger.log(Level.INFO, "Wrote {0}, size is {1}", new Object[] { file.toString(), fileEntry.getSize() });
            }
            zos.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Caught an IOException while making cache package: {0}", ex.getMessage());
        }
    }
