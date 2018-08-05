    protected void onSendSegment() {
        byte[] buffer = new byte[13100];
        int n;
        try {
            n = this.stream.read(buffer);
            if (n <= 0) this.event(this.SKIP); else {
                CRC32 crc = new CRC32();
                byte[] send = Arrays.copyOf(buffer, n);
                this.sent += n;
                crc.update(send);
                this.logger.debug("Send " + n + " bytes CRC32=" + crc.getValue());
                this.queue(new SendFile(this.fd, send));
            }
        } catch (Exception ex) {
            this.event(ex);
        }
    }
