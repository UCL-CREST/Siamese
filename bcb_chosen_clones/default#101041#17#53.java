    public void speak() {
        String osName = System.getProperty("os.name");
        String[] cmd = new String[3];
        parser.getFullText().read();
        if (osName.equals("Windows NT") || osName.equals("Windows XP")) {
            cmd[0] = "cmd.exe";
            cmd[1] = "/C";
            cmd[2] = "space\\mbrola.exe space\\" + voiceDB.getFileName() + " space\\temp.pho space\\temp.au";
        } else if (osName.equals("Windows 95")) {
            cmd[0] = "command.com";
            cmd[1] = "/C";
            cmd[2] = "space\\mbrola.exe space\\" + voiceDB.getFileName() + " space\\temp.pho space\\temp.au";
        } else if (osName.equals("Linux")) {
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
            cmd[2] = "space/mbrola-linux-i386 space/" + voiceDB.getFileName() + " space/temp.pho space/temp.au";
        } else {
            System.out.println("Unsupported OS");
            System.exit(1);
        }
        Runtime rt = Runtime.getRuntime();
        try {
            Process proc = rt.exec(cmd);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            proc.waitFor();
            InputStream in = new FileInputStream("space" + File.separator + "temp.au");
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
