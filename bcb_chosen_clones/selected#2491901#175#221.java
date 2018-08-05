    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = sortedPositions[0].position;
            useShort = true;
            for (int i = 1; i < sortedPositions.length; i++) {
                int currentStart = sortedPositions[i].position;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShort = false;
                    break;
                }
                bp = currentStart;
            }
        }
        String fileType;
        if (useShort) fileType = USeqUtilities.SHORT; else fileType = USeqUtilities.INT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedPositions[0].position);
            if (useShort) {
                int bp = sortedPositions[0].position;
                for (int i = 1; i < sortedPositions.length; i++) {
                    int currentStart = sortedPositions[i].position;
                    int diff = currentStart - bp - 32768;
                    dos.writeShort((short) (diff));
                    bp = currentStart;
                }
            } else {
                int bp = sortedPositions[0].position;
                for (int i = 1; i < sortedPositions.length; i++) {
                    int currentStart = sortedPositions[i].position;
                    int diff = currentStart - bp;
                    dos.writeInt(diff);
                    bp = currentStart;
                }
            }
            out.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(dos);
        }
    }
