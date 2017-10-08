// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18.01.2013 17:30:12
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitB29_TGunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner

public class CockpitMi24_GUNNER extends CockpitGunner {

	
	protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("CF_D0", true);
            aircraft().hierMesh().chunkVisible("Blister_D0", true);
            aircraft().hierMesh().chunkVisible("Door2_D0", true);
            aircraft().hierMesh().chunkVisible("Pitot_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
    	aircraft().hierMesh().chunkVisible("CF_D0", true);
        aircraft().hierMesh().chunkVisible("Blister_D0", true);
        aircraft().hierMesh().chunkVisible("Door2_D0", true);
        aircraft().hierMesh().chunkVisible("Pitot_D0", true);
        aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
        aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        super.doFocusLeave();
    }
    
    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("Blister_D0", hiermesh.isChunkVisible("Blister_D0"));
        mesh.chunkVisible("Door2_D0", hiermesh.isChunkVisible("Door2_D0"));
        mesh.chunkVisible("Pitot_D0", hiermesh.isChunkVisible("Pitot_D0"));
        mesh.chunkVisible("Turret1A_D0", hiermesh.isChunkVisible("Turret1A_D0"));
        mesh.chunkVisible("Turret1B_D0", hiermesh.isChunkVisible("Turret1B_D0"));
    }


	
	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
		mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -60F)
					f = -60F;
				if (f > 60F)
					f = 60F;
				if (f1 > 60F)
					f1 = 60F;
				if (f1 < -60F)
					f1 = -60F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		


		
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets()
					|| !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
//			if (bGunFire) {
//				if (hook1 == null)
//					hook1 = new HookNamed(aircraft(), "_MGUN03");
//				doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
//			}
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets()
					|| !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("Z_Holes1_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitMi24_GUNNER() {
		super("3DO/Cockpit/A-20G-TGun/TGunnerMi24.him", "he111_gunner");
		bNeedSetUp = true;
		prevTime = -1L;
		prevA0 = 0.0F;
		hook1 = null;
	}
	
	public void reflectWorldToInstruments(float f)
    {
		 reflectPlaneToModel();
    }

	private boolean bNeedSetUp;
	private long prevTime;
	private float prevA0;
	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 1);
	}
}