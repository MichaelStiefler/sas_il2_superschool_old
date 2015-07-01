// This file is part of the SAS IL-2 Sturmovik 1946 4.13
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
// Last Edited on: 2014/10/28

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_24J100 extends B_24 implements TypeX4Carrier, TypeGuidedBombCarrier {

	public B_24J100() {
		bToFire = false;
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
        fCSink = 0.0F;	 	 
	}

    public void update(float f)	 	 
    {	 	 
        super.update(f);	 	 
        if(FM.CT.getGearC() > 0.9F)	 	 
        {	 	 
            resetYPRmodifier();	 	 
            xyz[1] = fCSink;	 	 
            ypr[1] = fCSteer;	 	 
            hierMesh().chunkSetLocate("GearC3_D0", xyz, ypr);	 	 
        }	 	 
        if(iBallPos == 0)	 	 
        {	 	 
            if(fBallPos > 0.0F)	 	 
            {	 	 
                fBallPos -= 0.2F * f;	 	 
                resetYPRmodifier();	 	 
                xyz[1] = -0.635F + 0.635F * fBallPos;	 	 
                hierMesh().chunkSetLocate("Turret3C_D0", xyz, ypr);	 	 
            }	 	 
        } else	 	 
        if(iBallPos == 1 && fBallPos < 1.0F)	 	 
        {	 	 
            fBallPos += 0.2F * f;	 	 
            resetYPRmodifier();	 	 
            xyz[1] = -0.635F + 0.635F * fBallPos;	 	 
            hierMesh().chunkSetLocate("Turret3C_D0", xyz, ypr);	 	 
            if(fBallPos > 0.8F)	 	 
                if(fBallPos >= 0.9F);	 	 
        }	 	 
        if(FM.turret[2].bIsAIControlled)	 	 
        {	 	 
            if(FM.turret[2].target != null && FM.AS.astatePilotStates[5] < 90)	 	 
                iBallPos = 1;	 	 
            if(Time.current() > btme)	 	 
            {	 	 
                btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);	 	 
                if(FM.turret[2].target == null)	 	 
                    iBallPos = 0;	 	 
            }	 	 
        }	 	 
    }	 	 

    public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.endsWith("Bat")) {
			hierMesh().chunkVisible("BatWingRackR_D0", true);
			hierMesh().chunkVisible("BatWingRackL_D0", true);
			return;
		} else {
			return;
		}
	}

	public boolean typeGuidedBombCisMasterAlive() {
		return isMasterAlive;
	}

	public void typeGuidedBombCsetMasterAlive(boolean flag) {
		isMasterAlive = flag;
	}

	public boolean typeGuidedBombCgetIsGuiding() {
		return isGuidingBomb;
	}

	public void typeGuidedBombCsetIsGuiding(boolean flag) {
		isGuidingBomb = flag;
	}

	public void typeX4CAdjSidePlus() {
		deltaAzimuth = 0.002F;
	}

	public void typeX4CAdjSideMinus() {
		deltaAzimuth = -0.002F;
	}

	public void typeX4CAdjAttitudePlus() {
		deltaTangage = 0.002F;
	}

	public void typeX4CAdjAttitudeMinus() {
		deltaTangage = -0.002F;
	}

	public void typeX4CResetControls() {
		deltaAzimuth = deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return deltaTangage;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19: // '\023'
            killPilot(this, 5);
            killPilot(this, 6);	 	 
            killPilot(this, 7);	 	 
            killPilot(this, 8);	 	 
			killPilot(this, 4);
            cut("StabL"); 
            cut("StabR");	 	 
            break;	 	 
 	 	 
        case 13: // '\r'	 	 
            killPilot(this, 0);	 	 
            killPilot(this, 1);	 	 
            killPilot(this, 2);	 	 
            killPilot(this, 3);	 	 
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
		default:
			break;

		case 0: // '\0'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 85F) {
				f = 85F;
				flag = false;
			}
			if (f1 < -32F) {
				f1 = -32F;
				flag = false;
			}
			if (f1 > 46F) {
				f1 = 46F;
				flag = false;
			}
			break;

		case 1: // '\001'
			if (f1 < -0F) {
				f1 = -0F;
				flag = false;
			}
			if (f1 > 20F) {
				f1 = 20F;
				flag = false;
			}
			break;

		case 2: // '\002'
			if (f1 < -70F) {
				f1 = -70F;
				flag = false;
			}
			if (f1 > 7F) {
				f1 = 7F;
				flag = false;
			}
			break;

		case 3: // '\003'
			if (f < -35F) {
				f = -35F;
				flag = false;
			}
			if (f > 64F) {
				f = 64F;
				flag = false;
			}
			if (f1 < -37F) {
				f1 = -37F;
				flag = false;
			}
			if (f1 > 50F) {
				f1 = 50F;
				flag = false;
			}
			break;

		case 4: // '\004'
			if (f < -67F) {
				f = -67F;
				flag = false;
			}
			if (f > 34F) {
				f = 34F;
				flag = false;
			}
			if (f1 < -37F) {
				f1 = -37F;
				flag = false;
			}
			if (f1 > 50F) {
				f1 = 50F;
				flag = false;
			}
			break;

		case 5: // '\005'
			if (f < -85F) {
				f = -85F;
				flag = false;
			}
			if (f > 85F) {
				f = 85F;
				flag = false;
			}
			if (f1 < -32F) {
				f1 = -32F;
				flag = false;
			}
			if (f1 > 46F) {
				f1 = 46F;
				flag = false;
			}
			break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

    public float getExtraParasiteDrag()
    {
        return fBallPos * 0.2F;
    }
    
    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.05F, 0.75F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, cvt(f2, 0.05F, 0.75F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, cvt(f2, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, cvt(f2, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.12F, 0.99F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.12F, 0.99F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.12F, 0.99F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, cvt(f, 0.12F, 0.99F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.12F, 0.99F, 0.0F, -20F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.02F, 0.82F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.02F, 0.82F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.02F, 0.82F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, cvt(f1, 0.02F, 0.82F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.02F, 0.82F, 0.0F, -20F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        fCSink = cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.5F, 0.0F, 0.5F);
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.456F, 0.0F, 0.2821F);
        hierMesh().chunkSetLocate("GearL3_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.456F, 0.0F, 0.2821F);
        hierMesh().chunkSetLocate("GearR3_D0", xyz, ypr);
    }
	
	public boolean bToFire;
	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;
    private float fCSink;
	public static boolean bChangedPit = false;

	static {
		Class class1 = B_24J100.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-24");
		Property.set(class1, "meshName", "3DO/Plane/B-24J-100-CF(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/B-24J-100-CF(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1943.5F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-24J.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB_24J100.class, CockpitB_24J100_Copilot.class, CockpitB_24J100_Bombardier.class, CockpitB_24J100_FGunner.class, CockpitB_24J100_TGunner.class, CockpitB_24J100_AGunner.class, CockpitB_24J100_BGunner.class,
				CockpitB_24J100_RGunner.class, CockpitB_24J100_LGunner.class });
		weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 14, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05",
				"_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "16x500", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", "BombGun500lbs 2", null, null, null, null });
		weaponsRegister(class1, "16xRazon", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null });
		weaponsRegister(class1, "8xRazon", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", "RocketGunRazon 2", null, null, null, null, null, null, null, null });
		weaponsRegister(class1, "2xBat", new String[] { "MGunBrowning50t 365", "MGunBrowning50t 365", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 610", "MGunBrowning50t 375", "MGunBrowning50t 375",
				"MGunBrowning50t 500", "MGunBrowning50t 500", null, null, null, null, null, null, null, null, "RocketGunBat 1", "BombGunNull 1", "BombGunNull 1", "RocketGunBat 1" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
	}
}
