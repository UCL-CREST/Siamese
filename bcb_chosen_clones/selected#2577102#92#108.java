    private void unzipData(ZipFile zipfile, ZipEntry entry) {
        if (entry.getName().equals("backUpExternalInfo.out")) {
            File outputFile = new File("temp", entry.getName());
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            try {
                BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                throw new BackupException(e.getMessage());
            }
        }
    }
