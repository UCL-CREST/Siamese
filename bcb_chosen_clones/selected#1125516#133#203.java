    private void load(Runestone stone) throws RunesExceptionRuneExecution, RunesExceptionNoSuchContent {
        final Tokeniser tokeniser = stone.<Tokeniser>getContent("tokeniser").iterator().next();
        rules = new HashMap<Node, List<GazRule>>();
        System.out.println("Loading Gaz from: " + _url);
        if (_url == null) return;
        BufferedReader typesIn = null, entryIn = null;
        try {
            typesIn = new BufferedReader(new InputStreamReader(_url.openStream()));
            String tData = typesIn.readLine();
            while (tData != null) {
                Map<String, Map> gaz = new HashMap<String, Map>();
                String[] data = tData.split(":");
                URL listURL = new URL(_url, data[0]);
                System.err.println("Loading from " + listURL);
                entryIn = new BufferedReader(new InputStreamReader(listURL.openStream()));
                String entry = entryIn.readLine();
                while (entry != null) {
                    entry = entry.trim();
                    if (!entry.equals("")) {
                        final List<Token> tokens;
                        try {
                            tokens = tokeniser.tokenise(entry);
                        } catch (IOException e) {
                            throw new RunesExceptionRuneExecution(e, this);
                        }
                        Map<String, Map> m = gaz;
                        for (Token t : tokens) {
                            String token = t.getString();
                            if (_case_insensitive_gazetteer) token = token.toLowerCase();
                            @SuppressWarnings("unchecked") Map<String, Map> next = m.get(token);
                            if (next == null) next = new HashMap<String, Map>();
                            m.put(token, next);
                            m = next;
                        }
                        m.put(STOP, null);
                    }
                    entry = entryIn.readLine();
                }
                for (Map.Entry<String, Map> er : gaz.entrySet()) {
                    NodeAbstract start = new NodeStringImpl(TOKEN_TYPE, null);
                    if (_case_insensitive_gazetteer) {
                        start.addFeature(TOKEN_HAS_STRING, new NodeRegExpImpl(TOKEN_STRING, "(?i:" + er.getKey().toLowerCase() + ")"));
                    } else {
                        start.addFeature(TOKEN_HAS_STRING, new NodeStringImpl(TOKEN_STRING, er.getKey()));
                    }
                    @SuppressWarnings("unchecked") Transition transition = mapToTransition(er.getValue());
                    String major = data[1];
                    String minor = (data.length == 3 ? data[2] : null);
                    GazRule gr = new GazRule(major, minor, transition);
                    List<GazRule> rl = rules.get(start);
                    if (rl == null) rl = new ArrayList<GazRule>();
                    rl.add(gr);
                    rules.put(start, rl);
                }
                entryIn.close();
                System.err.println(rules.size());
                tData = typesIn.readLine();
            }
        } catch (IOException e) {
            throw new RunesExceptionRuneExecution(e, this);
        } finally {
            try {
                if (typesIn != null) typesIn.close();
            } catch (IOException e) {
            }
            try {
                if (entryIn != null) entryIn.close();
            } catch (IOException e) {
            }
        }
    }
