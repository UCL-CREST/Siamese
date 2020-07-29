    private static final void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws IOException {
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) {
                return;
            }
            File[] fileArray = sourceFile.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos);
            }
        } else {
            BufferedInputStream bis = null;
            String sFilePath = sourceFile.getPath();
            String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            ZipEntry zentry = new ZipEntry(zipEntryName);
            zentry.setTime(sourceFile.lastModified());
            zos.putNextEntry(zentry);
            byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                zos.write(buffer, 0, cnt);
            }
            zos.closeEntry();
            bis.close();
        }
    }
