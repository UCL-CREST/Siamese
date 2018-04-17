    private void controlarAlarma() {
        if (AlarmaVigente != null) {
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            proximaAlarma.setText("Proxima tarea con alarma: ".concat(AlarmaVigente.getTexto()));
            proximaAlarmaHora.setText("Hora de la alarma: ".concat(formatoFechaHora.format(AlarmaVigente.getFecha())));
            if (AlarmaVigente.getFecha().before(FechaYHora)) {
                try {
                    Clip sonido = AudioSystem.getClip();
                    sonido.open(AudioSystem.getAudioInputStream(new File("chau.wav")));
                    sonido.start();
                    JOptionPane.showMessageDialog(null, "Recuerde: ".concat(AlarmaVigente.getTexto()));
                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }
                AlarmaVigente = AlarmList.getProximaAlarma();
            }
        } else {
            proximaAlarma.setText("No hay ninguna alarma configurada");
            proximaAlarmaHora.setText("");
        }
    }
