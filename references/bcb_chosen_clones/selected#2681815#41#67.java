    private void loadMap(URI uri) throws IOException {
        BufferedReader reader = null;
        InputStream stream = null;
        try {
            URL url = uri.toURL();
            stream = url.openStream();
            if (url.getFile().endsWith(".gz")) {
                stream = new GZIPInputStream(stream);
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    String[] parts = line.split(" ");
                    if (parts.length == 2) {
                        pinyinZhuyinMap.put(parts[0], parts[1]);
                        zhuyinPinyinMap.put(parts[1], parts[0]);
                    }
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
