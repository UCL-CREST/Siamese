    public static void showPDF(String path) throws IOException {
        if (Desktop.isDesktopSupported()) try {
            File file = new File(path);
            file = file.getCanonicalFile();
            Desktop.getDesktop().browse(file.toURI());
        } catch (Exception e) {
            log.error(e);
        } else {
            String os = System.getProperty("os.name").toLowerCase();
            String runCmd = null;
            File file = new File(path);
            file = file.getCanonicalFile();
            if (os.startsWith("windows")) {
                runCmd = "rundll32 SHELL32.DLL,ShellExec_RunDLL \"" + file.getAbsolutePath() + "\"";
            }
            if (os.startsWith("mac")) {
                runCmd = "open " + path + "";
            }
            if (runCmd == null) {
                runCmd = "acroread '" + file.getAbsolutePath() + "'";
            }
            System.out.println(runCmd);
            Runtime.getRuntime().exec(runCmd);
        }
    }
