    synchronized List<String> getDatasetsList(String surl) {
        if (datasetsList == null) {
            datasetsList = new HashMap<String, List<String>>();
        }
        List<String> result = datasetsList.get(surl);
        if (result == null) {
            BufferedReader reader = null;
            try {
                URL url = new URL(surl + "?server=list");
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String s = reader.readLine();
                ArrayList<String> list = new ArrayList<String>();
                while (s != null) {
                    list.add(s);
                    s = reader.readLine();
                }
                datasetsList.put(surl, list);
            } catch (IOException ex) {
                Logger.getLogger(Das2ServerDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(Das2ServerDataSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return datasetsList.get(surl);
    }
