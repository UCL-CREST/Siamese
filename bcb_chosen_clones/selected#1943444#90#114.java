    public String loadURL(URL url) {
        String retVal = "";
        try {
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            retVal += line + "\n";
            while (line != null) {
                System.out.println(line);
                line = bufferedReader.readLine();
                if (line != null) retVal += line + "\n";
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            retVal = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            retVal = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            retVal = e.getMessage();
        }
        return retVal;
    }
