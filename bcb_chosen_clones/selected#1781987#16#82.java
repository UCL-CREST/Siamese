    public static void parseString(String str, String name) {
        BufferedReader reader;
        String zeile = null;
        boolean firstL = true;
        int lambda;
        float intens;
        int l_b = 0;
        int l_e = 0;
        HashMap<Integer, Float> curve = new HashMap<Integer, Float>();
        String[] temp;
        try {
            File f = File.createTempFile("tempFile", null);
            URL url = new URL(str);
            InputStream is = url.openStream();
            FileOutputStream os = new FileOutputStream(f);
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = is.read(buffer)) != -1; ) os.write(buffer, 0, len);
            is.close();
            os.close();
            reader = new BufferedReader(new FileReader(f));
            zeile = reader.readLine();
            lambda = 0;
            while (zeile != null) {
                if (!(zeile.length() > 0 && zeile.charAt(0) == '#')) {
                    zeile = reader.readLine();
                    break;
                }
                zeile = reader.readLine();
            }
            while (zeile != null) {
                if (zeile.length() > 0) {
                    temp = zeile.split(" ");
                    lambda = Integer.parseInt(temp[0]);
                    intens = Float.parseFloat(temp[1]);
                    if (firstL) {
                        firstL = false;
                        l_b = lambda;
                    }
                    curve.put(lambda, intens);
                }
                zeile = reader.readLine();
            }
            l_e = lambda;
        } catch (IOException e) {
            System.err.println("Error2 :" + e);
        }
        try {
            String tempV;
            File file = new File("C:/spectralColors/" + name + ".sd");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# COLOR: " + name + " Auto generated File: 02/09/2009; From " + l_b + " to " + l_e);
            bw.newLine();
            bw.write(l_b + "");
            bw.newLine();
            for (int i = l_b; i <= l_e; i++) {
                if (curve.containsKey(i)) {
                    tempV = i + " " + curve.get(i);
                    bw.write(tempV);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
