    private static Vector<String> getIgnoreList() {
        try {
            URL url = DeclarationTranslation.class.getClassLoader().getResource("ignorelist");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            Vector<String> ret = new Vector<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                ret.add(line);
            }
            return ret;
        } catch (Exception e) {
            return null;
        }
    }
