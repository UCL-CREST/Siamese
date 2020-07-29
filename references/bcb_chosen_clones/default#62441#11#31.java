    public static void main(String args[]) {
        try {
            if (args.length != 1) throw new Exception("usage: java Chopper <input>");
            File od = new File("chopped");
            if (!od.exists()) {
                od.mkdir();
            } else if (!od.isDirectory()) {
                throw new Exception("output directory obstructed");
            } else {
                File list[] = od.listFiles();
                for (File f : list) {
                    f.delete();
                }
            }
            Chopper c = new Chopper(ImageIO.read(new File(args[0])));
            c.dump();
        } catch (Throwable t) {
            System.err.println(t);
            System.exit(1);
        }
    }
