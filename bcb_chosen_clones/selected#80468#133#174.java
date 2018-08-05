    private void zipFiles(List<File> files, File forumDir, int zipPartNr) {
        String zipPart = zipPartNr == 0 ? "" : "_" + zipPartNr;
        String zipName = config.getZipNameForForum(forumDir.getName()) + zipPart;
        String outFilename = forumDir.getParent() + File.separator + zipName + ".zip";
        byte[] buf = new byte[1024];
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (File file : files) {
                try {
                    in = new FileInputStream(file);
                    String relativeFileName = getRelativeFileName(file, forumDir);
                    LOG.debug("Adding " + relativeFileName + " to " + zipName + ".zip");
                    out.putNextEntry(new ZipEntry(zipName + "/" + relativeFileName));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                } catch (IOException e) {
                    LOG.error("Error adding " + file + " to " + outFilename, e);
                } finally {
                    try {
                        out.closeEntry();
                        in.close();
                    } catch (IOException e) {
                        LOG.error("Error closing FileInputStream for " + file, e);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Error creating " + outFilename, e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LOG.error("Error closing Filestream for " + outFilename, e);
            }
        }
    }
