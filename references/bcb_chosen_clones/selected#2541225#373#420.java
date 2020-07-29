    public void sorter() {
        String inputLine1, inputLine2;
        String epiNames[] = new String[1000];
        String epiEpisodes[] = new String[1000];
        int lineCounter = 0;
        try {
            String pluginDir = pluginInterface.getPluginDirectoryName();
            String eplist_file = pluginDir + System.getProperty("file.separator") + "EpisodeList.txt";
            File episodeList = new File(eplist_file);
            if (!episodeList.isFile()) {
                episodeList.createNewFile();
            }
            final BufferedReader in = new BufferedReader(new FileReader(episodeList));
            while ((inputLine1 = in.readLine()) != null) {
                if ((inputLine2 = in.readLine()) != null) {
                    epiNames[lineCounter] = inputLine1;
                    epiEpisodes[lineCounter] = inputLine2;
                    lineCounter++;
                }
            }
            in.close();
            int epiLength = epiNames.length;
            for (int i = 0; i < (lineCounter); i++) {
                for (int j = 0; j < (lineCounter - 1); j++) {
                    if (epiNames[j].compareToIgnoreCase(epiNames[j + 1]) > 0) {
                        String temp = epiNames[j];
                        epiNames[j] = epiNames[j + 1];
                        epiNames[j + 1] = temp;
                        String temp2 = epiEpisodes[j];
                        epiEpisodes[j] = epiEpisodes[j + 1];
                        epiEpisodes[j + 1] = temp2;
                    }
                }
            }
            File episodeList2 = new File(eplist_file);
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(episodeList2));
            for (int i = 0; i <= lineCounter; i++) {
                if (epiNames[i] == null) {
                    break;
                }
                bufWriter.write(epiNames[i] + "\n");
                bufWriter.write(epiEpisodes[i] + "\n");
            }
            bufWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
