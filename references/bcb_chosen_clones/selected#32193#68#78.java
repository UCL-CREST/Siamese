        @Override
        public void run() {
            try {
                IOUtils.copy(_is, processOutStr);
            } catch (final IOException ioe) {
                proc.destroy();
            } finally {
                IOUtils.closeQuietly(_is);
                IOUtils.closeQuietly(processOutStr);
            }
        }
