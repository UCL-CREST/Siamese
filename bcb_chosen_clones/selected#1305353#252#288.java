    private String sendMail() throws IOException {
        String msg = StringEscapeUtils.escapeHtml(message.getText());
        StringBuffer buf = new StringBuffer();
        buf.append(encode("n", name.getText()));
        buf.append("&").append(encode("e", email.getText()));
        buf.append("&").append(encode("r", recpt.getText()));
        buf.append("&").append(encode("m", msg));
        buf.append("&").append(encode("s", score));
        buf.append("&").append(encode("i", calcScoreImage()));
        buf.append("&").append(encode("c", digest(recpt.getText() + "_" + score)));
        URL url = new URL("http://www.enerjy.com/share/mailit.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        boolean haveOk = false;
        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(buf.toString());
            writer.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (String line = reader.readLine(); null != line; line = reader.readLine()) {
                if (line.startsWith("err:")) {
                    return line.substring(4);
                } else if (line.equals("ok")) {
                    haveOk = true;
                }
            }
        } finally {
            StreamUtils.close(writer);
            StreamUtils.close(reader);
        }
        if (!haveOk) {
            return "The server did not return a response.";
        }
        return null;
    }
