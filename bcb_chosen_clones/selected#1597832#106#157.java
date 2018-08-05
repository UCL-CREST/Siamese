    public void run() {
        Vector<Update> updates = new Vector<Update>();
        if (dic != null) updates.add(dic);
        if (gen != null) updates.add(gen);
        if (res != null) updates.add(res);
        if (help != null) updates.add(help);
        for (Iterator iterator = updates.iterator(); iterator.hasNext(); ) {
            Update update = (Update) iterator.next();
            try {
                File temp = File.createTempFile("fm_" + update.getType(), ".jar");
                temp.deleteOnExit();
                FileOutputStream out = new FileOutputStream(temp);
                URL url = new URL(update.getAction());
                URLConnection conn = url.openConnection();
                com.diccionarioderimas.Utils.setupProxy(conn);
                InputStream in = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int read = 0;
                int total = 0;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    total += read;
                    if (total > 10000) {
                        progressBar.setValue(progressBar.getValue() + total);
                        total = 0;
                    }
                }
                out.close();
                in.close();
                String fileTo = basePath + "diccionariorimas.jar";
                if (update.getType() == Update.GENERATOR) fileTo = basePath + "generador.jar"; else if (update.getType() == Update.RESBC) fileTo = basePath + "resbc.me"; else if (update.getType() == Update.HELP) fileTo = basePath + "help.html";
                if (update.getType() == Update.RESBC) {
                    Utils.unzip(temp, new File(fileTo));
                } else {
                    Utils.copyFile(new FileInputStream(temp), new File(fileTo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setVisible(false);
        if (gen != null || res != null) {
            try {
                new Main(null, basePath, false);
            } catch (Exception e) {
                new ErrorDialog(frame, e);
            }
        }
        String restart = "";
        if (dic != null) restart += "\nAlgunas de ellas s�lo estar�n disponibles despu�s de reiniciar el diccionario.";
        JOptionPane.showMessageDialog(frame, "Se han terminado de realizar las actualizaciones." + restart, "Actualizaciones", JOptionPane.INFORMATION_MESSAGE);
    }
