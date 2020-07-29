    public List<ParsedAxiom> parse(File rules, boolean secondAnte) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(rules));
        String ruleString = new String();
        String s = new String();
        while ((s = in.readLine()) != null) {
            ruleString = ruleString + s;
        }
        String patternRegex;
        if (secondAnte) {
            patternRegex = "(\\d+ <- \\d+ \\d+  \\(\\d+.\\d, \\d+.\\d\\))";
        } else {
            patternRegex = "(\\d+ <- \\d+  \\(\\d+.\\d, \\d+.\\d\\))";
        }
        List<ParsedAxiom> axioms = new ArrayList<ParsedAxiom>();
        Pattern pattern = Pattern.compile(patternRegex);
        Matcher matcher = pattern.matcher(ruleString);
        while (matcher.find()) {
            String rule = ruleString.substring(matcher.start(), matcher.end());
            Pattern p = Pattern.compile("[\\d.]+");
            Matcher m = p.matcher(rule);
            m.find();
            int cons = Integer.parseInt(rule.substring(m.start(), m.end()));
            m.find();
            int ante = Integer.parseInt(rule.substring(m.start(), m.end()));
            int ante2 = -1;
            if (secondAnte) {
                m.find();
                ante2 = Integer.parseInt(rule.substring(m.start(), m.end()));
            }
            m.find();
            double supp = Double.parseDouble(rule.substring(m.start(), m.end()));
            m.find();
            double conf = Double.parseDouble(rule.substring(m.start(), m.end()));
            if (secondAnte) {
                axioms.add(new ParsedAxiom(ante, ante2, cons, supp, conf));
            }
            axioms.add(new ParsedAxiom(ante, cons, supp, conf));
        }
        return axioms;
    }
