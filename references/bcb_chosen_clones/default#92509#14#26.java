    public static void test1(String cmd) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(cmd);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running is test1:");
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
