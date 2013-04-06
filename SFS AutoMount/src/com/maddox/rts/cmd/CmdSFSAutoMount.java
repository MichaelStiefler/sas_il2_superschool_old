package com.maddox.rts.cmd;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.SFS;

public class CmdSFSAutoMount extends Cmd
{

	private File[] sfsFileList = null;
	private String SFSFolder = null;
    public Object exec(CmdEnv cmdenv, Map map)
    {
        boolean flag = false;
        if(nargs(map, "MOUNT") > 0)
        {
        	this.SFSFolder = arg(map, "MOUNT", 0);
            try
            {
            	this.AutoMountSFSFiles();
            }
            catch(Exception exception1)
            {
                bMountError = true;
                ERR_HARD("SFS files from folder (" + this.SFSFolder + ") NOT Mounted: " + exception1.getMessage());
                exception1.printStackTrace();
            }
            flag = true;
        }
        if(nargs(map, "UNMOUNT") > 0)
        {
        	this.SFSFolder = arg(map, "UNMOUNT", 0);
            try
            {
            	this.AutoUnMountSFSFiles();
            }
            catch(Exception exception)
            {
                ERR_HARD("SFS files from folder  (" + this.SFSFolder + ") NOT UnMounted: " + exception.getMessage());
                exception.printStackTrace();
            }
            flag = true;
        }
        if(flag)
        {
            return CmdEnv.RETURN_OK;
        } else
        {
            ERR_HARD("Bad command format");
            return null;
        }
    }
    
    private void CreateFileList()
    {
    	File dir = new File(this.SFSFolder);
    	this.sfsFileList = dir.listFiles(new FilenameFilter() {
    	    public boolean accept(File dir, String name) {
    	        return name.toLowerCase().endsWith(".sfs");
    	    }
    	});
    }
    
    private void AutoMountSFSFiles()
    {
    	CreateFileList();
    	System.out.println("AutoMounting SFS files from folder " + this.SFSFolder + " now...");
    	for (int i=0; i<this.sfsFileList.length; i++) {
    		try {
    			if (this.sfsFileList[i].getName().startsWith("-")) {
        			System.out.println("Skipping AutoMount of file " + this.sfsFileList[i].getParent() + File.separator + this.sfsFileList[i].getName().substring(1) + " (disabled)");
        			continue;
    			}
    			System.out.print("Trying to AutoMount " + this.sfsFileList[i].getPath() + "... ");
    			SFS.mount(this.sfsFileList[i].getPath(), 0);
    			System.out.println("mounted successfully!");
    		} catch (Exception e) {
    			System.out.println("mount failed, see error below:");
    			System.out.println(e.getMessage());
    		}
    	}
    	System.out.println("AutoMounting SFS files from folder " + this.SFSFolder + " finished.");
    }

    private void AutoUnMountSFSFiles()
    {
    	CreateFileList();
    	System.out.println("AutoUnMounting SFS files from folder " + this.SFSFolder + " now...");
    	for (int i=0; i<this.sfsFileList.length; i++) {
    		try {
    			if (this.sfsFileList[i].getName().startsWith("-")) {
        			continue;
    			}
    			System.out.print("Trying to AutoUnMount " + this.sfsFileList[i].getPath() + "... ");
    			SFS.unMount(this.sfsFileList[i].getPath());
    			System.out.println("unmounted successfully!");
    		} catch (Exception e) {
    			System.out.println("unmount failed, see error below:");
    			System.out.println(e.getMessage());
    		}
    	}
    	System.out.println("AutoUnMounting SFS files from folder " + this.SFSFolder + " finished.");
    }

    public CmdSFSAutoMount()
    {
        param.put("MOUNT", null);
        param.put("UNMOUNT", null);
        _properties.put("NAME", "sfsautomount");
        _levelAccess = 0;
    }

    public static final String MOUNT = "MOUNT";
    public static final String UNMOUNT = "UNMOUNT";
    public static boolean bMountError = false;

}
