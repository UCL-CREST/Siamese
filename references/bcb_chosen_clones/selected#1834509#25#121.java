    public static void copy(Object arg1, Object arg2) {
        Writer writer = null;
        Reader reader = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            if (arg2 instanceof Writer) {
                writer = (Writer) arg2;
                if (arg1 instanceof Reader) {
                    reader = (Reader) arg1;
                    copy(reader, writer);
                } else if (arg1 instanceof String) {
                    reader = new FileReader(new File((String) arg1));
                    copy(reader, writer);
                } else if (arg1 instanceof File) {
                    reader = new FileReader((File) arg1);
                    copy(reader, writer);
                } else if (arg1 instanceof URL) {
                    copy(((URL) arg1).openStream(), writer);
                } else if (arg1 instanceof InputStream) {
                    reader = new InputStreamReader((InputStream) arg1);
                    copy(reader, writer);
                } else if (arg1 instanceof RandomAccessFile) {
                    copy((RandomAccessFile) arg1, writer);
                } else {
                    throw new TypeError("Invalid first argument to copy()");
                }
            } else if (arg2 instanceof OutputStream) {
                outStream = (OutputStream) arg2;
                if (arg1 instanceof Reader) {
                    copy((Reader) arg1, new OutputStreamWriter(outStream));
                } else if (arg1 instanceof String) {
                    inStream = new FileInputStream(new File((String) arg1));
                    copy(inStream, outStream);
                } else if (arg1 instanceof File) {
                    inStream = new FileInputStream((File) arg1);
                    copy(inStream, outStream);
                } else if (arg1 instanceof URL) {
                    copy(((URL) arg1).openStream(), outStream);
                } else if (arg1 instanceof InputStream) {
                    copy((InputStream) arg1, outStream);
                } else if (arg1 instanceof RandomAccessFile) {
                    copy((RandomAccessFile) arg1, outStream);
                } else {
                    throw new TypeError("Invalid first argument to copy()");
                }
            } else if (arg2 instanceof RandomAccessFile) {
                RandomAccessFile out = (RandomAccessFile) arg2;
                if (arg1 instanceof Reader) {
                    copy((Reader) arg1, out);
                } else if (arg1 instanceof String) {
                    inStream = new FileInputStream(new File((String) arg1));
                    copy(inStream, out);
                } else if (arg1 instanceof File) {
                    inStream = new FileInputStream((File) arg1);
                    copy(inStream, out);
                } else if (arg1 instanceof URL) {
                    copy(((URL) arg1).openStream(), out);
                } else if (arg1 instanceof InputStream) {
                    copy((InputStream) arg1, out);
                } else if (arg1 instanceof RandomAccessFile) {
                    copy((RandomAccessFile) arg1, out);
                } else {
                    throw new TypeError("Invalid first argument to copy()");
                }
            } else if (arg2 instanceof File || arg2 instanceof String) {
                File outFile = null;
                if (arg2 instanceof File) {
                    outFile = (File) arg2;
                } else {
                    outFile = new File((String) arg2);
                }
                outStream = new FileOutputStream(outFile);
                if (arg1 instanceof Reader) {
                    copy((Reader) arg1, new OutputStreamWriter(outStream));
                } else if (arg1 instanceof String) {
                    inStream = new FileInputStream(new File((String) arg1));
                    copy(inStream, outStream);
                } else if (arg1 instanceof File) {
                    inStream = new FileInputStream((File) arg1);
                    copy(inStream, outStream);
                } else if (arg1 instanceof URL) {
                    copy(((URL) arg1).openStream(), outStream);
                } else if (arg1 instanceof InputStream) {
                    copy((InputStream) arg1, outStream);
                } else if (arg1 instanceof RandomAccessFile) {
                    copy((RandomAccessFile) arg1, outStream);
                } else {
                    throw new TypeError("Invalid first argument to copy()");
                }
            } else {
                throw new TypeError("Invalid second argument to copy()");
            }
        } catch (IOException e) {
            throw new IOError(e.getMessage(), e);
        }
    }
