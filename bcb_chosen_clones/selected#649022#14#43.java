    public he3Decode(String in_file) {
        try {
            File out = new File(in_file + extension);
            File in = new File(in_file);
            int file_size = (int) in.length();
            FileInputStream in_stream = new FileInputStream(in_file);
            out.createNewFile();
            FileOutputStream out_stream = new FileOutputStream(out.getName());
            ByteArrayOutputStream os = new ByteArrayOutputStream(file_size);
            byte byte_arr[] = new byte[8];
            int buff_size = byte_arr.length;
            int _fetched = 0;
            int _chars_read = 0;
            System.out.println(appname + ".\n" + "decoding: " + in_file + "\n" + "decoding to: " + in_file + extension + "\n" + "\nreading: ");
            while (_fetched < file_size) {
                _chars_read = in_stream.read(byte_arr, 0, buff_size);
                if (_chars_read == -1) break;
                os.write(byte_arr, 0, _chars_read);
                _fetched += _chars_read;
                System.out.print("*");
            }
            System.out.print("\ndecoding: ");
            out_stream.write(_decode((ByteArrayOutputStream) os));
            System.out.print("complete\n\n");
        } catch (java.io.FileNotFoundException fnfEx) {
            System.err.println("Exception: " + fnfEx.getMessage());
        } catch (java.io.IOException ioEx) {
            System.err.println("Exception: " + ioEx.getMessage());
        }
    }
