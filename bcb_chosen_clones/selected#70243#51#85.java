    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        String text = jcas.getDocumentText();
        if (!regularExpression) {
            List<String> searchTerms = StringUtil.splitString(search, ' ', '"');
            for (String searchTerm : searchTerms) {
                int i = 0;
                while (true) {
                    int index = -1;
                    if (caseSensitive) {
                        index = text.indexOf(searchTerm, i);
                    } else {
                        index = text.toLowerCase().indexOf(searchTerm.toLowerCase(), i);
                    }
                    if (index == -1) {
                        break;
                    }
                    createTerm(jcas, index, index + searchTerm.length(), categoryName, 1000, true);
                    i = index + 1;
                }
            }
        } else {
            try {
                Pattern pattern = Pattern.compile(search);
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    int start = matcher.start();
                    int end = matcher.end();
                    createTerm(jcas, start, end, categoryName, 1000, true);
                }
            } catch (Exception e) {
                getContext().getLogger().log(Level.INFO, "Invalid regular expression: " + search);
            }
        }
    }
