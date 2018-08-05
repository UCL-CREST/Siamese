    int old_javah(List<String> options, File outDir, File bootclasspath, String className) throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<String>();
        cmd.add(old_javah_cmd.getPath());
        cmd.addAll(options);
        cmd.add("-d");
        cmd.add(outDir.getPath());
        cmd.add("-bootclasspath");
        cmd.add(bootclasspath.getPath());
        cmd.add(className);
        System.err.println("old_javah: " + cmd);
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        System.err.println("old javah out: " + sb.toString());
        return p.waitFor();
    }
