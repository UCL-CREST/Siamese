    private List<Symbol> findSymbols(String regex, String sequence) {
        List<Symbol> symbols = new ArrayList<Symbol>();
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(sequence);
        while (matcher.find()) {
            symbols.add(new Symbol(regex, new Offsets(matcher.start(), matcher.end())));
        }
        return symbols;
    }
