    public ByteBuffer[] write(ByteBuffer[] byteBuffers) {
        if (!m_sslInitiated) {
            return m_writer.write(byteBuffers);
        }
        if (m_engine.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            if (!NIOUtils.isEmpty(byteBuffers)) {
                m_initialOutBuffer = NIOUtils.concat(m_initialOutBuffer, m_writer.write(byteBuffers));
                byteBuffers = new ByteBuffer[0];
            }
            ByteBuffer buffer = SSL_BUFFER.get();
            ByteBuffer[] buffers = null;
            try {
                SSLEngineResult result = null;
                while (m_engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                    buffer.clear();
                    result = m_engine.wrap(byteBuffers, buffer);
                    buffer.flip();
                    buffers = NIOUtils.concat(buffers, NIOUtils.copy(buffer));
                }
                if (result == null) return null;
                if (result.getStatus() != SSLEngineResult.Status.OK) throw new SSLException("Unexpectedly not ok wrapping handshake data, was " + result.getStatus());
                reactToHandshakeStatus(result.getHandshakeStatus());
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
            return buffers;
        }
        ByteBuffer buffer = SSL_BUFFER.get();
        buffer.clear();
        if (NIOUtils.isEmpty(byteBuffers)) {
            if (m_initialOutBuffer == null) return null;
        } else {
            byteBuffers = m_writer.write(byteBuffers);
        }
        if (m_initialOutBuffer != null) {
            byteBuffers = NIOUtils.concat(m_initialOutBuffer, byteBuffers);
            m_initialOutBuffer = null;
        }
        ByteBuffer[] encrypted = null;
        while (!NIOUtils.isEmpty(byteBuffers)) {
            buffer.clear();
            try {
                m_engine.wrap(byteBuffers, buffer);
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
            buffer.flip();
            encrypted = NIOUtils.concat(encrypted, NIOUtils.copy(buffer));
        }
        return encrypted;
    }
