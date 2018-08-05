    public static void readData(String dataFile, List data) throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader(dataFile));
        Map map = new HashMap();
        String line;
        while ((line = fin.readLine()) != null) {
            StringTokenizer strTok = new StringTokenizer(line, " \t\r\n");
            while (strTok.hasMoreTokens()) {
                String token = strTok.nextToken();
                if (token.indexOf(".") >= 0 && token.indexOf("/") <= 0) {
                    map.put(token, token);
                }
            }
        }
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            System.out.println((String) it.next());
        }
    }
