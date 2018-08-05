    private void addToZip(ZipOutputStream zos, String path, File dir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                addToZip(zos, path + file.getName() + "/", file);
            } else {
                String relative = path + file.getName();
                ZipEntry entry = new ZipEntry(relative);
                zos.putNextEntry(entry);
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) > -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            }
        }
    }
