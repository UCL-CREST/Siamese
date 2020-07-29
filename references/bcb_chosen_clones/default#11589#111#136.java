    void readVolumetricData() throws Exception {
        System.err.println("readVolumetricData");
        StringTokenizer st = new StringTokenizer("");
        volumetricData = new float[countX][][];
        for (int x = 0; x < countX; ++x) {
            float[][] plane = new float[countY][];
            volumetricData[x] = plane;
            for (int y = 0; y < countY; ++y) {
                float[] strip = new float[countZ];
                plane[y] = strip;
                for (int z = 0; z < countZ; ++z) {
                    if (!st.hasMoreTokens()) {
                        String line = br.readLine();
                        if (line == null) {
                            System.err.println("end of file in SqueezeCube?");
                            System.err.println("x=" + x + " y=" + y + " z=" + z);
                            throw new NullPointerException();
                        }
                        st = new StringTokenizer(line);
                    }
                    strip[z] = Float.parseFloat(st.nextToken());
                }
            }
        }
        System.err.println("Successfully read " + countX + " x " + countY + " x " + countZ + " data points");
    }
