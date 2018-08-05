    public static void main(String[] args) throws IOException {
        String cmd = "cmd /c D:\\workspace_c++\\img_find\\Debug\\img_find.exe e:\\1.png e:\\3.png";
        Process proc = Runtime.getRuntime().exec(cmd);
        System.out.println(cmd);
        InputStreamReader in = new InputStreamReader(proc.getInputStream());
        char[] c = new char[10];
        int s = 0;
        while ((s = in.read(c)) != -1) {
            for (int i = 0; i < s; i++) {
                System.out.print(c[i]);
            }
        }
    }
