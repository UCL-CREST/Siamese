    private CTAGS_Buffer parseFile(String fn, String[] arguments) throws IOException {
        arguments[6] = fn;
        Process ctags = Runtime.getRuntime().exec(arguments);
        BufferedReader in = new BufferedReader(new InputStreamReader(ctags.getInputStream()));
        CTAGS_Buffer buff = new CTAGS_Buffer(this);
        String line = new String();
        while ((line = in.readLine()) != null) {
            int index = line.lastIndexOf(";\"\t");
            if (index < 0) {
                continue;
            }
            buff.add(new CTAGS_Entry(line));
        }
        return buff;
    }
