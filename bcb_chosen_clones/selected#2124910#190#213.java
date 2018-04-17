    public static String getCRC(File file) {
        DataInputStream input = null;
        String result = null;
        try {
            input = new DataInputStream(new FileInputStream(file));
            CRC32 crc = new CRC32();
            byte[] data = new byte[(int) file.length()];
            crc.update(input.read(data));
            result = String.valueOf(crc.getValue());
        } catch (FileNotFoundException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
        } catch (IOException e) {
            IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    IdeLog.logError(ScriptingPlugin.getDefault(), Messages.FileUtilities_Error, e);
                }
            }
        }
        return result;
    }
