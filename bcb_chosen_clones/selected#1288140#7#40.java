    public static void main(String[] args) {
        FileInputStream fr = null;
        FileOutputStream fw = null;
        BufferedInputStream br = null;
        BufferedOutputStream bw = null;
        try {
            fr = new FileInputStream("D:/5.xls");
            fw = new FileOutputStream("c:/Dxw.java");
            br = new BufferedInputStream(fr);
            bw = new BufferedOutputStream(fw);
            int read = br.read();
            while (read != -1) {
                bw.write(read);
                read = br.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
