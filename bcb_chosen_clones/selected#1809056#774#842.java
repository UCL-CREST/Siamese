    private static boolean computeCustomerAverages(String completePath, String CustomerAveragesOutputFileName, String CustIndexFileName) {
        try {
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustIndexFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            TIntObjectHashMap CustomerLimitsTHash = new TIntObjectHashMap(480189, 1);
            int startIndex, endIndex;
            TIntArrayList a;
            int custid;
            while (mappedfile.hasRemaining()) {
                custid = mappedfile.getInt();
                startIndex = mappedfile.getInt();
                endIndex = mappedfile.getInt();
                a = new TIntArrayList(2);
                a.add(startIndex);
                a.add(endIndex);
                CustomerLimitsTHash.put(custid, a);
            }
            inC.close();
            mappedfile = null;
            System.out.println("Loaded customer index hash");
            File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustomerAveragesOutputFileName);
            FileChannel outC = new FileOutputStream(outFile, true).getChannel();
            int totalCusts = CustomerLimitsTHash.size();
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "MovieRatingBinaryFile.txt");
            inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            int[] itr = CustomerLimitsTHash.keys();
            startIndex = 0;
            endIndex = 0;
            a = null;
            ByteBuffer buf;
            for (int i = 0; i < totalCusts; i++) {
                int currentCust = itr[i];
                a = (TIntArrayList) CustomerLimitsTHash.get(currentCust);
                startIndex = a.get(0);
                endIndex = a.get(1);
                if (endIndex > startIndex) {
                    buf = ByteBuffer.allocate((endIndex - startIndex + 1) * 3);
                    inC.read(buf, (startIndex - 1) * 3);
                } else {
                    buf = ByteBuffer.allocate(3);
                    inC.read(buf, (startIndex - 1) * 3);
                }
                buf.flip();
                int bufsize = buf.capacity() / 3;
                float sum = 0;
                for (int q = 0; q < bufsize; q++) {
                    buf.getShort();
                    sum += buf.get();
                }
                ByteBuffer outbuf = ByteBuffer.allocate(8);
                outbuf.putInt(currentCust);
                outbuf.putFloat(sum / bufsize);
                outbuf.flip();
                outC.write(outbuf);
                buf.clear();
                buf = null;
                a.clear();
                a = null;
            }
            inC.close();
            outC.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
