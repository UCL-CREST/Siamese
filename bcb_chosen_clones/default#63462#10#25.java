    String runScript(String scriptName) {
        String data = "";
        try {
            URL url = new URL(getCodeBase().toString() + scriptName);
            InputStream in = url.openStream();
            BufferedInputStream buffIn = new BufferedInputStream(in);
            do {
                int temp = buffIn.read();
                if (temp == -1) break;
                data = data + (char) temp;
            } while (true);
        } catch (Exception e) {
            data = "error!";
        }
        return data;
    }
