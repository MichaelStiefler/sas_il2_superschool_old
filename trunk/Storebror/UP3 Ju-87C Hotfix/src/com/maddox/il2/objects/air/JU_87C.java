// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 02.08.2011 14:46:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   JU_87C.java
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            JU_87, PaintSchemeBMPar02, Aircraft, Cockpit, 
//            NetAircraft
public class JU_87C extends JU_87 {

	private boolean bDynamoOperational;
	private float dynamoOrient;
	private float arrestor2;
	private boolean bDynamoRotary;
	private int pk;
	private boolean bGearJettisoned;
	private boolean bGearInitialized;
	private boolean bOldStatusAI;

	public JU_87C() {
		bDynamoOperational = true;
		dynamoOrient = 0.0F;
		arrestor2 = 0.0F;
		bDynamoRotary = false;
		bGearJettisoned = false;
		bGearInitialized = false;
		bOldStatusAI = false;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		if (i == 36 || i == 37 || i == 10) {
			hierMesh().chunkVisible("GearR3_D0", false);
			hierMesh().chunkVisible("GearR3Rot_D0", false);
			bDynamoOperational = false;
		}
		return super.cutFM(i, j, actor);
	}

	private boolean isAI() {
		return (this != World.getPlayerAircraft() || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Pilot);
	}

	private void updateGearStatus() {
		float f = FM.getAltitude() - Landscape.HQ_Air((float)FM.Loc.x, (float)FM.Loc.y);
		if ((f < 30F) && !bGearJettisoned) {
			FM.Gears.rgear = true;
			FM.Gears.lgear = true;
		} else {
			FM.Gears.rgear = false;
			FM.Gears.lgear = false;
		}
		if (!bGearInitialized) {
			((FlightModelMain)(super.FM)).CT.GearControl = 1.0F;
			((FlightModelMain)(super.FM)).CT.setGear(1.0F);
			bGearInitialized = true;
		}
		if (isAI()) {
			if (bGearJettisoned) {
				((FlightModelMain)(super.FM)).CT.GearControl = 0.0F;
				((FlightModelMain)(super.FM)).CT.setGear(0.0F);
			} else {
				((FlightModelMain)(super.FM)).CT.GearControl = 1.0F;
				((FlightModelMain)(super.FM)).CT.setGear(1.0F);
			}
			((FlightModelMain)(super.FM)).CT.bHasGearControl = false;
			bOldStatusAI = true;
		} else {
			if (!bGearJettisoned) {
				((FlightModelMain)(super.FM)).CT.bHasGearControl = true;
				if (bOldStatusAI) {
					((FlightModelMain)(super.FM)).CT.GearControl = 1.0F;
					((FlightModelMain)(super.FM)).CT.setGear(1.0F);
				}
			}
			bOldStatusAI = false;
		}
	}

	protected void moveGear(float f) {
		if (isAI()) {
			return;
		}
		if (bGearJettisoned) {
			((FlightModelMain)(super.FM)).CT.GearControl = 0.0F;
			((FlightModelMain)(super.FM)).CT.setGear(0.0F);
			return;
		}
		if (!bGearJettisoned && f < 0.95F) {
			bGearJettisoned = true;
			cutFM(9, 0, this);
			cutFM(10, 0, this);
			((FlightModelMain)(super.FM)).CT.GearControl = 0.0F;
			((FlightModelMain)(super.FM)).CT.setGear(0.0F);
			((FlightModelMain)(super.FM)).Gears.setOperable(false);
			((FlightModelMain)(super.FM)).CT.bHasGearControl = false;
			super.FM.setGCenter(-0.5F);
			super.FM.GearCX = 0.0F;
			if (this == World.getPlayerAircraft()) {
				HUD.log("Gear Jettisoned");
			}
			return;
		} else {
			return;
		}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLFold", 100F * f, 22F * f, -90F * f);
		hiermesh.chunkSetAngles("WingRFold", -100F * f, -22F * f, -90F * f);
	}

	public void moveWingFold(float f) {
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			((FlightModelMain)(super.FM)).CT.WeaponControl[0] = false;
			hideWingWeapons(true);
		}
		moveWingFold(hierMesh(), f);
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.6F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) {
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			}
			setDoorSnd(f);
		}
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -45F * f, 0.0F);
	}

	public void msgCollision(Actor actor, String s, String s1) {
		// System.out.println("Ju-87C msgCollision(" + actor.getClass().getName() + ", " + s + ", " + s1 + ")");
		if (actor == this) {
			if (s.equals(s1)) {
				if (s.startsWith("Gear")) {
					return;
				}
			}
		}
		if ((!isNet() || !isNetMirror()) && !s.startsWith("Hook")) {
			super.msgCollision(actor, s, s1);
		}
	}

	protected void moveFan(float f) {
		if (bDynamoOperational) {
			pk = Math.abs((int)(((FlightModelMain)(super.FM)).Vwld.length() / 14D));
			if (pk >= 1) {
				pk = 1;
			}
		}
		if (bDynamoRotary != (pk == 1)) {
			bDynamoRotary = pk == 1;
			hierMesh().chunkVisible("GearR3_D0", !bDynamoRotary);
			hierMesh().chunkVisible("GearR3Rot_D0", bDynamoRotary);
		}
		dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F
				: (float)((double)dynamoOrient - ((FlightModelMain)(super.FM)).Vwld.length() * 1.5444015264511108D) % 360F;
		hierMesh().chunkSetAngles("GearR3_D0", 0.0F, dynamoOrient, 0.0F);
		super.moveFan(f);
	}

	protected void moveAirBrake(float f) {
		hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
		hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
	}

	public void update(float f) {
		updateGearStatus();
		for (int i = 1; i < 9; i++) {
			hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F,
					-15F * ((FlightModelMain)(super.FM)).EI.engines[0].getControlRadiator(), 0.0F);
		}

		// this.FM.CT.bHasGearControl = false;
		super.update(f);
		// this.FM.CT.bHasGearControl = !bGearJettisoned;
		if (((FlightModelMain)(super.FM)).CT.getArrestor() > 0.9F) {
			if (((FlightModelMain)(super.FM)).Gears.arrestorVAngle != 0.0F) {
				arrestor2 = Aircraft.cvt(((FlightModelMain)(super.FM)).Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
				hierMesh().chunkSetAngles("Hook_D0", 0.0F, arrestor2, 0.0F);
				((FlightModelMain)(super.FM)).Gears.getClass();
			} else {
				float f1 = -41F * ((FlightModelMain)(super.FM)).Gears.arrestorVSink;
				if (f1 < 0.0F && super.FM.getSpeedKMH() > 60F) {
					Eff3DActor.New(this, ((FlightModelMain)(super.FM)).Gears.arrestorHook, null, 1.0F,
							"3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
				}
				if (f1 > 0.0F && ((FlightModelMain)(super.FM)).CT.getArrestor() < 0.9F) {
					f1 = 0.0F;
				}
				if (f1 > 6.2F) {
					f1 = 6.2F;
				}
				arrestor2 += f1;
				if (arrestor2 < -23F) {
					arrestor2 = -23F;
				} else if (arrestor2 > 45F) {
					arrestor2 = 45F;
				}
				hierMesh().chunkSetAngles("Hook_D0", 0.0F, arrestor2, 0.0F);
			}
		}
		if ((this == World.getPlayerAircraft()) && ((this.FM instanceof RealFlightModel))) {
			if (((RealFlightModel)this.FM).isRealMode()) {
				switch (this.diveMechStage) {
				case 1:
					this.FM.CT.setTrimElevatorControl(-0.40F);
					this.FM.CT.trimElevator = -0.40F;
					break;
				case 2:
					if (this.FM.isTick(41, 0)) {
						this.FM.CT.setTrimElevatorControl(0.40F);
						this.FM.CT.trimElevator = 0.40F;
					}
					break;
				}
			}
		}
	}

	// public boolean cut(String s)
	// {
	// // System.out.println("Ju-87 cut(" + s + ")");
	// Exception e = new Exception("Ju-87 cut(" + s + ")");
	// e.printStackTrace();
	// return super.cut(s);
	// }

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "FlightModel", "FlightModels/Ju-87C.fmd");
		Property.set(class1, "meshName", "3do/plane/Ju-87C/hier.him");
		Property.set(class1, "iconFar_shortClassName", "Ju-87");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		try {
			Property.set(class1, "cockpitClass", new Class[] { Class.forName("com.maddox.il2.objects.air.CockpitJU_87B2"),
					Class.forName("com.maddox.il2.objects.air.CockpitJU_87B2_Gunner") });
		} catch (ClassNotFoundException classnotfoundexception) {
			EventLog.type("Exception in Ju-87C Cockpit init, " + classnotfoundexception.getMessage());
		}
		Property.set(class1, "LOSElevation", 0.8499F);
		Property.set(class1, "yearService", 1939.9F);
		Property.set(class1, "yearExpired", 1945.5F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01",
				"_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
		Aircraft.weaponsRegister(class1, "default", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, null });
		Aircraft.weaponsRegister(class1, "4xSC50", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900",
				"BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1" });
		Aircraft.weaponsRegister(class1, "1xSC500", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, "BombGunSC500 1" });
		Aircraft.weaponsRegister(class1, "1xSC500_4xSC50", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900",
				"BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC500 1" });
		Aircraft.weaponsRegister(class1, "1xSD500", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, "BombGunSD500 1" });
		Aircraft.weaponsRegister(class1, "1xAB500", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, "BombGunAB500 1" });
		Aircraft.weaponsRegister(class1, "1xSC250", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, "BombGunSC250 1" });
		Aircraft.weaponsRegister(class1, "1xSC250_4xSC50", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900",
				"BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC50 1", "BombGunSC250 1" });
		Aircraft.weaponsRegister(class1, "1xAB250", new String[] { "MGunMG17si 500", "MGunMG17si 500", "MGunMG15t 900", null,
				null, null, null, "BombGunAB250 1" });
		Aircraft.weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null });
	}
}
