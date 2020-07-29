    protected JarEntry createEntry(final Opener opener, String path) {
        final JarEntry entry = new JarEntry(path);
        if (this.conf.compress()) {
            entry.setMethod(ZipEntry.DEFLATED);
        } else {
            entry.setMethod(ZipEntry.STORED);
            entry(opener, new $entry() {

                public void invoke(InputStream in) throws Exception {
                    long size = 0;
                    CRC32 crc = new CRC32();
                    byte[] buf = new byte[4096];
                    while (true) {
                        int len = in.read(buf, 0, 4096);
                        if (-1 < len) {
                            crc.update(buf, 0, len);
                            size += len;
                        } else {
                            break;
                        }
                    }
                    entry.setSize(size);
                    entry.setCrc(crc.getValue());
                }
            });
        }
        return entry;
    }
