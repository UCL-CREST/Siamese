    public static final void main(String[] args) throws FileNotFoundException, IOException {
        ArrayList<String[]> result = new ArrayList<String[]>();
        IStream is = new StreamImpl();
        IOUtils.copy(new FileInputStream("H:\\7-项目预算表.xlsx"), is.getOutputStream());
        int count = loadExcel(result, is, 0, 0, -1, 16, 1);
        System.out.println(count);
        for (String[] rs : result) {
            for (String r : rs) {
                System.out.print(r + "\t");
            }
            System.out.println();
        }
    }
