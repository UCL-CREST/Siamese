    protected void annotateKeyPhrases(List phrases) throws Exception {
        if (phrases == null || phrases.isEmpty()) return;
        String patternStr = "";
        Iterator phraseIter = phrases.iterator();
        while (phraseIter.hasNext()) {
            String phrase = (String) phraseIter.next();
            patternStr += patternStr.length() == 0 ? "\\Q" + phrase + "\\E" : "|\\Q" + phrase + "\\E";
        }
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(document.getContent().toString());
        AnnotationSet outputSet = outputAS == null || outputAS.length() == 0 ? document.getAnnotations() : document.getAnnotations(outputAS);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            outputSet.add(new Long(start), new Long(end), keyphraseAnnotationType, Factory.newFeatureMap());
        }
        document.getFeatures().put("KEA matched keyphrases", phrases);
    }
