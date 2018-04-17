    public static String checkPublicIP() {
        String ipAddress = null;
        try {
            URL url;
            url = new URL("http://checkip.dyndns.org/");
            InputStreamReader in = new InputStreamReader(url.openStream());
            BufferedReader buffer = new BufferedReader(in);
            String line;
            Pattern p = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
            while ((line = buffer.readLine()) != null) {
                if (line.indexOf("IP Address:") != -1) {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        ipAddress = m.group();
                        break;
                    }
                }
            }
            buffer.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
