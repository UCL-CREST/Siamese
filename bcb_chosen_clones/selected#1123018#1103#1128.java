    private void translate(String sender, String message) {
        StringTokenizer st = new StringTokenizer(message, " ");
        message = message.replaceFirst(st.nextToken(), "");
        String typeCode = st.nextToken();
        message = message.replaceFirst(typeCode, "");
        try {
            String data = URLEncoder.encode(message, "UTF-8");
            URL url = new URL("http://babelfish.altavista.com/babelfish/tr?doit=done&urltext=" + data + "&lp=" + typeCode);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("input type=hidden name=\"q\"")) {
                    String[] tokens = line.split("\"");
                    sendMessage(sender, tokens[3]);
                }
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
        }
    }
