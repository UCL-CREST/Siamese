    public static ByteBuffer join(ByteBuffer buffer1, ByteBuffer buffer2) {
        if (buffer2 == null || buffer2.remaining() == 0) return NIOUtils.copy(buffer1);
        if (buffer1 == null || buffer1.remaining() == 0) return NIOUtils.copy(buffer2);
        ByteBuffer buffer = ByteBuffer.allocate(buffer1.remaining() + buffer2.remaining());
        buffer.put(buffer1);
        buffer.put(buffer2);
        buffer.flip();
        return buffer;
    }
