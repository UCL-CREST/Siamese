    private static void decompileDex(File dex) {
        try {
            String path = dex.getPath();
            File dir = new File(dex2jar);
            ArrayList<URL> urls = new ArrayList<URL>();
            for (File file : dir.listFiles()) {
                if (file.getName().endsWith(".jar")) urls.add(file.toURI().toURL());
            }
            URLClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));
            Class clazz = loader.loadClass(dex2jarMainClass);
            Method method = clazz.getMethod("doFile", File.class, File.class);
            File jar = new File(path + "_dex2jar.jar");
            method.invoke(null, dex, jar);
            File src = new File(path + ".src");
            extractZip(jar, src);
            jar.delete();
            decompileClass(src);
            System.out.println("decompileDex:" + src);
            String cmd = "java -jar \"" + baksmali + "\" \"" + path + "\" -o \"" + path + ".smali\"";
            Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/c", cmd });
            InputStream read = process.getInputStream();
            byte[] b = new byte[1024000];
            if (read != null) {
                while (read.read(b) != -1) ;
                read.close();
            }
            process.destroy();
            System.out.println("decompileDex:" + path + ".smali");
            cmd = "\"" + dexdump + "\" -d \"" + path + "\"";
            process = Runtime.getRuntime().exec(new String[] { cmd });
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path + ".txt")));
            for (int i = in.read(b); i > -1; i = in.read(b)) {
                if (i != 0) out.write(b, 0, i);
            }
            in.close();
            out.close();
            System.out.println("decompileDex:" + path + ".txt");
        } catch (Exception e) {
            System.err.println("decompileDex:" + dex + ":" + e);
        }
    }
