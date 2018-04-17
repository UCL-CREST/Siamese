    HTTPValuePatternComponent(final String url, final long seed) throws IOException {
        seedRandom = new Random(seed);
        random = new ThreadLocal<Random>();
        final ArrayList<String> lineList = new ArrayList<String>(100);
        final URL parsedURL = new URL(url);
        final HttpURLConnection urlConnection = (HttpURLConnection) parsedURL.openConnection();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        try {
            while (true) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lineList.add(line);
            }
        } finally {
            reader.close();
        }
        if (lineList.isEmpty()) {
            throw new IOException(ERR_VALUE_PATTERN_COMPONENT_EMPTY_FILE.get());
        }
        lines = new String[lineList.size()];
        lineList.toArray(lines);
    }
