    private void zipFile(File file) throws IOException, FileNotFoundException {
        if (out == null) {
            out = new ZipOutputStream(new FileOutputStream(backup.getSavePath() + File.separator + fileFormat.format(backup.getLastRunDate()) + ".a" + archiveNumber + ".zip"));
        }
        FileInputStream in = new FileInputStream(file);
        ZipEntry entry = new ZipEntry(getRelativeFilePath(file.getAbsolutePath()));
        out.putNextEntry(entry);
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        totalBytes += entry.getCompressedSize();
        in.close();
    }
