    public boolean copyStoredTileTo(File targetFileName, int x, int y, int zoom, MapSource tileSource) throws IOException {
        File sourceFile = getTileFile(x, y, zoom, tileSource);
        if (!sourceFile.exists()) return false;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long sourceBytes = 0;
        long writtenBytes = 0;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFileName);
            FileChannel source = fis.getChannel();
            FileChannel destination = fos.getChannel();
            sourceBytes = source.size();
            writtenBytes = destination.transferFrom(source, 0, sourceBytes);
        } finally {
            Utilities.closeStream(fis);
            Utilities.closeStream(fos);
        }
        if (writtenBytes != sourceBytes) throw new IOException("Target file's size is not equal to the source file's size!");
        return true;
    }
