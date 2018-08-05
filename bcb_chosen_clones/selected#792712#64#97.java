    private static void loadListFromRecouces(String category, URL url, DataSetArray<DataSetList> list, final StatusLineManager slm) {
        i = 0;
        try {
            if (url == null) return;
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                strLine = strLine.trim();
                i++;
                if (slm != null) {
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            slm.setMessage(_("Importing country code " + i));
                        }
                    });
                }
                if (!strLine.isEmpty() && !strLine.startsWith("#")) {
                    String parts[] = strLine.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        key = DataUtils.replaceAllAccentedChars(key).toUpperCase();
                        DataSetList newListEntry = new DataSetList(category, key, value);
                        list.addNewDataSetIfNew(newListEntry);
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            Logger.logError(e, "Error loading " + url.getFile());
        }
    }
