    public static boolean dumpFile(String from, File to, String lineBreak) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(from)));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(to)));
            String line = null;
            while ((line = in.readLine()) != null) out.write(Main.getInstance().resolve(line) + lineBreak);
            in.close();
            out.close();
        } catch (Exception e) {
            Installer.getInstance().getLogger().log(StringUtils.getStackTrace(e));
            return false;
        }
        return true;
    }
