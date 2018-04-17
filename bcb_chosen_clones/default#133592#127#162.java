    static ByteArrayOutputStream sys_exec(String exec) {
        if (exec.length() == 0) return null;
        Process p = null;
        StreamEater err = null, out = null;
        try {
            try {
                p = Runtime.getRuntime().exec(exec);
                err = new StreamEater(p.getErrorStream());
                out = new StreamEater(p.getInputStream());
                int status = p.waitFor();
                if (status != 0) throw new BuildError("Status " + status + " from " + exec);
                out.close();
                err.close();
            } catch (IOException e) {
                throw new BuildError("IOException from " + exec);
            } catch (InterruptedException e) {
                throw new BuildError("Interrupted while waiting on " + exec);
            } finally {
                if (p != null) p.destroy();
            }
        } catch (BuildError be) {
            System.out.println();
            if (out != null) try {
                out._buf.writeTo(System.out);
            } catch (IOException e) {
                throw new BuildError(e.toString());
            }
            if (err != null) try {
                err._buf.writeTo(System.out);
            } catch (IOException e) {
                throw new BuildError(e.toString());
            }
            throw be;
        }
        return out._buf;
    }
