    public String parse(String term) throws OntologyAdaptorException {
        try {
            String sUrl = getUrl(term);
            if (sUrl.length() > 0) {
                URL url = new URL(sUrl);
                InputStream in = url.openStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = r.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\r\n");
                    }
                    sb.append(line);
                }
                return sb.toString();
            } else {
                return "";
            }
        } catch (Exception ex) {
            throw new OntologyAdaptorException("Convertion to lucene failed.", ex);
        }
    }
