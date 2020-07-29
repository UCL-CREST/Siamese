    public void requestConfirm() throws Exception {
        if (!this._c.checkProperty("directory.request", "request")) {
            throw new Exception("product has no active request");
        }
        if (!new File(WBSAgnitioConfiguration.getHARequestFile()).canWrite()) {
            throw new Exception("cannot remove request from system");
        }
        HashMap<String, String> values = getValues(WBSAgnitioConfiguration.getHARequestFile());
        if (!values.containsKey("address.virtual")) {
            throw new Exception("failed to determine the virtual address");
        }
        if (!values.containsKey("address.real")) {
            throw new Exception("failed to determine the remote address");
        }
        HTTPClient _hc = new HTTPClient(values.get("address.real"));
        if (TomcatConfiguration.checkHTTPS()) {
            _hc.setSecure(true);
        }
        _hc.load("/admin/Comm?type=" + CommResponse.TYPE_HA + "&command=" + CommResponse.COMMAND_REQUEST_CONFIRM + "&virtual=" + values.get("address.virtual"));
        String _reply = new String(_hc.getContent());
        if (_reply.isEmpty()) {
            throw new Exception("remote product has not sent any reply");
        } else if (_reply.indexOf("done") == -1) {
            throw new Exception(_reply);
        }
        HAConfiguration.setSlave(values.get("address.virtual"), values.get("address.real"));
        File _f = new File(WBSAgnitioConfiguration.getOptionalSchemaRequestFile());
        if (_f.exists()) {
            FileOutputStream _fos = new FileOutputStream(WBSAgnitioConfiguration.getOptionalSchemaFile());
            FileInputStream _fis = new FileInputStream(_f);
            while (_fis.available() > 0) {
                _fos.write(_fis.read());
            }
            _fis.close();
            _fos.close();
            _f.delete();
        }
        _f = new File(WBSAgnitioConfiguration.getSchemaObjectRequestFile());
        if (_f.exists()) {
            FileOutputStream _fos = new FileOutputStream(WBSAgnitioConfiguration.getSchemaObjectFile());
            FileInputStream _fis = new FileInputStream(_f);
            while (_fis.available() > 0) {
                _fos.write(_fis.read());
            }
            _fis.close();
            _fos.close();
            _f.delete();
        }
        new File(WBSAgnitioConfiguration.getHARequestFile()).delete();
        this._c.removeProperty("directory.request");
        this._c.setProperty("directory.virtual", values.get("address.virtual"));
        this._c.setProperty("directory.status", "slave");
        this._c.store();
    }
