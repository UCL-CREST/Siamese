    private void zipFiles(Set<File> files, File zipFile) throws IOException {
        if (files.isEmpty()) {
            log.warn("No files to zip.");
        } else {
            try {
                BufferedInputStream origin = null;
                FileOutputStream dest = new FileOutputStream(zipFile);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                byte data[] = new byte[BUFFER];
                for (File f : files) {
                    log.debug("Adding file " + f + " to archive");
                    FileInputStream fi = new FileInputStream(f);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(f.getName());
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
                out.finish();
                out.close();
            } catch (IOException e) {
                log.error("IOException while zipping files: " + files);
                throw e;
            }
        }
    }
