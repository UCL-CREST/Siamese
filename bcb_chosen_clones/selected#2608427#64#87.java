    public static void refresh() {
        URL[] urls = Constants.Wiki2xhtml.getUpdateURLs();
        content.setLength(0);
        InputStream is = null;
        BufferedReader br = null;
        for (int i = 0; i < urls.length; i++) {
            try {
                is = urls[i].openStream();
                br = new BufferedReader(new InputStreamReader(is));
                String s;
                while ((s = br.readLine()) != null) {
                    if (s.length() == 0) continue;
                    if (s.startsWith("--")) break;
                    content.append(s + '\n');
                }
                is.close();
                break;
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + urls[i].getHost() + urls[i].getPath());
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
