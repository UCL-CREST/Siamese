    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == Show_button) f.setVisible(!f.isVisible());
        if (evt.getSource() == importpulse) {
            try {
                Runtime rt = Runtime.getRuntime();
                Process pr = null;
                pr = rt.exec("tools/Pulse Recorder.exe");
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()), 10000);
                int size = Integer.parseInt(input.readLine());
                if (size > 0) {
                    pulse = new int[size + 1];
                    endtime = size + 1;
                    String line;
                    int pos = 0;
                    while ((line = input.readLine()) != null) {
                        String parts[] = line.split(":");
                        int length = Integer.parseInt(parts[1]) + pos;
                        int data = Integer.parseInt(parts[0]);
                        for (; pos < length && pos < size; pos++) pulse[pos] = data;
                    }
                }
            } catch (java.io.IOException ioe) {
                System.out.println("Problem with PulseRecorder");
            }
        }
    }
