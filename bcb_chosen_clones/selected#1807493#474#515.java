    private static String getProviderName(URL url, PrintStream err) {
        InputStream in = null;
        try {
            in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String result = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                int commentPos = line.indexOf('#');
                if (commentPos >= 0) {
                    line = line.substring(0, commentPos);
                }
                line = line.trim();
                int len = line.length();
                if (len != 0) {
                    if (result != null) {
                        print(err, "checkconfig.multiproviders", url.toString());
                        return null;
                    }
                    result = line;
                }
            }
            if (result == null) {
                print(err, "checkconfig.missingprovider", url.toString());
                return null;
            }
            return result;
        } catch (IOException e) {
            print(err, "configconfig.read", url.toString(), e);
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
