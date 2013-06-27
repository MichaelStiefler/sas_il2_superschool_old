// This file is part of the SAS IL-2 Sturmovik 1946 4.12
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/02/03

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.CLASS;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class RE_2002 extends RE_2002xyz implements TypeDiveBomber {

	public RE_2002() {
		canopyF = 0.0F;
		tiltCanopyOpened = false;
		slideCanopyOpened = false;
		blisterRemoved = false;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (tiltCanopyOpened && !blisterRemoved && FM.getSpeed() > 75F)
			doRemoveBlister1();
	}

	private final void doRemoveBlister1() {
		blisterRemoved = true;
		if (hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			hierMesh().hideSubTrees("Blister1_D0");
			Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
			wreckage.collide(true);
			Vector3d vector3d = new Vector3d();
			vector3d.set(FM.Vwld);
			wreckage.setSpeed(vector3d);
		}
	}

	public void moveCockpitDoor(float f) {
		if (f > canopyF) {
			if (FM.Gears.onGround() && FM.getSpeed() < 5F || tiltCanopyOpened) {
				tiltCanopyOpened = true;
				hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
			} else {
				slideCanopyOpened = true;
				resetYPRmodifier();
				Aircraft.xyz[0] = -0.01F;
				Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
				hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
				Aircraft.xyz[0] = 0.01F;
				hierMesh().chunkSetLocate("Blister4R_D0", Aircraft.xyz, Aircraft.ypr);
			}
		} else if (FM.Gears.onGround() && FM.getSpeed() < 5F && !slideCanopyOpened || tiltCanopyOpened) {
			hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
			if (FM.getSpeed() > 50F && f < 0.6F && !blisterRemoved)
				doRemoveBlister1();
			if (f == 0.0F)
				tiltCanopyOpened = false;
		} else {
			resetYPRmodifier();
			Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.3F);
			hierMesh().chunkSetLocate("Blister4L_D0", Aircraft.xyz, Aircraft.ypr);
			hierMesh().chunkSetLocate("Blister4R_D0", Aircraft.xyz, Aircraft.ypr);
			if (f == 0.0F)
				slideCanopyOpened = false;
		}
		canopyF = f;
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	public boolean typeDiveBomberToggleAutomation() {
		return false;
	}

	public void typeDiveBomberAdjAltitudeReset() {
	}

	public void typeDiveBomberAdjAltitudePlus() {
	}

	public void typeDiveBomberAdjAltitudeMinus() {
	}

	public void typeDiveBomberAdjVelocityReset() {
	}

	public void typeDiveBomberAdjVelocityPlus() {
	}

	public void typeDiveBomberAdjVelocityMinus() {
	}

	public void typeDiveBomberAdjDiveAngleReset() {
	}

	public void typeDiveBomberAdjDiveAnglePlus() {
	}

	public void typeDiveBomberAdjDiveAngleMinus() {
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	protected void mydebug(String s) {
		System.out.println(s);
	}

	public float canopyF;
	private boolean tiltCanopyOpened;
	private boolean slideCanopyOpened;
	private boolean blisterRemoved;

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "RE.2002");
		Property.set(class1, "meshName_it", "3DO/Plane/RE-2002(it)/hier.him");
		Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
		Property.set(class1, "meshName", "3DO/Plane/RE-2002(multi)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1943F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/RE-2000.fmd");
		Property.set(class1, "LOSElevation", 0.9119F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitRE_2002.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 9 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalTorp01", "_ExternalDev01" });
		weaponsRegister(class1, "default", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", null, null, null, null, null, null });
		weaponsRegister(class1, "2x100kg", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", "BombGunIT_100_M 1", "BombGunIT_100_M 1", null, null, null, null });
		weaponsRegister(class1, "2x100kg+1x240lTank", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", "BombGunIT_100_M 1", "BombGunIT_100_M 1", null, null, null,
				"FuelTankGun_Tank240 1" });
		weaponsRegister(class1, "1x250kg+2x100kg", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", "BombGunIT_100_M 1", "BombGunIT_100_M 1", "BombGunIT_250_T 1", null, null, null });
		weaponsRegister(class1, "1x250kg", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", null, null, "BombGunIT_250_T 1", null, null, null });
		weaponsRegister(class1, "1x500kg", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", null, null, "BombGunIT_500_T 1", null, null, null });
		weaponsRegister(class1, "1x630kg", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", null, null, null, "BombGunIT_630 1", null, null });
		weaponsRegister(class1, "1xTorpedo", new String[] { "MGunBredaSAFAT127re 390", "MGunBredaSAFAT127re 450", "MGunBredaSAFAT77ki 640", "MGunBredaSAFAT77ki 640", null, null, null, null, "BombGunTorp650 1", null });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null });
	}
}
