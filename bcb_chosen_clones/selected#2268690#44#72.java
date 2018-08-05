    private String getEncoding() throws IOException {
        BufferedReader reader = null;
        String encoding = null;
        try {
            URLConnection connection = url.openConnection();
            Map<String, List<String>> header = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : header.entrySet()) {
                if (entry.getKey().toLowerCase().equals("content-type")) {
                    String item = entry.getValue().toString().toLowerCase();
                    if (item.contains("charset")) {
                        encoding = extractEncoding(item);
                        if (encoding != null && !encoding.isEmpty()) return encoding;
                    }
                }
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("charset") || line.contains("encoding")) {
                    encoding = extractEncoding(line);
                    if (encoding != null && !encoding.isEmpty()) return encoding;
                }
            }
            return STANDARDENCODING;
        } finally {
            if (reader != null) reader.close();
        }
    }
