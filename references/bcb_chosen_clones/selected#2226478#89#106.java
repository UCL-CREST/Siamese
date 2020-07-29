    private void zipFile(String filePath, ZipOutputStream zos) {
        int bytesIn = 0;
        try {
            counter += 1;
            File file = new File(filePath);
            String id = Long.toString(System.nanoTime());
            fileGroup.getFileList().add(createBackUpFile(id, file));
            FileInputStream fis = new FileInputStream(file);
            ZipEntry anEntry = new ZipEntry(id);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(BUFFER)) != -1) {
                zos.write(BUFFER, 0, bytesIn);
            }
            fis.close();
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }
