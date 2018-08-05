    private String[] parsePolicyAllRulesAndFacts(String policy) throws Exception {
        List<String> allRulesAndFacts = new ArrayList<String>();
        int lastSuccessfulIndex = 0;
        boolean allowNextIndexOfQuotes = true;
        boolean ignoreDot = false;
        boolean missingClosingQuotes = false;
        Pattern pattDot = Pattern.compile("\\.");
        Pattern pattQuotes = Pattern.compile("\"");
        Matcher matcherDot = pattDot.matcher(policy);
        Matcher matcherQuotes = pattQuotes.matcher(policy);
        while (matcherDot.find()) {
            if (ignoreDot && (matcherDot.start() < matcherQuotes.start())) continue;
            ignoreDot = false;
            while (true) {
                if (allowNextIndexOfQuotes) if (!matcherQuotes.find()) break;
                if (matcherQuotes.start() < matcherDot.start()) {
                    missingClosingQuotes = false;
                    while (true) {
                        if (!matcherQuotes.find()) {
                            missingClosingQuotes = true;
                            break;
                        }
                        if (!policy.startsWith("\\", matcherQuotes.start() - 1)) break;
                    }
                    if (missingClosingQuotes) throw new Exception("Unable to parse. Missing a closing quotes (\") in disclosure policy files.");
                    allowNextIndexOfQuotes = true;
                    if (matcherQuotes.start() > matcherDot.start()) {
                        ignoreDot = true;
                        break;
                    }
                } else {
                    allowNextIndexOfQuotes = false;
                    break;
                }
            }
            if (!ignoreDot) {
                allRulesAndFacts.add(policy.substring(lastSuccessfulIndex, matcherDot.start()));
                lastSuccessfulIndex = matcherDot.end();
            }
        }
        if (allRulesAndFacts.isEmpty()) throw new Exception("No rules parsed from disclosure policy files.");
        return allRulesAndFacts.toArray(new String[allRulesAndFacts.size()]);
    }
