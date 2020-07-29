    @SuppressWarnings("unchecked")
    private List<String> getWordList() {
        IConfiguration config = Configurator.getDefaultConfigurator().getConfig(CONFIG_ID);
        List<String> wList = (List<String>) config.getObject("word_list");
        if (wList == null) {
            wList = new ArrayList<String>();
            InputStream resrc = null;
            try {
                resrc = new URL(list_url).openStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (resrc != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(resrc));
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        if (line.length() != 0) {
                            wList.add(line);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return wList;
    }
