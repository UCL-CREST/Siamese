    private void extractLocation(String _loc, int _end) {
        String get_u = _loc.substring(_end, _loc.length()).trim();
        if (get_u.length() > 0) {
            System.out.print(".");
            System.out.flush();
            Pattern _p = Pattern.compile("^http://", Pattern.CASE_INSENSITIVE);
            Matcher _m = _p.matcher(get_u);
            boolean _b = false;
            int _iend = -1;
            while (_m.find()) {
                _b = true;
                _iend = _m.end();
            }
            if (_iend > 0) {
                String pars02 = get_u.substring(_iend, get_u.length());
                _p = Pattern.compile("/");
                _m = _p.matcher(pars02.trim());
                String[] tok = _p.split(pars02.trim());
                String hst = tok[0].trim();
                int _s = -1;
                while (_m.find()) {
                    _s = _m.start();
                    if (_s > 0) break;
                }
                String rest = pars02.substring(_s, pars02.length()).trim();
                _host = hst;
                StringBuffer _buf = new StringBuffer();
                _buf.append("GET " + rest);
                _buf.append(" HTTP/1.1\r\n");
                _buf.append("Accept: text/html\r\n");
                _buf.append("Referer: " + _refer + "\r\n");
                _buf.append("Accept-Language: en-us\r\n");
                _buf.append("User-Agent: Mozilla/4.0 (compatible; ");
                _buf.append("MSIE 6.0; Windows NT 5.1; ");
                _buf.append("Avant Browser [avantbrowser.com]; ");
                _buf.append(".NET CLR 1.1.4322)\r\n");
                _buf.append("Host: " + _host + "\r\n" + "Connection: close\r\n\r\n");
                String httpPost = _buf.toString();
                System.out.print(".");
                System.out.flush();
                try {
                    Thread.sleep(100);
                    closeConnection();
                    _socket = new Socket(_host, _port);
                    if (_socket == null) throw new RuntimeException("Invalid Host Connection"); else System.out.print("..");
                    _socket.setSoTimeout(2 * 60 * 1000);
                    PrintWriter writer = new PrintWriter(_socket.getOutputStream(), true);
                    writer.print(httpPost);
                    writer.flush();
                    StringBuffer resultBuffer = new StringBuffer();
                    String line = null;
                    BufferedReader bufferedReader = null;
                    bufferedReader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                    do {
                        try {
                            line = bufferedReader.readLine();
                        } catch (IOException exception) {
                            throw new RuntimeException(exception);
                        }
                        if (line != null) resultBuffer.append(line + "\r\n");
                    } while (line != null);
                    try {
                        _socket.close();
                        _socket = null;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    String result = resultBuffer.toString();
                    _passResults = result.trim();
                    redirectPassedFlag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("Invalid URL");
            }
        }
    }
