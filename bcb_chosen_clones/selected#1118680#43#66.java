    private void addZipEntry(final int bufsize, final ZipOutputStream zos, final byte[] input_buffer, final File file) throws IOException {
        log.debug("Adding zip entry for file {}", file);
        if (file.exists() && file.isFile()) {
            final ZipEntry zip_entry = new ZipEntry(file.getName());
            if (this.zipEntries.contains(file.getName())) {
                log.info("Skipping duplicate zip entry {}", file.getName());
                return;
            } else {
                this.zipEntries.add(file.getName());
            }
            zos.putNextEntry(zip_entry);
            final FileInputStream in = new FileInputStream(file);
            final BufferedInputStream source = new BufferedInputStream(in, bufsize);
            int len = 0;
            while ((len = source.read(input_buffer, 0, bufsize)) != -1) {
                zos.write(input_buffer, 0, len);
            }
            zos.flush();
            source.close();
            zos.closeEntry();
        } else {
            log.warn("Skipping nonexistant file or directory {}", file);
        }
    }
