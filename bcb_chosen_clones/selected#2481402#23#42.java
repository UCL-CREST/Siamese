    public static <T extends ZipNode> void saveAll(OutputStream outputstream, Collection<T> list, ZipStreamFilter filter) throws Exception {
        ZipOutputStream zip_out = new ZipOutputStream(outputstream);
        try {
            ZipEntry entry = new ZipEntry(".info/info.properties");
            entry.setTime(0);
            zip_out.putNextEntry(entry);
            String info = "filter = " + filter.getClass().getName() + "\n" + "count  = " + list.size() + "\n";
            zip_out.write(info.getBytes());
            for (T object : list) {
                save(zip_out, object, filter);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            try {
                zip_out.close();
            } catch (IOException e) {
            }
        }
    }
