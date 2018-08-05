    public boolean loadMetadataSource(String classname) {
        MetadataSource ms;
        boolean retval;
        _log.debug("loadMetadataSource");
        acquirePlayerLock();
        ms = _fallbackSource;
        _fallbackSource = null;
        try {
            _fallbackSource = (MetadataSource) Class.forName(classname).getConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            _log.log("MetadataSourceLoader: " + e + "\n");
            _fallbackSource = ms;
        } catch (NoSuchMethodException e) {
            _log.log("MetadataSourceLoader: " + e + "\n");
            _fallbackSource = ms;
        } catch (IllegalAccessException e) {
            _log.log("MetadataSourceLoader: Could not access constructor of " + classname + "\n");
            _fallbackSource = ms;
        } catch (Exception e) {
            e.printStackTrace();
            _log.error("MetadataSourceLoader: A queer error has been encountered\n" + e + "\n");
            _fallbackSource = ms;
        }
        _log.debug("MetadataLoader: made it through...\n");
        if (_fallbackSource == ms) retval = false; else retval = true;
        releasePlayerLock();
        return retval;
    }
