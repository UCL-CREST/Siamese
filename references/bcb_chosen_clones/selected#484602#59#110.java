    public void writePCMtoStream(File file, OutputStream os) {
        System.out.println("Writing pcm of " + file.toString());
        Process p = null;
        BufferedInputStream is = null;
        try {
            String convertcommand;
            String argumentstring;
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                convertcommand = this.getScriptDir().toString() + "\\" + getCommand() + ".bat";
                argumentstring = "\"" + file + "\"";
            } else {
                convertcommand = this.getScriptDir().toString() + "/" + getCommand();
                argumentstring = file.getCanonicalPath();
            }
            String[] cmd = { convertcommand, argumentstring };
            byte input[] = new byte[1000];
            System.out.println(convertcommand);
            System.out.println(cmd[1]);
            p = Runtime.getRuntime().exec(cmd, null, this.getScriptDir());
            is = new BufferedInputStream(p.getInputStream());
            ;
            BufferedInputStream es = new BufferedInputStream(p.getErrorStream());
            ;
            Consumer a = new Consumer(es);
            new Thread(a).start();
            Consumer b = new Consumer(is);
            new Thread(b).start();
            FileInputStream fis = new FileInputStream("C:\\temp\\buffer.wmv");
            File testFile = new File("C:\\temp\\buffer.wmv");
            System.out.println("Reading from file");
            int count;
            long counter = 0;
            while ((count = fis.read(input)) != -1) {
                os.write(input, 0, count);
                counter += input.length;
                long waitcounter = 0;
                while (counter + 4096 > testFile.length() && waitcounter < 10000) {
                    waitcounter += 100;
                    Thread.sleep(100);
                }
                if (counter % (4096 * 100) == 0) System.out.println(counter);
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
