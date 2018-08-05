    public void writeToStream(File file, OutputStream os) {
        System.out.println("Writing stream for file using buffer" + file);
        Process p = null;
        BufferedInputStream is = null;
        long startTime = System.currentTimeMillis();
        try {
            File testFile = new File("C:\\temp\\buffer.wmv");
            if (testFile.exists()) testFile.delete();
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
            Consumer b = new Consumer(is);
            new Thread(b).start();
            while (!testFile.exists()) {
                Thread.sleep(100);
            }
            FileInputStream fis = new FileInputStream("C:\\temp\\buffer.wmv");
            System.out.println("Reading from file");
            long waitcounter = 0;
            while (input.length + 1 > testFile.length() && waitcounter < 10000) {
                waitcounter += 100;
                Thread.sleep(100);
            }
            int count;
            long counter = 0;
            while ((count = fis.read(input)) != -1) {
                os.write(input, 0, count);
                counter += count;
                waitcounter = 0;
                while (counter + input.length + 1 > testFile.length() && waitcounter < 10000) {
                    waitcounter += 100;
                    Thread.sleep(100);
                }
                if (counter > 1000000 && (counter / 1000000) == 0) System.out.println("Wrote:" + counter + " bytes in time:" + "Time taken:" + (System.currentTimeMillis() - startTime) / 1000);
            }
            System.out.println("Wrote:" + counter + " bytes in time:" + "Time taken:" + (System.currentTimeMillis() - startTime) / 1000);
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            System.out.println("Time taken:" + (System.currentTimeMillis() - startTime) / 1000);
        } finally {
            if (p != null) p.destroy();
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
