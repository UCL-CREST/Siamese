    public String GetMemberName(String id) {
        String name = null;
        try {
            String line;
            URL url = new URL(intvasmemberDeatails + "?CID=" + id);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                name = line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] parts = name.split(" ");
        rating = parts[2];
        return parts[0] + " " + parts[1];
    }
