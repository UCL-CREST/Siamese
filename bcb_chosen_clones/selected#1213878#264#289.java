    private void zip(String session_id, FileFormat file_format, File file) {
        ZipOutputStream zos = null;
        try {
            File zip = new File(Config.getTmpPath() + session_id + file_format.getFileType() + FileFormat.ZIP.getFileType());
            if (zip.exists()) {
                zip.delete();
            }
            zip.createNewFile();
            zos = new ZipOutputStream(new FileOutputStream(zip));
            zos.putNextEntry(new ZipEntry(session_id + file_format.getFileType()));
            zos.write(FileUtils.readFileToByteArray(file));
            zos.closeEntry();
            zos.flush();
        } catch (Exception e) {
            Log.writeErrorLog(WriteFile.class, e.getMessage(), e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    Log.writeErrorLog(WriteFile.class, e.getMessage(), e);
                }
            }
            file.delete();
        }
    }
