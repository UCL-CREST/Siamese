    private static String getNextInputToken(boolean skip) {
        final String delimiters = " \t\n\r\f";
        String token = null;
        try {
            if (reader == null) {
                reader = new StringTokenizer(in.readLine(), delimiters, true);
            }
            while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
                while (!reader.hasMoreTokens()) {
                    reader = new StringTokenizer(in.readLine(), delimiters, true);
                }
                token = reader.nextToken();
            }
        } catch (Exception exception) {
            token = null;
        }
        return token;
    }
