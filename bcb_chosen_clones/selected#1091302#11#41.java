    public static void main(String args[]) {
        if (args.length < 1) {
            printUsage();
        }
        URL url;
        BufferedReader in = null;
        try {
            url = new URL(args[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                System.out.println("Response code " + responseCode + " means there was an error reading url " + args[0]);
            }
        } catch (IOException e) {
            System.err.println("IOException attempting to read url " + args[0]);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ignore) {
                }
            }
        }
    }
