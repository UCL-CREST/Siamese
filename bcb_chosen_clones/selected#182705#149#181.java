    public ArrayList<ODIE_IndexFinderAnnotation> process(String phrase) {
        Pattern tokeniserPattern = Pattern.compile("\\S+");
        Matcher matcher = tokeniserPattern.matcher(phrase);
        ArrayList<ODIE_IndexFinderAnnotation> odieAnnots = new ArrayList<ODIE_IndexFinderAnnotation>();
        int annotationId = 0;
        while (matcher.find()) {
            int spos = matcher.start();
            int epos = matcher.end();
            ODIE_IndexFinderNode sNode = new ODIE_IndexFinderNode();
            sNode.setOffset(new Long(spos));
            ODIE_IndexFinderNode eNode = new ODIE_IndexFinderNode();
            eNode.setOffset(new Long(epos));
            String token = matcher.group(0);
            ODIE_IndexFinderAnnotation annot = new ODIE_IndexFinderAnnotation();
            annot.setStartNode(sNode);
            annot.setEndNode(eNode);
            annot.setAnnotationId(annotationId++);
            annot.setAnnotationSetName("");
            annot.setAnnotationTypeName("Token");
            annot.setFeatures(new HashMap<String, Object>());
            annot.getFeatures().put("string", token);
            if (this.stemmer != null) {
                this.stemmer.add(token);
                this.stemmer.stem();
                String normalizedForm = this.stemmer.getResultString();
                annot.getFeatures().put("normalizedForm", normalizedForm);
            }
            odieAnnots.add(annot);
        }
        this.strategyEngine.setSortedTokens(odieAnnots);
        this.strategyEngine.execute();
        return this.strategyEngine.getResultingConcepts();
    }
