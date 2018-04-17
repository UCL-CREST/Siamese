    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.out.println("Starting encoding test....");
        Properties p = new Properties();
        try {
            InputStream pStream = ClassLoader.getSystemResourceAsStream("sample_weather.properties");
            p.load(pStream);
        } catch (Exception e) {
            System.err.println("Could not load properties file.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        if (WeatherUpdater.DEBUG) {
            System.out.println("hostname: " + p.getProperty("weather.hostname"));
        }
        if (WeatherUpdater.DEBUG) {
            System.out.println("database: " + p.getProperty("weather.database"));
        }
        if (WeatherUpdater.DEBUG) {
            System.out.println("username: " + p.getProperty("weather.username"));
        }
        if (WeatherUpdater.DEBUG) {
            System.out.println("password: " + p.getProperty("weather.password"));
        }
        SqlAccount sqlAccount = new SqlAccount(p.getProperty("weather.hostname"), p.getProperty("weather.database"), p.getProperty("weather.username"), p.getProperty("weather.password"));
        DatabaseInterface dbi = null;
        try {
            dbi = new DatabaseInterface(sqlAccount);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Established connection to database.");
        String query = "SELECT * FROM Current_Weather WHERE ZipCode = '99702'";
        ResultTable results;
        System.out.println("Executing query: " + query);
        try {
            results = dbi.executeQuery(query);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Got results from query.");
        System.out.println("Converted results into the following table:");
        System.out.println(results);
        System.out.println();
        Class<? extends ResultEncoder> encoder_class;
        Class<? extends ResultDecoder> decoder_class;
        try {
            encoder_class = (Class<? extends ResultEncoder>) Class.forName(p.getProperty("mysms.coding.resultEncoder"));
            decoder_class = (Class<? extends ResultDecoder>) Class.forName(p.getProperty("mysms.coding.resultDecoder"));
        } catch (Exception e) {
            System.err.println("Could not find specified encoder: " + p.getProperty("result.encoder"));
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Found class of encoder: " + encoder_class);
        System.out.println("Found class of decoder: " + decoder_class);
        ResultEncoder encoder;
        ResultDecoder decoder;
        try {
            encoder = encoder_class.newInstance();
            if (encoder_class.equals(decoder_class) && decoder_class.isInstance(encoder)) {
                decoder = (ResultDecoder) encoder;
            } else {
                decoder = decoder_class.newInstance();
            }
        } catch (Exception e) {
            System.err.println("Could not create instances of encoder and decoder.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Created instances of encoder and decoder.");
        if (decoder.equals(encoder)) {
            System.out.println("Decoder and encoder are same object.");
        }
        ByteBuffer buffer;
        try {
            buffer = encoder.encode(null, results);
        } catch (Exception e) {
            System.err.println("Could not encode results.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Encoded results to ByteBuffer with size: " + buffer.capacity());
        File temp;
        try {
            temp = File.createTempFile("encoding_test", ".results");
            temp.deleteOnExit();
            FileChannel out = new FileOutputStream(temp).getChannel();
            out.write(buffer);
            out.close();
        } catch (Exception e) {
            System.err.println("Could not write buffer to file.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Wrote buffer to file: \"" + temp.getName() + "\" with length: " + temp.length());
        ByteBuffer re_buffer;
        try {
            FileInputStream in = new FileInputStream(temp.getAbsolutePath());
            byte[] temp_buffer = new byte[(int) temp.length()];
            int totalRead = 0;
            int numRead = 0;
            while (totalRead < temp_buffer.length) {
                numRead = in.read(temp_buffer, totalRead, temp_buffer.length - totalRead);
                if (numRead < 0) {
                    break;
                } else {
                    totalRead += numRead;
                }
            }
            re_buffer = ByteBuffer.wrap(temp_buffer);
            in.close();
        } catch (Exception e) {
            System.err.println("Could not read from temporary file into buffer.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Read file back into buffer with length: " + re_buffer.capacity());
        ResultTable re_results;
        try {
            re_results = decoder.decode(null, re_buffer);
        } catch (Exception e) {
            System.err.println("Could not decode buffer into a ResultTable.");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Decoded buffer back into the following table:");
        System.out.println(re_results);
        System.out.println();
        System.out.println("... encoding test complete.");
    }
