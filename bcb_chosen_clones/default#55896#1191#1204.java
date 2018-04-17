    public boolean checkForModules() {
        try {
            String line;
            String modules = "cat /proc/modules | grep raw1394";
            String[] module_cmd = { "/bin/sh", "-c", modules };
            Process p = Runtime.getRuntime().exec(module_cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = in.readLine();
            p.waitFor();
            if (line == null) return false; else return true;
        } catch (Exception ioe) {
            return false;
        }
    }
