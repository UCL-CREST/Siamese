    private void copyFile(File from, File to) throws IOException {
        FileUtils.ensureParentDirectoryExists(to);
        byte[] buffer = new byte[1024];
        int read;
        FileInputStream is = new FileInputStream(from);
        FileOutputStream os = new FileOutputStream(to);
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        is.close();
        os.close();
    }
