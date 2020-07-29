    public List<String> loadList(String name) {
        List<String> ret = new ArrayList<String>();
        try {
            URL url = getClass().getClassLoader().getResource("lists/" + name + ".utf-8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                ret.add(line);
            }
            reader.close();
        } catch (IOException e) {
            showError("No se puede cargar la lista de valores: " + name, e);
        }
        return ret;
    }
