    private void zipDir(File zipDir, ZipOutputStream zos, String startingPath, PXBuildContext context) {
        try {
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                String entryPath = f.getPath();
                if (entryPath.startsWith(startingPath)) {
                    entryPath = entryPath.substring(startingPath.length(), entryPath.length());
                }
                if (f.isDirectory()) {
                    @SuppressWarnings("unused") String filePath = entryPath;
                    zipDir(f, zos, startingPath, context);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry = new ZipEntry(entryPath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            NSDictionary info = new NSDictionary(new Object[] { e.getMessage() }, new String[] { NSError.LocalizedDescriptionKey });
            NSError message = new NSError("Pachyderm Build Domain", -1, info);
            context.appendBuildMessage(message);
        }
    }
