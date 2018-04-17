    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        String text = jcas.getDocumentText();
        List<PubTerm> addedTerms = new ArrayList<PubTerm>();
        List<Concept> concepts = maxMatcher.extract(jcas.getDocumentText());
        for (Concept concept : concepts) {
            Word startWord = concept.getStartingWord();
            int startWordPos = startWord.getPosInSentence();
            Word endWord = concept.getEndingWord();
            int endWordPos = endWord.getPosInSentence();
            String searchString = null;
            if (startWord.getContent().equals(endWord.getContent()) && startWordPos == endWordPos) {
                searchString = startWord.getContent();
            } else {
                if (startWordPos < endWordPos) {
                    StringBuffer buffer = new StringBuffer();
                    Word word = startWord;
                    for (int i = startWordPos; i <= endWordPos; i++) {
                        if (buffer.length() != 0) {
                            buffer.append("\\W");
                        }
                        buffer.append(word.getContent());
                        word = word.next;
                    }
                    searchString = buffer.toString();
                } else {
                    getContext().getLogger().log(Level.WARNING, "Internal Error. Word start position greater than end position.");
                    continue;
                }
            }
            if (searchString == null) {
                getContext().getLogger().log(Level.WARNING, "Internal Error. Search string of concept is null.");
                continue;
            }
            Pattern pattern = Pattern.compile("\\b" + searchString + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                List<Category> categories = categoryMapper.getCategories(concept.getSemanticType());
                for (Category category : categories) {
                    boolean alreadyAdded = false;
                    for (PubTerm addedTerm : addedTerms) {
                        if (addedTerm.getBegin() == matcher.start() && addedTerm.getEnd() == matcher.end() && addedTerm.getCategoryName().equals(category.getName())) {
                            alreadyAdded = true;
                        }
                    }
                    if (!alreadyAdded) {
                        PubTerm term = createTerm(jcas, matcher.start(), matcher.end(), category.getName(), 800, true);
                        FSArray identifiers = new FSArray(jcas, 1);
                        identifiers.set(0, createIdentifier(jcas, PredefinedIdentifierTypes.UMLS_CUI, concept.getEntryID(), null));
                        term.setIdentifiers(identifiers);
                        addedTerms.add(term);
                    }
                }
            }
        }
    }
