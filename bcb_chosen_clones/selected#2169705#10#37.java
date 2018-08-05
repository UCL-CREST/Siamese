    public static void main(String args[]) throws IOException {
        String inFileName = args[0];
        String outFileName = args[1];
        long position = 0L;
        try {
            position = Long.parseLong(args[2]);
        } catch (NumberFormatException nfex1) {
            try {
                position = Long.parseLong(args[2], 16);
            } catch (NumberFormatException nfex2) {
                System.err.println("Wrong offset");
                System.exit(0);
            }
        }
        if (position < 1L) {
            System.err.println("Wrong offset. Must be more than 0");
            System.exit(0);
        }
        System.out.println("Copying  input: " + inFileName);
        System.out.println("        output: " + outFileName);
        System.out.println("          from: " + position);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFileName));
        bis.skip(position);
        for (byte[] b = new byte[1]; bis.read(b) > 0; bos.write(b)) ;
        bis.close();
        bos.close();
    }
