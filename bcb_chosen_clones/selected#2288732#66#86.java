    private static void createUncompressedZip(ByteArrayOutputStream bytesOut) throws IOException {
        ZipOutputStream out = new ZipOutputStream(bytesOut);
        try {
            long[] crcs = { 0x205fbff3, 0x906fae57L, 0x2c235131 };
            int i;
            for (i = 0; i < 3; i++) {
                byte[] input = makeSampleFile(i);
                ZipEntry newEntry = new ZipEntry("file-" + i);
                if (i != 1) newEntry.setComment("this is file " + i);
                newEntry.setMethod(ZipEntry.STORED);
                newEntry.setSize(128 * 1024);
                newEntry.setCrc(crcs[i]);
                out.putNextEntry(newEntry);
                out.write(input, 0, input.length);
                out.closeEntry();
            }
            out.setComment("This is a lovely, but uncompressed, archive!");
        } finally {
            out.close();
        }
    }
