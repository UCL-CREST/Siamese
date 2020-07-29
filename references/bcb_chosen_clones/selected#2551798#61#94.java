    public static void _he3Decode(String in_file) {
        try {
            File out = new File(in_file + dec_extension);
            File in = new File(in_file);
            int file_size = (int) in.length();
            FileInputStream in_stream = new FileInputStream(in_file);
            out.createNewFile();
            FileOutputStream out_stream = new FileOutputStream(out.getName());
            InputStreamReader inputReader = new InputStreamReader(in_stream, "ISO8859_1");
            OutputStreamWriter outputWriter = new OutputStreamWriter(out_stream, "ISO8859_1");
            ByteArrayOutputStream os = new ByteArrayOutputStream(file_size);
            byte byte_arr[] = new byte[8];
            char char_arr[] = new char[8];
            int buff_size = char_arr.length;
            int _fetched = 0;
            int _chars_read = 0;
            System.out.println(appname + ".\n" + dec_mode + ": " + in_file + "\n" + dec_mode + " to: " + in_file + dec_extension + "\n" + "\nreading: ");
            while (_fetched < file_size) {
                _chars_read = inputReader.read(char_arr, 0, buff_size);
                if (_chars_read == -1) break;
                for (int i = 0; i < _chars_read; i++) byte_arr[i] = (byte) char_arr[i];
                os.write(byte_arr, 0, _chars_read);
                _fetched += _chars_read;
                System.out.print("*");
            }
            System.out.print("\n" + dec_mode + ": ");
            outputWriter.write(new String(_decode((ByteArrayOutputStream) os), "ISO-8859-1"));
            System.out.print("complete\n\n");
        } catch (java.io.FileNotFoundException fnfEx) {
            System.err.println("Exception: " + fnfEx.getMessage());
        } catch (java.io.IOException ioEx) {
            System.err.println("Exception: " + ioEx.getMessage());
        }
    }
