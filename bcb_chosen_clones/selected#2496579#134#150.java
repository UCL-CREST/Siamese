    public static String retrieveQ(String urlToRetrieve) throws MalformedURLException, IOException {
        URL url = new URL(urlToRetrieve);
        URLConnection uc = url.openConnection();
        InputStream is = uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder output = new StringBuilder(16384);
        String str;
        boolean first = true;
        while ((str = in.readLine()) != null) {
            if (!first) output.append("\n");
            first = false;
            output.append(str);
        }
        in.close();
        System.err.println(((HttpURLConnection) uc).getResponseMessage());
        return output.toString();
    }
