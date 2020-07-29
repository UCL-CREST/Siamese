    static void createCompleteXML(File file) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(errorFile);
            fos = new FileOutputStream(file);
            byte[] data = new byte[Integer.parseInt(BlueXStatics.prop.getProperty("allocationUnit"))];
            int offset;
            while ((offset = fis.read(data)) != -1) fos.write(data, 0, offset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(file, true);
            fw.append("</detail>");
            fw.append("\n</exception>");
            fw.append("\n</log>");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
            }
        }
    }
