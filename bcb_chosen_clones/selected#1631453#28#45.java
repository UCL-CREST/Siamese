    public void close() {
        try {
            if (writer != null) {
                BufferedReader reader;
                writer.close();
                writer = new BufferedWriter(new FileWriter(fileName));
                for (int i = 0; i < headers.size(); i++) writer.write(headers.get(i) + ",");
                writer.write("\n");
                reader = new BufferedReader(new FileReader(file));
                while (reader.ready()) writer.write(reader.readLine() + "\n");
                reader.close();
                writer.close();
                file.delete();
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
