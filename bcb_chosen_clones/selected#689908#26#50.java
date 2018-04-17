    public static List<String> readListFile(URL url) {
        List<String> names = new ArrayList<String>();
        if (url != null) {
            InputStream in = null;
            try {
                in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        names.add(line);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (in != null) in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return names;
    }
