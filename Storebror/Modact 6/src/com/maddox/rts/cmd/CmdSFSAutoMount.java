package com.maddox.rts.cmd;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Map;

import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.SFS;

public class CmdSFSAutoMount extends Cmd {

	public Object exec(CmdEnv cmdenv, Map map) {
		String theSFSFolderName = null;
		boolean flag = false;
		if (nargs(map, "MOUNT") > 0) {
			theSFSFolderName = arg(map, "MOUNT", 0);
			try {
				this.AutoMountSFSFiles(new File(theSFSFolderName));
			} catch (Exception exception1) {
				bMountError = true;
				ERR_HARD("SFS files from folder (" + theSFSFolderName + ") NOT Mounted: " + exception1.getMessage());
				exception1.printStackTrace();
			}
			flag = true;
		}
		if (nargs(map, "UNMOUNT") > 0) {
			theSFSFolderName = arg(map, "UNMOUNT", 0);
			try {
				this.AutoUnMountSFSFiles(new File(theSFSFolderName));
			} catch (Exception exception) {
				ERR_HARD("SFS files from folder  (" + theSFSFolderName + ") NOT UnMounted: " + exception.getMessage());
				exception.printStackTrace();
			}
			flag = true;
		}
		if (flag) {
			return CmdEnv.RETURN_OK;
		} else {
			ERR_HARD("Bad command format");
			return null;
		}
	}

	private File[] CreateFileList(File theSFSFolder) {
		return theSFSFolder.listFiles(new FilenameFilter() {
			public boolean accept(File theFileFolder, String theFileName) {
				return theFileName.toLowerCase().endsWith(".sfs");
			}
		});
	}

	private File[] CreateSubFolderList(File theSFSFolder) {
		return theSFSFolder.listFiles(new FileFilter() {
			public boolean accept(File theFile) {
				return theFile.isDirectory();
			}
		});
	}

	private void AutoMountSFSFiles(File theSFSFolder) {
		File[] theSFSFileList = CreateFileList(theSFSFolder);
		if (theSFSFileList.length < 1)
			return;
		System.out.println("AutoMounting SFS files from folder " + theSFSFolder.getPath() + " now...");
		for (int i = 0; i < theSFSFileList.length; i++) {
			try {
				if (theSFSFileList[i].getName().startsWith("-")) {
					System.out.println("Skipping AutoMount of file " + theSFSFileList[i].getParent() + File.separator
							+ theSFSFileList[i].getName().substring(1) + " (disabled)");
					continue;
				}
				if (theSFSFileList[i].isDirectory())
					continue;
				System.out.print("Trying to AutoMount " + theSFSFileList[i].getPath() + "... ");
				SFS.mount(theSFSFileList[i].getPath(), 0);
				System.out.println("mounted successfully!");
			} catch (Exception e) {
				System.out.println("mount failed, see error below:");
				System.out.println(e.getMessage());
			}
		}
		System.out.println("AutoMounting SFS files from folder " + theSFSFolder.getPath() + " finished.");
		File[] theSFSSubfolderList = CreateSubFolderList(theSFSFolder);
		for (int i = 0; i < theSFSSubfolderList.length; i++) {
			try {
				if (theSFSSubfolderList[i].getName().startsWith("-")) {
					System.out.println("Skipping AutoMount of subfolder " + theSFSSubfolderList[i].getParent() + File.separator
							+ theSFSSubfolderList[i].getName().substring(1) + " (disabled)");
					continue;
				}
				this.AutoMountSFSFiles(theSFSSubfolderList[i]);
			} catch (Exception e) {
				System.out.println("Automount invocation for subfolder " + theSFSSubfolderList[i].getPath()
						+ " failed, see error below:");
				System.out.println(e.getMessage());
			}
		}
	}

	private void AutoUnMountSFSFiles(File theSFSFolder) {
		File[] theSFSFileList = CreateFileList(theSFSFolder);
		if (theSFSFileList.length < 1)
			return;
		System.out.println("AutoUnMounting SFS files from folder " + theSFSFolder.getPath() + " now...");
		for (int i = 0; i < theSFSFileList.length; i++) {
			try {
				if (theSFSFileList[i].getName().startsWith("-")) {
					continue;
				}
				if (theSFSFileList[i].isDirectory())
					continue;
				System.out.print("Trying to AutoUnMount " + theSFSFileList[i].getPath() + "... ");
				SFS.unMount(theSFSFileList[i].getPath());
				System.out.println("unmounted successfully!");
			} catch (Exception e) {
				System.out.println("unmount failed, see error below:");
				System.out.println(e.getMessage());
			}
		}
		System.out.println("AutoUnMounting SFS files from folder " + theSFSFolder.getPath() + " finished.");
		File[] theSFSSubfolderList = CreateSubFolderList(theSFSFolder);
		for (int i = 0; i < theSFSSubfolderList.length; i++) {
			try {
				if (theSFSSubfolderList[i].getName().startsWith("-")) {
					continue;
				}
				this.AutoUnMountSFSFiles(theSFSSubfolderList[i]);
			} catch (Exception e) {
				System.out.println("Autounmount invocation for subfolder " + theSFSSubfolderList[i].getPath()
						+ " failed, see error below:");
				System.out.println(e.getMessage());
			}
		}
	}

	public CmdSFSAutoMount() {
		param.put("MOUNT", null);
		param.put("UNMOUNT", null);
		_properties.put("NAME", "sfsautomount");
		_levelAccess = 0;
	}

	public static final String MOUNT = "MOUNT";
	public static final String UNMOUNT = "UNMOUNT";
	public static boolean bMountError = false;

}
