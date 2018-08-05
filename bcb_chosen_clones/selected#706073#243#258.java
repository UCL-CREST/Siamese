    private void writeManifestEntry(File f, JarOutputStream out) {
        byte[] buffer = new byte[BUFFERSIZE];
        int bytes_read;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f), BUFFERSIZE);
            String en = "META-INF" + "/" + "MANIFEST.MF";
            out.putNextEntry(new ZipEntry(en));
            while ((bytes_read = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
            in.close();
            out.closeEntry();
        } catch (Exception e) {
            System.out.println("[writeManifestEntry(), JarWriter] ERROR\n" + e);
        }
    }
