    public void unmount() {
        if (activeMntdir == null) {
            throw new AlreadyUnmountedException("Already unmounted.");
        }
        System.out.println("unmounting " + activeMntdir);
        Process unmountProc;
        try {
            unmountProc = Runtime.getRuntime().exec(fusermount + " -u " + activeMntdir);
            InputStream err = unmountProc.getErrorStream();
            InputStream out = unmountProc.getInputStream();
            int unmountExitCode = unmountProc.waitFor();
            if (unmountExitCode != 0) {
                dump(System.out, out);
                dump(System.err, err);
                throw new UnmountFailedException(fusermount + " exited with exit code " + unmountExitCode);
            }
            activeMntdir = null;
        } catch (IOException e) {
            throw new UnmountFailedException(e);
        } catch (InterruptedException e) {
            throw new UnmountFailedException(e);
        }
    }
