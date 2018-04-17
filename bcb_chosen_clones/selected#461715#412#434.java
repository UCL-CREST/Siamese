    private void store(ZipInputStream in, ZipOutputStream bos, ZipEntry e) {
        try {
            bentry.reset();
            int bb;
            while (true) {
                bb = in.read();
                if (bb < 0) {
                    break;
                } else {
                    bentry.write(bb);
                }
            }
            bentry.close();
            e = new ZipEntry(e);
            e.setSize(bentry.size());
            e.setCompressedSize(-1);
            bos.putNextEntry(e);
            bentry.writeTo(bos);
            bos.closeEntry();
        } catch (IOException ex) {
            throw new UpdateIOException("Cannot store " + e.getName(), ex);
        }
    }
