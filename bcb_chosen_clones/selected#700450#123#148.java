    public void refreshStatus() {
        if (!enabledDisplay) return;
        try {
            String url = getServerFortURL();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String data = null;
            int counter = 0;
            while ((data = reader.readLine()) != null && counter < 9) {
                status[counter] = UNKNOWN;
                if (data.matches(".*_alsius.gif.*")) {
                    status[counter] = ALSIUS;
                    counter++;
                }
                if (data.matches(".*_syrtis.gif.*")) {
                    status[counter] = SYRTIS;
                    counter++;
                }
                if (data.matches(".*_ignis.gif.*")) {
                    status[counter] = IGNIS;
                    counter++;
                }
            }
        } catch (Exception exc) {
            for (int i = 0; i < status.length; i++) status[i] = UNKNOWN;
        }
    }
