    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = sortedRegionScoreTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < sortedRegionScoreTexts.length; i++) {
                int currentStart = sortedRegionScoreTexts[i].start;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < sortedRegionScoreTexts.length; i++) {
                int diff = sortedRegionScoreTexts[i].stop - sortedRegionScoreTexts[i].start;
                if (diff > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) fileType = USeqUtilities.SHORT; else fileType = USeqUtilities.INT;
        if (useShortLength) fileType = fileType + USeqUtilities.SHORT; else fileType = fileType + USeqUtilities.INT;
        fileType = fileType + USeqUtilities.FLOAT + USeqUtilities.TEXT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedRegionScoreTexts[0].start);
            int bp = sortedRegionScoreTexts[0].start;
            if (useShortBeginning) {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionScoreTexts[0].stop - sortedRegionScoreTexts[0].start);
                    dos.writeFloat(sortedRegionScoreTexts[0].score);
                    dos.writeUTF(sortedRegionScoreTexts[0].text);
                    for (int i = 1; i < sortedRegionScoreTexts.length; i++) {
                        int currentStart = sortedRegionScoreTexts[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeInt(sortedRegionScoreTexts[i].stop - sortedRegionScoreTexts[i].start);
                        dos.writeFloat(sortedRegionScoreTexts[i].score);
                        dos.writeUTF(sortedRegionScoreTexts[i].text);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionScoreTexts[0].stop - sortedRegionScoreTexts[0].start - 32768));
                    dos.writeFloat(sortedRegionScoreTexts[0].score);
                    dos.writeUTF(sortedRegionScoreTexts[0].text);
                    for (int i = 1; i < sortedRegionScoreTexts.length; i++) {
                        int currentStart = sortedRegionScoreTexts[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeShort((short) (sortedRegionScoreTexts[i].stop - sortedRegionScoreTexts[i].start - 32768));
                        dos.writeFloat(sortedRegionScoreTexts[i].score);
                        dos.writeUTF(sortedRegionScoreTexts[i].text);
                        bp = currentStart;
                    }
                }
            } else {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionScoreTexts[0].stop - sortedRegionScoreTexts[0].start);
                    dos.writeFloat(sortedRegionScoreTexts[0].score);
                    dos.writeUTF(sortedRegionScoreTexts[0].text);
                    for (int i = 1; i < sortedRegionScoreTexts.length; i++) {
                        int currentStart = sortedRegionScoreTexts[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeInt(sortedRegionScoreTexts[i].stop - sortedRegionScoreTexts[i].start);
                        dos.writeFloat(sortedRegionScoreTexts[i].score);
                        dos.writeUTF(sortedRegionScoreTexts[i].text);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionScoreTexts[0].stop - sortedRegionScoreTexts[0].start - 32768));
                    dos.writeFloat(sortedRegionScoreTexts[0].score);
                    dos.writeUTF(sortedRegionScoreTexts[0].text);
                    for (int i = 1; i < sortedRegionScoreTexts.length; i++) {
                        int currentStart = sortedRegionScoreTexts[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeShort((short) (sortedRegionScoreTexts[i].stop - sortedRegionScoreTexts[i].start - 32768));
                        dos.writeFloat(sortedRegionScoreTexts[i].score);
                        dos.writeUTF(sortedRegionScoreTexts[i].text);
                        bp = currentStart;
                    }
                }
            }
            out.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(dos);
        }
    }
