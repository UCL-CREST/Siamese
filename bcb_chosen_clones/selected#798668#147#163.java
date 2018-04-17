    private void zipImages(File zipFile, File srcDir) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            ZipEntry entry = new ZipEntry(files[i].getName());
            out.putNextEntry(entry);
            FileInputStream in = new FileInputStream(files[i]);
            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            files[i].delete();
        }
        out.close();
    }
