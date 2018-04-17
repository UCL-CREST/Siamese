    public static void main(String args[]) {
        if (args.length != 2) {
            String u = "Usage: java -cp . UpgradeRomizedProperties <infile> <outfile>";
            System.out.println(u);
            System.exit(0);
        }
        Runtime rt = Runtime.getRuntime();
        String cmd = "javac -d " + System.getProperty("user.dir") + " " + args[0];
        int exitCode;
        try {
            Process p = rt.exec(cmd);
            exitCode = p.waitFor();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        Hashtable rp = new Hashtable();
        Object[] argz = { rp };
        try {
            String cn = "com.sun.midp.chameleon.skins.resources.RomizedProperties";
            Class clazz = Class.forName(cn);
            Method loadProperties;
            loadProperties = clazz.getMethod("load", new Class[] { Hashtable.class });
            loadProperties.invoke(null, argz);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        Converter converter = new Converter(SkinProperty.properties, rp);
        converter.convert();
        try {
            FileOutputStream fout = new FileOutputStream(args[1]);
            OutputStreamWriter w = new OutputStreamWriter(fout);
            writer = new PrintWriter(w);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        printHeader();
        printSkinProperties();
        printFooter();
        writer.close();
        reportUnknownProperties(converter.unknownProps);
        reportMissingProperties(converter.missingProps);
    }
