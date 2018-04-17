    private static boolean genCustRatingFileAndMovieIndexFile(String completePath, String masterFile, String CustRatingFileName, String MovieIndexFileName) {
        try {
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + masterFile);
            FileChannel inC = new FileInputStream(inFile).getChannel();
            File outFile1 = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieIndexFileName);
            FileChannel outC1 = new FileOutputStream(outFile1, true).getChannel();
            File outFile2 = new File(completePath + fSep + "SmartGRAPE" + fSep + CustRatingFileName);
            FileChannel outC2 = new FileOutputStream(outFile2, true).getChannel();
            int fileSize = (int) inC.size();
            int totalNoDataRows = fileSize / 7;
            ByteBuffer mappedBuffer = inC.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            int startIndex = 1, count = 0;
            short currentMovie = 1;
            while (mappedBuffer.hasRemaining()) {
                count++;
                short movieName = mappedBuffer.getShort();
                int customer = mappedBuffer.getInt();
                byte rating = mappedBuffer.get();
                if (movieName != currentMovie) {
                    ByteBuffer outBuf1 = ByteBuffer.allocate(10);
                    outBuf1.putShort(currentMovie);
                    outBuf1.putInt(startIndex);
                    outBuf1.putInt(count - 1);
                    outBuf1.flip();
                    outC1.write(outBuf1);
                    currentMovie = movieName;
                    startIndex = count;
                }
                ByteBuffer outBuf2 = ByteBuffer.allocate(5);
                outBuf2.putInt(customer);
                outBuf2.put(rating);
                outBuf2.flip();
                outC2.write(outBuf2);
            }
            ByteBuffer endOfIndexFile = ByteBuffer.allocate(10);
            endOfIndexFile.putShort(currentMovie);
            endOfIndexFile.putInt(startIndex);
            endOfIndexFile.putInt(100480506);
            endOfIndexFile.flip();
            outC1.write(endOfIndexFile);
            outC1.close();
            outC2.close();
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
