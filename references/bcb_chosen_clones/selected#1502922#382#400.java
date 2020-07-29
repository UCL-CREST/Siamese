    private void setCommandInput() {
        mCommandInput = null;
        try {
            Class<?> consoleReaderClass = Class.forName("jline.ConsoleReader");
            if (consoleReaderClass != null) {
                Class<?> consoleInputClass = Class.forName("jline.ConsoleReaderInputStream");
                if (consoleInputClass != null) {
                    Object jline = consoleReaderClass.newInstance();
                    Constructor<?> constructor = consoleInputClass.getConstructor(consoleReaderClass);
                    if (constructor != null) {
                        mCommandInput = (InputStream) constructor.newInstance(jline);
                    }
                }
            }
        } catch (Exception e1) {
            mLogger.info("Exception loading jline");
        }
        if (mCommandInput == null) mCommandInput = System.in;
    }
