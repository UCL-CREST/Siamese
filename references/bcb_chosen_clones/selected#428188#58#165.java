    public Long split(File targetDirectory, String prefix, long maxUnitBases, long maxUnitEntries) throws Exception {
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) throw new Exception("Could not create target directory " + targetDirectory.getAbsolutePath());
        }
        if (!size.isUnknown() && size.getBases() < maxUnitBases && (maxUnitEntries <= 0 || size.getEntries() < maxUnitEntries)) {
            FileInputStream fis = new FileInputStream(this);
            FileChannel fci = fis.getChannel();
            FileOutputStream fos = new FileOutputStream(new File(targetDirectory, prefix + "_0" + ".fasta"));
            FileChannel fco = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(64000);
            while (fci.read(buffer) > 0) {
                buffer.flip();
                fco.write(buffer);
                buffer.clear();
            }
            fci.close();
            fco.close();
            return (long) 1;
        } else {
            long currentBasesCount = 0;
            long currentEntriesCount = 0;
            int targetCount = 0;
            FileChannel fastaChannel = new FileInputStream(this).getChannel();
            int totalSeqCount = 0;
            long totalResiduesCount = 0;
            try {
                long prevTime = System.currentTimeMillis();
                long fastaFileSize = this.length();
                long fastaFileReadOffset = 0L;
                long partitionStartOffset = 0L;
                final int bufferSize = 1024 * 1024;
                ByteBuffer fastaBuffer = ByteBuffer.allocateDirect(bufferSize);
                int fastaReadState = FASTAFileTokenizer.UNKNOWN;
                for (; fastaFileReadOffset < fastaFileSize; ) {
                    long nBytes = fastaChannel.read(fastaBuffer);
                    if (nBytes <= 0) {
                        fastaBuffer.limit(0);
                        break;
                    } else {
                        fastaBuffer.flip();
                        fastaFileReadOffset += nBytes;
                    }
                    for (; ; ) {
                        if (!fastaBuffer.hasRemaining()) {
                            fastaBuffer.clear();
                            break;
                        }
                        int b = fastaBuffer.get();
                        if (b == '\r') {
                        } else if (b == '\n') {
                            if (fastaReadState == FASTAFileTokenizer.DEFLINE) {
                                fastaReadState = FASTAFileTokenizer.SEQUENCELINE;
                            }
                        } else if (b == '>') {
                            if (fastaReadState == FASTAFileTokenizer.UNKNOWN) {
                                fastaReadState = FASTAFileTokenizer.STARTDEFLINE;
                            } else if (fastaReadState == FASTAFileTokenizer.SEQUENCELINE) {
                                fastaReadState = FASTAFileTokenizer.STARTDEFLINE;
                            }
                            if (fastaReadState == FASTAFileTokenizer.STARTDEFLINE) {
                                if (currentBasesCount >= maxUnitBases || maxUnitEntries > 0 && currentEntriesCount >= maxUnitEntries) {
                                    fastaBuffer.position(fastaBuffer.position() - 1);
                                    long currentTime = System.currentTimeMillis();
                                    System.out.println(new java.util.Date() + " Partition " + targetCount + " containing " + currentEntriesCount + " sequences and " + currentBasesCount + " residues ends at " + (fastaFileReadOffset - fastaBuffer.remaining()) + " and was created in " + (currentTime - prevTime) + " ms");
                                    prevTime = currentTime;
                                    long partitionEndOffset = fastaFileReadOffset - fastaBuffer.remaining();
                                    FileChannel partitionChannel = new FileOutputStream(new File(targetDirectory, prefix + "_" + targetCount + ".fasta")).getChannel();
                                    nBytes = fastaChannel.transferTo(partitionStartOffset, partitionEndOffset - partitionStartOffset, partitionChannel);
                                    partitionChannel.force(true);
                                    partitionChannel.close();
                                    targetCount++;
                                    partitionStartOffset += nBytes;
                                    currentBasesCount = 0;
                                    currentEntriesCount = 0;
                                    fastaReadState = FASTAFileTokenizer.UNKNOWN;
                                } else {
                                    fastaReadState = FASTAFileTokenizer.DEFLINE;
                                    currentEntriesCount++;
                                }
                                totalSeqCount++;
                            }
                        } else {
                            if (fastaReadState == FASTAFileTokenizer.SEQUENCELINE) {
                                totalResiduesCount++;
                                currentBasesCount++;
                            }
                        }
                    }
                }
                if (partitionStartOffset < fastaFileSize) {
                    long currentTime = System.currentTimeMillis();
                    System.out.println(new java.util.Date() + " Partition " + targetCount + " containing " + currentEntriesCount + " sequences and " + currentBasesCount + " residues ends at " + (fastaFileSize) + " and was created in " + (currentTime - prevTime) + " ms");
                    FileChannel partitionChannel = new FileOutputStream(new File(targetDirectory, prefix + "_" + targetCount + ".fasta")).getChannel();
                    fastaChannel.transferTo(partitionStartOffset, fastaFileSize - partitionStartOffset, partitionChannel);
                    partitionChannel.force(true);
                    partitionChannel.close();
                    targetCount++;
                }
                if (size.isUnknown()) {
                    size.setBases(totalResiduesCount);
                    size.setEntries(totalSeqCount);
                }
            } finally {
                fastaChannel.close();
            }
            return (long) targetCount;
        }
    }
