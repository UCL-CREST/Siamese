    public static int save() {
        try {
            String filename = "tyrant.sav";
            FileDialog fd = new FileDialog(new Frame(), "Save Game", FileDialog.SAVE);
            fd.setFile(filename);
            fd.setVisible(true);
            if (fd.getFile() != null) {
                filename = fd.getDirectory() + fd.getFile();
            } else {
                return 0;
            }
            FileOutputStream f = new FileOutputStream(filename);
            ZipOutputStream z = new ZipOutputStream(f);
            z.putNextEntry(new ZipEntry("data.xml"));
            if (!save(new ObjectOutputStream(z))) {
                throw new Error("Save game failed");
            }
            Game.message("Game saved - " + filename);
            z.closeEntry();
            z.close();
            if (saveHasBeenCalledAlready) Game.message("Please note that you can only restore the game with the same version of Tyrant (v" + VERSION + ").");
            saveHasBeenCalledAlready = false;
        } catch (Exception e) {
            Game.message("Error while saving: " + e.toString());
            if (QuestApp.isapplet) {
                Game.message("This may be due to your browser security restrictions");
                Game.message("If so, run the web start or downloaded application version instead");
            }
            System.out.println(e);
            return -1;
        }
        return 1;
    }
