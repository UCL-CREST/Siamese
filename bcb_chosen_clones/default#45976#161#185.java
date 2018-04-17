    private static void decompileXml(File xml) {
        File directory = xml.getParentFile();
        File temp = new File(directory, xml.getName() + "." + System.currentTimeMillis() + ".tmp");
        try {
            String cmd = "java -jar \"" + AXMLPrinter2 + "\" \"" + xml.getPath() + "\" > \"" + temp.getPath() + "\"";
            Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/c", cmd }, null, directory);
            InputStream read = process.getInputStream();
            byte[] b = new byte[1024000];
            if (read != null) {
                while (read.read(b) != -1) ;
                read.close();
            }
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(temp));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(xml));
            for (int i = in.read(b); i > -1; i = in.read(b)) {
                if (i != 0) out.write(b, 0, i);
            }
            in.close();
            out.close();
            temp.delete();
            System.out.println("decompileXml:" + xml);
        } catch (Exception e) {
            System.err.println("decompileXml:" + xml + ":" + e);
        }
    }
