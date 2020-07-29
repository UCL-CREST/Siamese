    private static void list(File dir) throws Exception {
        String prefix = dir.getCanonicalPath();
        if (prefix.length() > root.length()) {
            prefix = prefix.substring(root.length());
        } else {
            prefix = "";
        }
        for (String name : dir.list()) {
            if (!name.startsWith(".") && !name.endsWith(".jar") && !ignoreList.contains(name)) {
                File file = new File(dir, name);
                if (file.isDirectory()) {
                    System.out.println(file);
                    list(file);
                } else {
                    out.putNextEntry(new ZipEntry(prefix + "\\" + name));
                    FileInputStream in = new FileInputStream(file);
                    int l;
                    while ((l = in.read(b)) != -1) {
                        if (l != 0) out.write(b, 0, l);
                    }
                    in.close();
                    out.closeEntry();
                }
            }
        }
    }
