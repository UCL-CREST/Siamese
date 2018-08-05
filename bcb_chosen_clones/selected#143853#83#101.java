    private List getPluginClassList(List pluginFileList) {
        ArrayList l = new ArrayList();
        for (Iterator i = pluginFileList.iterator(); i.hasNext(); ) {
            URL url = (URL) i.next();
            log.debug("Trying file " + url.toString());
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.length() == 0 || line.charAt(0) == '#') continue;
                    l.add(line);
                }
            } catch (Exception e) {
                log.warn("Could not load " + url, e);
            }
        }
        return l;
    }
