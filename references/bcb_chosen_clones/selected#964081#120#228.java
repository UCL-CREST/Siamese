    public static boolean predictDataSet(String completePath, String Type, String predictionOutputFileName, String CFDataFolderName) {
        try {
            if (Type.equalsIgnoreCase("Qualifying")) {
                File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "CompleteQualifyingDataInByteFormat.txt");
                FileChannel inC = new FileInputStream(inputFile).getChannel();
                int filesize = (int) inC.size();
                TShortObjectHashMap qualMap = new TShortObjectHashMap(17770, 1);
                ByteBuffer qualmappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
                while (qualmappedfile.hasRemaining()) {
                    short movie = qualmappedfile.getShort();
                    int customer = qualmappedfile.getInt();
                    if (qualMap.containsKey(movie)) {
                        TIntArrayList arr = (TIntArrayList) qualMap.get(movie);
                        arr.add(customer);
                        qualMap.put(movie, arr);
                    } else {
                        TIntArrayList arr = new TIntArrayList();
                        arr.add(customer);
                        qualMap.put(movie, arr);
                    }
                }
                System.out.println("Populated qualifying hashmap");
                File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + predictionOutputFileName);
                FileChannel outC = new FileOutputStream(outFile).getChannel();
                ByteBuffer buf;
                TShortObjectHashMap movieDiffStats;
                double finalPrediction;
                short[] movies = qualMap.keys();
                Arrays.sort(movies);
                for (int i = 0; i < movies.length; i++) {
                    short movieToProcess = movies[i];
                    movieDiffStats = loadMovieDiffStats(completePath, movieToProcess, CFDataFolderName);
                    TIntArrayList customersToProcess = (TIntArrayList) qualMap.get(movieToProcess);
                    for (int j = 0; j < customersToProcess.size(); j++) {
                        int customerToProcess = customersToProcess.getQuick(j);
                        finalPrediction = predictPearsonWeightedSlopeOneRating(knn, movieToProcess, customerToProcess, movieDiffStats);
                        if (finalPrediction == finalPrediction) {
                            if (finalPrediction < 1.0) finalPrediction = 1.0; else if (finalPrediction > 5.0) finalPrediction = 5.0;
                        } else finalPrediction = movieAverages.get(movieToProcess);
                        buf = ByteBuffer.allocate(10);
                        buf.putShort(movieToProcess);
                        buf.putInt(customerToProcess);
                        buf.putFloat(new Double(finalPrediction).floatValue());
                        buf.flip();
                        outC.write(buf);
                    }
                }
                outC.close();
                return true;
            } else if (Type.equalsIgnoreCase("Probe")) {
                File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "CompleteProbeDataInByteFormat.txt");
                FileChannel inC = new FileInputStream(inputFile).getChannel();
                int filesize = (int) inC.size();
                TShortObjectHashMap probeMap = new TShortObjectHashMap(17770, 1);
                ByteBuffer probemappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
                while (probemappedfile.hasRemaining()) {
                    short movie = probemappedfile.getShort();
                    int customer = probemappedfile.getInt();
                    byte rating = probemappedfile.get();
                    if (probeMap.containsKey(movie)) {
                        TIntByteHashMap actualRatings = (TIntByteHashMap) probeMap.get(movie);
                        actualRatings.put(customer, rating);
                        probeMap.put(movie, actualRatings);
                    } else {
                        TIntByteHashMap actualRatings = new TIntByteHashMap();
                        actualRatings.put(customer, rating);
                        probeMap.put(movie, actualRatings);
                    }
                }
                System.out.println("Populated probe hashmap");
                File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + predictionOutputFileName);
                FileChannel outC = new FileOutputStream(outFile).getChannel();
                ByteBuffer buf;
                double finalPrediction;
                TShortObjectHashMap movieDiffStats;
                short[] movies = probeMap.keys();
                Arrays.sort(movies);
                for (int i = 0; i < movies.length; i++) {
                    short movieToProcess = movies[i];
                    movieDiffStats = loadMovieDiffStats(completePath, movieToProcess, CFDataFolderName);
                    TIntByteHashMap custRatingsToProcess = (TIntByteHashMap) probeMap.get(movieToProcess);
                    TIntArrayList customersToProcess = new TIntArrayList(custRatingsToProcess.keys());
                    for (int j = 0; j < customersToProcess.size(); j++) {
                        int customerToProcess = customersToProcess.getQuick(j);
                        byte rating = custRatingsToProcess.get(customerToProcess);
                        finalPrediction = predictPearsonWeightedSlopeOneRating(knn, movieToProcess, customerToProcess, movieDiffStats);
                        if (finalPrediction == finalPrediction) {
                            if (finalPrediction < 1.0) finalPrediction = 1.0; else if (finalPrediction > 5.0) finalPrediction = 5.0;
                        } else {
                            finalPrediction = movieAverages.get(movieToProcess);
                            System.out.println("NaN Prediction");
                        }
                        buf = ByteBuffer.allocate(11);
                        buf.putShort(movieToProcess);
                        buf.putInt(customerToProcess);
                        buf.put(rating);
                        buf.putFloat(new Double(finalPrediction).floatValue());
                        buf.flip();
                        outC.write(buf);
                    }
                }
                outC.close();
                return true;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
