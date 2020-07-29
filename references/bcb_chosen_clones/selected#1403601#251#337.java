    private int[] initExistingFile() throws IOException {
        final int blockCapacity = this.blockFile.getBlockCapacity();
        if (blockCapacity == 0) return new int[0];
        long checksum;
        RandomAccessFile invalidDataFile = null;
        boolean invalidDataFileError = false;
        byte[] invalidData;
        boolean hasInvalidBlocks = false;
        final StringBuffer invalidBlocksBuffer = new StringBuffer();
        final IntList occupiedBlocks = new IntList(Math.max(blockCapacity / 5, 100), Math.max(blockCapacity / 5, 100));
        final IntList unreferencedBlocks = new IntList(Math.max(blockCapacity / 5, 100), Math.max(blockCapacity / 5, 100));
        int dataStartBlocksAddIndex;
        int[] initialDataChainsArray = new int[blockCapacity];
        Arrays.fill(initialDataChainsArray, NULL_LINK);
        final IntList initialDataChains = new IntList(initialDataChainsArray);
        final byte[] blockHeaderBytes = new byte[BLOCK_HEADER_SIZE * blockCapacity];
        int bufferBlocks = blockCapacity / 10;
        for (int i = 1; ((bufferBlocks * this.blockSize) > (10 * 1024 * 1024)); i++) bufferBlocks = blockCapacity / (10 * i);
        if (bufferBlocks < 1) bufferBlocks = 1;
        final byte[] buffer = new byte[bufferBlocks * this.blockSize];
        int blockOffset = 0;
        int readSize;
        int readBlocks;
        this.blockFile.setFilePointer(this.blockFile.getBlockStartFP(0));
        while (blockOffset < blockCapacity) {
            readSize = Math.min(buffer.length, (blockCapacity - blockOffset) * this.blockSize);
            readBlocks = (readSize / this.blockSize);
            this.blockFile.read(buffer, 0, readSize);
            for (int i = 0; i < readBlocks; i++) {
                System.arraycopy(buffer, i * this.blockSize, blockHeaderBytes, (blockOffset + i) * BLOCK_HEADER_SIZE, BLOCK_HEADER_SIZE);
            }
            blockOffset += readBlocks;
        }
        int offset = 0;
        for (int i = 0; i < blockCapacity; i++) {
            offset = i * BLOCK_HEADER_SIZE;
            checksum = this.parseLongFromByteArray(blockHeaderBytes, BLOCK_HEADER_CHECKSUM_OFFSET + offset);
            CRC32 crc32 = new CRC32();
            crc32.update(blockHeaderBytes, offset, BLOCK_HEADER_CHECKSUM_OFFSET);
            if (checksum == crc32.getValue()) {
                if (this.parseBooleanFromByteArray(blockHeaderBytes, BLOCK_HEADER_OCCUPIED_FLAG_OFFSET + offset)) {
                    occupiedBlocks.addSorted(i);
                    if (this.parseBooleanFromByteArray(blockHeaderBytes, BLOCK_HEADER_START_BLOCK_FLAG_OFFSET + offset)) {
                        dataStartBlocksAddIndex = this.dataStartBlocks.addSorted(i);
                        this.itemDataSizes.add(dataStartBlocksAddIndex, this.getBlockDataLength(blockHeaderBytes, offset));
                        this.dataChainsArray.add(dataStartBlocksAddIndex, null);
                    } else {
                        unreferencedBlocks.addSorted(i);
                    }
                    initialDataChains.set(i, this.parseIntFromByteArray(blockHeaderBytes, BLOCK_HEADER_NEXT_BLOCK_POINTER_OFFSET + offset));
                }
            } else if ((!invalidDataFileError) && (!this.readOnlyMode)) {
                String logString = "{" + i + ": expected checksum: " + crc32.getValue() + ", read checksum: " + checksum + ", size: " + this.parseIntFromByteArray(blockHeaderBytes, BLOCK_HEADER_DATA_LENGTH_OFFSET + offset) + "}";
                if (hasInvalidBlocks) invalidBlocksBuffer.append(", ");
                invalidBlocksBuffer.append(logString);
                hasInvalidBlocks = true;
                if (invalidDataFile == null) {
                    try {
                        invalidDataFile = createInvalidDataFile();
                    } catch (IOException ioe) {
                        JServerUtilities.logWarning(fullName, "Error creating file for invalid data (" + fileName + ".invalid" + ")!", ioe);
                        invalidDataFileError = true;
                        continue;
                    }
                }
                invalidData = this.blockFile.readBlock(i);
                invalidDataFile.write(invalidData);
                invalidData = null;
                this.blockFile.writePartialBlock(i, 0, this.deallocatedBlockHeaderBuffer, 0, this.deallocatedBlockHeaderBuffer.length);
            }
        }
        if (hasInvalidBlocks) {
            JServerUtilities.logWarning(fullName, "Invalid blocks detected while checking file consistency. The following blocks were invalid: " + invalidBlocksBuffer.toString() + ".");
        }
        this.checkDataChains(initialDataChains, occupiedBlocks, unreferencedBlocks);
        if (!this.readOnlyMode) {
            int index;
            for (int i = 0; i < unreferencedBlocks.size(); i++) {
                index = unreferencedBlocks.get(i);
                if (index >= 0) {
                    occupiedBlocks.removeValueSorted(index);
                    this.blockFile.writePartialBlock(index, 0, this.deallocatedBlockHeaderBuffer, 0, this.deallocatedBlockHeaderBuffer.length);
                }
            }
        }
        return occupiedBlocks.toArray();
    }
