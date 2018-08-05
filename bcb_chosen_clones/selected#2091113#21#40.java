    private String loadStatusResult() {
        try {
            URL url = new URL(getServerUrl());
            InputStream input = url.openStream();
            InputStreamReader is = new InputStreamReader(input, "utf-8");
            BufferedReader reader = new BufferedReader(is);
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }
