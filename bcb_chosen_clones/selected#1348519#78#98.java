    public static String ReadURLString(String str) throws IOException {
        try {
            URL url = new URL(str);
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            String line = "";
            int i = 0;
            while ((inputLine = in.readLine()) != null) {
                line += inputLine + "\n";
            }
            is.close();
            isr.close();
            in.close();
            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
