    public static CompositeMap createZIP(IObjectRegistry registry, Integer function_id, String filename) throws IOException, SAXException, InstantiationException, IllegalAccessException, ClassNotFoundException, ParserConfigurationException, SQLException {
        if (registry == null) throw new RuntimeException("paramter error. 'registry' can not be null.");
        File webHome = SourceCodeUtil.getWebHome(registry);
        DataSource ds = (DataSource) registry.getInstanceOfType(DataSource.class);
        Connection conn = ds.getConnection();
        Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        File sourceFile = new File(webHome, "");
        if (sourceFile == null || !sourceFile.exists()) throw new ResourceNotDefinedException(".");
        String[] screens = getRegisteredScreens(stm, function_id);
        String[] bms = getRegisteredBM(stm, function_id);
        stm.close();
        conn.close();
        File of = new File(webHome, filename + ".zip");
        if (of.exists()) of.delete();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(of);
            zos = new ZipOutputStream(fos);
            byte[] b = new byte[1024 * 8];
            int len = -1;
            for (String s : screens) {
                ZipEntry ze = new ZipEntry(s);
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(new File(webHome + "/" + s));
                while ((len = fis.read(b)) != -1) {
                    zos.write(b, 0, len);
                }
                fis.close();
            }
            for (String s : bms) {
                String e = "WEB-INF/classes/" + s.replace('.', '/') + ".bm";
                ZipEntry ze = new ZipEntry(e);
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(new File(webHome + "/" + e));
                while ((len = fis.read(b)) != -1) {
                    zos.write(b, 0, len);
                }
                fis.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        of.deleteOnExit();
        return new CompositeMap();
    }
