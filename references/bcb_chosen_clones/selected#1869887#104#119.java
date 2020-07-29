    public static String ReadURLStringAndWrite(URL url, String str) throws Exception {
        String stringToReverse = URLEncoder.encode(str, "UTF-8");
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(stringToReverse);
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String decodedString;
        String back = "";
        while ((decodedString = in.readLine()) != null) {
            back += decodedString + "\n";
        }
        in.close();
        return back;
    }
