    public static void copyFile(File in, File out) throws IOException {
        try {
            FileReader inf = new FileReader(in);
            OutputStreamWriter outf = new OutputStreamWriter(new FileOutputStream(out), "UTF-8");
            int c;
            while ((c = inf.read()) != -1) outf.write(c);
            inf.close();
            outf.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
