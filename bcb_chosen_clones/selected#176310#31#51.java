    private static void search(List<CSequenceView> sequences, CRegexSearchParams params, CRegexResults results) {
        if (params.getRegexType() == CConstants.RegexType.PSSM) {
            searchPssm(sequences, params, results);
            return;
        }
        CRegexResults.Regex regex = results.addRegex(params.getQuery(), parsePattern(params), params.getRegexType());
        results.addRegex(regex);
        Pattern pat = Pattern.compile(regex.getRegex());
        for (CSequenceView seq : sequences) {
            String str = seq.getSequence(params.getSequenceType());
            if (str == null) continue;
            CRegexResults.Sequence sequence = results.addSequence(seq);
            Matcher matcher = pat.matcher(str);
            while (matcher.find()) {
                CRegexResults.Match match = regex.addMatch(sequence);
                match.setStart(matcher.start());
                match.setEnd(matcher.end());
                match.setMatch(matcher.group());
            }
        }
    }
