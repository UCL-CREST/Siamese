    public SystemWrapper(String commandstring, String path) {
        try {
            String cmdline = stringArrayToString(commandstring.split("\\[\\]"));
            (Megatron.getLog()).debug("Trying to invoke: " + cmdline + "\n");
            Process p = (Runtime.getRuntime()).exec(commandstring.split("\\[\\]"), null, new File(path));
            String line;
            _noutput = new LinkedList<String>();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = r.readLine()) != null) _noutput.add(line);
            boolean done = false;
            while (!done) {
                try {
                    _retval = p.waitFor();
                    done = true;
                    (Megatron.getLog()).debug("Process finished: " + cmdline + "\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
