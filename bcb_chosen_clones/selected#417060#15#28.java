    public static void readTestData(String getDkpUrl) throws Exception {
        final URL url = new URL(getDkpUrl);
        final InputStream is = url.openStream();
        try {
            final LineNumberReader rd = new LineNumberReader(new BufferedReader(new InputStreamReader(is)));
            String line = rd.readLine();
            while (line != null) {
                System.out.println(line);
                line = rd.readLine();
            }
        } finally {
            is.close();
        }
    }
