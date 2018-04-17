    private void encode(RasterLayer.RasterContent dest) throws IOException {
        DataOutputStream dout = new DataOutputStream(out);
        RasterLayer layer = (RasterLayer) dest.getLayer();
        dout.writeByte(0x89);
        dout.writeByte('P');
        dout.writeByte('N');
        dout.writeByte('G');
        dout.writeByte(0x0D);
        dout.writeByte(0x0A);
        dout.writeByte(0x1A);
        dout.writeByte(0x0A);
        int width = layer.getW();
        int height = layer.getH();
        byte colorType = (byte) ((layer.getChannelDepth(0) == 32) ? 2 : 3);
        byte depth = (byte) ((colorType == 2) ? 8 : layer.getChannelDepth(0));
        ByteArrayOutputStream bout;
        DataOutputStream bdout;
        byte[] bytes;
        CRC32 crc = new CRC32();
        bout = new ByteArrayOutputStream();
        bdout = new DataOutputStream(bout);
        bdout.writeByte('I');
        bdout.writeByte('H');
        bdout.writeByte('D');
        bdout.writeByte('R');
        bdout.writeInt(width);
        bdout.writeInt(height);
        bdout.writeByte(depth);
        bdout.writeByte(colorType);
        bdout.writeByte(0);
        bdout.writeByte(0);
        bdout.writeByte(0);
        bdout.close();
        bytes = bout.toByteArray();
        dout.writeInt(bytes.length - 4);
        dout.write(bytes);
        crc.reset();
        crc.update(bytes);
        dout.writeInt((int) crc.getValue());
        if (colorType == 3) {
            bout = new ByteArrayOutputStream();
            bdout = new DataOutputStream(bout);
            bdout.writeByte('P');
            bdout.writeByte('L');
            bdout.writeByte('T');
            bdout.writeByte('E');
            Color[] palette = ((ByteRasterLayer.ByteRasterContent) dest).getColorPaletteModel().getArray();
            for (int i = 0; i < palette.length; ++i) {
                Color color = palette[i];
                bdout.writeByte(color.getRed());
                bdout.writeByte(color.getGreen());
                bdout.writeByte(color.getBlue());
            }
            bdout.close();
            bytes = bout.toByteArray();
            dout.writeInt(bytes.length - 4);
            dout.write(bytes);
            crc.reset();
            crc.update(bytes);
            dout.writeInt((int) crc.getValue());
        }
        bout = new ByteArrayOutputStream();
        bout.write('I');
        bout.write('D');
        bout.write('A');
        bout.write('T');
        DeflaterOutputStream dfout = new DeflaterOutputStream(bout);
        if (colorType == 2) {
            int[] pixels = (int[]) dest.getPixels(0);
            int i = 0;
            for (int v = 0; v < height; ++v) {
                dfout.write(0);
                for (int h = 0; h < width; ++h) {
                    dfout.write((pixels[i] >> 16) & 0x000000FF);
                    dfout.write((pixels[i] >> 8) & 0x000000FF);
                    dfout.write(pixels[i] & 0x000000FF);
                    ++i;
                }
            }
        } else {
            byte[] pixels = (byte[]) dest.getPixels(0);
            BitOutputStream bdfout = new BitOutputStream(dfout);
            for (int v = 0; v < height; ++v) {
                bdfout.write(0);
                for (int h = 0; h < width; ++h) {
                    bdfout.writeBits(pixels[h + v * width], depth);
                }
            }
            bdfout.flush();
        }
        dfout.finish();
        dfout.close();
        bytes = bout.toByteArray();
        dout.writeInt(bytes.length - 4);
        dout.write(bytes);
        crc.reset();
        crc.update(bytes);
        dout.writeInt((int) crc.getValue());
        bytes = new byte[4];
        bytes[0] = (byte) 'I';
        bytes[1] = (byte) 'E';
        bytes[2] = (byte) 'N';
        bytes[3] = (byte) 'D';
        dout.writeInt(0);
        crc.reset();
        crc.update(bytes);
        dout.write(bytes);
        dout.writeInt((int) crc.getValue());
        dout.flush();
    }
