    String getNagiosParameter(String sToken) {
        String retval = "", line;
        StringBuffer config = parseConfig(NAG_CONFIG);
        BufferedReader br = new BufferedReader(new StringReader(config.toString()));
        try {
            while (((line = br.readLine()) != null) && (retval == "")) {
                StringTokenizer tokens = new StringTokenizer(line, "=");
                while (tokens.hasMoreTokens()) {
                    if (tokens.nextToken().trim().equals(sToken)) {
                        retval = tokens.nextToken().trim();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return (retval);
    }
