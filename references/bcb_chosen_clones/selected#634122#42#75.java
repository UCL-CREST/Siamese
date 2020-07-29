    public void copyFile(File sourceFile, String toDir, boolean create, boolean overwrite) throws FileNotFoundException, IOException {
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        File toFile = new File(toDir);
        if (create && !toFile.exists()) toFile.mkdirs();
        if (toFile.exists()) {
            File destFile = new File(toDir + "/" + sourceFile.getName());
            try {
                if (!destFile.exists() || overwrite) {
                    source = new FileInputStream(sourceFile);
                    destination = new FileOutputStream(destFile);
                    buffer = new byte[1024];
                    while (true) {
                        bytes_read = source.read(buffer);
                        if (bytes_read == -1) break;
                        destination.write(buffer, 0, bytes_read);
                    }
                }
            } catch (Exception exx) {
                exx.printStackTrace();
            } finally {
                if (source != null) try {
                    source.close();
                } catch (IOException e) {
                }
                if (destination != null) try {
                    destination.close();
                } catch (IOException e) {
                }
            }
        }
    }
