    public static void unzipFile(File zipFile, File destFile, boolean removeSrcFile) throws Exception {
        ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipentry = zipinputstream.getNextEntry();
        int BUFFER_SIZE = 4096;
        while (zipentry != null) {
            String entryName = zipentry.getName();
            log.info("<<<<<< ZipUtility.unzipFile - Extracting: " + zipentry.getName());
            File newFile = null;
            if (destFile.isDirectory()) newFile = new File(destFile, entryName); else newFile = destFile;
            if (zipentry.isDirectory() || entryName.endsWith(File.separator + ".")) {
                newFile.mkdirs();
            } else {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                byte[] bufferArray = buffer.array();
                FileUtilities.createDirectory(newFile.getParentFile());
                FileChannel destinationChannel = new FileOutputStream(newFile).getChannel();
                while (true) {
                    buffer.clear();
                    int lim = zipinputstream.read(bufferArray);
                    if (lim == -1) break;
                    buffer.flip();
                    buffer.limit(lim);
                    destinationChannel.write(buffer);
                }
                destinationChannel.close();
                zipinputstream.closeEntry();
            }
            zipentry = zipinputstream.getNextEntry();
        }
        zipinputstream.close();
        if (removeSrcFile) {
            if (zipFile.exists()) zipFile.delete();
        }
    }
