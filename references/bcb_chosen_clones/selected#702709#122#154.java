    protected void createFile(File sourceActionDirectory, File destinationActionDirectory, LinkedList<String> segments) throws DuplicateActionFileException {
        File currentSrcDir = sourceActionDirectory;
        File currentDestDir = destinationActionDirectory;
        String segment = "";
        for (int i = 0; i < segments.size() - 1; i++) {
            segment = segments.get(i);
            currentSrcDir = new File(currentSrcDir, segment);
            currentDestDir = new File(currentDestDir, segment);
        }
        if (currentSrcDir != null && currentDestDir != null) {
            File srcFile = new File(currentSrcDir, segments.getLast());
            if (srcFile.exists()) {
                File destFile = new File(currentDestDir, segments.getLast());
                if (destFile.exists()) {
                    throw new DuplicateActionFileException(srcFile.toURI().toASCIIString());
                }
                try {
                    FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
                    FileChannel destChannel = new FileOutputStream(destFile).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate((int) srcChannel.size());
                    while (srcChannel.position() < srcChannel.size()) {
                        srcChannel.read(buffer);
                    }
                    srcChannel.close();
                    buffer.rewind();
                    destChannel.write(buffer);
                    destChannel.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
