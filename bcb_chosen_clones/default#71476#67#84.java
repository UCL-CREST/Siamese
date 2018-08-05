    public static String queryRegistry(String cmd, String token) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String result = reader.getResult();
            int p = result.indexOf(token);
            if (p == -1) {
                return null;
            }
            return result.substring(p + token.length()).trim();
        } catch (Exception e) {
            System.out.println("Query of Registry failed.");
            return null;
        }
    }
