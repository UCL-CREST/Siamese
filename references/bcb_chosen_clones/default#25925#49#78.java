    public void run() {
        String outConsole = "";
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] cl = buildCommandLine();
            if (!PatchManager.mute) {
                for (int i = 0; i < cl.length; i++) System.out.println(cl[i] + "__");
                System.out.println("starting the process...");
            }
            final Process process = runtime.exec(cl);
            InputStreamReader in = new InputStreamReader(process.getInputStream());
            in = new InputStreamReader(process.getInputStream(), in.getEncoding());
            BufferedReader reader = new BufferedReader(in);
            String line = "";
            try {
                while ((line = reader.readLine()) != null) {
                    if (myMaster != null) myMaster.writeOnLog("Console : " + line); else if (!PatchManager.mute) System.out.println("Console : " + line);
                    outConsole += line + "\n";
                }
            } finally {
                reader.close();
            }
        } catch (Exception ioe) {
            if (myMaster != null) myMaster.writeOnLog("Console : " + ioe.toString()); else System.out.println("Console : " + ioe.toString());
        }
        if (myMaster != null) {
            myMaster.releaseToken();
            myMaster.writeOnLog("I released a token for having proceeded : " + arguments[0]);
        }
    }
