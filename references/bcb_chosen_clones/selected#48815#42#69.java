    private CathUtils() throws Exception {
        super(Ontology.CATH);
        InputStream is = null;
        BufferedReader reader = null;
        try {
            final String CATH_REGEXP = OntologyFactory.getOntology(Ontology.CATH).getRegularExpression();
            final URL url = new URL("http://release.cathdb.info/v3.4.0/CathNames");
            is = url.openStream();
            reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                final String[] tokens = line.split("\\s+");
                if (RegularExpressionUtils.getMatches(tokens[0], CATH_REGEXP).size() > 0) {
                    idToName.put(tokens[0], line.substring(line.indexOf(':') + 1, line.length()));
                }
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
