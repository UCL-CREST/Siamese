    public boolean loadPlayer(String classname) {
        Player p;
        boolean retval;
        acquirePlayerLock();
        p = _player;
        _player = null;
        try {
            _player = (Player) Class.forName(classname).getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            (Megatron._log).log("PlayerLoader: " + e + "\n");
            _player = p;
        } catch (NoSuchMethodException e) {
            (Megatron._log).log("PlayerLoader: " + e + "\n");
            _player = p;
        } catch (IllegalAccessException e) {
            (Megatron._log).log("PlayerLoader: Could not access constructor of " + classname + "\n");
            _player = p;
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("PlayerLoader: A queer error has been encountered\n" + e + "\n");
            _player = p;
        }
        _log.debug("PlayerLoader: made it through...\n");
        if (_player == p) retval = false; else retval = true;
        releasePlayerLock();
        return retval;
    }
