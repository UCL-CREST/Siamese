    private void computePartialChunkCrc(long blkoff, long ckoff, int bytesPerChecksum) throws IOException {
        int sizePartialChunk = (int) (blkoff % bytesPerChecksum);
        int checksumSize = checksum.getChecksumSize();
        blkoff = blkoff - sizePartialChunk;
        LOG.info("computePartialChunkCrc sizePartialChunk " + sizePartialChunk + " block " + block + " offset in block " + blkoff + " offset in metafile " + ckoff);
        byte[] buf = new byte[sizePartialChunk];
        byte[] crcbuf = new byte[checksumSize];
        FSDataset.BlockInputStreams instr = null;
        try {
            instr = datanode.data.getTmpInputStreams(block, blkoff, ckoff);
            IOUtils.readFully(instr.dataIn, buf, 0, sizePartialChunk);
            IOUtils.readFully(instr.checksumIn, crcbuf, 0, crcbuf.length);
        } finally {
            IOUtils.closeStream(instr);
        }
        partialCrc = new CRC32();
        partialCrc.update(buf, 0, sizePartialChunk);
        LOG.info("Read in partial CRC chunk from disk for block " + block);
        if (partialCrc.getValue() != FSInputChecker.checksum2long(crcbuf)) {
            String msg = "Partial CRC " + partialCrc.getValue() + " does not match value computed the " + " last time file was closed " + FSInputChecker.checksum2long(crcbuf);
            throw new IOException(msg);
        }
    }
