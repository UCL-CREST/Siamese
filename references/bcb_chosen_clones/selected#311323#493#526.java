    public static boolean compress(File source, File target, Manifest manifest) {
        try {
            if (!(source.exists() & source.isDirectory())) return false;
            if (target.exists()) target.delete();
            ZipOutputStream output = null;
            boolean isJar = target.getName().toLowerCase().endsWith(".jar");
            if (isJar) {
                File manifestDir = new File(source, "META-INF");
                remove(manifestDir);
                if (manifest != null) output = new JarOutputStream(new FileOutputStream(target), manifest); else output = new JarOutputStream(new FileOutputStream(target));
            } else output = new ZipOutputStream(new FileOutputStream(target));
            ArrayList list = getContents(source);
            String baseDir = source.getAbsolutePath().replace('\\', '/');
            if (!baseDir.endsWith("/")) baseDir = baseDir + "/";
            int baseDirLength = baseDir.length();
            byte[] buffer = new byte[1024];
            int bytesRead;
            for (int i = 0, n = list.size(); i < n; i++) {
                File file = (File) list.get(i);
                FileInputStream f_in = new FileInputStream(file);
                String filename = file.getAbsolutePath().replace('\\', '/');
                if (filename.startsWith(baseDir)) filename = filename.substring(baseDirLength);
                if (isJar) output.putNextEntry(new JarEntry(filename)); else output.putNextEntry(new ZipEntry(filename));
                while ((bytesRead = f_in.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
                f_in.close();
                output.closeEntry();
            }
            output.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
        return true;
    }
