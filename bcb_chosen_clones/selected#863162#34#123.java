    public static void main(String[] args) {
        try {
            String completePath = null;
            String predictionFileName = null;
            if (args.length == 2) {
                completePath = args[0];
                predictionFileName = args[1];
            } else {
                System.out.println("Please provide complete path to training_set parent folder as an argument. EXITING");
                System.exit(0);
            }
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieIndexFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            MovieLimitsTHash = new TShortObjectHashMap(17770, 1);
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
            inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustIndexFileName);
            inC = new FileInputStream(inputFile).getChannel();
            filesize = (int) inC.size();
            mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            CustomerLimitsTHash = new TIntObjectHashMap(480189, 1);
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
            MoviesAndRatingsPerCustomer = InitializeMovieRatingsForCustomerHashMap(completePath, CustomerLimitsTHash);
            System.out.println("Populated MoviesAndRatingsPerCustomer hashmap");
            File outfile = new File(completePath + fSep + "SmartGRAPE" + fSep + predictionFileName);
            FileChannel out = new FileOutputStream(outfile, true).getChannel();
            inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "formattedProbeData.txt");
            inC = new FileInputStream(inputFile).getChannel();
            filesize = (int) inC.size();
            ByteBuffer probemappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            int custAndRatingSize = 0;
            TIntByteHashMap custsandratings = new TIntByteHashMap();
            int ignoreProcessedRows = 0;
            int movieViewershipSize = 0;
            while (probemappedfile.hasRemaining()) {
                short testmovie = probemappedfile.getShort();
                int testCustomer = probemappedfile.getInt();
                if ((CustomersAndRatingsPerMovie != null) && (CustomersAndRatingsPerMovie.containsKey(testmovie))) {
                } else {
                    CustomersAndRatingsPerMovie = InitializeCustomerRatingsForMovieHashMap(completePath, testmovie);
                    custsandratings = (TIntByteHashMap) CustomersAndRatingsPerMovie.get(testmovie);
                    custAndRatingSize = custsandratings.size();
                }
                TShortByteHashMap testCustMovieAndRatingsMap = (TShortByteHashMap) MoviesAndRatingsPerCustomer.get(testCustomer);
                short[] testCustMovies = testCustMovieAndRatingsMap.keys();
                float finalPrediction = 0;
                finalPrediction = predictRating(testCustomer, testmovie, custsandratings, custAndRatingSize, testCustMovies, testCustMovieAndRatingsMap);
                System.out.println("prediction for movie: " + testmovie + " for customer " + testCustomer + " is " + finalPrediction);
                ByteBuffer buf = ByteBuffer.allocate(11);
                buf.putShort(testmovie);
                buf.putInt(testCustomer);
                buf.putFloat(finalPrediction);
                buf.flip();
                out.write(buf);
                buf = null;
                testCustMovieAndRatingsMap = null;
                testCustMovies = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
