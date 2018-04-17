    public static void save(String fileName, boolean source) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(savedItems.size());
            ByteArrayOutputStream src_bos = null;
            DataOutputStream src_dos = null;
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileName + ".jar")));
            String root = fileName + "_src" + File.separator + "jdos";
            String dirName = root + File.separator + "cpu" + File.separator + "core_dynamic";
            if (source) {
                src_bos = new ByteArrayOutputStream();
                src_dos = new DataOutputStream(src_bos);
                src_dos.writeInt(savedItems.size());
                File dir = new File(dirName);
                if (!dir.exists()) dir.mkdirs();
                File[] existing = dir.listFiles();
                for (int i = 0; i < existing.length; i++) {
                    existing[i].delete();
                }
            }
            for (int i = 0; i < savedItems.size(); i++) {
                SaveItem item = (SaveItem) savedItems.elementAt(i);
                out.putNextEntry(new ZipEntry(item.name + ".class"));
                out.write(item.byteCode);
                dos.writeUTF(item.name);
                dos.writeInt(item.start);
                dos.writeInt(item.opCode.length);
                dos.write(item.opCode);
                if (source) {
                    FileOutputStream fos = new FileOutputStream(dirName + File.separator + item.name.substring(item.name.lastIndexOf('.') + 1) + ".java");
                    fos.write(item.source.getBytes());
                    fos.close();
                    src_dos.writeUTF("jdos.cpu.core_dynamic." + item.name);
                    src_dos.writeInt(item.opCode.length);
                    src_dos.write(item.opCode);
                }
            }
            out.putNextEntry(new ZipEntry("jdos/Cache.index"));
            dos.flush();
            out.write(bos.toByteArray());
            out.flush();
            out.close();
            if (source) {
                src_dos.flush();
                FileOutputStream fos = new FileOutputStream(root + File.separator + "Cache.index");
                fos.write(src_bos.toByteArray());
                fos.close();
            }
            System.out.println("Saved " + savedItems.size() + " blocks");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
