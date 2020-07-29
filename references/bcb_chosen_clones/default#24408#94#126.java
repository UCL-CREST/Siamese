    private static void sortDirectory(File directory, File src, File res) throws Exception {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                String name = file.getName();
                if (!name.endsWith("src") && !name.endsWith("res")) sortDirectory(file, src, res);
            } else if (file.getName().endsWith(".java")) {
                File move = new File(src, file.getPath().replace(src.getParent(), ""));
                File dir = move.getParentFile();
                if (!dir.exists()) dir.mkdirs();
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                PrintWriter out = new PrintWriter(move, charsetName);
                byte[] b = new byte[1024 * 1024];
                int i;
                while ((i = in.read(b)) != -1) {
                    if (i > 0) out.write(decodeUnicode(new String(b, 0, i)));
                }
                in.close();
                file.delete();
                out.close();
            } else {
                File move = null;
                if (file.getName().endsWith("MANIFEST.MF")) {
                    move = new File(src.getParent(), new File(jarPath).getName().replace(".jar", ".jad"));
                } else {
                    move = new File(res, file.getPath().replace(src.getParent(), ""));
                }
                File dir = move.getParentFile();
                if (!dir.exists()) dir.mkdirs();
                file.renameTo(move);
            }
        }
        if (directory.compareTo(src.getParentFile()) != 0) directory.delete();
    }
