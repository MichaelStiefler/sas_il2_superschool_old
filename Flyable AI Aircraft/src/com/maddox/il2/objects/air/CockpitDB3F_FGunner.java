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
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitDB3F_FGunner extends CockpitGunner {

    private class Variables
    {

        AnglesFork azimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
        }

    }

    public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("TurretA", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("TurretB", 0.0F, -f1, 0.0F);
		mesh.chunkSetAngles("Ret_Base", 0.0F, f1, 0.0F);
		mesh.chunkSetAngles("Reticle", 0.0F, f1, 0.0F);
		if (f > 25F)
			f = 25F;
		if (f < -25F)
			f = -25F;
		if (f1 < -22F)
			f1 = -22F;
		mesh.chunkSetAngles("Cam_A", 0.0F, f, 0.0F);
		mesh.chunkSetAngles("Cam_B", 0.0F, -f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode()) {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f > 30F)
				f = 30F;
			if (f < -30F)
				f = -30F;
			if (f1 > 30F)
				f1 = 30F;
			if (f1 < -30F)
				f1 = -30F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
        if(IL_4_DB3F.bChangedPit)
        {
            reflectPlaneToModel();
            IL_4_DB3F.bChangedPit = false;
        }
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN01");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
			}
		}
        setTmp = setOld;
        setOld = setNew;
        setNew = setTmp;
        setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public CockpitDB3F_FGunner() {
		super("3DO/Cockpit/Il-4-NGun/hier.him", "he111_gunner");
		hook1 = null;
        pictAiler = 0.0F;
        setOld = new Variables();
        setNew = new Variables();
        cockpitNightMats = (new String[] {
            "Prib_two", "Prib_three", "DPrib_two"
        });
        setNightMats(false);
	}

	protected boolean doFocusEnter() {
		boolean flag = super.doFocusEnter();
		HookGunner hookgunner = HookGunner.current();
		hookgunner.setLimits(90F, -50F, 80F);
        aircraft().hierMesh().chunkVisible("CFE_D0", false);
        aircraft().hierMesh().chunkVisible("CFE_D1", false);
        aircraft().hierMesh().chunkVisible("CFE_D2", false);
        aircraft().hierMesh().chunkVisible("CFE_D3", false);
        aircraft().hierMesh().chunkVisible("Cockpit1_D0", false);
        aircraft().hierMesh().chunkVisible("Cockpit2_D0", false);
		return flag;
	}

    protected void doFocusLeave()
    {
        if(isFocused())
        {
            aircraft().hierMesh().chunkVisible("CFE_D" + aircraft().chunkDamageVisible("CF"), true);
            aircraft().hierMesh().chunkVisible("Cockpit1_D0", true);
            aircraft().hierMesh().chunkVisible("Cockpit2_D0", true);
            super.doFocusLeave();
        }
    }

    public boolean focusEnter() {
		return super.focusEnter();
	}

	public void reflectWorldToInstruments(float f) {
        mesh.chunkSetAngles("Z_Wheel", 0.0F, 45F * (pictAiler = 0.65F * pictAiler + 0.35F * fm.CT.AileronControl), 0.0F);
        mesh.chunkSetAngles("Z_Compass2b", setNew.azimuth.getDeg(f) - 5F, 0.0F, 0.0F);
	}

	public void reflectCockpitState() {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("DM_6", true);
            mesh.chunkVisible("DM_8", true);
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("DM_2", true);
            mesh.chunkVisible("DM_1", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_4", true);
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("DM_2", true);
            mesh.chunkVisible("DM_4", true);
            mesh.chunkVisible("DM_5", true);
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("DM_7", true);
            mesh.chunkVisible("DM_3", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("DM_8", true);
            mesh.chunkVisible("DM_6", true);
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Prib_two", false);
            mesh.chunkVisible("Prib_two_Dm", true);
            mesh.chunkVisible("DM_5", true);
            mesh.chunkVisible("DM_3", true);
        }
	}

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    protected void reflectPlaneToModel()
    {
        if(isFocused())
        {
            aircraft().hierMesh().chunkVisible("CFE_D0", false);
            aircraft().hierMesh().chunkVisible("CFE_D1", false);
            aircraft().hierMesh().chunkVisible("CFE_D2", false);
            aircraft().hierMesh().chunkVisible("CFE_D3", false);
        }
    }

    private Hook hook1;
    private float pictAiler;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

	static {
		Property.set(CockpitDB3F_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitDB3F_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitDB3F_FGunner.class, "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "normZN", 1.0F);
	}
}
