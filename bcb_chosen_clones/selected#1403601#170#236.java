    public DefaultDataFile(String fullName, int blockSize, int initialCapacity, boolean useDataChecksum, DataIO dataIO, BlockFileFactory blockFileFactory, BlockAllocatorFactory blockAllocatorFactory) throws IOException {
        this.fullName = fullName;
        this.fileName = dataIO.getName();
        this.readOnlyMode = dataIO.isReadOnly();
        if (blockSize <= (BLOCK_HEADER_SIZE + BLOCK_FOOTER_SIZE)) blockSize = 1024;
        if (initialCapacity < 0) initialCapacity = 100;
        this.initialCapacity = initialCapacity;
        boolean newFile = false;
        if (!dataIO.isNew() && (dataIO.length() >= DATA_FILE_HEADER_SIZE)) {
            dataIO.setFilePointer(0L);
            this.version = dataIO.readShort();
            this.allocationUnitSize = dataIO.readInt();
            this.useDataChecksum = dataIO.readBoolean();
            long checkSum = dataIO.readLong();
            byte[] headerBuf = dataFileHeaderFieldsToBytes();
            CRC32 crc32 = new CRC32();
            crc32.update(headerBuf);
            if (crc32.getValue() != checkSum) {
                throw new IOException("Headers of " + fullName + " corrupted! Calculated checksum: " + crc32.getValue() + ", checksum read from file: " + checkSum + ".");
            }
            this.blockSize = (allocationUnitSize + BLOCK_HEADER_SIZE + BLOCK_FOOTER_SIZE);
            newFile = false;
        } else {
            if (this.readOnlyMode) {
                if (!dataIO.isNew()) throw new IOException("Insufficient headers found in '" + this.fileName + "'!"); else throw new FileNotFoundException("'" + this.fileName + "' not found!");
            }
            this.version = 1;
            this.allocationUnitSize = blockSize - (BLOCK_HEADER_SIZE + BLOCK_FOOTER_SIZE);
            this.useDataChecksum = useDataChecksum;
            byte[] headerBuf = dataFileHeaderFieldsToBytes();
            CRC32 crc32 = new CRC32();
            crc32.update(headerBuf);
            dataIO.setFilePointer(0L);
            dataIO.writeShort(version);
            dataIO.writeInt(allocationUnitSize);
            dataIO.writeBoolean(useDataChecksum);
            dataIO.writeLong(crc32.getValue());
            this.blockSize = blockSize;
            newFile = true;
        }
        this.blockFile = blockFileFactory.createBlockFile(dataIO, this.blockSize, DATA_FILE_HEADER_SIZE);
        this.deallocatedBlockHeaderBuffer = new byte[BLOCK_HEADER_SIZE];
        formatBlockHeader(false, false, -1, NULL_LINK, this.deallocatedBlockHeaderBuffer, 0);
        if (newFile) {
            this.dataChainsArray = new ArrayList(this.initialCapacity);
            final int listSize = this.initialCapacity / 5 + 10;
            this.dataStartBlocks = new IntList(listSize, listSize);
            this.itemDataSizes = new IntList(listSize, listSize);
            this.initNewFile();
            this.blockAllocator = blockAllocatorFactory.createBlockAllocator(this.initialCapacity, new int[0]);
        } else {
            int blockCapacity = this.blockFile.getBlockCapacity();
            if (!this.readOnlyMode) {
                if (blockCapacity < this.initialCapacity) {
                    blockCapacity = this.initialCapacity;
                    this.blockFile.setBlockCapacity(blockCapacity);
                }
            }
            this.dataChainsArray = new ArrayList(blockCapacity);
            final int listSize = blockCapacity / 5 + 10;
            this.dataStartBlocks = new IntList(listSize, listSize);
            this.itemDataSizes = new IntList(listSize, listSize);
            int[] occupiedIndices = this.initExistingFile();
            if (!this.readOnlyMode) this.blockAllocator = blockAllocatorFactory.createBlockAllocator(blockCapacity, occupiedIndices); else this.blockAllocator = null;
        }
        if (this.initialCapacity < 10) this.initialCapacityIncrementFactor = 1; else this.initialCapacityIncrementFactor = Math.min((int) Math.ceil(this.initialCapacity / 10), 1000);
    }
