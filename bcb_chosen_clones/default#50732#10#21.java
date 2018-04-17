    @Test
    public void testA() throws IOException {
        Process ps = Runtime.getRuntime().exec("WebCam/WebCam.exe");
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    }
