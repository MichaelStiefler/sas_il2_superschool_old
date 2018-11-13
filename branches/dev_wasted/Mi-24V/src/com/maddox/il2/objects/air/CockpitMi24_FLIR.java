// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 18.01.2013 17:30:12
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitB29_TGunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.engine.hotkey.HookViewEnemy;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import java.io.PrintStream;


// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner

public class CockpitMi24_FLIR extends CockpitGunner {

	public void moveGun(Orient orient) {
		Orient orient1 = hookGunner().getGunMove();
        float f = orient1.getYaw();
        float f1 = orient1.getTangage();
        Mi24V.LaserOr = hookGunner().getGunMove();
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret1A", -f, 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);	
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
				if (f1 > 20F)
					f1 = 20F;
				if (f1 < -80F)
					f1 = -80F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
		}
	}
	
	protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            enter();         
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
        if(!isFocused())
        {
            return;
        } else
        {
        	((Mi24V)aircraft()).FLIR = false;
            leave();
            
            aircraft().hierMesh().chunkVisible("CF_D0", true);
            aircraft().hierMesh().chunkVisible("Blister_D0", true);
            aircraft().hierMesh().chunkVisible("Door2_D0", true);
            aircraft().hierMesh().chunkVisible("Pitot_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            
            super.doFocusLeave();
            return;
        }
    }

    private void enter()
    {
        saveFov = Main3D.FOVX;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        CmdEnv.top().exec("fov 33.3");
        bEntered = true;
    }

    private void leave()
    {
        if(!bEntered)
        {
            return;
        } else
        {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            
            HUD.training("   ");
            
            return;
        }
    }


	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("Z_Holes1_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitMi24_FLIR() {
		super("3DO/Cockpit/A-20G-TGun/TGunnerMi24FLIR.him", "he111_gunner");
		bNeedSetUp = true;
		prevTime = -1L;
		prevA0 = 0.0F;
		hook1 = null;
	}
	
	public void reflectWorldToInstruments(float f)
    {
    }

	

    private Hook[] LaserHook = { null, null, null, null };
    
    private LightPointWorld[] Laser;
	
    private static Loc LaserLoc1 = new Loc();
    private static Point3d LaserP1 = new Point3d();
    private static Point3d LaserP2 = new Point3d();
    private static Point3d LaserPL = new Point3d();
    
	private float[] headPos = new float[3];
	private float[] headOr = new float[3];
	private float pilotHeadT = 0.0F;
	private float pilotHeadY = 0.0F;
	public static Orient tmpOr = new Orient();
	private static Vector3d Ve = new Vector3d();
	
	private boolean bEntered;
	private float saveFov;

	public Actor victim;
	private boolean bNeedSetUp;
	private long prevTime;
	private float prevA0;
	private Hook hook1;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 1);
		Property.set(CLASS.THIS(), "weaponControlNum", 11);
		Property.set(CLASS.THIS(), "astatePilotIndx", 0);
	}
}