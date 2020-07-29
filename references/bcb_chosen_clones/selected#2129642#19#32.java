    public List<String> query(String query) throws IOException {
        List<String> list = new LinkedList<String>();
        query = URLEncoder.encode(query, "UTF-8");
        String queryurl = baseurl + "?type=tuples&lang=itql&format=csv&query=" + query;
        URL url = new URL(queryurl);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();
        while (line != null) {
            list.add(line);
            line = reader.readLine();
        }
        reader.close();
        return list;
    }
