    private static void updateLeapSeconds() throws IOException, MalformedURLException, NumberFormatException {
        URL url = new URL("http://cdf.gsfc.nasa.gov/html/CDFLeapSeconds.txt");
        InputStream in;
        try {
            in = url.openStream();
        } catch (IOException ex) {
            url = LeapSecondsConverter.class.getResource("CDFLeapSeconds.txt");
            in = url.openStream();
            System.err.println("Using local copy of leap seconds!!!");
        }
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String s = "";
        leapSeconds = new ArrayList(50);
        withoutLeapSeconds = new ArrayList(50);
        String lastLine = s;
        while (s != null) {
            s = r.readLine();
            if (s == null) {
                System.err.println("Last leap second read from " + url + " " + lastLine);
                continue;
            }
            if (s.startsWith(";")) {
                continue;
            }
            String[] ss = s.trim().split("\\s+", -2);
            if (ss[0].compareTo("1972") < 0) {
                continue;
            }
            int iyear = Integer.parseInt(ss[0]);
            int imonth = Integer.parseInt(ss[1]);
            int iday = Integer.parseInt(ss[2]);
            int ileap = (int) (Double.parseDouble(ss[3]));
            double us2000 = TimeUtil.createTimeDatum(iyear, imonth, iday, 0, 0, 0, 0).doubleValue(Units.us2000);
            leapSeconds.add(Long.valueOf(((long) us2000) * 1000L - 43200000000000L + (long) (ileap - 32) * 1000000000));
            withoutLeapSeconds.add(us2000);
        }
        leapSeconds.add(Long.MAX_VALUE);
        withoutLeapSeconds.add(Double.MAX_VALUE);
        lastUpdateMillis = System.currentTimeMillis();
    }
