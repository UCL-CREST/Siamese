    public static boolean copy(String from, String to) {
        boolean result;
        String newLine;
        FileInputStream input;
        FileOutputStream output;
        File source;
        int fileLength;
        byte byteBuff[];
        result = false;
        input = null;
        output = null;
        source = null;
        try {
            input = new FileInputStream(from);
            output = new FileOutputStream(to);
            source = new File(from);
            fileLength = (int) source.length();
            byteBuff = new byte[fileLength];
            while (input.read(byteBuff, 0, fileLength) != -1) output.write(byteBuff, 0, fileLength);
            result = true;
        } catch (FileNotFoundException e) {
            System.out.println(from + " does not exist!");
        } catch (IOException e) {
            System.out.println("Error reading/writing files!");
        } finally {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
            }
        }
        return result;
    }
