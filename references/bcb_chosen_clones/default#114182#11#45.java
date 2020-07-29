    SkyLocalizer(String dataDir) throws StellariumException {
        String cultureName;
        String cultureDirectory;
        String fileName = dataDir + "skycultures.fab";
        try {
            File fic = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(fic));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    if (st.hasMoreTokens()) {
                        cultureName = st.nextToken();
                        if (st.hasMoreTokens()) {
                            cultureDirectory = st.nextToken();
                            nameToDir.put(cultureName, cultureDirectory);
                            dirToName.put(cultureDirectory, cultureName);
                        }
                    }
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new StellariumException("Could not create SkyLocalizer with dataDir=" + dataDir);
        }
        localeToName.put("eng", "English");
        localeToName.put("esl", "Spanish");
        localeToName.put("fra", "French");
        localeToName.put("haw", "Hawaiian");
        for (Object o : localeToName.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            nameToLocale.put((String) entry.getValue(), (String) entry.getKey());
        }
    }
