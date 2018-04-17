    public static long getCRC32(InputStream iFile1, InputStream iFile2, InputStream iFile3, InputStream iFile4, InputStream iFile5, InputStream iFile6, InputStream iFile7, InputStream iFile8, InputStream iFile9, InputStream iFile10) throws IOException {
        CRC32 crc = new CRC32();
        try {
            if (iFile1 != null) {
                System.out.println("Updating CRC for Stream 1 ...");
                crc.update(getBytesFromInputStream(iFile1));
                System.out.println("Computed.");
            }
            if (iFile2 != null) {
                System.out.println("Updating CRC for Stream 2 ...");
                crc.update(getBytesFromInputStream(iFile2));
                System.out.println("Computed.");
            }
            if (iFile3 != null) {
                System.out.println("Updating CRC for Stream 3 ...");
                crc.update(getBytesFromInputStream(iFile3));
                System.out.println("Computed.");
            }
            if (iFile4 != null) {
                System.out.println("Updating CRC for Stream 4 ...");
                crc.update(getBytesFromInputStream(iFile4));
                System.out.println("Computed.");
            }
            if (iFile5 != null) {
                System.out.println("Updating CRC for Stream 5 ...");
                crc.update(getBytesFromInputStream(iFile5));
                System.out.println("Computed.");
            }
            if (iFile6 != null) {
                System.out.println("Updating CRC for Stream 6 ...");
                crc.update(getBytesFromInputStream(iFile6));
                System.out.println("Computed.");
            }
            if (iFile7 != null) {
                System.out.println("Updating CRC for Stream 7 ...");
                crc.update(getBytesFromInputStream(iFile7));
                System.out.println("Computed.");
            }
            if (iFile8 != null) {
                System.out.println("Updating CRC for Stream 8 ...");
                crc.update(getBytesFromInputStream(iFile8));
                System.out.println("Computed.");
            }
            if (iFile9 != null) {
                System.out.println("Updating CRC for Stream 9 ...");
                crc.update(getBytesFromInputStream(iFile9));
                System.out.println("Computed.");
            }
            if (iFile10 != null) {
                System.out.println("Updating CRC for Stream 10 ...");
                crc.update(getBytesFromInputStream(iFile10));
                System.out.println("Computed.");
            }
            return crc.getValue();
        } catch (IOException ex) {
            System.err.println("ERROR : CRC32 Java computation error : " + ex.getMessage());
            System.err.println("Full Stack below :\n" + ex.getMessage());
            System.err.println("Throwing exception ...\nBye.");
            throw ex;
        }
    }
