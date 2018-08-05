    public static boolean copyFile(String sourceFileName, String destFileName) {
        FileChannel ic = null;
        FileChannel oc = null;
        try {
            ic = new FileInputStream(sourceFileName).getChannel();
            oc = new FileOutputStream(destFileName).getChannel();
            ic.transferTo(0, ic.size(), oc);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ic.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                oc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
