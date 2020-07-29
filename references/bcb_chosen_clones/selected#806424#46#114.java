    public void generateExe() throws IOException {
        new File(System.getProperty("java.io.tmpdir") + "/config.txt").delete();
        new File(System.getProperty("java.io.tmpdir") + "/installer.7z").delete();
        if (System.getProperty("os.name").startsWith("Windows")) p = Runtime.getRuntime().exec(new String[] { RunnerClass.homedir + "utils/wrappers/izpack2exe/7za.exe", "a", "-t7z", "-mx=9", "-ms=off", System.getProperty("java.io.tmpdir") + "/installer.7z", jarF.getAbsolutePath() }, null, new File(RunnerClass.homedir + "utils/wrappers/izpack2exe")); else p = Runtime.getRuntime().exec(new String[] { "7za", "a", "-t7z", "-mx=9", "-ms=off", System.getProperty("java.io.tmpdir") + "/installer.7z", jarF.getAbsolutePath() }, null, new File(RunnerClass.homedir + "utils/wrappers/izpack2exe"));
        final BufferedReader inps = new BufferedReader(new InputStreamReader(p.getInputStream()));
        final BufferedReader errs = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    fr.taskMsg.setText("Compressing");
                    fr.taskPB.setIndeterminate(true);
                    String c;
                    long l = System.currentTimeMillis();
                    while ((c = inps.readLine()) != null) {
                        RunnerClass.logger.info((System.currentTimeMillis() - l) + " Input: " + c);
                        l = System.currentTimeMillis();
                    }
                    if (crashed) fr.crash();
                    jarF.delete();
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String c;
                    while ((c = errs.readLine()) != null) {
                        crashed = true;
                        RunnerClass.logger.severe("Error: " + c);
                    }
                } catch (IOException ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
        }
        if (stopped) return;
        PrintWriter out = new PrintWriter(new FileWriter(System.getProperty("java.io.tmpdir") + "/config.txt"));
        out.println(";!@Install@!UTF-8!");
        out.println("Title=\"Installer\"");
        out.println("InstallPath=\"%temp%\\\\packjacket-installer\"");
        out.println("ExtractDialogText=\"Extracting installer\"");
        out.println("Progress=\"yes\"");
        out.println("GUIFlags=\"4+32\"");
        out.println("GUIMode=\"1\"");
        out.println("ExecuteFile=\"" + jarF.getName() + "\"");
        out.println(";!@InstallEnd@!");
        out.close();
        exeF.delete();
        FileOutputStream outFile = new FileOutputStream(new File(exeF.getAbsolutePath()), true);
        writeFile(new FileInputStream(new File(RunnerClass.homedir + "utils/wrappers/izpack2exe/7zS.sfx")), outFile);
        writeFile(new FileInputStream(new File(System.getProperty("java.io.tmpdir") + "/config.txt")), outFile);
        writeFile(new FileInputStream(new File(System.getProperty("java.io.tmpdir") + "/installer.7z")), outFile);
        outFile.close();
        fr.setPB(100);
        fr.taskPB.setIndeterminate(false);
        fr.taskMsg.setText("Completed");
        new File(System.getProperty("java.io.tmpdir") + "/config.txt").delete();
        new File(System.getProperty("java.io.tmpdir") + "/installer.7z").delete();
    }
