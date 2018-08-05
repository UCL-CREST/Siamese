    private void loadFile(Assignment ZAssignment) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ByteArrayOutputStream bout2 = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bout);
            zout.setLevel(9);
            ObjectOutputStream oout = new ObjectOutputStream(bout2);
            oout.writeObject(ZAssignment);
            ByteArrayInputStream bin = new ByteArrayInputStream(bout2.toByteArray());
            zout.putNextEntry(new ZipEntry("Assignment"));
            byte buffer[] = new byte[512];
            int index;
            while ((index = bin.read(buffer)) > 0) zout.write(buffer, 0, index);
            zout.closeEntry();
            zout.close();
            file = bout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
