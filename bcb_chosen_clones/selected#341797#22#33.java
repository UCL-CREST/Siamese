    public static void loadMessages(String filename, Map<String, String> map) throws FileNotFoundException, IOException {
        String line;
        URL url = CurrentLocale.class.getResource("MessagesBundle_" + filename + ".properties");
        InputStreamReader isr = new InputStreamReader(url.openStream());
        BufferedReader br = new BufferedReader(isr);
        while ((line = br.readLine()) != null) {
            String[] l = line.split("=", 2);
            map.put(l[0].trim(), l[1].trim());
        }
        br.close();
        isr.close();
    }
