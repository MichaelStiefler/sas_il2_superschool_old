package com.maddox.il2.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameTrack;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.MsgNet;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetObj;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class NetMissionTrack {
	static class OutChannelCreater implements MsgTimeOutListener {

		public void msgTimeOut(Object obj) {
			if (obj != null && (obj instanceof NetChannel)) {
				NetChannel netchannel = (NetChannel)obj;
				if (netchannel.isDestroying())
					return;
				int i = netchannel.state();
				switch (i) {
				case 2: // '\002'
					try {
						netchannel.stopSortGuaranted();
					} catch (Exception exception) {
						netchannel.destroy("Cycle inits");
						NetMissionTrack.printDebug(exception);
						return;
					}
					netchannel.setStateInit(0);
					if (Main.cur().netChannelListener != null)
						Main.cur().netChannelListener.netChannelCreated(netchannel);
					netchannel.userState = 1;
					if (Mission.cur() != null && Mission.cur().netObj() != null)
						MsgNet.postRealNewChannel(Mission.cur().netObj(), netchannel);
					return;
				}
				return;
			} else {
				return;
			}
		}

		MsgTimeOut ticker;

		public OutChannelCreater() {
			ticker = new MsgTimeOut();
			ticker.setNotCleanAfterSend();
			ticker.setFlags(64);
			ticker.setListener(this);
			NetMissionTrack.netOut.startSortGuaranted();
			HashMapInt hashmapint = NetEnv.cur().objects;
			for (HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null); hashmapintentry != null; hashmapintentry = hashmapint
					.nextEntry(hashmapintentry)) {
				NetObj netobj = (NetObj)hashmapintentry.getValue();
				if (!NetMissionTrack.netOut.isMirrored(netobj) && !netobj.isCommon())
					MsgNet.postRealNewChannel(netobj, NetMissionTrack.netOut);
			}

			NetMissionTrack.netOut.setStateInit(2);
			MsgNet.postRealNewChannel((NetObj)NetEnv.cur().objects.get(9), NetMissionTrack.netOut);
			MsgTimeOut.post(64, Time.currentReal() + 1L, this, NetMissionTrack.netOut);
		}
	}

	public NetMissionTrack() {
	}

	public static InOutStreams recordingStreams() {
		return io;
	}

	public static boolean isRecording() {
		return io != null;
	}

	public static boolean isQuickRecording() {
		return io != null && bQuick;
	}

	public static NetChannel netChannelOut() {
		return netOut;
	}

	public static boolean isPlaying() {
		if (!(Main.cur() instanceof Main3D))
			return false;
		else
			return Main3D.cur3D().playRecordedStreams() != null;
	}

	public static float getRecordSpeed() {
		return recordSpeed;
	}

	public static int playingVersion() {
		return playingVersion;
	}

	public static int playingOriginalVersion() {
		return playingOriginalVersion;
	}

	public static void startPlaying(InOutStreams inoutstreams, int i, int j) {
		try {
			InputStream inputstream = inoutstreams.openStream("speed");
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
			recordSpeed = Float.parseFloat(bufferedreader.readLine());
			inputstream.close();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			recordSpeed = 100F;
		}
		playingVersion = i;
		playingOriginalVersion = j;
		netFilesTrackPlaying = new NetFilesTrack();
		netFilesTrackPlaying.startPlaying(inoutstreams);
	}

	public static void stopPlaying() {
		if (netFilesTrackPlaying == null) {
			return;
		} else {
			netFilesTrackPlaying.stopPlaying();
			netFilesTrackPlaying = null;
			return;
		}
	}

	public static void stopRecording() {
		if (!isRecording())
			return;
		try {
			bQuick = false;
			String s = recordingFileName;
			s = s.substring(s.indexOf("/") + 1);
			if (bRecordStarting) {
				System.out.println("Track NOT Saved");
				HUD.log(0, I18N.hud_log("TrackNotSaved"), false);
			} else {
				System.out.println("Stop Recording: " + recordingFileName);
				HUD.log(0, I18N.hud_log("StopRecording") + " " + s, false);
			}
			netFilesTrackRecording.stopRecording();
			Main3D.cur3D().keyRecord.stopRecording(true);
			Main3D.cur3D().keyRecord.clearRecorded();
			Main3D.cur3D().keyRecord.clearListExcludeCmdEnv();
			netOut.destroy();
			io.close();
			if (bRecordStarting) {
				File file = new File(HomePath.toFileSystemName(recordingFileName, 0));
				file.delete();
			}
		} catch (Exception exception) {
			printDebug(exception);
		}
		netFilesTrackRecording = null;
		netOut = null;
		io = null;
	}

	public static void startedRecording() {
		bRecordStarting = false;
	}

	public static void startRecording(String s, float f) {
		stopRecording();
		countRecorded++;
		recordingFileName = s;
		recordSpeed = f;
		io = new InOutStreams();
		try {
			bRecordStarting = true;
			io.create(new File(s), 2, 32768);
			PrintWriter printwriter = new PrintWriter(io.createStream("speed"));
			printwriter.println(f);
			printwriter.flush();
			printwriter.close();
			printwriter = new PrintWriter(io.createStream("version"));
			printwriter.println(103);
			if (isPlaying())
				printwriter.println(playingOriginalVersion());
			else
				printwriter.println(103);
			printwriter.flush();
			printwriter.close();
			java.io.OutputStream outputstream = io.createStream("traffic");
			netOut = new NetChannelOutStream(outputstream, 3);
			netOut.setMaxSpeed(100D);
			((NetChannelOutStream)netOut).setCheckSpeed(false);
			RTSConf.cur.netEnv.addChannel(netOut);
			new GameTrack(netOut);
			new OutChannelCreater();
			Main3D.cur3D().keyRecord.clearRecorded();
			Main3D.cur3D().keyRecord.stopRecording(false);
			Main3D.cur3D().keyRecord.addExcludeCmdEnv("pilot");
			Main3D.cur3D().keyRecord.addExcludeCmdEnv("move");
			Main3D.cur3D().keyRecord.addExcludeCmdEnv("gunner");
			netFilesTrackRecording = new NetFilesTrack();
			netFilesTrackRecording.startRecording();
		} catch (Exception exception) {
			netOut = null;
			io = null;
			netFilesTrackRecording = null;
			printDebug(exception);
		}
	}

	public static void startQuickRecording() {
		if (!Mission.isPlaying())
			return;
		if (isRecording())
			return;
		float f = (float)Config.cur.netSpeed / 1000F + 5F;
		if (isPlaying())
			f = recordSpeed;
		String s = quickFileName();
		startRecording(s, f);
		bQuick = true;
	}

	private static String quickFileName() {
		File file = new File(HomePath.toFileSystemName("records", 0));
		String as[] = file.list();
		if (as == null || as.length == 0)
			return "records/quick0000.ntrk";
		int i = 0;
		for (int j = 0; j < as.length; j++) {
			String s = as[j];
			if (s == null)
				continue;
			s = s.toLowerCase();
			if (!s.startsWith("quick") || s.length() != "quick0000.ntrk".length())
				continue;
			String s1 = s.substring(s.length() - "0000.ntrk".length(), s.length() - ".ntrk".length());
			try {
				int k = Integer.parseInt(s1);
				if (i <= k)
					i = k + 1;
			} catch (Exception exception) {
			}
		}

		if (i > 999)
			return "records/quick" + i + ".ntrk";
		if (i > 99)
			return "records/quick0" + i + ".ntrk";
		if (i > 9)
			return "records/quick00" + i + ".ntrk";
		else
			return "records/quick000" + i + ".ntrk";
	}

	static void printDebug(Exception exception) {
		System.out.println(exception.getMessage());
		exception.printStackTrace();
	}

	protected static InOutStreams io;
	protected static NetChannel netOut;
	protected static NetFilesTrack netFilesTrackRecording;
	protected static NetFilesTrack netFilesTrackPlaying;
	public static long playingStartTime;
	public static String recordingFileName;
	private static boolean bQuick;
	private static float recordSpeed;
	private static boolean bRecordStarting;
	public static int countRecorded;
	private static int playingVersion;
	private static int playingOriginalVersion;
}
