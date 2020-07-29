    public static void initalize() {
        try {
            Process proc = Runtime.getRuntime().exec("gksudo -m OBD2ner /home/adam/obd2ner &");
            BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = read.readLine()) != null) {
                System.out.println(line);
                if (line.contains("active>")) {
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
