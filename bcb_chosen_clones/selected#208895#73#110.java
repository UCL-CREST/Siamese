    public void run() {
        try {
            URL url = new URL("http://pokedev.org/time.php");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringTokenizer s = new StringTokenizer(in.readLine());
            m_day = Integer.parseInt(s.nextToken());
            m_hour = Integer.parseInt(s.nextToken());
            m_minutes = Integer.parseInt(s.nextToken());
            in.close();
        } catch (Exception e) {
            System.out.println("ERROR: Cannot reach time server, reverting to local time");
            Calendar cal = Calendar.getInstance();
            m_hour = cal.get(Calendar.HOUR_OF_DAY);
            m_minutes = 0;
            m_day = 0;
        }
        while (m_isRunning) {
            m_minutes = m_minutes == 59 ? 0 : m_minutes + 1;
            if (m_minutes == 0) {
                if (m_hour == 23) {
                    incrementDay();
                    m_hour = 0;
                } else {
                    m_hour += 1;
                }
            }
            m_hour = m_hour == 23 ? 0 : m_hour + 1;
            if (System.currentTimeMillis() - m_lastWeatherUpdate >= 3600000) {
                generateWeather();
                m_lastWeatherUpdate = System.currentTimeMillis();
            }
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
        }
        System.out.println("INFO: Time Service stopped");
    }
