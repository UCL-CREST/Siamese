    public void run(String[] args) throws Throwable {
        FileInputStream input = new FileInputStream(args[0]);
        FileOutputStream output = new FileOutputStream(args[0] + ".out");
        Reader reader = $(Reader.class, $declass(input));
        Writer writer = $(Writer.class, $declass(output));
        Pump pump;
        if (args.length > 1 && "diag".equals(args[1])) {
            pump = $(new Reader() {

                int counter;

                @ToContext(mode = InvocationMode.sideEffect)
                public int read(byte[] buffer, int off, int len) throws Exception {
                    Integer rd = (Integer) $next();
                    if (rd > 0) {
                        counter += rd;
                    }
                    return 0;
                }

                @ToContext(mode = InvocationMode.sideEffect)
                public void close() throws Exception {
                    System.out.println("Read from input " + counter + " bytes.");
                }
            }, reader, writer, new Writer() {

                int counter;

                @ToContext(mode = InvocationMode.sideEffect)
                public void write(byte[] buffer, int off, int len) throws Exception {
                    counter += len;
                }

                @ToContext(mode = InvocationMode.sideEffect)
                public void close() throws Exception {
                    System.out.println("Written to output " + counter + " bytes.");
                }
            });
        } else {
            pump = $(reader, writer);
        }
        pump.pump();
    }
