package com.maddox.il2.game;

import java.io.IOException;
import java.util.Calendar;

import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetChannelOutStream;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.Spawn;

public class GameTrack extends NetObj {
	static class SPAWN implements NetSpawn {

		public void netSpawn(int i, NetMsgInput netmsginput) {
			try {
				new GameTrack(netmsginput.channel(), i);
			} catch (Exception exception) {
				GameTrack.printDebug(exception);
			}
		}

		SPAWN() {
		}
	}

	public NetChannel channel() {
		return channel;
	}

	public boolean netInput(NetMsgInput netmsginput) throws IOException {
		if (Main3D.cur3D().gameTrackRecord() != null)
			try {
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
				Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
			} catch (Exception exception) {
			}
		byte byte0 = netmsginput.readByte();
		switch (byte0) {
		case 0: // '\0'
		case 1: // '\001'
		case 2: // '\002'
		case 3: // '\003'
			Main3D.cur3D().hud.netInputLog(byte0, netmsginput);
			break;

		case 4: // '\004'
			Voice.netInputPlay(netmsginput);
			break;

		case 5: // '\005'
			Main3D.cur3D().aircraftHotKeys.fromTrackSign(netmsginput);
			break;

		default:
			return false;
		}
		return true;
	}

	public void startKeyPlay() {
		Main3D.cur3D().startPlayRecordedMission();
		Main3D.cur3D().keyRecord.startRecordingNet();
	}

	public void startKeyRecord(NetMsgGuaranted netmsgguaranted) {
		com.maddox.rts.InOutStreams inoutstreams;
		((NetChannelOutStream)channel).flush();
		inoutstreams = NetMissionTrack.recordingStreams();
		if (inoutstreams == null)
			return;
		try {
			Main3D.cur3D().saveRecordedStates0(inoutstreams);
			Main3D.cur3D().saveRecordedStates1(inoutstreams);
			Main3D.cur3D().saveRecordedStates2(inoutstreams);
			NetChannelInStream.sendSyncMsg(channel);
			((NetChannelOutStream)channel)._putMessage(netmsgguaranted);
			((NetChannelOutStream)channel).flush();
			Main3D.cur3D().keyRecord.startRecordingNet();
			System.out.println("Start Recording: " + NetMissionTrack.recordingFileName);
			String s = NetMissionTrack.recordingFileName;
			s = s.substring(s.indexOf("/") + 1);
			HUD.log(0, I18N.hud_log("StartRecording") + " " + s, false);
			channel.setMaxSpeed(NetMissionTrack.getRecordSpeed());
			((NetChannelOutStream)channel).setCheckSpeed(true);
			NetMissionTrack.startedRecording();
		} catch (Exception exception) {
			printDebug(exception);
		}
		return;
	}

	public GameTrack(NetChannel netchannel) {
		super(null);
		channel = netchannel;
		Main3D.cur3D().setGameTrackRecord(this);
	}

	public GameTrack(NetChannel netchannel, int i) {
		super(null, netchannel, i);
		channel = netchannel;
		Main3D.cur3D().setGameTrackPlay(this);
	}

	public void msgNetDelChannel(NetChannel netchannel) {
		if (channel == netchannel)
			destroy();
	}

	public void destroy() {
		Main3D.cur3D().clearGameTrack(this);
		channel = null;
		super.destroy();
	}

	public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
		if (!(netchannel instanceof NetChannelOutStream) || isMirror()) {
			return null;
		} else {
			NetMsgSpawn netmsgspawn = new NetMsgSpawn(this);
			return netmsgspawn;
		}
	}

	public static final int SINGLE_VERSION = 130;
	public static final int NET_VERSION = 103;
	public static final int HUD_log_Integer = 0;
	public static final int HUD_log_Id = 1;
	public static final int HUD_log_RightBottom = 2;
	public static final int HUD_log_Center = 3;
	public static final int VOICE = 4;
	public static final int HOT_KEY_SIGHT = 5;
	private NetChannel channel;

	static {
		Spawn.add(com.maddox.il2.game.GameTrack.class, new SPAWN());
	}

}
