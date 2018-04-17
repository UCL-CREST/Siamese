    public void writeMP3toStream(File file, OutputStream os) {
        System.out.println("Writing mp3 to stream for file " + file);
        Process p = null;
        BufferedInputStream is = null;
        try {
            String convertcommand;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                convertcommand = this.getScriptDir().toString() + "\\" + getCommand() + ".bat";
            } else {
                convertcommand = this.getScriptDir().toString() + "/" + getCommand();
            }
            String[] cmd = { convertcommand, "\"" + file + "\"" };
            byte input[] = new byte[1000];
            System.out.println(convertcommand);
            System.out.println(file.toString());
            p = Runtime.getRuntime().exec(cmd, null, this.getScriptDir());
            is = new BufferedInputStream(p.getInputStream());
            ;
            BufferedInputStream es = new BufferedInputStream(p.getErrorStream());
            ;
            Consumer a = new Consumer(es);
            new Thread(a).start();
            int count;
            while ((count = is.read(input)) != -1) {
                os.write(input, 0, count);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (p != null) p.destroy();
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
