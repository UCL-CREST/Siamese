    private static String appletLoad(String file, Output OUT) {
        if (!urlpath.endsWith("/")) {
            urlpath += '/';
        }
        if (!urlpath.startsWith("http://")) {
            urlpath = "http://" + urlpath;
        }
        String url = "";
        if (file.equals("languages.txt")) {
            url = urlpath + file;
        } else {
            url = urlpath + "users/" + file;
        }
        try {
            StringBuffer sb = new StringBuffer(2000);
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            String a;
            while ((a = br.readLine()) != null) {
                sb.append(a).append('\n');
            }
            return sb.toString();
        } catch (Exception e) {
            OUT.println("load failed for file->" + file);
        }
        return "";
    }
