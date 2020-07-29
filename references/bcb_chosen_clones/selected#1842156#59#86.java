    static ConversionMap create(String file) {
        ConversionMap out = new ConversionMap();
        URL url = ConversionMap.class.getResource(file);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = in.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    String[] arr = line.split("\t");
                    try {
                        double value = Double.parseDouble(arr[1]);
                        out.put(translate(lowercase(arr[0].getBytes())), value);
                        out.defaultValue += value;
                        out.length = arr[0].length();
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Something is wrong with in conversion file: " + e);
                    }
                }
                line = in.readLine();
            }
            in.close();
            out.defaultValue /= Math.pow(4, out.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Their was an error while reading the conversion map: " + e);
        }
        return out;
    }
