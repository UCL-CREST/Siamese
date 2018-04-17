    private void updateFile(BackUpInfoFile fileInfo, ZipOutputStream zos) {
        try {
            int bytesIn = 0;
            File file = new File(fileInfo.getPath() + fileInfo.getName() + "." + fileInfo.getType());
            FileInputStream fis = new FileInputStream(file);
            ZipEntry anEntry = new ZipEntry(fileInfo.getId());
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(BUFFER)) != -1) {
                zos.write(BUFFER, 0, bytesIn);
            }
            fileInfo.setSize(file.length());
            fis.close();
        } catch (IOException e) {
            throw new BackupException(e.getMessage());
        }
    }
