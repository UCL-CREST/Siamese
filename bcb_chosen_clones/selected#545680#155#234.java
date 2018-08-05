    private void fileMaker() {
        try {
            long allData = 0;
            double a = 10;
            int range = 0;
            int blockLength = 0;
            File newFile = new File(mfr.getFilename() + ".part");
            if (newFile.exists()) {
                newFile.delete();
            }
            ArrayList<DataRange> rangeList = null;
            byte[] data = null;
            newFile.createNewFile();
            ByteBuffer buffer = ByteBuffer.allocate(mfr.getBlocksize());
            FileChannel rChannel = new FileInputStream(inputFileName).getChannel();
            FileChannel wChannel = new FileOutputStream(newFile, true).getChannel();
            System.out.println();
            System.out.print("File completion: ");
            System.out.print("|----------|");
            openConnection();
            http.getResponseHeader();
            for (int i = 0; i < fileMap.length; i++) {
                fileOffset = fileMap[i];
                if (fileOffset != -1) {
                    rChannel.read(buffer, fileOffset);
                    buffer.flip();
                    wChannel.write(buffer);
                    buffer.clear();
                } else {
                    if (!rangeQueue) {
                        rangeList = rangeLookUp(i);
                        range = rangeList.size();
                        openConnection();
                        http.setRangesRequest(rangeList);
                        http.sendRequest();
                        http.getResponseHeader();
                        data = http.getResponseBody(mfr.getBlocksize());
                        allData += http.getAllTransferedDataLength();
                    }
                    if ((i * mfr.getBlocksize() + mfr.getBlocksize()) < mfr.getLength()) {
                        blockLength = mfr.getBlocksize();
                    } else {
                        blockLength = (int) ((int) (mfr.getBlocksize()) + (mfr.getLength() - (i * mfr.getBlocksize() + mfr.getBlocksize())));
                    }
                    buffer.put(data, (range - rangeList.size()) * mfr.getBlocksize(), blockLength);
                    buffer.flip();
                    wChannel.write(buffer);
                    buffer.clear();
                    rangeList.remove(0);
                    if (rangeList.isEmpty()) {
                        rangeQueue = false;
                    }
                }
                if ((((double) i / ((double) fileMap.length - 1)) * 100) >= a) {
                    progressBar(((double) i / ((double) fileMap.length - 1)) * 100);
                    a += 10;
                }
            }
            newFile.setLastModified(getMTime());
            sha = new SHA1(newFile);
            if (sha.SHA1sum().equals(mfr.getSha1())) {
                System.out.println("\nverifying download...checksum matches OK");
                System.out.println("used " + (mfr.getLength() - (mfr.getBlocksize() * missing)) + " " + "local, fetched " + (mfr.getBlocksize() * missing));
                new File(mfr.getFilename()).renameTo(new File(mfr.getFilename() + ".zs-old"));
                newFile.renameTo(new File(mfr.getFilename()));
                allData += mfr.getLengthOfMetafile();
                System.out.println("really downloaded " + allData);
                double overhead = ((double) (allData - (mfr.getBlocksize() * missing)) / ((double) (mfr.getBlocksize() * missing))) * 100;
                System.out.println("overhead: " + df.format(overhead) + "%");
            } else {
                System.out.println("\nverifying download...checksum don't match");
                System.out.println("Deleting temporary file");
                newFile.delete();
                System.exit(1);
            }
        } catch (IOException ex) {
            System.out.println("Can't read or write, check your permissions.");
            System.exit(1);
        }
    }
