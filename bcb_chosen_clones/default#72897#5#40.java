    public static void main(String args[]) {
        InputStream input = System.in;
        OutputStream output = System.out;
        if (args.length > 0) {
            try {
                input = new FileInputStream(args[0]);
            } catch (FileNotFoundException e) {
                System.err.println("Unable to open file: " + args[0]);
                System.exit(-1);
            } catch (IOException e) {
                System.err.println("Unable to access file: " + args[0]);
                System.exit(-1);
            }
        }
        if (args.length > 1) {
            try {
                output = new FileOutputStream(args[1]);
            } catch (FileNotFoundException e) {
                System.err.println("Unable to open file: " + args[1]);
                System.exit(-1);
            } catch (IOException e) {
                System.err.println("Unable to access file: " + args[1]);
                System.exit(-1);
            }
        }
        byte buffer[] = new byte[512];
        int len;
        try {
            while ((len = input.read(buffer)) > 0) output.write(buffer, 0, len);
        } catch (IOException e) {
            System.err.println("Error copying file");
        } finally {
            input.close();
            output.close();
        }
    }
