  @SuppressWarnings("finally")
  private void decompress(final File src) throws IOException
  {
    final String srcPath = src.getPath();
    checkSourceFile(src);
    final boolean test = this.switches.contains(Switch.test);

    final File dst;
    if (test)
      dst = File.createTempFile("jaxlib-bzip", null);
    else
    {
      if (srcPath.endsWith(".bz2"))
        dst = new File(srcPath.substring(0, srcPath.length() - 4));
      else
      {
        this.log.println("WARNING: Can't guess original name, using extension \".out\":").println(srcPath);
        dst = new File(srcPath + ".out");
      }
    }
    if (!checkDestFile(dst))
      return;

    final boolean showProgress = this.switches.contains(Switch.showProgress);

    BZip2InputStream  in          = null;
    FileOutputStream  out         = null;
    FileChannel       outChannel  = null;
    FileLock          inLock      = null;
    FileLock          outLock     = null;

    try
    {
      final FileInputStream in0       = new FileInputStream(src);
      final FileChannel     inChannel = in0.getChannel();
      final long            inSize    = inChannel.size();

      inLock = inChannel.tryLock(0, inSize, true);
      if (inLock == null)
        throw error("source file locked by another process: " + src);

      in          = new BZip2InputStream(new BufferedXInputStream(in0, 8192));
      out         = new FileOutputStream(dst);
      outChannel  = out.getChannel();

      outLock = outChannel.tryLock();
      if (outLock == null)
        throw error("destination file locked by another process: " + dst);

      if (showProgress || this.verbose)
      {
        this.log.print("source: " + src).print(": size=").println(inSize);
        this.log.println("target: " + dst);
      }

      long  pos       = 0;
      int   progress  = 0;
      final long maxStep = showProgress ? Math.max(8192, inSize / MAX_PROGRESS) : Integer.MAX_VALUE;

      while (true)
      {
        final long step = outChannel.transferFrom(in, pos, maxStep);
        if (step <= 0)
        {
          final long a = inChannel.size();
          if (a != inSize)
            throw error("file " + src + " has been modified concurrently by another process");

          if (inChannel.position() >= inSize)
          {
            if (showProgress)
            {
              for (int i = progress; i < MAX_PROGRESS; i++)
                this.log.print('#');
              this.log.println(" done");
            }
            break;
          }
        }
        else
        {
          pos += step;
          if (showProgress)
          {
            final double  p           = (double) inChannel.position() / (double) inSize;
            final int     newProgress = (int) (MAX_PROGRESS * p);
            for (int i = progress; i < newProgress; i++)
              this.log.print('#');
            progress = newProgress;
          }
        }
      }

      final long outSize = outChannel.size();
      in.close();
      out.close();

      if (this.verbose)
      {
        final double ratio = (outSize == 0) ? (inSize * 100) : ((double) inSize / (double) outSize);
        this.log.print("compressed size: ").print(inSize)
          .print("; decompressed size: ").print(outSize)
          .print("; compression ratio: ").print(ratio).println('%');
      }

      if (!test && !this.switches.contains(Switch.keep))
      {
        if (!src.delete())
          throw error("unable to delete sourcefile: " + src);
      }

      if (test && !dst.delete())
        throw error("unable to delete testfile: " + dst);
    }
    catch (final IOException ex)
    {
      IO.tryClose(in);
      IO.tryClose(out);
      IO.tryRelease(inLock);
      IO.tryRelease(outLock);
      try
      {
        this.log.println();
      }
      finally
      {
        throw ex;
      }
    }

  }
