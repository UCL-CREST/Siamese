    public static String load(String id) {
        String xml = "";
        if (id.length() < 5) return "";
        try {
            working = true;
            URL url = new URL("http://pastebin.com/download.php?i=" + id);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            xml = "";
            String str;
            while ((str = reader.readLine()) != null) {
                xml += str;
            }
            reader.close();
            working = false;
            return xml.toString();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, " Load error");
        }
        working = false;
        return xml;
    }
