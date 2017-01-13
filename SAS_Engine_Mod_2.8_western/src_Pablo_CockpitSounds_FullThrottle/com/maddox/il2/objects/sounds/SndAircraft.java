// PAL-CockpitSoundsMod and Sani-Full Throttle Flame effect for 4.12.2m -- updated for SAS Engine MOD 2.8 on 13th/Jan./2017
// Based on PAL-CockpitSoundsMod version 25th/Dec./2016

package com.maddox.il2.objects.sounds;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.objects.air.TypeSeaPlane;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.rts.*;
import com.maddox.sound.*;
import java.io.PrintStream;

public class SndAircraft extends ActorHMesh {
	private int mState;
	private float mVol;
	private byte oldPS[];
	private float otu[];
	private byte newPS;
	private boolean bMask;
	public FlightModel FM;
	protected SoundFX sndRoot;
	protected float doorSndControl;
	protected Point3d sndRelPos;
	protected MotorSound motorSnd[];
	private static final float divSpeed = 0.3F;
	private boolean bDiving;
	private boolean sndDivingState;
	private float divState;
	private float divIncr;
	private static final float GV0 = 0.4F;
	private long prevRt;
	public static final int FEED_PNEUMATIC = 0;
	public static final int FEED_ELECTRIC = 1;
	public static final int FEED_HYDRAULIC = 2;
	protected AudioStream sndGear;
	protected AudioStream sndFlaps;
	protected AudioStream sndAirBrake;
	protected AudioStream sndEject;
	protected AudioStream sndEject2;
	protected AudioStream sndEject3;
	protected AudioStream sndBang;
	protected AudioStream sndStall;
	protected AudioStream sndFire;
	protected AudioStream sndWind;
	protected AudioStream sndBeat;
	protected static SoundPreset screamPreset = null;
	protected SoundFX fxScream[];
	private static final int nScream = 3;
	protected static SoundPreset stressPreset = null;
	protected SoundFX fxStress[];
	private static final int nStress = 5;
	protected static SoundPreset maskPreset = null;
	protected SoundFX fxMask[];
	private static final int nMask = 5;
	protected SoundFX fxTurrets[];
	protected SoundFX fxShells[];
	private static final int nTur = 8;
	protected SoundFX smokeSound;
	protected SoundFX doorSound;
	private float dShake;
	private float dWheels;
	private boolean prevWG;
	protected static SoundPreset prsHit = null;
	protected static SoundPreset morsePreset = null;
	protected static SoundPreset morsePresetExtra = null;
	protected SoundFX morseSounds[];
	protected SoundFX morseSoundsExtra[];
	protected Point3d morsePos;
	private int morseSequence[];
	private int currentMorse;
	private float morseSequenceVolume;
	public static final int MORSE_A = 0;
	public static final int MORSE_B = 1;
	public static final int MORSE_C = 2;
	public static final int MORSE_D = 3;
	public static final int MORSE_E = 4;
	public static final int MORSE_F = 5;
	public static final int MORSE_G = 6;
	public static final int MORSE_H = 7;
	public static final int MORSE_I = 8;
	public static final int MORSE_J = 9;
	public static final int MORSE_K = 10;
	public static final int MORSE_L = 11;
	public static final int MORSE_M = 12;
	public static final int MORSE_N = 13;
	public static final int MORSE_O = 14;
	public static final int MORSE_P = 15;
	public static final int MORSE_Q = 16;
	public static final int MORSE_R = 17;
	public static final int MORSE_S = 18;
	public static final int MORSE_T = 19;
	public static final int MORSE_U = 20;
	public static final int MORSE_V = 21;
	public static final int MORSE_W = 22;
	public static final int MORSE_X = 23;
	public static final int MORSE_Y = 24;
	public static final int MORSE_Z = 25;
	public static final int LORENZ_DASH = 26;
	public static final int LORENZ_DOT = 27;
	public static final int LORENZ_SOLID = 28;
	public static final int LORENZ_OUTER_MARKER = 29;
	public static final int LORENZ_INNER_MARKER = 30;
	public static final int BEACON_CARRIER = 31;
	public static final int MORSE_COUNT = 32;
	public static final int MORSE_1 = 32;
	public static final int MORSE_2 = 33;
	public static final int MORSE_3 = 34;
	public static final int MORSE_4 = 35;
	public static final int MORSE_5 = 36;
	public static final int MORSE_6 = 37;
	public static final int MORSE_7 = 38;
	public static final int MORSE_8 = 39;
	public static final int MORSE_9 = 40;
	public static final int MORSE_0 = 41;
	public static final int MORSE_COMMA = 42;
	public static final int MORSE_PERIOD = 43;
	public static final int MORSE_QUESTION = 44;
	public static final int MORSE_HYPHEN = 45;
	public static final int MORSE_SPACE = 46;
	public static final int RADIO_STATIC1 = 47;
	public static final int RADIO_STATIC2 = 48;
	public static final int BEACON_STATIC = 49;
	public static final int YEYG_STATIC = 50;
	public static final int MORSE_EXTRA_COUNT = 19;
	private static float radio_static1 = 1.0F;
	private static float radio_static2 = 0.0F;
	private static int radio_static1dir = 0;
	private static int radio_static2dir = 0;
	private static final char VALID_MORSE_CHARS[] = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
		'U', 'V', 'W', 'X', 'Y', 'Z', '.', ',', '-', '?', 
		' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'0'
	};
	private float doorPrev;
	private boolean doorEnabled;
	private Point3d doorSndPos;
	private int smf[];
	private static boolean bHasRGM = HasRGM();
	private boolean bWepEffect;

	public SoundFX getRootFX() {
		return sndRoot;
	}

	public void sfxInit(int i) {
		if (sndRoot != null) {
			SectFile sectfile = new SectFile("presets/sounds/aircraft.misc.prs");
			String s = i != 0 ? "e_" : "p_";
			sndGear = (new Sample(sectfile, s + "gear")).get();
			sndRoot.add(sndGear);
			if (!haveFlaps()) s = "m_";
			sndFlaps = (new Sample(sectfile, s + "flaps")).get();
			sndRoot.add(sndFlaps);
			if (FM instanceof RealFlightModel) {
				sndAirBrake = (new Sample(sectfile, "air_brake")).get();
				sndRoot.add(sndAirBrake);
			}
			if (FM instanceof RealFlightModel) {
				if (sndEject == null)
					sndEject = (new Sample(sectfile, "EjectCanopy")).get();
				sndRoot.add(sndEject);
				if (sndEject2 == null)
					sndEject2 = (new Sample(sectfile, "EjectSeat")).get();
				sndRoot.add(sndEject2);
				if (sndEject3 == null)
					sndEject3 = (new Sample(sectfile, "EjectWind")).get();
				sndRoot.add(sndEject3);
				if (sndBang == null)
					sndBang = (new Sample(sectfile, "SonicBang")).get();
				sndRoot.add(sndBang);
				if (sndStall == null)
					sndStall = (new Sample(sectfile, "Stall")).get();
				sndRoot.add(sndStall);
				if (sndFire == null)
					sndFire = (new Sample(sectfile, "Fire")).get();
				sndRoot.add(sndFire);
				if (sndWind == null)
					sndWind = (new Sample(sectfile, "Wind")).get();
				sndRoot.add(sndWind);
				if (sndBeat == null)
					sndBeat = (new Sample(sectfile, "Beat")).get();
				sndRoot.add(sndBeat);
				if (screamPreset == null)
					screamPreset = new SoundPreset("Scream");
				fxScream = new SoundFX[3];
				if (stressPreset == null)
					stressPreset = new SoundPreset("Stress");
				fxStress = new SoundFX[5];
				if (maskPreset == null)
					maskPreset = new SoundPreset("Mask");
				fxMask = new SoundFX[5];
			}
		}
		if (prsHit == null)
			prsHit = new SoundPreset("hit.air");
	}

	public void sfxShellsPlay(int t, float v, Point3d p) {
		if (fxShells[t] == null) {
			fxShells[t] = newSound("Turrets", false);
			fxShells[t].setParent(sndRoot);
			if (fxShells[t] == null)
				return;
		}
		if (fxShells[t].isPlaying()) {
			return;
		} else {
			fxShells[t].setUsrFlag(1);
			fxShells[t].setVolume(v);
			fxShells[t].setPitch(World.Rnd().nextFloat(0.9F, 1.2F));
			fxShells[t].setPosition(p);
			fxShells[t].setParent(sndRoot);
			fxShells[t].play();
			return;
		}
	}

	public void sfxTurretsPlay(int t, float v, Point3d p) {
		if (fxTurrets[t] == null) {
			fxTurrets[t] = newSound("Turrets", false);
			if (fxTurrets[t] == null)
				return;
		}
		if (fxTurrets[t].isPlaying()) {
			return;
		} else {
			fxTurrets[t].setUsrFlag(0);
			fxTurrets[t].setVolume(v);
			fxTurrets[t].setPitch(World.Rnd().nextFloat(0.85F, 1.25F));
			fxTurrets[t].setPosition(p);
			fxTurrets[t].setParent(sndRoot);
			fxTurrets[t].play();
			return;
		}
	}

	public void sfxTurretsStop(int t) {
		if (fxTurrets[t] != null && fxTurrets[t].isPlaying())
			fxTurrets[t].stop();
	}

	public void sfxScream(int lvl, float vol, Point3d p) {
		// added by western, keep backward compatibility.
		sfxScream(lvl, vol, true, p);
	}

	public void sfxScream(int lvl, float vol, boolean flag, Point3d p) {
		if (fxScream != null && lvl < fxScream.length) {
			SoundFX sound = fxScream[lvl];
			if (sound == null) {
				sound = newSound(screamPreset, false, false);
				if (sound == null)
					return;
				sndRoot.add(sound);
				fxScream[lvl] = sound;
				sound.setUsrFlag(lvl);
			}
			sound.setPitch(World.Rnd().nextFloat(0.95F, 1.05F));
			sound.setPosition(p);
			sound.setPlay(flag);
		}
	}

	public void sfxStressPlay(int lvl, float vol, Point3d p) {
		// added by western, keep backward compatibility.
		sfxStressPlay(lvl, vol, true, p);
	}

	public void sfxStressPlay(int lvl, float vol, boolean flag, Point3d p) {
		if (fxStress != null && lvl < fxStress.length) {
			SoundFX sound = fxStress[lvl];
			if (sound == null) {
				sound = newSound(stressPreset, false, false);
				if (sound == null) return;
				sndRoot.add(sound);
				fxStress[lvl] = sound;
				sound.setUsrFlag(lvl);
			}
			sound.setPitch(World.Rnd().nextFloat(0.85F, 1.2F));
			sound.setPosition(p);
			sound.setPlay(flag);
		}
	}

	public void sfxMaskPlay(int l, float v, Point3d p) {
		// added by western, keep backward compatibility.
		sfxMaskPlay(l, v, true, p);
	}

	public void sfxMaskPlay(int lvl, float vol, boolean flag, Point3d p) {
		if (fxMask != null && lvl < fxMask.length) {
			SoundFX sound = fxMask[lvl];
			if (sound == null) {
				sound = newSound(maskPreset, false, false);
				if (sound == null) return;
				sndRoot.add(sound);
				fxMask[lvl] = sound;
				sound.setUsrFlag(lvl);
			}
			sound.setPitch(World.Rnd().nextFloat(0.95F, 1.05F));
			sound.setPlay(flag);
		}
	}

	protected boolean haveFlaps() {
		return true;
	}

	public void sfxCrash(Point3d point3d) {
		SoundFX soundfx = newSound("crash.cutoff", true);
		if (soundfx != null) {
			soundfx.setPosition(point3d);
			soundfx.setParent(sndRoot);
		}
	}

	public void sfxHit(float f, Point3d point3d) {
		if (sndRoot != null) {
			SoundFX soundfx = newSound(prsHit, false, true);
			soundfx.setParent(sndRoot);
			soundfx.setPosition(point3d);
			soundfx.setUsrFlag(f <= 0.05F ? 0 : 1);
			soundfx.play();
		}
	}

	public void sfxTow() {
		dShake = 1.0F;
		SoundFX soundfx = newSound("aircraft.tow", true);
		if (soundfx != null) {
			soundfx.setPosition(new Point3d(0.0D, 0.0D, -1D));
			soundfx.setParent(sndRoot);
		}
	}

	public void sfxWheels() {
		SoundFX soundfx = newSound("aircraft.wheels", true);
		if (soundfx != null) {
			soundfx.setPosition(new Point3d(0.0D, 0.0D, -1.5D));
			soundfx.setParent(sndRoot);
		}
	}

	public void setDoorSnd(float f) {
		if (FM != null) {
			doorSndControl = f;
			if (doorSound == null && FM.CT.dvCockpitDoor > 0.0F) {
				byte byte0 = 1;
				float f1 = 1.0F / FM.CT.dvCockpitDoor;
				doorSndPos = new Point3d(-1D, 0.0D, 0.0D);
				doorSound = newSound("aircraft.door", false);
				if (doorSound != null) {
					doorSound.setParent(getRootFX());
					if (f1 <= 1.1F) byte0 = 0;
					else if (f1 >= 1.8F) byte0 = 2;
					doorSound.setUsrFlag(byte0);
				}
			}
			if ((f != 0.0F && doorPrev == 0.0F || f != 1.0F && doorPrev == 1.0F) && doorSound != null) doorSound.play(doorSndPos);
			doorPrev = f;
		}
	}

	public void enableDoorSnd(boolean flag) {
		doorEnabled = flag;
	}

	public void sfxSmokeState(int i, int j, boolean flag) {
		if (flag) {
			smf[i] |= 1 << j;
			if (smokeSound == null) smokeSound = newSound("objects.flame", true);
		} else {
			boolean flag1 = true;
			smf[i] &= ~(1 << j);
			for (int k = 0; k < 3; k++)
				flag1 = flag1 && smf[k] == 0;

			if (i == 0) flag1 = true;
			if (flag1 && smokeSound != null) {
				smokeSound.cancel();
				smokeSound = null;
			}
		}
	}

	public void sfxGear(boolean flag) {
		if (sndGear != null) sndGear.setPlay(flag);
	}

	public void sfxFlaps(boolean flag) {
		if (sndFlaps != null) sndFlaps.setPlay(flag);
	}

	public void sfxAirBrake() {
		if (sndAirBrake != null) sndAirBrake.play();
	}

	public void sfxEject() {
		if (sndEject != null) sndEject.play();
	}

	public void sfxEject2() {
		if (sndEject2 != null) sndEject2.play();
	}

	public void sfxEject3() {
		if (sndEject3 != null) sndEject3.play();
	}

	public void sfxBang() {
		if (sndBang != null) sndBang.play();
	}

	public void sfxStall() {
		if (sndStall != null) sndStall.play();
	}

	public void sfxFire() {
		if (sndFire != null) sndFire.play();
	}

	public void sfxWind() {
		if (sndWind != null) sndWind.play();
	}

	public void sfxBeat() {
		if (sndBeat != null) sndBeat.play();
	}

	public void updateLLights() {
	}

	public void update(float f) {
		if (dShake > 0.0F) {
			dShake -= f / 1.7F;
			if (dShake <= 0.05F) dShake = 0.0F;
		}
		if (dWheels > 0.0F) {
			dWheels -= f;
			if (dWheels < 0.0F) dWheels = 0.0F;
		}
		if (bWepEffect) {
			Actor cam = Main3D.cur3D().camera3D;
			if (((Interpolate) (FM)).actor.distance(cam) < 500D) WepEffect();
		}
	}

	public SndAircraft() {
		bWepEffect = true;
		if (Config.cur.ini.get("Mods", "WepEffect", 1) == 0) bWepEffect = false;
		newPS = 0;
		bMask = false;
		FM = null;
		sndRoot = null;
		doorSndControl = 0.0F;
		sndRelPos = new Point3d(3D, 0.0D, 0.0D);
		motorSnd = null;
		bDiving = false;
		sndDivingState = false;
		divState = 0.0F;
		divIncr = 0.0F;
		prevRt = 0L;
		sndGear = null;
		sndFlaps = null;
		sndAirBrake = null;
		sndEject = null;
		sndEject2 = null;
		sndEject3 = null;
		sndBang = null;
		sndStall = null;
		sndFire = null;
		sndWind = null;
		sndBeat = null;
		oldPS = new byte[9];
		otu = new float[9];
		smokeSound = null;
		doorSound = null;
		dShake = 0.0F;
		dWheels = 0.0F;
		prevWG = true;
		morseSounds = null;
		morseSoundsExtra = null;
		morsePos = new Point3d(0.0D, 0.0D, 0.0D);
		morseSequence = null;
		currentMorse = 0;
		morseSequenceVolume = 0.0F;
		doorPrev = 0.0F;
		doorEnabled = false;
		doorSndPos = null;
		smf = new int[3];
		for (int i = 0; i < 3; i++)
			smf[i] = 0;

		draw = new ActorMeshDraw() {

			public int preRender(Actor actor) {
				updateLLights();
				return super.preRender(actor);
			}

			public void soundUpdate(Actor actor, Loc loc) {
				if (!Config.cur.isSoundUse() || sndRoot == null) return;
				if (FM != null) {
					if (bDiving) {
						if (FM.CT.AirBrakeControl > 0.4F) {
							if (divState == 0.0F || divIncr < 0.0F) divIncr = 0.3F;
						} else if (divState > 0.0F) divIncr = -0.39F;
						else divIncr = 0.0F;
						if (divState > 1.0F) {
							divIncr = 0.0F;
							divState = 1.0F;
						} else if (divState < 0.0F) {
							divIncr = 0.0F;
							divState = 0.0F;
						}
						sndRoot.setControl(102, divState);
						long l = Time.real();
						if (divIncr != 0.0F) divState += ((float) (l - prevRt) * divIncr) / 1000F;
						prevRt = l;
					} else {
						sndRoot.setControl(102, 0.0F);
					}
					if (FM instanceof RealFlightModel) {
						float f = ((RealFlightModel) FM).shakeLevel + dShake;
						if (f > 1.0F) f = 1.0F;
						sndRoot.setControl(103, f);
					}
//TODO: PAS++
// simple version - Left/RightBrakeControl combined
					float fb = Math.max(FM.CT.BrakeControl, Math.max(FM.CT.BrakeLeftControl, FM.CT.BrakeRightControl));
					sndRoot.setControl(104, (FM.Gears.gVelocity[0] <= 0.40000000596046448D || !FM.Gears.lgear) && (FM.Gears.gVelocity[1] <= 0.40000000596046448D || !FM.Gears.rgear) && FM.Gears.gVelocity[2] <= 0.40000000596046448D ? 0.0F : fb);
// advanced version - positional brake sound
// TBD
//PAS--
					sndRoot.setControl(105, FM.Gears.getLandingState());
					sndRoot.setControl(111, (float) FM.Vrel.length());
					boolean flag = FM.Gears.getWheelsOnGround();
					if (!prevWG && flag && dWheels == 0.0F) sfxWheels();
					if (flag) dWheels = 4F;
					prevWG = flag;
				}
				if (doorEnabled) sndRoot.setControl(110, doorSndControl);
				else sndRoot.setControl(110, 0.0F);
				if (motorSnd != null) {
					for (int j = 0; j < motorSnd.length; j++)
						motorSnd[j].update();

				}
				if ((com.maddox.il2.objects.air.TypeFastJet.class).isAssignableFrom(((Interpolate) (FM)).actor.getClass()))
					bMask = hierMesh().isChunkVisible("HMask1_D0") || FM.getSpeedKMH() > 300F;
				else if (bMask)
					bMask = FM.getAltitude() > 2850F;
				else
					bMask = FM.getAltitude() > 3150F;
				float vol = 100F + FM.EI.engines[0].getRPM() / 240F;
				if (FM instanceof RealFlightModel)
				{
					int k = Main3D.cur3D().cockpits.length;
					for (int i = 0; i < k; i++)
					{
						try
						{
							Class class1 = Main3D.cur3D().cockpits[i].getClass();
							int ft = Property.intValue(class1, "astatePilotIndx", -1);
							int c = Property.intValue(class1, "aiTuretNum", -1);
							if (c > -1)
							{
								float v = World.Rnd().nextFloat(15F, 50F);
								if (FM.turret[c].bIsAIControlled)
								{
									if (FM.turret[c].tuLim[0] > 160F)
										switch(FM.turret[c].tMode)
										{
										case 1: // '\001'
											sfxTurretsPlay(c, v * vol, ((Actor) (Main3D.cur3D().cockpits[i])).pos.getAbsPoint());
											break;

										case 5: // '\005'
											sfxTurretsStop(c);
											break;
										}
									if (FM.turret[c].tMode == 2)
										sfxShellsPlay(c, v * vol, ((Actor) (Main3D.cur3D().cockpits[i])).pos.getAbsPoint());
								}
							}
						}
						catch(Exception exc)
						{
							System.out.println(exc);
						}
						com.maddox.il2.objects.air.Cockpit cockpit = Main3D.cur3D().cockpits[i];
						if (Actor.isValid(cockpit))
						{
							newPS = FM.AS.astatePilotStates[i];
							if (newPS >= oldPS[i] + 20)
								if (newPS < 95)
								{
									if (i == FM.AS.astatePlayerIndex)
										sfxScream(0, vol, true, ((Actor) (Main3D.cur3D().cockpits[i])).pos.getAbsPoint());
									else
										sfxScream(1, vol, true, ((Actor) (Main3D.cur3D().cockpits[i])).pos.getAbsPoint());
								} else
								{
									sfxScream(2, vol, true, ((Actor) (Main3D.cur3D().cockpits[i])).pos.getAbsPoint());
								}
							oldPS[i] = newPS;
						}
					}

					boolean isRGM = false;
					if (SndAircraft.bHasRGM)
						isRGM = ((RealFlightModel)FM).isRealGunnerMode();
					if (((RealFlightModel)FM).isRealMode() || isRGM)
					{
						byte f1 = FM.AS.astatePilotStates[FM.AS.astatePlayerIndex];
						if (f1 > 25)
						{
							sndBeat.setVolume(vol);
							sndBeat.play();
						}
						if (f1 > 50)
						{
							if (bMask)
								sfxMaskPlay(1, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
							else
								sfxMaskPlay(3, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
						} else
						if (!World.isPlayerDead())
						{
							if ((FM.AS.astateCockpitState & 0x3f) > 0 && FM.getSpeed() > 150F)
								sfxWind();
							float ld = FM.getOverload();
							if (ld > 3.7F)
							{
								if (bMask)
									sfxMaskPlay(2, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
								else
									sfxMaskPlay(4, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
							} else
							if (ld < -1.8F)
							{
								if (bMask)
									sfxMaskPlay(1, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
								else
									sfxMaskPlay(3, vol, true, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
							} else
							if (bMask)
								sfxMaskPlay(0, vol, ((Actor) (Main3D.cur3D().cockpitCur)).pos.getAbsPoint());
							if (FM.Gears.getLandingState() == 0.0F)
							{
								float ol = ld / World.getPlayerFM().getLimitLoad();
								float v = World.Rnd().nextFloat(15F, 50F);
								if (ol > 0.55F && ol < 0.57F)
									sfxStressPlay(0, v * vol, true, new Point3d(World.Rnd().nextDouble(-4D, 4D), -4D, 0.0D));
								else
								if (ol > 0.62F && ol < 0.65F)
									sfxStressPlay(1, v * vol, true, new Point3d(World.Rnd().nextDouble(-3D, 3D), -4D, 0.0D));
								else
								if (ol > 0.7F && ol < 0.775F)
									sfxStressPlay(2, v * vol, true, new Point3d(World.Rnd().nextDouble(-2D, 2D), -4D, 0.0D));
								else
								if (ol > 0.8F && ol < 0.875F)
									sfxStressPlay(3, v * vol, true, new Point3d(World.Rnd().nextDouble(-2D, 2D), -3D, -1D));
								else
								if (ol > 0.9F)
									sfxStressPlay(4, v * vol, true, new Point3d(0.0D, World.Rnd().nextDouble(-2D, -4D), -1D));
							}
						}
						if (FM.AS.astateBailoutStep == 3)
							sndEject.play();
						if (FM.AS.astateBailoutStep == 3 && FM.getSpeedKMH() > 200F)
							sndEject3.play();
					}
				}
				updateMorseSequence();
				super.soundUpdate(actor, loc);
			}

		};
	}

	public boolean hasInternalSounds() {
		return false;
	}

	public void setMotorPos(Point3d point3d) {
		Point3d point3d1 = null;
		if (point3d != null) {
			point3d1 = new Point3d(pos.getAbsPoint());
			point3d1.sub(point3d);
		}
		for (int i = 0; i < motorSnd.length; i++)
			motorSnd[i].setPos(point3d1);

	}

	protected void initSound(SectFile sectfile) {
		if (!BaseObject.isEnabled()) return;
		if (this instanceof TypeSeaPlane) sndRoot = newSound("aircraft.common_w", true);
		else sndRoot = newSound("aircraft.common", true);
		int i = FM.EI.getNum();
		motorSnd = new MotorSound[i];
		for (int j = 0; j < i; j++)
			motorSnd[j] = new MotorSound(FM.EI.engines[j], this);

		bDiving = sectfile.get("SOUND", "Diving", "").length() > 0;
		sfxInit(0);
		initMorse();
	}

	public void WepEffect() {
		for (int e = 0; e < FM.EI.getNum(); e++) {
			int ty = FM.EI.engines[e].getType();
			int i = World.Rnd().nextInt(0, 24);
			if (ty == 0 || ty == 1) {
				try {
					com.maddox.il2.engine.Hook hook = ((Interpolate) (FM)).actor.findHook("_Engine" + Integer.toString(e + 1) + "EF_" + (i < 10 ? "0" : "") + Integer.toString(i));
					if (hook != null && ((FlightModelMain) (FM)).EI.engines[e].getStage() >= 2 && ((FlightModelMain) (FM)).EI.engines[e].getStage() < 4 && FM.getSpeedKMH() < 50F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() > 0.0F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() <= 0.2F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook, null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
					if (hook != null && ((FlightModelMain) (FM)).EI.engines[e].getStage() >= 2 && ((FlightModelMain) (FM)).EI.engines[e].getStage() < 4 && FM.getSpeedKMH() < 50F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() > 0.2F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() <= 0.3F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook, null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1b.eff", -1F);
					if (hook != null && ((FlightModelMain) (FM)).EI.engines[e].getStage() >= 2 && ((FlightModelMain) (FM)).EI.engines[e].getStage() < 4 && FM.getSpeedKMH() < 50F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() > 0.3F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook, null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1c.eff", -1F);
					if (hook != null && ((FlightModelMain) (FM)).EI.engines[e].getRPM() > 1000F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() > 0.86F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() <= 0.99F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook, null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
					if (hook != null && ((FlightModelMain) (FM)).EI.engines[e].getRPM() > 1000F && ((FlightModelMain) (FM)).EI.engines[e].getControlThrottle() > 0.99F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook, null, 1.0F, "3DO/Effects/Aircraft/EngineWep2.eff", -1F);
				}
				catch (Exception exception) { }
			}
			if (ty == 2) {
				try {
					com.maddox.il2.engine.Hook hook6 = ((Interpolate) (FM)).actor.findHook("_Engine" + Integer.toString(e + 1) + "EFS_" + (i < 10 ? "0" : "") + Integer.toString(i));
					if (hook6 != null && ((FlightModelMain) (FM)).EI.engines[e].getStage() >= 2 && ((FlightModelMain) (FM)).EI.engines[e].getStage() < 4 && FM.getSpeedKMH() < 50F)
						Eff3DActor.New(((Interpolate) (FM)).actor, hook6, null, 1.0F, "3DO/Effects/Aircraft/HolyGrailMe262.eff", -1F);
				}
				catch (Exception exception6) { }
			}
		}
	}

	public void destroy() {
		if (isDestroyed()) return;
		super.destroy();
		if (sndRoot != null) sndRoot.cancel();
		if (sndGear != null) sndGear.cancel();
		if (sndFlaps != null) sndFlaps.cancel();
		if (sndAirBrake != null) sndAirBrake.cancel();
		if (smokeSound != null) smokeSound.cancel();
		if (fxScream != null) {
			for (int i = 0; i < fxScream.length; i++)
				if (fxScream[i] != null) fxScream[i].cancel();
		}
		if (fxStress != null) {
			for (int i = 0; i < fxStress.length; i++)
				if (fxStress[i] != null) fxStress[i].cancel();
		}
		if (fxMask != null) {
			for (int i = 0; i < fxMask.length; i++)
				if (fxMask[i] != null) fxMask[i].cancel();
		}
		if (fxTurrets != null) {
			for (int i = 0; i < fxTurrets.length; i++)
				if (fxTurrets[i] != null) fxTurrets[i].cancel();
		}
		if (fxShells != null) {
			for (int i = 0; i < fxShells.length; i++)
				if (fxShells[i] != null) fxShells[i].cancel();
		}
		if (sndEject != null) sndEject.cancel();
		if (sndEject2 != null) sndEject2.cancel();
		if (sndEject3 != null) sndEject3.cancel();
		if (sndBang != null) sndBang.cancel();
		if (sndStall != null) sndStall.cancel();
		if (sndFire != null) sndFire.cancel();
		if (sndWind != null) sndWind.cancel();
		if (sndBeat != null) sndBeat.cancel();
	}

	protected void initMorse() {
		if (morsePreset == null) morsePreset = new SoundPreset("aircraft.morse");
		morseSounds = new SoundFX[32];
		if (morsePresetExtra == null) morsePresetExtra = new SoundPreset("aircraft.morse.extra");
		morseSoundsExtra = new SoundFX[19];
	}

	private void updateMorseSequence() {
		if (morseSequence == null || morseSequence.length <= 0) return;
		if (!isMorsePlaying(morseSequence[currentMorse])) if (currentMorse < morseSequence.length - 1) {
			currentMorse++;
			playMorseEffect(morseSequence[currentMorse], true, morseSequenceVolume);
		} else {
			morseSequence = null;
			if (World.cur().showMorseAsText) HUD.training("");
		}
	}

	public boolean isMorseSequencePlaying() {
		return morseSequence != null;
	}

	private int[] getCharArray(String s) {
		int ai[] = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case 65: // 'A'
				ai[i] = 0;
				break;

			case 66: // 'B'
				ai[i] = 1;
				break;

			case 67: // 'C'
				ai[i] = 2;
				break;

			case 68: // 'D'
				ai[i] = 3;
				break;

			case 69: // 'E'
				ai[i] = 4;
				break;

			case 70: // 'F'
				ai[i] = 5;
				break;

			case 71: // 'G'
				ai[i] = 6;
				break;

			case 72: // 'H'
				ai[i] = 7;
				break;

			case 73: // 'I'
				ai[i] = 8;
				break;

			case 74: // 'J'
				ai[i] = 9;
				break;

			case 75: // 'K'
				ai[i] = 10;
				break;

			case 76: // 'L'
				ai[i] = 11;
				break;

			case 77: // 'M'
				ai[i] = 12;
				break;

			case 78: // 'N'
				ai[i] = 13;
				break;

			case 79: // 'O'
				ai[i] = 14;
				break;

			case 80: // 'P'
				ai[i] = 15;
				break;

			case 81: // 'Q'
				ai[i] = 16;
				break;

			case 82: // 'R'
				ai[i] = 17;
				break;

			case 83: // 'S'
				ai[i] = 18;
				break;

			case 84: // 'T'
				ai[i] = 19;
				break;

			case 85: // 'U'
				ai[i] = 20;
				break;

			case 86: // 'V'
				ai[i] = 21;
				break;

			case 87: // 'W'
				ai[i] = 22;
				break;

			case 88: // 'X'
				ai[i] = 23;
				break;

			case 89: // 'Y'
				ai[i] = 24;
				break;

			case 90: // 'Z'
				ai[i] = 25;
				break;

			case 45: // '-'
				ai[i] = 45;
				break;

			case 46: // '.'
				ai[i] = 43;
				break;

			case 44: // ','
				ai[i] = 42;
				break;

			case 63: // '?'
				ai[i] = 44;
				break;

			case 32: // ' '
				ai[i] = 46;
				break;

			case 49: // '1'
				ai[i] = 32;
				break;

			case 50: // '2'
				ai[i] = 33;
				break;

			case 51: // '3'
				ai[i] = 34;
				break;

			case 52: // '4'
				ai[i] = 35;
				break;

			case 53: // '5'
				ai[i] = 36;
				break;

			case 54: // '6'
				ai[i] = 37;
				break;

			case 55: // '7'
				ai[i] = 38;
				break;

			case 56: // '8'
				ai[i] = 39;
				break;

			case 57: // '9'
				ai[i] = 40;
				break;

			case 48: // '0'
				ai[i] = 41;
				break;
			}
		}

		return ai;
	}

	private static String getMorseString(int i) {
		switch (i) {
		case 0: // '\0'
			return "Morse: A  \267 \u2014";

		case 1: // '\001'
			return "Morse: B  \u2014 \267 \267 \267";

		case 2: // '\002'
			return "Morse: C  \u2014 \267 \u2014 \267";

		case 3: // '\003'
			return "Morse: D  \u2014 \267 \267";

		case 4: // '\004'
			return "Morse: E  \267";

		case 5: // '\005'
			return "Morse: F  \267 \267 \u2014 \267";

		case 6: // '\006'
			return "Morse: G  \u2014 \u2014 \267";

		case 7: // '\007'
			return "Morse: H  \267 \267 \267 \267";

		case 8: // '\b'
			return "Morse: I  \267 \267";

		case 9: // '\t'
			return "Morse: J  \267 \u2014 \u2014 \u2014";

		case 10: // '\n'
			return "Morse: K  \u2014 \267 \u2014";

		case 11: // '\013'
			return "Morse: L  \267 \u2014 \267 \267";

		case 12: // '\f'
			return "Morse: M  \u2014 \u2014";

		case 13: // '\r'
			return "Morse: N  \u2014 \267";

		case 14: // '\016'
			return "Morse: O  \u2014 \u2014 \u2014";

		case 15: // '\017'
			return "Morse: P  \267 \u2014 \u2014 \267";

		case 16: // '\020'
			return "Morse: Q  \u2014 \u2014 \267 \u2014";

		case 17: // '\021'
			return "Morse: R  \267 \u2014 \267";

		case 18: // '\022'
			return "Morse: S  \267 \267 \267";

		case 19: // '\023'
			return "Morse: T  \u2014";

		case 20: // '\024'
			return "Morse: U  \267 \267 \u2014";

		case 21: // '\025'
			return "Morse: V  \267 \267 \267 \u2014";

		case 22: // '\026'
			return "Morse: W  \267 \u2014 \u2014";

		case 23: // '\027'
			return "Morse: X  \u2014 \267 \267 \u2014";

		case 24: // '\030'
			return "Morse: Y  \u2014 \267 \u2014 \u2014";

		case 25: // '\031'
			return "Morse: Z  \u2014 \u2014 \267 \267";

		case 45: // '-'
			return "Morse: <hyphen>   \u2014 \267 \267 \267 \267 \u2014";

		case 43: // '+'
			return "Morse: .  \267 \u2014 \267 \u2014 \267 \u2014";

		case 42: // '*'
			return "Morse: ,  \u2014 \u2014 \267 \267 \u2014 \u2014";

		case 44: // ','
			return "Morse: ?  \267 \267 \u2014 \u2014 \267 \267";

		case 46: // '.'
			return "";

		case 32: // ' '
			return "Morse: 1  \267 \u2014 \u2014 \u2014 \u2014";

		case 33: // '!'
			return "Morse: 2  \267 \267 \u2014 \u2014 \u2014";

		case 34: // '"'
			return "Morse: 3  \267 \267 \267 \u2014 \u2014";

		case 35: // '#'
			return "Morse: 4  \267 \267 \267 \267 \u2014";

		case 36: // '$'
			return "Morse: 5  \267 \267 \267 \267 \267";

		case 37: // '%'
			return "Morse: 6  \u2014 \267 \267 \267 \267";

		case 38: // '&'
			return "Morse: 7  \u2014 \u2014 \267 \267 \267";

		case 39: // '\''
			return "Morse: 8  \u2014 \u2014 \u2014 \267 \267";

		case 40: // '('
			return "Morse: 9  \u2014 \u2014 \u2014 \u2014 \267";

		case 41: // ')'
			return "Morse: 0  \u2014 \u2014 \u2014 \u2014 \u2014";

		case 26: // '\032'
		case 27: // '\033'
		case 28: // '\034'
		case 29: // '\035'
		case 30: // '\036'
		case 31: // '\037'
		default:
			return "";
		}
	}

	public void playChatMsgAsMorse(String s) {
		if (isMorseSequencePlaying() || s == null) return;
		String s1 = s.substring(6).trim().toUpperCase();
		if (s1.length() == 0) {
			return;
		} else {
			s1 = cleanMorseString(s1);
			morseSequenceStart(getCharArray(s1), 0.5F);
			return;
		}
	}

	private String cleanMorseString(String s) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int j = 0;
			do {
				if (j >= VALID_MORSE_CHARS.length) break;
				if (VALID_MORSE_CHARS[j] == c) {
					stringbuffer.append(c);
					break;
				}
				j++;
			} while (true);
		}

		return stringbuffer.toString();
	}

	public void morseSequenceStart(String s, float f) {
		if (isMorseSequencePlaying()) morseSequenceStop();
		morseSequenceStart(getCharArray(cleanMorseString(s.toUpperCase())), f);
	}

	private void morseSequenceStart(int ai[], float f) {
		currentMorse = 0;
		if (ai.length == 0)
		{
			return;
		} else
		{
			morseSequence = ai;
			morseSequenceVolume = f;
			playMorseEffect(morseSequence[currentMorse], true, morseSequenceVolume);
			return;
		}
	}

	protected void morseSequenceStop() {
		morseStop(currentMorse);
		morseSequence = null;
	}

	private void showMorseAsText(int i) {
		if (!World.cur().showMorseAsText) return;
		if (Main.cur().netServerParams != null && !Main.cur().netServerParams.isSingle() && !Main.cur().netServerParams.allowMorseAsText) return;
		if (i >= 0 && i <= 25 || i >= 32 && i <= 46) HUD.training(getMorseString(i));
	}

	protected void playMorseEffect(int i, boolean flag, float f) {
		if (flag && f >= 0.1F) showMorseAsText(i);
		if (i < 32) {
			if (morseSounds != null && morseSounds.length > i) {
				SoundFX soundfx = morseSounds[i];
				if (soundfx == null) {
					soundfx = newSound(morsePreset, false, false);
					if (soundfx == null) return;
					soundfx.setParent(sndRoot);
					morseSounds[i] = soundfx;
					soundfx.setUsrFlag(i);
				}
				soundfx.setVolume(f);
				soundfx.setPlay(flag);
			}
		} else {
			i -= 32;
			if (morseSoundsExtra != null && morseSoundsExtra.length > i) {
				SoundFX soundfx1 = morseSoundsExtra[i];
				if (soundfx1 == null) {
					soundfx1 = newSound(morsePresetExtra, false, false);
					if (soundfx1 == null) return;
					soundfx1.setParent(sndRoot);
					morseSoundsExtra[i] = soundfx1;
					soundfx1.setUsrFlag(i);
				}
				soundfx1.setVolume(f);
				soundfx1.setPlay(flag);
			}
		}
	}

	protected boolean isMorsePlaying(int i) {
		if (i < 32) {
			if (morseSounds != null && morseSounds.length > i && morseSounds[i] != null) return morseSounds[i].isPlaying();
		} else {
			i -= 32;
			if (morseSoundsExtra != null && morseSoundsExtra.length > i && morseSoundsExtra[i] != null) return morseSoundsExtra[i].isPlaying();
		}
		return false;
	}

	protected void morseStop(int i) {
		if (i < 32) {
			if (morseSounds != null && morseSounds.length > i && morseSounds[i] != null) morseSounds[i].stop();
		} else {
			i -= 32;
			if (morseSoundsExtra != null && morseSoundsExtra.length > i && morseSoundsExtra[i] != null) morseSoundsExtra[i].stop();
		}
	}

	public void playLorenzDash(boolean flag, float f) {
		playMorseEffect(26, flag, f);
	}

	public void playLorenzDot(boolean flag, float f) {
		playMorseEffect(27, flag, f);
	}

	public void playLorenzSolid(boolean flag, float f) {
		playMorseEffect(28, flag, f);
	}

	public void playLorenzInnerMarker(boolean flag, float f) {
		playMorseEffect(30, flag, f);
	}

	public void playLorenzOuterMarker(boolean flag, float f) {
		playMorseEffect(29, flag, f);
	}

	public void playYEYGCarrier(boolean flag, float f) {
		playMorseEffect(50, flag, f);
	}

	public void playBeaconCarrier(boolean flag, float f) {
		playMorseEffect(31, flag, f * 0.85F);
		playMorseEffect(49, flag, (-0.7F + (1.0F - f)) * 3.333F);
	}

	public void playRadioStatic(boolean flag, float f) {
		if (flag) {
			double d = Math.random();
			double d1 = Math.random();
			if (d < 0.20000000000000001D) radio_static1dir = -1;
			else if (d > 0.80000000000000004D) radio_static1dir = 1;
			else radio_static1dir = 0;
			if (d1 < 0.20000000000000001D) radio_static2dir = -1;
			else if (d1 > 0.80000000000000004D) radio_static2dir = 1;
			else radio_static2dir = 0;
			radio_static1 += (float) radio_static1dir * 0.1F;
			radio_static2 += (float) radio_static2dir * 0.1F;
			if (radio_static1 < 0.0F) radio_static1 = 0.0F;
			if (radio_static2 < 0.0F) radio_static2 = 0.0F;
			if (radio_static1 > radio_static2) radio_static1 = f;
			else radio_static2 = f;
		}
		playMorseEffect(47, flag, radio_static1);
		playMorseEffect(48, flag, radio_static2);
	}

	public void stopMorseSounds() {
		playLorenzDot(false, 0.0F);
		playLorenzDash(false, 0.0F);
		playLorenzSolid(false, 0.0F);
		playLorenzInnerMarker(false, 0.0F);
		playLorenzOuterMarker(false, 0.0F);
		playBeaconCarrier(false, 0.0F);
		playRadioStatic(false, 0.0F);
		playYEYGCarrier(false, 0.0F);
		HUD.training("");
	}

	public static boolean HasRGM() {
		java.lang.reflect.Method method = null;
		try
		{
			method = (com.maddox.il2.fm.RealFlightModel.class).getMethod("setRealGunnerMode", new Class[] {
				Boolean.TYPE
			});
		}
		catch(Exception e) { }
		return method != null;
	}
}