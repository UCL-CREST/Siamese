    private static boolean computeMovieAverages(String completePath, String MovieAveragesOutputFileName, String MovieIndexFileName) {
        try {
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieIndexFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            TShortObjectHashMap MovieLimitsTHash = new TShortObjectHashMap(17770, 1);
            int i = 0, totalcount = 0;
            short movie;
            int startIndex, endIndex;
            TIntArrayList a;
            while (mappedfile.hasRemaining()) {
                movie = mappedfile.getShort();
                startIndex = mappedfile.getInt();
                endIndex = mappedfile.getInt();
                a = new TIntArrayList(2);
                a.add(startIndex);
                a.add(endIndex);
                MovieLimitsTHash.put(movie, a);
            }
            inC.close();
            mappedfile = null;
            System.out.println("Loaded movie index hash");
            File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieAveragesOutputFileName);
            FileChannel outC = new FileOutputStream(outFile, true).getChannel();
            int totalMovies = MovieLimitsTHash.size();
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "CustomerRatingBinaryFile.txt");
            inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            short[] itr = MovieLimitsTHash.keys();
            Arrays.sort(itr);
            ByteBuffer buf;
            for (i = 0; i < totalMovies; i++) {
                short currentMovie = itr[i];
                a = (TIntArrayList) MovieLimitsTHash.get(currentMovie);
                startIndex = a.get(0);
                endIndex = a.get(1);
                if (endIndex > startIndex) {
                    buf = ByteBuffer.allocate((endIndex - startIndex + 1) * 5);
                    inC.read(buf, (startIndex - 1) * 5);
                } else {
                    buf = ByteBuffer.allocate(5);
                    inC.read(buf, (startIndex - 1) * 5);
                }
                buf.flip();
                int bufsize = buf.capacity() / 5;
                float sum = 0;
                for (int q = 0; q < bufsize; q++) {
                    buf.getInt();
                    sum += buf.get();
                }
                ByteBuffer outbuf = ByteBuffer.allocate(6);
                outbuf.putShort(currentMovie);
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
