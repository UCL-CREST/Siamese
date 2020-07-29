    public static String[] parseM3U(String strURL, Context c) {
        URL url;
        URLConnection urlConn = null;
        String TAG = "parseM3U";
        Vector<String> radio = new Vector<String>();
        final String filetoken = "http";
        try {
            url = new URL(strURL);
            urlConn = url.openConnection();
            Log.d(TAG, "Got data");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not connect to " + strURL);
        }
        try {
            DataInputStream in = new DataInputStream(urlConn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String temp = strLine.toLowerCase();
                Log.d(TAG, strLine);
                if (temp.startsWith(filetoken)) {
                    radio.add(temp);
                    Log.d(TAG, "Found audio " + temp);
                }
            }
            br.close();
            in.close();
        } catch (Exception e) {
            Log.e(TAG, "Trouble reading file: " + e.getMessage());
        }
        String[] t = new String[0];
        String[] r = null;
        if (radio.size() != 0) {
            r = (String[]) radio.toArray(t);
            Log.d(TAG, "Found total: " + String.valueOf(r.length));
        }
        return r;
    }
