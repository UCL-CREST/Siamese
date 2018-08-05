    private static boolean genCustomerLocationsFileAndCustomerIndexFile(String completePath, String masterFile, String CustLocationsFileName, String CustIndexFileName) {
        try {
            TIntObjectHashMap CustInfoHash = new TIntObjectHashMap(480189, 1);
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + masterFile);
            FileChannel inC = new FileInputStream(inFile).getChannel();
            File outFile1 = new File(completePath + fSep + "SmartGRAPE" + fSep + CustIndexFileName);
            FileChannel outC1 = new FileOutputStream(outFile1, true).getChannel();
            File outFile2 = new File(completePath + fSep + "SmartGRAPE" + fSep + CustLocationsFileName);
            FileChannel outC2 = new FileOutputStream(outFile2, true).getChannel();
            int fileSize = (int) inC.size();
            int totalNoDataRows = fileSize / 7;
            for (int i = 1; i <= totalNoDataRows; i++) {
                ByteBuffer mappedBuffer = ByteBuffer.allocate(7);
                inC.read(mappedBuffer);
                mappedBuffer.position(0);
                short movieName = mappedBuffer.getShort();
                int customer = mappedBuffer.getInt();
                byte rating = mappedBuffer.get();
                mappedBuffer.clear();
                if (CustInfoHash.containsKey(customer)) {
                    TIntArrayList locations = (TIntArrayList) CustInfoHash.get(customer);
                    locations.add(i);
                    CustInfoHash.put(customer, locations);
                } else {
                    TIntArrayList locations = new TIntArrayList();
                    locations.add(i);
                    CustInfoHash.put(customer, locations);
                }
            }
            int[] customers = CustInfoHash.keys();
            Arrays.sort(customers);
            int count = 1;
            for (int i = 0; i < customers.length; i++) {
                int customer = customers[i];
                TIntArrayList locations = (TIntArrayList) CustInfoHash.get(customer);
                int noRatingsForCust = locations.size();
                ByteBuffer outBuf1 = ByteBuffer.allocate(12);
                outBuf1.putInt(customer);
                outBuf1.putInt(count);
                outBuf1.putInt(count + noRatingsForCust - 1);
                outBuf1.flip();
                outC1.write(outBuf1);
                count += noRatingsForCust;
                for (int j = 0; j < locations.size(); j++) {
                    ByteBuffer outBuf2 = ByteBuffer.allocate(4);
                    outBuf2.putInt(locations.get(j));
                    outBuf2.flip();
                    outC2.write(outBuf2);
                }
            }
            inC.close();
            outC1.close();
            outC2.close();
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
