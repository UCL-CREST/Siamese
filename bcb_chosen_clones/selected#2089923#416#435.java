    public BigDecimal stringFind(VariableResolver resolver, String str, String pattern) throws ParserException {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        int found = 0;
        int matchId = nextMatchNo();
        resolver.setVariable("match." + matchId + ".groupCount", m.groupCount());
        while (m.find()) {
            found++;
            for (int i = 1; i < m.groupCount() + 1; i++) {
                resolver.setVariable("match." + matchId + ".m" + found + ".group" + i, m.group(i) == null ? "" : m.group(i));
                resolver.setVariable("match." + matchId + ".m" + found + ".group" + i + ".start", m.start(i));
                resolver.setVariable("match." + matchId + ".m" + found + ".group" + i + ".end", m.end(i));
            }
            resolver.setVariable("match." + matchId + ".m" + found + ".group0", m.group() == null ? "" : m.group());
            resolver.setVariable("match." + matchId + ".m" + found + ".group0.start", m.start());
            resolver.setVariable("match." + matchId + ".m" + found + ".group0.end", m.end());
        }
        resolver.setVariable("match." + matchId + ".matchCount", found);
        return BigDecimal.valueOf(matchId);
    }
