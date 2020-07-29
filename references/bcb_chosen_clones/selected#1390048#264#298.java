    private void readVersion() {
        URL url = ClassLoader.getSystemResource("version");
        if (url == null) {
            return;
        }
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Version=")) {
                    version = (line.split("="))[1];
                }
                if (line.startsWith("Revision=")) {
                    revision = (line.split("="))[1];
                }
                if (line.startsWith("Date=")) {
                    String sSec = (line.split("="))[1];
                    Long lSec = Long.valueOf(sSec);
                    compileDate = new Date(lSec);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return;
    }
