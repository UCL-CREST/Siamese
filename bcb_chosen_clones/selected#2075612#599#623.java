    public static boolean copyFile(String src, String dst) {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(new File(src)).getChannel();
            outChannel = new FileOutputStream(new File(dst)).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MessageGenerator.briefError("ERROR could not find/access file(s): " + src + " and/or " + dst);
            return false;
        } catch (IOException e) {
            MessageGenerator.briefError("ERROR copying file: " + src + " to " + dst);
            return false;
        } finally {
            try {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                MessageGenerator.briefError("Error closing files involved in copying: " + src + " and " + dst);
                return false;
            }
        }
        return true;
    }
