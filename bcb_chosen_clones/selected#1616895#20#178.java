    public NegExResult negCheck(String sentence, int phraseStart, int phraseEnd, boolean possibleNegations) throws Exception {
        if (phraseStart >= phraseEnd) {
            throw new IllegalArgumentException();
        }
        List<NegPhrase> negPhrases = new ArrayList<NegPhrase>();
        Iterator<String> iRule = sortedRules.iterator();
        while (iRule.hasNext()) {
            String rule = (String) iRule.next();
            Pattern p = Pattern.compile("[\\t]+");
            String[] ruleTokens = p.split(rule.trim());
            String typeString = ruleTokens[1];
            String[] ruleMembers = ruleTokens[0].trim().split(" ");
            String rule2 = "";
            for (int i = 0; i <= ruleMembers.length - 1; i++) {
                if (!ruleMembers[i].equals("")) {
                    if (ruleMembers.length == 1) {
                        rule2 = ruleMembers[i];
                    } else {
                        rule2 = rule2 + ruleMembers[i].trim() + "\\s+";
                    }
                }
            }
            if (rule2.endsWith("\\s+")) {
                rule2 = rule2.substring(0, rule2.lastIndexOf("\\s+"));
            }
            rule2 = "((?m)(?i)[[\\p{Punct}&&[^\\]\\[]]|\\s+])(" + rule2 + ")([[\\p{Punct}&&[^_]]|\\s+])";
            Pattern p2 = Pattern.compile(rule2.trim());
            Matcher m = p2.matcher(sentence);
            while (m.find() == true) {
                int type = -1;
                if (typeString.equals("[PSEU]")) {
                    type = NegPhrase.PSEU;
                } else if (typeString.equals("[PREN]")) {
                    type = NegPhrase.PREN;
                } else if (typeString.equals("[PREP]")) {
                    type = NegPhrase.PREP;
                } else if (typeString.equals("[POST]")) {
                    type = NegPhrase.POST;
                } else if (typeString.equals("[POSP]")) {
                    type = NegPhrase.POSP;
                } else if (typeString.equals("[CONJ]")) {
                    type = NegPhrase.CONJ;
                }
                int negPhraseStart = m.start() + m.group(1).length();
                int negPhraseEnd = m.end() - m.group(3).length();
                NegPhrase negPhrase = new NegPhrase(negPhraseStart, negPhraseEnd, type, m.group(2));
                negPhrases.add(negPhrase);
            }
        }
        NegExResult result = new NegExResult();
        result.setResult(NegExResult.NOT_NEGATED);
        Collections.sort(negPhrases, new Comparator<NegPhrase>() {

            @Override
            public int compare(NegPhrase o1, NegPhrase o2) {
                return o1.getEnd() - o2.getEnd();
            }
        });
        {
            boolean inNegation = false;
            NegPhrase trigger = null;
            for (int i = 0; i < negPhrases.size(); i++) {
                NegPhrase negPhrase = negPhrases.get(i);
                if (negPhrase.getType() == NegPhrase.PREN && negPhrase.getEnd() <= phraseStart) {
                    inNegation = true;
                    trigger = negPhrase;
                }
                if (negPhrase.getType() != NegPhrase.PREN && negPhrase.getEnd() <= phraseStart) {
                    inNegation = false;
                    trigger = null;
                }
                if (negPhrase.getEnd() > phraseStart) {
                    break;
                }
            }
            if (inNegation) {
                assert trigger != null;
                result.setResult(NegExResult.NEGATED);
                result.setTrigger(trigger);
            }
        }
        if (result.getResult() != NegExResult.NEGATED) {
            boolean inPossibleNegation = false;
            NegPhrase trigger = null;
            for (int i = 0; i < negPhrases.size(); i++) {
                NegPhrase negPhrase = negPhrases.get(i);
                if (negPhrase.getType() == NegPhrase.PREP && negPhrase.getEnd() <= phraseStart) {
                    inPossibleNegation = true;
                    trigger = negPhrase;
                }
                if (negPhrase.getType() != NegPhrase.PREP && negPhrase.getEnd() <= phraseStart) {
                    inPossibleNegation = false;
                    trigger = null;
                }
                if (negPhrase.getEnd() > phraseStart) {
                    break;
                }
            }
            if (inPossibleNegation) {
                assert trigger != null;
                result.setResult(NegExResult.POSSIBLY_NEGATED);
                result.setTrigger(trigger);
            }
        }
        Collections.sort(negPhrases, new Comparator<NegPhrase>() {

            @Override
            public int compare(NegPhrase o1, NegPhrase o2) {
                return -(o1.getStart() - o2.getStart());
            }
        });
        if (result.getResult() != NegExResult.NEGATED) {
            boolean inNegation = false;
            NegPhrase trigger = null;
            for (int i = 0; i < negPhrases.size(); i++) {
                NegPhrase negPhrase = negPhrases.get(i);
                if (negPhrase.getType() == NegPhrase.POST && negPhrase.getStart() >= phraseEnd) {
                    inNegation = true;
                    trigger = negPhrase;
                }
                if (negPhrase.getType() != NegPhrase.POST && negPhrase.getStart() >= phraseEnd) {
                    inNegation = false;
                    trigger = null;
                }
                if (negPhrase.getStart() < phraseEnd) {
                    break;
                }
            }
            if (inNegation) {
                assert trigger != null;
                result.setResult(NegExResult.NEGATED);
                result.setTrigger(trigger);
            }
        }
        if (result.getResult() != NegExResult.NEGATED && result.getResult() != NegExResult.POSSIBLY_NEGATED) {
            boolean inPossibleNegation = false;
            NegPhrase trigger = null;
            for (int i = 0; i < negPhrases.size(); i++) {
                NegPhrase negPhrase = negPhrases.get(i);
                if (negPhrase.getType() == NegPhrase.POSP && negPhrase.getStart() >= phraseEnd) {
                    inPossibleNegation = true;
                    trigger = negPhrase;
                }
                if (negPhrase.getType() != NegPhrase.POSP && negPhrase.getStart() >= phraseEnd) {
                    inPossibleNegation = false;
                    trigger = null;
                }
                if (negPhrase.getStart() < phraseEnd) {
                    break;
                }
            }
            if (inPossibleNegation) {
                assert trigger != null;
                result.setResult(NegExResult.POSSIBLY_NEGATED);
                result.setTrigger(trigger);
            }
        }
        return result;
    }
