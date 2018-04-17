    public static byte[] zipUpdate(byte[] zip, String name, String oldname, byte[] contents, boolean delete) {
        try {
            File temp = File.createTempFile("atf", ".zip");
            InputStream in = new BufferedInputStream(new ByteArrayInputStream(zip));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(temp));
            ZipInputStream zin = new ZipInputStream(in);
            ZipOutputStream zout = new ZipOutputStream(os);
            ZipEntry e;
            ZipEntry e2;
            byte buffer[] = new byte[TEMP_FILE_BUFFER_SIZE];
            int bytesRead;
            boolean found = false;
            boolean rename = false;
            String oname = name;
            if (oldname != null) {
                name = oldname;
                rename = true;
            }
            while ((e = zin.getNextEntry()) != null) {
                if (!e.isDirectory()) {
                    String ename = e.getName();
                    if (delete && ename.equals(name)) continue;
                    e2 = new ZipEntry(rename ? oname : ename);
                    zout.putNextEntry(e2);
                    if (ename.equals(name)) {
                        found = true;
                        zout.write(contents);
                    } else {
                        while ((bytesRead = zin.read(buffer)) != -1) zout.write(buffer, 0, bytesRead);
                    }
                    zout.closeEntry();
                }
            }
            if (!found && !delete) {
                e = new ZipEntry(name);
                zout.putNextEntry(e);
                zout.write(contents);
                zout.closeEntry();
            }
            zin.close();
            zout.close();
            byte[] res = MLUtil.loadFileToBuffer(temp.getPath());
            temp.delete();
            return (res);
        } catch (FileNotFoundException e) {
            MLUtil.runtimeError(e, name);
        } catch (IOException e) {
            MLUtil.runtimeError(e, name);
        }
        return (null);
    }
