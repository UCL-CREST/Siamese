    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            PrintUtil.prt("arguments: sourcefile, destfile");
            System.exit(1);
        }
        FileChannel in = new FileInputStream(args[0]).getChannel(), out = new FileOutputStream(args[1]).getChannel();
        in.transferTo(0, in.size(), out);
    }
