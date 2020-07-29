    private Set read() throws IOException {
        URL url = new URL(urlPrefix + channelId + ".dat");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = in.readLine();
        Set programs = new HashSet();
        while (line != null) {
            String[] values = line.split("~");
            if (values.length != 23) {
                throw new RuntimeException("error: incorrect format for radiotimes information");
            }
            Program program = new RadioTimesProgram(values, channelId);
            programs.add(program);
            line = in.readLine();
        }
        return programs;
    }
