    public static File compress(String outputFilePath, List<File> files) {
        final int buffer = 2048;
        BufferedInputStream bufferedInputStream;
        File outputFile = new File(outputFilePath);
        try {
            FileUtils.touch(outputFile);
        } catch (IOException e) {
            LOGGER.error("create zip file fail.", e);
            throw new FileOperationException("create zip file fail.", e);
        }
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        FileInputStream fileInputStream;
        try {
            fileOutputStream = new FileOutputStream(outputFilePath);
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
            byte[] data = new byte[buffer];
            for (File file : files) {
                if (!file.exists()) {
                    continue;
                }
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream, buffer);
                ZipEntry entry = new ZipEntry(file.getName());
                entry.setSize(file.length());
                entry.setTime(file.lastModified());
                zipOutputStream.putNextEntry(entry);
                int count;
                while ((count = bufferedInputStream.read(data, 0, buffer)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                zipOutputStream.closeEntry();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find the output file.", e);
            throw new FileOperationException("Can't find the output file.", e);
        } catch (IOException e) {
            LOGGER.error("Can't compress file to zip archive, occured IOException", e);
            throw new FileOperationException("Can't compress file to zip archive, occured IOException", e);
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("Close zipOutputStream and fileOutputStream occur IOException", e);
            }
        }
        return outputFile;
    }
