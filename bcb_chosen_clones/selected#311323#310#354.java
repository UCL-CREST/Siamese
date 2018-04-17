    public static File extract(File source, String filename, File target) {
        if (source.exists() == false || filename == null || filename.trim().length() < 1 || target == null) return null;
        boolean isDirectory = (filename.lastIndexOf("/") == filename.length() - 1);
        try {
            Map contents = (Map) jarContents.get(source.getPath());
            if (contents == null) {
                contents = new HashMap();
                jarContents.put(source.getPath(), contents);
                ZipInputStream input = new ZipInputStream(new FileInputStream(source));
                ZipEntry zipEntry = null;
                while ((zipEntry = input.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) continue;
                    contents.put(zipEntry.getName(), zipEntry);
                }
                input.close();
            }
            if (isDirectory) {
                Iterator it = contents.keySet().iterator();
                while (it.hasNext()) {
                    String next = (String) it.next();
                    if (next.startsWith(filename)) {
                        ZipEntry zipEntry = (ZipEntry) contents.get(next);
                        int n = filename.length();
                        File newTarget = new File(target, zipEntry.getName().substring(n));
                        extract(source, next, newTarget);
                    }
                }
                return target;
            }
            ZipEntry entry = (ZipEntry) contents.get(filename);
            ZipFile input = new ZipFile(source);
            InputStream in = input.getInputStream(entry);
            target.getParentFile().mkdirs();
            int bytesRead;
            byte[] buffer = new byte[1024];
            FileOutputStream output = new FileOutputStream(target);
            while ((bytesRead = in.read(buffer)) != -1) output.write(buffer, 0, bytesRead);
            output.close();
            input.close();
            return target;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
