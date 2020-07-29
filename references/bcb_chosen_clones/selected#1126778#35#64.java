    private MimeTypes() {
        try {
            final URL url = RES.getURL("types");
            final InputStream is = url.openStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while (line != null) {
                line = line.trim();
                final int p = line.indexOf('#');
                if (p >= 0) {
                    line = line.substring(0, p).trim();
                }
                if (line.length() > 0) {
                    final StringTokenizer st = new StringTokenizer(line, " \t");
                    if (st.countTokens() > 1) {
                        final String mime = st.nextToken();
                        while (st.hasMoreTokens()) {
                            extnMap.put(st.nextToken(), mime);
                        }
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        canParse.add(TEXT_HTML);
        canParse.add(TEXT_CSS);
    }
