    public static void main() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("C:/Deloitte/ffmpeg/bin/ffmpeg.exe -i \"C:/Deloitte/Personel/Videos/Clean Install Ubuntu Linux on a PC.mp4\"");
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running is:");
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
