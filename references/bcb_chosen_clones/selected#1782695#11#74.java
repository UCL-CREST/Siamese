    public static void copyFile(String source_name, String dest_name) throws IOException {
        source_name = Shell.getUtils().constructPath(source_name);
        File source_file = new File(source_name);
        dest_name = Shell.getUtils().constructPath(dest_name);
        File destination_file = new File(dest_name);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        try {
            if (!source_file.exists() || !source_file.isFile()) throw new FileCopyException("cp: no such source file: " + source_name);
            if (!source_file.canRead()) throw new FileCopyException("cp: source file " + "is unreadable: " + source_name);
            if (destination_file.exists()) {
                if (destination_file.isFile()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    String response;
                    if (!destination_file.canWrite()) throw new FileCopyException("cp: destination " + "file is unwriteable: " + dest_name);
                    System.out.print("cp: file " + dest_name + " already exists. Overwrite it ? (Y/N): ");
                    System.out.flush();
                    response = in.readLine();
                    if (!response.equals("Y") && !response.equals("y")) throw new FileCopyException("cp: copy cancelled.");
                } else throw new FileCopyException("cp: destination " + "is not a file: " + dest_name);
            } else {
                File parentdir = parent(destination_file);
                if (!parentdir.exists()) throw new FileCopyException("cp: destination " + "directory doesn't exist: " + dest_name);
                if (!parentdir.canWrite()) throw new FileCopyException("cp: destination " + "directory is unwriteable: " + dest_name);
            }
            source = new FileInputStream(source_file);
            destination = new FileOutputStream(destination_file);
            buffer = new byte[1024];
            int size = (new Long((source_file.length() / 1024) / 50)).intValue();
            int c = 1;
            int written = 0;
            System.out.print("cp: ");
            while (true) {
                if (written < 50) {
                    if ((c - 1) == size && size != 0) {
                        System.out.print("#");
                        c = 1;
                        written++;
                    } else if (size == 0) {
                        int j = 1;
                        if (c > 1) j = (50 / c) - 50; else j = 50 / c;
                        for (int i = 0; i < j; i++) System.out.print("#");
                        written += j;
                    }
                }
                bytes_read = source.read(buffer);
                if (bytes_read == -1) break;
                destination.write(buffer, 0, bytes_read);
                c++;
            }
            System.out.println();
        } finally {
            if (source != null) try {
                source.close();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.close();
            } catch (IOException e) {
            }
        }
    }
