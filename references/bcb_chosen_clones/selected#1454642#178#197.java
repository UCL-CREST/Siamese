    private Map<String, String> readStuff(Iterator<URL> urls) throws IOException {
        Map<String, String> result = new LinkedHashMap();
        while (urls.hasNext()) {
            URL url = urls.next();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = reader.readLine();
            while (s != null) {
                s = s.trim();
                if (s.length() > 0) {
                    String[] ss = s.split("\\s");
                    for (int i = 1; i < ss.length; i++) {
                        result.put(ss[i], ss[0]);
                    }
                }
                s = reader.readLine();
            }
            reader.close();
        }
        return result;
    }
