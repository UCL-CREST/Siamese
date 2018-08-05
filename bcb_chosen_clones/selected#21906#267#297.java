    public static String recoverPassword(String token) {
        try {
            token = encryptGeneral1(token);
            String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
            URL url = new URL("https://mypasswords-server.appspot.com/recover_password");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder finalResult = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                finalResult.append(line);
            }
            wr.close();
            rd.close();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(finalResult.toString())));
            document.normalizeDocument();
            Element root = document.getDocumentElement();
            String password = root.getTextContent();
            password = decryptGeneral1(password);
            return password;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
