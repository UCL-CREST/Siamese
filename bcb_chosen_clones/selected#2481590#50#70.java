    public String parse(String queryText) throws ParseException {
        try {
            StringBuilder sb = new StringBuilder();
            queryText = Val.chkStr(queryText);
            if (queryText.length() > 0) {
                URL url = new URL(getUrl(queryText));
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\r\n");
                    }
                    sb.append(line);
                }
            }
            return sb.toString();
        } catch (IOException ex) {
            throw new ParseException("Ontology parser is unable to parse term: \"" + queryText + "\" due to internal error: " + ex.getMessage());
        }
    }
