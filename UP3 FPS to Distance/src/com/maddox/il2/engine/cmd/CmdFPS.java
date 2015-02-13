package com.maddox.il2.engine.cmd;

import java.text.DecimalFormat;
import java.util.Map;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.TextScr;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.Time;
import com.maddox.sound.AudioDevice;

public class CmdFPS extends Cmd implements MsgTimeOutListener {

	public void msgTimeOut(Object obj) {
		msg.post();
		if (!bGo)
			return;
		long l = Time.real();
		int i = RendersMain.frame();
		
		// TODO: +++ FPS to Distance Mod +++
		if (this.bAdjVisToFps) this.adjDistanceToFps(l, i);
		// TODO: --- FPS to Distance Mod ---
		
		if (l >= timePrev + 250L) {
			fpsCur = (1000D * (double)(i - framePrev)) / (double)(l - timePrev);
			if (fpsMin > fpsCur)
				fpsMin = fpsCur;
			if (fpsMax < fpsCur)
				fpsMax = fpsCur;
			framePrev = i;
			timePrev = l;
		}
		if (framePrev == frameStart)
			return;
		if (bShow) {
			Render render = (Render)Actor.getByName("renderTextScr");
			if (render == null)
				return;
			TTFont ttfont = TextScr.font();
			int j = render.getViewPortWidth();
			int k = render.getViewPortHeight();
			String s = "fps:" + fpsInfo();
			int i1 = (int)ttfont.width(s);
			int j1 = k - ttfont.height() - 5;
			int k1 = (j - i1) / 2;
			TextScr.output(k1, j1, s);
		}
		if (logPeriod > 0L && l >= logPrintTime + logPeriod) {
			System.out.println("fps:" + fpsInfo());
			logPrintTime = l;
		}
	}

	public Object exec(CmdEnv cmdenv, Map map) {
		boolean flag = false;
		checkMsg();
		if (map.containsKey(SHOW)) {
			bShow = true;
			flag = true;
		} else if (map.containsKey(HIDE)) {
			bShow = false;
			flag = true;
		}
		if (map.containsKey(LOG)) {
			int i = arg(map, LOG, 0, 5);
			if (i < 0)
				i = 0;
			logPeriod = (long)i * 1000L;
			flag = true;
		}
		if (map.containsKey(PERF)) {
			AudioDevice.setPerformInfo(true);
			flag = true;
		}
		if (map.containsKey(START)) {
			if (bGo && timeStart != timePrev && logPeriod == 0L)
				INFO_HARD("fps:" + fpsInfo());
			timeStart = Time.real();
			frameStart = RendersMain.frame();
			fpsMin = 1000000D;
			fpsMax = 0.0D;
			fpsCur = 0.0D;
			timePrev = timeStart;
			framePrev = frameStart;
			logPrintTime = timeStart;
			bGo = true;
			flag = true;
		} else if (map.containsKey(STOP)) {
			if (bGo && timeStart != timePrev && logPeriod == 0L)
				INFO_HARD("fps:" + fpsInfo());
			bGo = false;
			flag = true;
		}

		// TODO: +++ FPS to Distance Mod +++
		if (map.containsKey(VISSTART)) {
			this.bAdjVisToFps = true;
			this.curFpsValid = false;
			this.initFpsToDistance();
			flag = true;
		}
		if (map.containsKey(VISSTOP)) {
			this.bAdjVisToFps = false;
			this.curFpsValid = false;
			flag = true;
		}
		if (map.containsKey(VISSETFPS)) {
			this.visualDistanceDesiredFps = arg(map, VISSETFPS, 0, DEFAULT_VISUAL_DISTANCE_DESIRED_FPS);
			flag = true;
		}
		if (map.containsKey(VISSETMINDIST)) {
			this.minVisualDistance = arg(map, VISSETFPS, 0, DEFAULT_MIN_VISUAL_DISTANCE);
			flag = true;
		}
		// TODO: --- FPS to Distance Mod ---

		if (!flag) {
			INFO_HARD("  LOG  " + logPeriod / 1000L);
			if (bShow)
				INFO_HARD("  SHOW");
			else
				INFO_HARD("  HIDE");
			if (bGo) {
				if (timeStart != timePrev)
					INFO_HARD("  " + fpsInfo());
			} else {
				INFO_HARD("  STOPPED");
			}
		}
		return CmdEnv.RETURN_OK;
	}

	private String fpsInfo() {
		// TODO: +++ FPS to Distance Mod +++
		String retVal = "" + (int)Math.floor(fpsCur);
		if	(this.bAdjVisToFps) {
			retVal += " tgt:" + this.visualDistanceDesiredFps;
			retVal += " mul:" + twoDigits.format(this.visualDistanceMultiplier);
		}
		retVal += " avg:" + (int)Math.floor((1000D * (double)(framePrev - frameStart)) / (double)(timePrev - timeStart));
		retVal += " max:" + (int)Math.floor(fpsMax);
		retVal += " min:" + (int)Math.floor(fpsMin);
		retVal += " #fr:" + (framePrev - frameStart);
		return retVal;
		
//		return "" + (int)Math.floor(fpsCur) + " avg:"
//				+ (int)Math.floor((1000D * (double)(framePrev - frameStart)) / (double)(timePrev - timeStart)) + " max:"
//				+ (int)Math.floor(fpsMax) + " min:" + (int)Math.floor(fpsMin) + " #fr:" + (framePrev - frameStart);
		// TODO: --- FPS to Distance Mod ---
	}

	private void checkMsg() {
		if (msg == null) {
			msg = new MsgTimeOut();
			msg.setListener(this);
			msg.setNotCleanAfterSend();
			msg.setFlags(72);
		}
		if (!msg.busy())
			msg.post();
	}

	public CmdFPS() {
		bGo = false;
		bShow = false;
		logPeriod = 5000L;
		logPrintTime = -1L;
		param.put(LOG, null);
		param.put(START, null);
		param.put(STOP, null);
		param.put(SHOW, null);
		param.put(HIDE, null);
		param.put(PERF, null);
		// TODO: +++ FPS to Distance Mod +++
		param.put(VISSTART, null);
		param.put(VISSTOP, null);
		param.put(VISSETFPS, null);
		param.put(VISSETMINDIST, null);
		this.bAdjVisToFps = false;
		this.initFpsToDistance();
		// TODO: --- FPS to Distance Mod ---
		_properties.put("NAME", "fps");
		_levelAccess = 1;
	}

	// TODO: +++ FPS to Distance Mod +++
	private void initFpsToDistance() {
		this.visualDistanceMultiplier = 1.0F;
		this.curFpsValid = false;
		this.timeRealLast = Time.real();
		this.framesLast = RendersMain.frame();
		this.minVisualDistance = DEFAULT_MIN_VISUAL_DISTANCE;
		this.visualDistanceDesiredFps = DEFAULT_VISUAL_DISTANCE_DESIRED_FPS;
		this.worldMaxVisualDistance = Config.cur.ini.get("MODS", "MaxVisualDistance", World.MaxVisualDistance);
//		System.out.println("worldMaxVisualDistance " + worldMaxVisualDistance);
		this.worldMaxStaticVisualDistance = Config.cur.ini.get("MODS", "MaxStaticVisualDistance", World.MaxStaticVisualDistance);
//		System.out.println("worldMaxStaticVisualDistance " + worldMaxStaticVisualDistance);
		this.worldMaxLongVisualDistance = Config.cur.ini.get("MODS", "MaxLongVisualDistance", World.MaxLongVisualDistance);
//		System.out.println("worldMaxLongVisualDistance " + worldMaxLongVisualDistance);
		this.worldMaxPlateVisualDistance = Config.cur.ini.get("MODS", "MaxPlateVisualDistance", World.MaxPlateVisualDistance);
//		System.out.println("worldMaxPlateVisualDistance " + worldMaxPlateVisualDistance);
		this.minVisualDistance = Config.cur.ini.get("MODS", "MinVisualDistance", this.minVisualDistance);
//		System.out.println("minVisualDistance " + minVisualDistance);
		this.visualDistanceDesiredFps = Config.cur.ini.get("MODS", "VisualDistanceDesiredFps", this.visualDistanceDesiredFps);
//		System.out.println("visualDistanceDesiredFps " + visualDistanceDesiredFps);
	}

	private void adjDistanceToFps(long timeReal, int frames) {
//		System.out.println("TEST1 " + timeReal + " " + frames);
		if (Time.speed() > 1.1F) return;
		if (!this.curFpsValid) {
			this.framesLast = frames;
			this.timeRealLast = timeReal;
			this.curFpsValid = true;
			return;
		}
		float curFps = (1000F * (float)(frames - this.framesLast)) / (float)(timeReal - this.timeRealLast);
		this.framesLast = frames;
		this.timeRealLast = timeReal;
//		System.out.println("TEST2 " + curFps);
		if (curFps < (float)this.visualDistanceDesiredFps * 0.9F) {
			this.visualDistanceMultiplier *= World.Rnd().nextFloat(0.8F, 0.9F);
		} else if (curFps > (float)this.visualDistanceDesiredFps / 0.9F) {
			this.visualDistanceMultiplier *= World.Rnd().nextFloat(1.1F, 1.2F);
		}
		if (this.visualDistanceMultiplier > 10F) this.visualDistanceMultiplier = 10F;
		if (this.visualDistanceMultiplier < 0.1F) this.visualDistanceMultiplier = 0.1F;
//		System.out.println("TEST3 " + this.visualDistanceMultiplier);
		World.MaxVisualDistance = Math.max(this.worldMaxVisualDistance * this.visualDistanceMultiplier, this.minVisualDistance);
		World.MaxStaticVisualDistance = Math.max(this.worldMaxStaticVisualDistance * this.visualDistanceMultiplier, this.minVisualDistance);
		World.MaxLongVisualDistance = Math.max(this.worldMaxLongVisualDistance * this.visualDistanceMultiplier, this.minVisualDistance);
		World.MaxPlateVisualDistance = Math.max(this.worldMaxPlateVisualDistance * this.visualDistanceMultiplier, this.minVisualDistance);
//		System.out.println("TEST4: " + World.MaxVisualDistance);
	}
	// TODO: --- FPS to Distance Mod ---

	private boolean bGo;
	private boolean bShow;
	private long timeStart;
	private int frameStart;
	private double fpsMin;
	private double fpsMax;
	private double fpsCur;
	private long timePrev;
	private int framePrev;
	private long logPeriod;
	private long logPrintTime;
	public static final String LOG = "LOG";
	public static final String START = "START";
	public static final String STOP = "STOP";
	public static final String SHOW = "SHOW";
	public static final String HIDE = "HIDE";
	public static final String PERF = "PERF";
	private MsgTimeOut msg;

	// TODO: +++ FPS to Distance Mod +++
	private static final float DEFAULT_MIN_VISUAL_DISTANCE = 2000.0F;
	private static final int DEFAULT_VISUAL_DISTANCE_DESIRED_FPS = 60;
	public static final String VISSTART = "VISSTART";
	public static final String VISSTOP = "VISSTOP";
	public static final String VISSETFPS = "VISSETFPS";
	public static final String VISSETMINDIST = "VISSETMINDIST";
	private static final DecimalFormat twoDigits = new DecimalFormat("#0.##");
	private float minVisualDistance;
	private int visualDistanceDesiredFps;
	private boolean bAdjVisToFps = false;
	private boolean curFpsValid = false;
	private float worldMaxVisualDistance;
	private float worldMaxStaticVisualDistance;
	private float worldMaxLongVisualDistance;
	private float worldMaxPlateVisualDistance;
	private float visualDistanceMultiplier;
	private int framesLast;
	private long timeRealLast;
	// TODO: --- FPS to Distance Mod ---

	// TODO: +++ FPS to Distance Mod +++
	// TODO: --- FPS to Distance Mod ---

}
