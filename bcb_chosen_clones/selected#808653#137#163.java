    private void addFileToZip(final File file, final ZipOutputStream zipOutputStream) {
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                final ZipEntry ze = new ZipEntry(File.separator + file.getName());
                ze.setTime(file.lastModified());
                zipOutputStream.putNextEntry(ze);
                final byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) > 0) {
                    zipOutputStream.write(buf, 0, len);
                }
            } catch (final IOException ioe) {
                log.error("addFileToZip : IO failure : ", ioe);
            } finally {
                try {
                    zipOutputStream.closeEntry();
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException ioe) {
                    log.error("addFileToZip : IO close error : ", ioe);
                }
            }
        }
    }
