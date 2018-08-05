    public void modifyApplicationMessage(String locale, String messageName, String messageValue) {
        Properties properties = new Properties();
        try {
            String i18nPath = ServerUtil.removelastResourceURL(ConfigurationServiceImpl.class.getResource("/es/ua/tranube/prototype/main/client/TranubeConstants_en.properties").getPath()).toString();
            File englishFile = new File(i18nPath + "TranubeConstants_en.properties");
            if (!englishFile.exists()) throw new Exception("English file not found");
            String propertiesFilePath = i18nPath + "TranubeConstants_" + locale + ".properties";
            File file = new File(propertiesFilePath);
            if (!file.exists()) {
                FileReader in = new FileReader(englishFile);
                FileWriter out = new FileWriter(file);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
            }
            InputStream is = ConfigurationServiceImpl.class.getResourceAsStream("/es/ua/tranube/prototype/main/client/TranubeConstants_" + locale + ".properties");
            BufferedReader breader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            StringBuilder strBuilder = new StringBuilder();
            boolean found = false;
            while ((line = breader.readLine()) != null) {
                if (line.startsWith("#")) strBuilder.append(line).append("\n"); else {
                    String[] pieces = line.split("=");
                    if (pieces.length == 2) {
                        if (pieces[0].trim().equals(messageName)) {
                            strBuilder.append(pieces[0].trim() + " = " + messageValue + "\n");
                            found = true;
                        } else strBuilder.append(line).append("\n");
                    } else strBuilder.append(line).append("\n");
                }
            }
            if (!found) strBuilder.append(messageName).append(" = ").append(messageValue).append("\n");
            breader.close();
            is.close();
            FileWriter writer = new FileWriter(file);
            writer.write(strBuilder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
