    public static void test(String args[]) {
        int trace;
        int bytes_read = 0;
        int last_contentLenght = 0;
        try {
            BufferedReader reader;
            URL url;
            url = new URL(args[0]);
            URLConnection istream = url.openConnection();
            last_contentLenght = istream.getContentLength();
            reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
            System.out.println(url.toString());
            String line;
            trace = t2pNewTrace();
            while ((line = reader.readLine()) != null) {
                bytes_read = bytes_read + line.length() + 1;
                t2pProcessLine(trace, line);
            }
            t2pHandleEventPairs(trace);
            t2pSort(trace, 0);
            t2pExportTrace(trace, new String("pngtest2.png"), 1000, 700, (float) 0, (float) 33);
            t2pExportTrace(trace, new String("pngtest3.png"), 1000, 700, (float) 2.3, (float) 2.44);
            System.out.println("Press any key to contiune read from stream !!!");
            System.out.println(t2pGetProcessName(trace, 0));
            System.in.read();
            istream = url.openConnection();
            if (last_contentLenght != istream.getContentLength()) {
                istream = url.openConnection();
                istream.setRequestProperty("Range", "bytes=" + Integer.toString(bytes_read) + "-");
                System.out.println(Integer.toString(istream.getContentLength()));
                reader = new BufferedReader(new InputStreamReader(istream.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    t2pProcessLine(trace, line);
                }
            } else System.out.println("File not changed !");
            t2pDeleteTrace(trace);
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException !!!");
        } catch (IOException e) {
            System.out.println("File not found " + args[0]);
        }
        ;
    }
