    public String captureMediaserverInfo() {
        String cm = "ps mediaserver";
        String memoryUsage = null;
        int ch;
        try {
            Process p = Runtime.getRuntime().exec(cm);
            InputStream in = p.getInputStream();
            StringBuffer sb = new StringBuffer(512);
            while ((ch = in.read()) != -1) {
                sb.append((char) ch);
            }
            memoryUsage = sb.toString();
        } catch (IOException e) {
            Log.v(TAG, e.toString());
        }
        String[] poList = memoryUsage.split("\r|\n|\r\n");
        String memusage = poList[1].concat("\n");
        return memusage;
    }
