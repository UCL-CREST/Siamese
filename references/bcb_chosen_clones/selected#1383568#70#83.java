    public static void copyFile(File source, File target) {
        try {
            target.getParentFile().mkdirs();
            byte[] buffer = new byte[4096];
            int len = 0;
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(target);
            while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
