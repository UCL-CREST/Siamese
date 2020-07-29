    private void bootKernel(String conf) {
        try {
            AssetManager am = getResources().getAssets();
            InputStream is = am.open(conf + ".conf");
            Properties props = new Properties();
            props.load(is);
            is.close();
            Log.d("bootKernel", "Listing sdcard assets...");
            String[] sdcardfiles = am.list("sdcard");
            for (String file : sdcardfiles) {
                Log.d("bootKernel", "Copying sdcard asset " + file + ".");
                AssetFileDescriptor afd = am.openFd("sdcard/" + file);
                FileInputStream fis = afd.createInputStream();
                FileChannel fic = fis.getChannel();
                FileOutputStream fos = new FileOutputStream("/sdcard/" + file);
                FileChannel foc = fos.getChannel();
                fic.transferTo(0, fic.size(), foc);
                fic.close();
                foc.close();
            }
            Configuration gconf = new JavaPropertiesConfiguration(props);
            Configuration bconf = gconf.subset("boot");
            String kclass_name = bconf.getString("kernel");
            Log.d("bootKernel", "Attempting to load kernel from class '" + kclass_name + "'...");
            Class<? extends Kernel> kclass = Class.forName(kclass_name).asSubclass(Kernel.class);
            Kernel kernel = kclass.newInstance();
            Log.d("bootKernel", "Kernel loaded, proceeding with boot...");
            BootContext bctx = new SimpleBootContext(gconf, AndroidBridgeService.class, AndroidBridgeServiceImpl.class);
            kernel.boot(bctx).get();
            Log.d("bootKernel", "Kernel boot complete.");
        } catch (Exception e) {
            Log.e("bootKernel", "Unable to boot kernel due to exception.", e);
            finish();
        }
    }
