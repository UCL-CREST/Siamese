    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        String text = jcas.getDocumentText();
        List<PubTerm> humans = new ArrayList<PubTerm>();
        List<PubTerm> mice = new ArrayList<PubTerm>();
        Pattern p = Pattern.compile("\\s(human|humans|homo sapiens)\\s", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            int begin = m.start() + 1;
            int end = m.end() - 1;
            int score = 1000;
            PubTerm term = createTerm(jcas, begin, end, PredefinedCategories.HUMAN, score, true);
            term.setIdentifiers(createIdentifiers(jcas, new String[][] { { PredefinedIdentifierTypes.UMLS_CUI, "C0086418" } }));
            humans.add(term);
        }
        p = Pattern.compile("\\s(mouse|mus musculus|mice)\\s", Pattern.CASE_INSENSITIVE);
        m = p.matcher(text);
        while (m.find()) {
            int begin = m.start() + 1;
            int end = m.end() - 1;
            int score = 1000;
            PubTerm term = createTerm(jcas, begin, end, PredefinedCategories.MOUSE, score, true);
            term.setIdentifiers(createIdentifiers(jcas, new String[][] { { PredefinedIdentifierTypes.UMLS_CUI, "C0025914" }, { PredefinedIdentifierTypes.MESH_HEADING, "Mice" } }));
            mice.add(term);
        }
    }
