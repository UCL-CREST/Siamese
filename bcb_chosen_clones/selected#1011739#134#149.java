    public void loadLicenceText() {
        try {
            URL url = this.getClass().getResource("/licences/" + this.files[this.licence_text_id]);
            InputStreamReader ins = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(ins);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            this.licence_text = sb.toString();
        } catch (Exception ex) {
            System.out.println("LicenceInfo::error reading. Ex: " + ex);
            ex.printStackTrace();
        }
    }
