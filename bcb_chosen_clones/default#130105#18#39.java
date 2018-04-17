    public static void runCommand(ArrayList<String> cmd) {
        System.out.println("Running " + cmd);
        try {
            Process process = new ProcessBuilder().command(cmd).redirectErrorStream(true).start();
            try {
                InputStream in = process.getInputStream();
                OutputStream out = process.getOutputStream();
                BufferedReader bfrd = new BufferedReader(new InputStreamReader(in));
                String prnt = bfrd.readLine();
                while (prnt != null) {
                    System.out.println(prnt);
                    prnt = bfrd.readLine();
                }
            } catch (Exception ex) {
                System.out.println("Error.");
            } finally {
                process.destroy();
            }
        } catch (IOException io) {
            System.out.println("I/O Error.");
        }
    }
