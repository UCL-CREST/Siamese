        @Override
        public void testAction(ITestThread testThread) throws Throwable {
            try {
                final InputStream urlIn = new URL("http://jdistunit.sourceforge.net").openStream();
                final int availableBytes = urlIn.available();
                if (0 == availableBytes) {
                    throw new IllegalStateException("Zero bytes on target host.");
                }
                in = new BufferedReader(new InputStreamReader(urlIn));
                String line;
                while (null != (line = in.readLine())) {
                    page.append(line);
                    page.append('\n');
                    if (0 != lineDelay) {
                        OS.sleep(lineDelay);
                    }
                    if (null != testThread && testThread.isActionStopped()) {
                        break;
                    }
                }
            } finally {
                if (null != in) {
                    in.close();
                    in = null;
                }
            }
        }
