    private static void decompileClass(File directory) throws Exception {
        boolean needDecompile = false;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                decompileClass(file);
            } else {
                needDecompile = true;
            }
        }
        if (needDecompile) {
            builder.directory(directory);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] b = new byte[1024];
            while (in.read(b) != -1) ;
            in.close();
            for (File file : directory.listFiles()) {
                String name = file.getName();
                if (name.endsWith(".jad")) {
                    file.renameTo(new File(file.getParent(), file.getName().replace(".jad", ".java")));
                } else if (name.endsWith(".class")) {
                    file.delete();
                }
            }
        }
    }
