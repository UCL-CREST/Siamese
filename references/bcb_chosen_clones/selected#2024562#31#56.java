    private static void grab(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        BufferedReader in = null;
        StringBuffer sb = new StringBuffer();
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        boolean f = false;
        while ((inputLine = in.readLine()) != null) {
            inputLine = inputLine.trim();
            if (inputLine.startsWith("<tbody>")) {
                f = true;
                continue;
            }
            if (inputLine.startsWith("</table>")) {
                f = false;
                continue;
            }
            if (f) {
                sb.append(inputLine);
                sb.append("\n");
            }
        }
        process(sb.toString());
    }
