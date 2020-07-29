    public static boolean buildPerMovieDiffBinary(String masterFile) {
        try {
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + masterFile);
            FileChannel inC = new FileInputStream(inFile).getChannel();
            System.out.println(inC.size());
            short movie1, movie2;
            int count;
            float diffRating, sumXY, sumX, sumY, sumX2, sumY2, pearsonCorr, adjustedCosineCorr, cosineCorr;
            long position;
            for (long i = 1; i < 17770; i++) {
                File outFile = new File("C:\\NetflixData\\download\\SmartGrape\\CFItemToItemStats\\Movie--" + i + "-MatrixData.txt");
                FileChannel outC = new FileOutputStream(outFile, true).getChannel();
                ByteBuffer buf = ByteBuffer.allocate(17770 * 44);
                for (long j = 1; j < i; j++) {
                    ByteBuffer bbuf = ByteBuffer.allocate(44);
                    position = 0;
                    position += new Long(17769).longValue() * new Long(17770).longValue() * new Long(22).longValue();
                    position -= new Long((17769 - (j - 1))).longValue() * new Long((17770 - (j - 1))).longValue() * new Long(22).longValue();
                    position += new Long((i - j - 1) * 44).longValue();
                    inC.position(position);
                    inC.read(bbuf);
                    bbuf.flip();
                    buf.putShort(bbuf.getShort());
                    bbuf.getShort();
                    buf.putInt(bbuf.getInt());
                    buf.putFloat(-bbuf.getInt());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                    buf.putFloat(bbuf.getFloat());
                }
                buf.putShort(new Long(i).shortValue());
                buf.putInt(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                buf.putFloat(0);
                position = 0;
                position += new Long(17769).longValue() * new Long(17770).longValue() * new Long(22).longValue();
                position -= new Long((17769 - (i - 1))).longValue() * new Long((17770 - (i - 1))).longValue() * new Long(22).longValue();
                ByteBuffer remainingBuf = inC.map(FileChannel.MapMode.READ_ONLY, position, (17770 - i) * 44);
                while (remainingBuf.hasRemaining()) {
                    remainingBuf.getShort();
                    buf.putShort(remainingBuf.getShort());
                    buf.putInt(remainingBuf.getInt());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                    buf.putFloat(remainingBuf.getFloat());
                }
                buf.flip();
                outC.write(buf);
                buf.clear();
                outC.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
