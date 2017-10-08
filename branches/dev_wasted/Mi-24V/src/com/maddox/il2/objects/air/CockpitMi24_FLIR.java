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
		
		
//		String s3 = " ";
//        if(victim instanceof TgtTank)
//            s3 = "Armor";
//        if(victim instanceof ArtilleryGeneric)
//            s3 = "Misc";
//        if(victim instanceof TgtVehicle)
//            s3 = "Vehicle";
//        if(victim instanceof AAA)
//            s3 = "AAA";

		

		
	}
	
//    public void laserUpdate() {
//    	
//    	this.pos.setUpdateEnable(true);
//    	
//    	this.pos.getRender(_tmpLoc);
//    	LaserHook[1] = new HookNamed(super.mesh, "_Laser1");
//    	
//    	LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//    	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
//    	LaserLoc1.get(LaserP1);
//    	LaserLoc1.set(30000.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
//    	this.LaserHook[1].computePos(this, _tmpLoc, LaserLoc1);
//    	LaserLoc1.get(LaserP2);
//    	Engine.land(); 
//    	if (Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL)) 
//    	{
//    		LaserPL.z -= 0.95D;
//    		LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
// //   		this.Laser[1].setPos(LaserP2);
//    		
//    		Mi24V.spot.set(LaserP2);
//    		
//    		Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
//    		
//    	}
//    }

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
	
	
//    public void clipAnglesGun(Orient orient)
//    {
//        float f = orient.getYaw();
//        float f1 = orient.getTangage();
//        float f2 = Math.abs(f);
//        for(; f < -180F; f += 360F);
//        for(; f > 180F; f -= 360F);
//        for(; prevA0 < -180F; prevA0 += 360F);
//        for(; prevA0 > 180F; prevA0 -= 360F);
//        if(!isRealMode())
//        {
//            prevA0 = f;
//        } else
//        {
//            if(bNeedSetUp)
//            {
//                prevTime = Time.current() - 1L;
//                bNeedSetUp = false;
//            }
//            if(f < -120F && prevA0 > 120F)
//                f += 360F;
//            else
//            if(f > 120F && prevA0 < -120F)
//                prevA0 += 360F;
//            float f3 = f - prevA0;
//            float f4 = 0.001F * (float)(Time.current() - prevTime);
//            float f5 = Math.abs(f3 / f4);
//            if(f5 > 120F)
//                if(f > prevA0)
//                    f = prevA0 + 120F * f4;
//                else
//                if(f < prevA0)
//                    f = prevA0 - 120F * f4;
//            prevTime = Time.current();
//            if(f1 > 0.0F)
//                f1 = 0.0F;
//            if(f1 < -95F)
//                f1 = -95F;
//            orient.setYPR(f, f1, 0.0F);
//            orient.wrap();
//            prevA0 = f;
//        }
//    }
    
    

	protected void interpTick() {
		if (isRealMode()) {
			
			
			
			
			
			
//			lock();
			
//			Orient orient1 = hookGunner().getGunMove();
//	        float f = orient1.getYaw();
//	        float f1 = orient1.getTangage();
//	        
//	        float kren = ((FlightModelMain) (super.fm)).Or.getKren();
//	        float yaw = ((FlightModelMain) (super.fm)).Or.getYaw();
//	        float tangage = ((FlightModelMain) (super.fm)).Or.getTangage();
//	        
//	        	
//	        	if (kren < -30F)
//	        		kren = -30F;
//	        	if (kren > 30F)
//	        		kren = 30F;
//		        mesh.chunkSetAngles("Body", 0.0F, kren, 0.0F);
//				mesh.chunkSetAngles("Turret1A", -f - yaw, 0.0F, 0.0F);
//				mesh.chunkSetAngles("Turret1B", 0.0F, f1 - tangage, 0.0F);
			
			

			

		}
	}
	
//	private void lock()
//    {
//        if(!bEntered)
//        {
//            return;
//        } else
//        {
//        	victim = Selector.look(true, true, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], -1, -1, World.getPlayerAircraft(), false);
//        	Mi24V.lock = victim;
//            return;
//        }
//    }
	
	protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
        	((Mi24V)aircraft()).FLIR = true;
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

//	public void doGunFire(boolean flag) {
//		if (isRealMode()) {
//			if (emitter == null || !emitter.haveBullets()
//					|| !aiTurret().bIsOperable)
//				bGunFire = false;
//			else
//				bGunFire = flag;
//			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
//		}
//	}

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