    private static void dumpUrl(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String s = reader.readLine();
        while (s != null) {
            System.out.println(s);
            s = reader.readLine();
        }
        reader.close();
    }
