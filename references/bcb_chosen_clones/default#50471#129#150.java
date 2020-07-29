    boolean grep(String resource_name, File f) throws IOException {
        if (this.verbose) log.println(f);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(jar_cmd + " -tf " + f.getAbsolutePath());
        InputStream proc_out = proc.getInputStream();
        BufferedReader reader = null;
        boolean found = false;
        try {
            reader = new BufferedReader(new InputStreamReader(proc_out));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (line.indexOf(resource_name) != -1) {
                    found = true;
                    out.println(line.trim() + "\t" + f);
                }
            }
            return found;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
