    public void compress(String dir2zip, ZipOutputStream zip) throws IOException {
        String[] dirList = AgentFilesystem.listDir(dir2zip);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (String filename : dirList) {
            File file = new File(dir2zip, filename);
            if (file.isDirectory()) {
                String filepath = file.getPath();
                compress(filepath, zip);
                continue;
            }
            if (LibraryFile.getExtension(file.getName()).equals("zip")) continue;
            FileInputStream input = new FileInputStream(file);
            String entryPath = file.getCanonicalPath().substring(outputPath.length() + 1, file.getCanonicalPath().length());
            ZipEntry entry = new ZipEntry(entryPath);
            zip.putNextEntry(entry);
            while ((bytesIn = input.read(readBuffer)) != -1) {
                zip.write(readBuffer, 0, bytesIn);
            }
            input.close();
        }
    }
