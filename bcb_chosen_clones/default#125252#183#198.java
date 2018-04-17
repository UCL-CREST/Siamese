    public String callPDManager(String[] args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.directory(new File(".." + File.separator + "bin"));
            Process process = processBuilder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            System.out.println("result: " + result + "\r\n");
            return result;
        } catch (IOException e) {
            System.out.println("ERROR: " + e + "\r\n");
        }
        return "";
    }
