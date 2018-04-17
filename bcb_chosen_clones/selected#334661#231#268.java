    private final long getDiskForLinuxInfo(final String dirPath) {
        try {
            String dir = dirPath.startsWith("/") ? dirPath : "/" + dirPath;
            long space = -1;
            Process process;
            Runtime run = Runtime.getRuntime();
            String osName = System.getProperty("os.name").toLowerCase();
            String command = "";
            if (osName.startsWith("linux")) {
                command = "df -k " + dir;
            }
            process = run.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String freeSpace = "", line;
            while ((line = in.readLine()) != null) {
                if (line.length() > 0) {
                    freeSpace = line;
                }
            }
            if (freeSpace == null || freeSpace.length() == 0) {
                return -1;
            }
            process.destroy();
            freeSpace = freeSpace.trim().replaceAll("\\\\", "\\/");
            String[] results = freeSpace.split(" ");
            for (int i = results.length - 1; i > 0; i--) {
                try {
                    space = Long.parseLong(results[i]);
                    return space;
                } catch (NumberFormatException ex) {
                    continue;
                }
            }
        } catch (IOException e) {
            return -1;
        }
        return -1;
    }
