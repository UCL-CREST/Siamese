    private void appendArchive() throws Exception {
        String cmd;
        if (profile == CompilationProfile.UNIX_GCC) {
            cmd = "cat";
        } else if (profile == CompilationProfile.MINGW_WINDOWS) {
            cmd = "type";
        } else {
            throw new Exception("Unknown cat equivalent for profile " + profile);
        }
        compFrame.writeLine("<span style='color: green;'>" + cmd + " \"" + imageArchive.getAbsolutePath() + "\" >> \"" + outputFile.getAbsolutePath() + "\"</span>");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile, true));
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(imageArchive));
        int read;
        while ((read = in.read()) != -1) {
            out.write(read);
        }
        in.close();
        out.close();
    }
