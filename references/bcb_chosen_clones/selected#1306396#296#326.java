    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            URL url = new URL(upgradeURL);
            InputStream in = url.openStream();
            BufferedInputStream buffIn = new BufferedInputStream(in);
            FileOutputStream out = new FileOutputStream("");
            String bytes = "";
            int data = buffIn.read();
            int downloadedByteCount = 1;
            while (data != -1) {
                out.write(data);
                bytes.concat(Character.toString((char) data));
                buffIn.read();
                downloadedByteCount++;
                updateProgressBar(downloadedByteCount);
            }
            out.close();
            buffIn.close();
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(bytes.getBytes());
            String hash = m.digest().toString();
            if (hash.length() == 31) {
                hash = "0" + hash;
            }
            if (!hash.equalsIgnoreCase(md5Hash)) {
            }
        } catch (MalformedURLException e) {
        } catch (IOException io) {
        } catch (NoSuchAlgorithmException a) {
        }
    }
