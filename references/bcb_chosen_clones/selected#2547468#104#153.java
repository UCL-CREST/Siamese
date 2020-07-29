    public static void checkAndUpdateGameData() {
        new ErrThread() {

            @Override
            public void handledRun() throws Throwable {
                try {
                    URL url = new URL(ONLINE_CLIENT_DATA + "gamedata.xml");
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    int lastversion = 0;
                    String readHeader1 = br.readLine();
                    String readHeader2 = br.readLine();
                    String[] parts = readHeader2.split(" ");
                    lastversion = new Integer(parts[1]);
                    GameDatabase.loadVersion();
                    if (GameDatabase.version < lastversion) {
                        Logger.log(LogTypes.LOG, "Downloading new gamedata");
                        BufferedOutputStream bo = null;
                        File destfile = new File(GameDatabase.dataFilePath);
                        if (!destfile.createNewFile()) {
                            destfile.delete();
                            destfile.createNewFile();
                        }
                        bo = new BufferedOutputStream(new FileOutputStream(destfile));
                        bo.write((readHeader1 + "\n").getBytes());
                        bo.write((readHeader2 + "\n").getBytes());
                        int readedbyte;
                        while ((readedbyte = br.read()) != -1) {
                            bo.write(readedbyte);
                        }
                        bo.flush();
                        try {
                            br.close();
                            bo.close();
                        } catch (Exception ex) {
                            Logger.log(ex);
                        }
                    }
                } catch (java.net.UnknownHostException unknownHost) {
                    Logger.log("Sourceforge is down, cannot update gamedata");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(FrameOrganizer.getClientFrame(), "The gamedata is outdated, but Coopnet couldn't update it!", "Gamedata outdated", JOptionPane.INFORMATION_MESSAGE);
                    throw e;
                } finally {
                    GameDatabase.loadVersion();
                    GameDatabase.load("", GameDatabase.dataFilePath);
                    GameDatabase.detectGames();
                }
            }
        }.start();
    }
