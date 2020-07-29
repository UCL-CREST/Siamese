    public IOCacheArray(final File file, int capacity, final IIOCacheArrayObjectMaker iomaker, int chunkSize, String name) {
        super(capacity, null, chunkSize, name);
        generator = new ICacheArrayObjectMaker() {

            FileOutputStream outStream;

            FileInputStream inStream;

            FileChannel outChannel;

            FileChannel inChannel;

            boolean inited = false;

            private synchronized void init() {
                if (!inited) {
                    try {
                        outStream = new FileOutputStream(file);
                        inStream = new FileInputStream(file);
                        outChannel = outStream.getChannel();
                        inChannel = inStream.getChannel();
                    } catch (FileNotFoundException foe) {
                        Logging.errorln("IOCacheArray constuctor error: Could not open file " + file + ".  Exception " + foe);
                        Logging.errorln("outStream " + outStream + "  inStream " + inStream + "  outchan " + outChannel + "  inchannel " + inChannel);
                    }
                }
                inited = true;
            }

            public Object make(int itemIndex, int baseIndex, Object[] data) {
                init();
                return iomaker.read(inChannel, itemIndex, baseIndex, data);
            }

            public boolean flush(int baseIndex, Object[] data) {
                init();
                return iomaker.write(outChannel, baseIndex, data);
            }

            public CacheArrayBlockSummary summarize(int baseIndex, Object[] data) {
                init();
                return iomaker.summarize(baseIndex, data);
            }
        };
    }
