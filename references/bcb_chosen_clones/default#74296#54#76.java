    public void execute(String script) {
        File scriptFile = new File(SCRIPT_FILE_NAME);
        try {
            BufferedWriter bufWrtr = new BufferedWriter(new FileWriter(scriptFile));
            String line = null;
            bufWrtr.write(script);
            bufWrtr.close();
            String exec = "osascript " + SCRIPT_FILE_NAME;
            System.out.println(exec);
            Process proc = Runtime.getRuntime().exec(exec);
            DataInputStream in = new DataInputStream(proc.getInputStream());
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
