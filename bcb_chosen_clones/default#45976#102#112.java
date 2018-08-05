    private static void packageDex(File directory) throws Exception {
        String cmd = "java -jar \"" + smali + "\" \"" + smaliSrcPath + "\" -o \"" + dex + "\"";
        Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/c", cmd }, null, directory);
        InputStream read = process.getInputStream();
        byte[] b = new byte[1024000];
        if (read != null) {
            while (read.read(b) != -1) ;
            read.close();
        }
        process.destroy();
    }
