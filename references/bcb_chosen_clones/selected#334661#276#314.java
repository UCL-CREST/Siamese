    private final long getDiskForWindowsInfo(String dirPath) {
        try {
            long space = -1;
            Process process;
            Runtime run = Runtime.getRuntime();
            String osName = LSystem.OS_NAME;
            String command = "";
            if (osName.startsWith("windows") && osName.indexOf("98") == -1) {
                command = "cmd.exe /c dir " + dirPath;
            } else if (osName.startsWith("windows") && osName.indexOf("98") != -1) {
                command = "command.com /c dir " + dirPath;
            }
            process = run.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String freeSpace = "", line;
            while ((line = in.readLine()) != null) {
                freeSpace = line;
            }
            if (freeSpace == null) {
                return -1;
            }
            process.destroy();
            freeSpace = freeSpace.trim();
            freeSpace = freeSpace.replaceAll("\\\\\\\\\\\\\\\\.", "");
            freeSpace = freeSpace.replaceAll(",", "");
            String[] results = freeSpace.split(" ");
            for (int i = 1; i < results.length; i++) {
                try {
                    space = Long.parseLong(results[i]);
                    return space;
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
            return space;
        } catch (IOException e) {
            return -1;
        }
    }
