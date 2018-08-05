    public void setGUIPrefs(GUIPrefs prefs) throws Exception {
        sendMsg("Setting the GUI preferences ...");
        outJar.putNextEntry(new ZipEntry("GUIPrefs"));
        ObjectOutputStream objOut = new ObjectOutputStream(outJar);
        objOut.writeObject(prefs);
        objOut.flush();
        outJar.closeEntry();
    }
