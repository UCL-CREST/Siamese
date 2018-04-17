    public static int exec(String script) throws IOException, InterruptedException {
        Process shell;
        shell = Runtime.getRuntime().exec(script);
        BufferedReader in = new BufferedReader(new InputStreamReader(shell.getInputStream()));
        String line = null;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        return shell.waitFor();
    }
