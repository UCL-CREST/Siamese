    public void load(String file) throws IOException {
        String s;
        String z;
        String token;
        StringTokenizer t;
        int i;
        BufferedReader in = new BufferedReader(new FileReader(file));
        ArrayList tokenList = new ArrayList();
        while ((s = in.readLine()) != null) {
            if (s.startsWith("#") == true) continue;
            t = new StringTokenizer(s, "\t");
            while (t.hasMoreTokens()) {
                z = t.nextToken();
                tokenList.add(z);
            }
            for (i = 0; i < tokenList.size(); i++) {
                token = (String) tokenList.get(i);
                if (token.length() != 0) {
                    break;
                }
            }
            if (i < tokenList.size()) {
                m_line.add(tokenList);
                tokenList = new ArrayList();
            } else {
                tokenList.clear();
            }
        }
        in.close();
    }
