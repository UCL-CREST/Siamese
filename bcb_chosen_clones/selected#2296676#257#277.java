    static void linkBlocks(File from, File to, int oldLV) throws IOException {
        if (!from.isDirectory()) {
            if (from.getName().startsWith(COPY_FILE_PREFIX)) {
                IOUtils.copyBytes(new FileInputStream(from), new FileOutputStream(to), 16 * 1024, true);
            } else {
                if (oldLV >= PRE_GENERATIONSTAMP_LAYOUT_VERSION) {
                    to = new File(convertMetatadataFileName(to.getAbsolutePath()));
                }
                HardLink.createHardLink(from, to);
            }
            return;
        }
        if (!to.mkdir()) throw new IOException("Cannot create directory " + to);
        String[] blockNames = from.list(new java.io.FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.startsWith(BLOCK_SUBDIR_PREFIX) || name.startsWith(BLOCK_FILE_PREFIX) || name.startsWith(COPY_FILE_PREFIX);
            }
        });
        for (int i = 0; i < blockNames.length; i++) linkBlocks(new File(from, blockNames[i]), new File(to, blockNames[i]), oldLV);
    }
