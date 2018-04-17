    public List<BadassEntry> parse() {
        mBadassEntries = new ArrayList<BadassEntry>();
        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            boolean flag1 = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!flag1 && line.contains(START_PARSE)) flag1 = true;
                if (flag1 && line.contains(STOP_PARSE)) break;
                if (flag1) {
                    if (line.contains(ENTRY_HINT)) {
                        parseBadass(line);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mBadassEntries;
    }
