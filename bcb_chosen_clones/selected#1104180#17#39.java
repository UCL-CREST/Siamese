    private static String[] loadDB(String name) throws IOException {
        URL url = SpecialConstants.class.getResource(name);
        if (url == null) throw new FileNotFoundException("file " + name + " not found");
        InputStream is = url.openStream();
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf8");
            BufferedReader br = new BufferedReader(isr);
            ArrayList<String> entries = new ArrayList<String>();
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '#') {
                    entries.add(line);
                }
            }
            String[] r = new String[entries.size()];
            entries.toArray(r);
            return r;
        } finally {
            is.close();
        }
    }
