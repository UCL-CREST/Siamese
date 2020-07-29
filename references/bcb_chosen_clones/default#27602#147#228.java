    void run(String[] args) {
        InputStream istream = System.in;
        System.out.println("TradeMaximizer " + version);
        String filename = parseArgs(args, false);
        if (filename != null) {
            System.out.println("Input from: " + filename);
            try {
                if (filename.startsWith("http:") || filename.startsWith("ftp:")) {
                    URL url = new URL(filename);
                    istream = url.openStream();
                } else istream = new FileInputStream(filename);
            } catch (IOException ex) {
                fatalError(ex.toString());
            }
        }
        List<String[]> wantLists = readWantLists(istream);
        if (wantLists == null) return;
        if (options.size() > 0) {
            System.out.print("Options:");
            for (String option : options) System.out.print(" " + option);
            System.out.println();
        }
        System.out.println();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            for (String[] wset : wantLists) {
                for (String w : wset) {
                    digest.update((byte) ' ');
                    digest.update(w.getBytes());
                }
                digest.update((byte) '\n');
            }
            System.out.println("Input Checksum: " + toHexString(digest.digest()));
        } catch (NoSuchAlgorithmException ex) {
        }
        parseArgs(args, true);
        if (iterations > 1 && seed == -1) {
            seed = System.currentTimeMillis();
            System.out.println("No explicit SEED, using " + seed);
        }
        if (!(metric instanceof MetricSumSquares) && priorityScheme != NO_PRIORITIES) System.out.println("Warning: using priorities with the non-default metric is normally worthless");
        buildGraph(wantLists);
        if (showMissing && officialNames != null && officialNames.size() > 0) {
            for (String name : usedNames) officialNames.remove(name);
            List<String> missing = new ArrayList<String>(officialNames);
            Collections.sort(missing);
            for (String name : missing) {
                System.out.println("**** Missing want list for official name " + name);
            }
            System.out.println();
        }
        if (showErrors && errors.size() > 0) {
            Collections.sort(errors);
            System.out.println("ERRORS:");
            for (String error : errors) System.out.println(error);
            System.out.println();
        }
        long startTime = System.currentTimeMillis();
        graph.removeImpossibleEdges();
        List<List<Graph.Vertex>> bestCycles = graph.findCycles();
        int bestMetric = metric.calculate(bestCycles);
        if (iterations > 1) {
            System.out.println(metric);
            graph.saveMatches();
            for (int i = 0; i < iterations - 1; i++) {
                graph.shuffle();
                List<List<Graph.Vertex>> cycles = graph.findCycles();
                int newMetric = metric.calculate(cycles);
                if (newMetric < bestMetric) {
                    bestMetric = newMetric;
                    bestCycles = cycles;
                    graph.saveMatches();
                    System.out.println(metric);
                } else if (verbose) System.out.println("# " + metric);
            }
            System.out.println();
            graph.restoreMatches();
        }
        long stopTime = System.currentTimeMillis();
        displayMatches(bestCycles);
        if (showElapsedTime) System.out.println("Elapsed time = " + (stopTime - startTime) + "ms");
    }
