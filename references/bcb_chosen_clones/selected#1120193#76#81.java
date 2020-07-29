    public static String executeDaikon(String fileName) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec("java -Xmx512m daikon.Daikon --nohierarchy " + fileName);
        InputStream output = p.getInputStream();
        return stringForStream(output);
    }
