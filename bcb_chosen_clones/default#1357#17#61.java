    public void main(String args[]) {
        try {
            System.out.println("OCR script started");
            System.out.println("Version Build 156");
            System.out.println("RCBot Beta.");
            System.out.println("To start a script type ::on scriptname.");
            System.out.println("E.g: ::on varrockiron to stop just type ::off");
            while (true) {
                String word = "", ocrWord = "";
                while (autoSleep && fatigue() == 72 && !sleeping()) {
                    useItem(getItemSlot(1263));
                    wait(1000);
                }
                String path = "media" + File.separator + mc.aClass31_1156.aString294 + File.separator;
                File f = new File(path);
                if (sleeping() && !f.exists()) System.out.println(f.getName() + ".mkdirs: " + f.mkdirs());
                if (sleeping()) {
                    ImageIO.write(mc.aBufferedImage1258, "png", new File(path + "sleepword.png"));
                }
                if (sleeping()) {
                    System.out.println("X" + "ocrutils" + File.separator + "nconvert -out ppm -o " + "\"" + path + "sleepword.ppm\" " + "\"" + path + "sleepword.png\"" + "X");
                    Process p = Runtime.getRuntime().exec("ocrutils" + File.separator + "nconvert -out ppm -o " + "\"" + path + "sleepword.ppm\" " + "\"" + path + "sleepword.png\"");
                    p.waitFor();
                    p.destroy();
                    p = Runtime.getRuntime().exec("ocrutils" + File.separator + "gocr048 -i " + "\"" + path + "sleepword.ppm\"");
                    p.waitFor();
                    if (p.getInputStream().available() > 0) {
                        InputStream in = p.getInputStream();
                        int c = 0;
                        while ((c = in.read()) != -1) word += (char) c;
                        in.close();
                    }
                    p.destroy();
                    System.out.println("File deleted: " + new File(path + "sleepword.ppm").delete());
                    ocrWord = Functions.parseSleepWord(word);
                    mc.aString39 = ocrWord;
                    mc.sendSleepWord(mc.aString39);
                    wait(1000);
                }
                wait(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
