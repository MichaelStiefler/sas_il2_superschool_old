// This file is part of the SAS IL-2 Sturmovik 1946
// Late Seafire Mod package.
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
// Last Edited at: 2013/03/11

package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class SeafireLate extends SEAFIRE3 implements TypeFighterAceMaker, TypeBNZFighter {
	public SeafireLate() {
		k14Mode = 0;
		k14WingspanType = 0;
		k14Distance = 200F;
		flapps = 0.0F;
		arrestor = 0.0F;
		bWingsFolded = false;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (FM.isPlayers())
			bChangedPit = true;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.6F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, -95F), 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.09F, 0.0F, -75F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(hierMesh(), f);
	}

	public void moveSteering(float f) {
		hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
	}

	public void moveWheelSink() {
		resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.247F, 0.0F, -0.247F);
		hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[2] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.247F, 0.0F, 0.247F);
		hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	public void moveCockpitDoor(float f) {
		resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
		hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
		hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
		hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			setDoorSnd(f);
		}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		if (!this.FM.CT.bHasWingControl)
			return;
		hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, -112F * f, 0.0F);
		hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -112F * f, 0.0F);
	}

	public void moveWingFold(float f) {
		if (!this.FM.CT.bHasWingControl)
			return;
		this.moveWingFold(hierMesh(), f);
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			this.FM.CT.WeaponControl[0] = false;
			hideWingWeapons(true);
		}
		bWingsFolded = (f > 0.999F);
	}

	public void moveArrestorHook(float f) {
		if (!this.FM.CT.bHasArrestorControl)
			return;
		hierMesh().chunkSetAngles("Hook2_D0", 0.0F, -35F * f, 0.0F);
		resetYPRmodifier();
		Aircraft.xyz[2] = 0.3385F * f;
		hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
		arrestor = f;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		if (this.FM.CT.bHasArrestorControl && i == 19)
			FM.CT.bHasArrestorControl = false;
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		super.update(f);
		float fRadiatorPos = this.FM.EI.engines[0].getControlRadiator();
		if (Math.abs(flapps - fRadiatorPos) > 0.01F) {
			flapps = fRadiatorPos;
			hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * fRadiatorPos, 0.0F);
			hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * fRadiatorPos, 0.0F);
		}

		if (this.FM.CT.bHasArrestorControl) {

			float fArrestorPos = this.FM.CT.getArrestor();
			float fArrestorPosPow = 81F * (float) Math.pow(fArrestorPos, 7);
			if (fArrestorPos > 0.01F) {
				if (this.FM.Gears.arrestorVAngle != 0.0F) {
					arrestor = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -fArrestorPosPow, fArrestorPosPow, -fArrestorPosPow, fArrestorPosPow);
					moveArrestorHook(fArrestorPos);
				} else {
					float f3 = 58F * this.FM.Gears.arrestorVSink;
					if (f3 > 0.0F && this.FM.getSpeedKMH() > 60F)
						Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
					arrestor += f3;
					if (arrestor > fArrestorPosPow)
						arrestor = fArrestorPosPow;
					if (arrestor < -fArrestorPosPow)
						arrestor = -fArrestorPosPow;
					moveArrestorHook(fArrestorPos);
				}
			}

		}

		if (this.FM.CT.bHasWingControl) {
			if (bWingsFolded) {
				hierMesh().chunkVisible("SoporteR_ala", true);
				hierMesh().chunkVisible("SoporteL_ala", true);
			} else if (FM.Gears.onGround()) {
				hierMesh().chunkVisible("SoporteR_ala", false);
				hierMesh().chunkVisible("SoporteL_ala", false);
			}
		}

	}

	public boolean typeFighterAceMakerToggleAutomation() {
		k14Mode++;
		if (k14Mode > 2)
			k14Mode = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset() {
	}

	public void typeFighterAceMakerAdjDistancePlus() {
		k14Distance += 10F;
		if (k14Distance > 800F)
			k14Distance = 800F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
	}

	public void typeFighterAceMakerAdjDistanceMinus() {
		k14Distance -= 10F;
		if (k14Distance < 200F)
			k14Distance = 200F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
	}

	public void typeFighterAceMakerAdjSideslipReset() {
	}

	public void typeFighterAceMakerAdjSideslipPlus() {
		k14WingspanType--;
		if (k14WingspanType < 0)
			k14WingspanType = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus() {
		k14WingspanType++;
		if (k14WingspanType > 9)
			k14WingspanType = 9;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte(k14Mode);
		netmsgguaranted.writeByte(k14WingspanType);
		netmsgguaranted.writeFloat(k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		k14Mode = netmsginput.readByte();
		k14WingspanType = netmsginput.readByte();
		k14Distance = netmsginput.readFloat();
	}

	// public int getK14Mode() { return this.k14Mode; }
	// public int getK14WingspanType() { return this.k14WingspanType; }
	// public float getK14Distance() { return this.k14Distance; }

	public int k14Mode;
	public int k14WingspanType;
	public float k14Distance;
	private float flapps;
	protected float arrestor;
	private boolean bWingsFolded;
}
