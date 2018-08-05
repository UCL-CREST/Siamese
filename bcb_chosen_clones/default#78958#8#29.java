    public String Execute(String app, String directory, String arguments) {
        Process process = null;
        int AppPid = 0;
        String processpid = "";
        String LaunchHelper = System.getProperty("java.io.tmpdir") + "AppEmbed\\" + "AppLauncher.exe";
        System.err.println("App Loader is at: " + LaunchHelper);
        directory = "\"" + directory + "\"";
        app = "\"" + app + " " + Main.ARGUMENTS + "\"";
        System.err.println("Attempting to send following command to LaunchHelper: " + app + " " + directory);
        try {
            String[] Program = { LaunchHelper, app, directory };
            process = Runtime.getRuntime().exec(Program);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String out;
            while ((out = input.readLine()) != null) {
                processpid = out;
            }
            input.close();
        } catch (Exception ex) {
        }
        return processpid;
    }
