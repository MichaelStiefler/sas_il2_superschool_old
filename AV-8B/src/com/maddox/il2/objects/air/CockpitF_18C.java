// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/3/2012 3:09:47 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitT4.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.FuelTankGun_Tank18C;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.rts.Time;
import com.maddox.sound.*;

import java.util.ArrayList;




















import java.util.List;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;


import com.maddox.il2.objects.sounds.SndAircraft;


// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, AircraftLH, F_18, Aircraft, 
//            Cockpit, TypeSupersonic

public class CockpitF_18C extends CockpitPilot
{
    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float PDI;
        float ilsLoc;
        float ilsGS;
        float navDiviation0;
        float navDiviation1;
        boolean navTo0;
        boolean navTo1;
        float navDist0;
        float navDist1;
        float vspeed;
        float vspeed2;
        float dimPosition;
        float beaconDirection;
        float beaconRange;
        float pitch;
        float bank;
        float fpmPitch;
        float fpmYaw;
        boolean isGeneratorAllive;
        boolean isBatteryOn;
        float k14wingspan;
        float k14mode;
        float k14x;
        float k14y;
        float k14w;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
            isGeneratorAllive = false;
            isBatteryOn = false;
        }

        Variables(Variables variables)
        {
            this();
        }
    }
    
    protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			//aircraft().hierMesh().chunkVisible("Canopy_D0", false);
			aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			aircraft().hierMesh().chunkVisible("Head1_D0", false);
			aircraft().hierMesh().chunkVisible("HMask1_D0", false);
			aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
			aircraft().hierMesh().chunkVisible("Seat1_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		//aircraft().hierMesh().chunkVisible("Canopy_D0", true);
		aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		aircraft().hierMesh().chunkVisible("Head1_D0", true);
		aircraft().hierMesh().chunkVisible("HMask1_D0", true);
		aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
		aircraft().hierMesh().chunkVisible("Seat1_D0", true);
		super.doFocusLeave();
	}


    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
        	setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            float f = 200F;
			if(((F_18) aircraft()).radargunsight == 0 && ((F_18) aircraft()).k14Mode == 1)
				f = 200F;
			if(((F_18) aircraft()).radargunsight == 1 && ((F_18) aircraft()).k14Mode == 1)
				f = ((F_18) aircraft()).k14Distance;
			Point3d pointAC = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
			float z = (float)((Tuple3d) (pointAC)).z;
			if((((F_18) aircraft()).radargunsight == 2 || ((F_18) aircraft()).radargunsight == 3) && ((F_18) aircraft()).k14Mode == 1){
				f = z/(float)Math.cos(Math.toRadians(fm.Or.getPitch()-270F)) + 200F;				
			}
            setNew.k14w = (5F * CockpitF_18C.k14TargetWingspanScale[((F_18)aircraft()).k14WingspanType]) / f;
            setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
            setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF_18C.k14TargetMarkScale[((F_18)aircraft()).k14WingspanType];
            setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F_18)aircraft()).k14Mode;
            com.maddox.JGP.Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
            double d = 0.00125D * (double)f;
            float f1 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
            float f2 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);            
            float f3 = floatindex((f - 200F) * 0.04F, CockpitF_18C.k14BulletDrop) - CockpitF_18C.k14BulletDrop[0];
            if(((F_18) aircraft()).radargunsight == 2)
            	f3 = floatindex((f - 200F) * 0.04F, CockpitF_18C.k14RocketDrop) - CockpitF_18C.k14RocketDrop[0];
            f2 += (float)Math.toDegrees(Math.atan(f3 / f));
            setNew.k14x = 0.92F * setOld.k14x + 0.08F * f1;
            setNew.k14y = 0.92F * setOld.k14y + 0.08F * f2;            
            if(setNew.k14x > 9F)
                setNew.k14x = 9F;
            if(setNew.k14x < -9F)
                setNew.k14x = -9F;
            if(setNew.k14y > 9F)
                setNew.k14y = 9F;
            if(setNew.k14y < -9F)
                setNew.k14y = -9F;
            f = waypointAzimuth();
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding)
                {
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    setNew.navDiviation0 = setNew.ilsLoc;
                } else
                {
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                    setNew.navDiviation0 = normalizeDegree(normalizeDegree(waypointAzimuth(0.02F) + 90F) - normalizeDegree(getNDBDirection()));
                    if(Math.abs(setNew.navDiviation0) < 90F)
                        setNew.navTo0 = true;
                    else
                    if(setNew.navDiviation0 > 270F)
                    {
                        setNew.navTo0 = true;
                        setNew.navDiviation0 = setNew.navDiviation0 - 360F;
                    } else
                    {
                        setNew.navTo0 = false;
                        setNew.navDiviation0 = normalizeDegree180(setNew.navDiviation0);
                        if(setNew.navDiviation0 >= 90F)
                            setNew.navDiviation0 = 180F - setNew.navDiviation0;
                        else
                            setNew.navDiviation0 = -180F - setNew.navDiviation0;
                    }
                }
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
            }
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            setNew.vspeed2 = (49F * setOld.vspeed2 + fm.getVertSpeed()) / 50F;
            setNew.pitch = ((FlightModelMain) (fm)).Or.getPitch();
            setNew.bank = ((FlightModelMain) (fm)).Or.getRoll();
            Vector3d vector3d1 = new Vector3d();
            ((FlightModelMain) (fm)).Or.transformInv(((FlightModelMain) (fm)).Vwld, vector3d1);
            setNew.fpmPitch = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (vector3d1)).z, ((Tuple3d) (vector3d1)).x));
            setNew.fpmYaw = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (vector3d1)).y, ((Tuple3d) (vector3d1)).x));
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            if(((FlightModelMain) (fm)).EI.engines[0].getRPM() > 200F || ((FlightModelMain) (fm)).EI.engines[0].getRPM() > 200F)
                setNew.isGeneratorAllive = true;
            else
                setNew.isGeneratorAllive = false;
            f = ((F_18)aircraft()).fSightCurForwardAngle;
            f1 = ((F_18)aircraft()).fSightCurSideslip;
            f2 = fm.getAltitude();
            f3 = (float)(-(Math.abs(fm.Vwld.length()) * Math.sin(Math.toRadians(Math.abs(fm.Or.getTangage())))) * 0.10189999639987946D);
            f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f2 * 0.1019F);
            float f4 = Math.abs((float)fm.Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
            float f5 = (f4 * f3 + 10F) - 10F;
            alpha = 90F - Math.abs(fm.Or.getTangage()) - (float)Math.toDegrees(Math.atan(f5 / f2));
            return true;
        }

        Interpolater()
        {
        }
    }

    


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public CockpitF_18C()
    {
        super("3DO/Cockpit/F-18/hier.him", "bf109");
        gun = new Gun[4];
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        tmpMat = new Matrix3d();
        bNeedSetUp = true;
        isDimmer = false;
        pictAiler = 0.0F;
        pictElev = 0.0F;
        blinkCounter = 0;
        bU4 = false;
        super.cockpitNightMats = (new String[] {
            "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", 
            "petitfla", "turnbank"
        });
        setNightMats(false);
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
        if(super.acoustics != null)
            super.acoustics.globFX = new ReverbFXRoom(0.05F);
        aoaWarnFX = aircraft().newSound("aircraft.AOA_Slow", false);
        aoaWarnFX2 = aircraft().newSound("aircraft.AOA_Mid", false);
        aoaWarnFX3 = aircraft().newSound("aircraft.AOA_Fast", false);
        aoaWarnFX4 = aircraft().newSound("aircraft.AOA_Stall", false);
        PullupWarn = aircraft().newSound("aircraft.Pullup", false);
        AltitudeWarn = aircraft().newSound("aircraft.Altitude", false);
        hudPitchRudderStr = new String[37];
        for(int i = 18; i >= 0; i--)
            hudPitchRudderStr[i] = "Z_Z_HUD_PITCH" + (90 - i * 5);

        for(int j = 1; j <= 18; j++)
            hudPitchRudderStr[j + 18] = "Z_Z_HUD_PITCHN" + j * 5;

        ((AircraftLH)aircraft()).bWantBeaconKeys = true; //TODO:Radar parameter
		t2 = 0;
		t3 = 0L;
		FOV = 1.0D;
        ScX = 0.0059999997764825821D;
        ScY = 0.0000065;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 50000F;
        RClose = 5F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        radarLock = new ArrayList();
        radarmissile = new ArrayList();
        victim = new ArrayList();
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(0F, 300.0F,0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        tw = 0L;
        x = 0F;
        start = false;
        left = false;
    	right = false;
    }
    
    private LightPointActor light1;

    public void reflectWorldToInstruments(float f)
    {
    	if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
    	HUDgunsight();
		radarclutter(f);
    	boolean flag = false;
    	boolean flag1 = false;
    	int i = ((F_18)aircraft()).leftscreen;
    	if(i == 0)
    	{
    		flag = true;
    		flag1 = false;
    	}
    	if(i == 1)	
    	{
    		flag = false;
    		flag1 = true;
    	}
    	super.mesh.chunkVisible("HDD_Fuel", flag);        	
    	super.mesh.chunkVisible("HDD_FuelFlow", flag1);
    	int j = ((F_18)aircraft()).leftscreen;
    	if(j == 0)
    	{
    		movescreenfuel();
    	}
    	if(j == 1)
    	{
    		movescreenfuelflow();
    	}
    	if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;        
        resetYPRmodifier();
        moveControls(f);
        //moveHUD(f);
        HUD(f);
        //moveGauge(f);
        drawSound(f); 
        backupGauges(f);
        //if(((FlightModelMain) (super.fm)).AS.bShowSmokesOn)
            //super.mesh.chunkVisible("Int_SmokeON", true);
        //else
            //super.mesh.chunkVisible("Int_SmokeON", false);
        //if(((FlightModelMain) (super.fm)).CT.getAirBrake() > 0.0F)
            //super.mesh.chunkVisible("Int_Speed_Ext", true);
        //else
            //super.mesh.chunkVisible("Int_Speed_Ext", false);
        //if(((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F)
            //super.mesh.chunkVisible("Int_LDG_ON", false);
        //else
            //super.mesh.chunkVisible("Int_LDG_ON", true);
        //super.mesh.chunkVisible("Int_Marker", isDimmer);
        ((FlightModelMain) (super.fm)).Or.getMatrix(tmpMat);
        float f1 = (float)tmpMat.getElement(2, 0);
        f1 = 1.0F - f1;
        f1 *= 0.045F;
        f1 += (float)tmpMat.getElement(2, 2) * -0.014F;
        if(f1 < 0.0F)
            f1 = 0.0F;
        //resetYPRmodifier();
        //Cockpit.xyz[2] = f1;
        //super.mesh.chunkSetLocate("ShadowMove1", Cockpit.xyz, Cockpit.ypr);
    }
    
    private void HUDgunsight()
    {
    	if ((fm.AS.astateCockpitState & 2) == 0) {
			int i = ((F_18) aircraft()).k14Mode;
			resetYPRmodifier();
			Cockpit.xyz[0] = setNew.k14w;
			if (i == 0) {				
				resetYPRmodifier();
				super.mesh.chunkSetAngles("Z_Z_Bomb", 0.0F, 0.0F, -((FlightModelMain) (fm)).Or.getRoll());
				super.mesh.chunkSetAngles("Z_Z_Bombsteer", -setNew.k14x, 0.0F, 0.0F);
				super.mesh.chunkVisible("Z_Z_Bombsteer", true);
				super.mesh.chunkVisible("Z_Z_Bombmark2", true);
				super.mesh.chunkVisible("Z_Z_Bombmark3", true);
				super.mesh.chunkVisible("Z_Z_Bombmark4", true);
				super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -1.4F * alpha - setNew.k14y, 0.0F);
				super.mesh.chunkVisible("Z_Z_RETICLE", false);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);
				Cockpit.xyz[2] = 1.3F;
				super.mesh.chunkSetLocate("Z_Z_Bombmark2", Cockpit.xyz, Cockpit.ypr);
				Cockpit.xyz[2] = ((FlightModelMain) (fm)).getAltitude()/700F;
				super.mesh.chunkSetLocate("Z_Z_Bombmark4", Cockpit.xyz, Cockpit.ypr);				
			}
			if (i == 1) {//TODO funnel
				super.mesh.chunkVisible("Z_Z_Bombsteer", false);
				super.mesh.chunkVisible("Z_Z_Bombmark2", false);
				super.mesh.chunkVisible("Z_Z_Bombmark3", false);
				super.mesh.chunkVisible("Z_Z_Bombmark4", false);
				if(((F_18) aircraft()).radargunsight == 1){
				super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x, -setNew.k14y, 0.0F);
				super.mesh.chunkVisible("Z_Z_RETICLE", true);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);				
				} else
				if(((F_18) aircraft()).radargunsight == 0){				
					super.mesh.chunkVisible("Z_Z_RETICLE", false);
					super.mesh.chunkVisible("Z_Z_BulletdropL", true);
					super.mesh.chunkVisible("Z_Z_BulletdropR", true);
					//super.mesh.chunkSetAngles("Z_Z_Bulletdrop", -setNew.k14x - ((FlightModelMain) (fm)).Or.getRoll(), 0.0F, -setNew.k14y);
					super.mesh.chunkSetAngles("Z_Z_Bulletdrop", setNew.k14x*27F, 0.0F, setNew.k14y*10F);
					resetYPRmodifier();
					Cockpit.xyz[1] = -setNew.k14y/100F;
					super.mesh.chunkSetLocate("Z_Z_BulletdropL", Cockpit.xyz, Cockpit.ypr);
					Cockpit.xyz[1] = setNew.k14y/100F;
					super.mesh.chunkSetLocate("Z_Z_BulletdropR", Cockpit.xyz, Cockpit.ypr);
					super.mesh.chunkVisible("Z_Z_radarlock", false);
					super.mesh.chunkVisible("Z_Z_missilelock", false);
				}
				if(((F_18) aircraft()).radargunsight == 2){
					super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x*0.75F, -setNew.k14y*0.75F, 0.0F);
					super.mesh.chunkVisible("Z_Z_RETICLE", true);
					super.mesh.chunkVisible("Z_Z_BulletdropL", false);
					super.mesh.chunkVisible("Z_Z_BulletdropR", false);
					super.mesh.chunkVisible("Z_Z_radarlock", false);
					super.mesh.chunkVisible("Z_Z_missilelock", false);
				}
				if(((F_18) aircraft()).radargunsight == 3){
					super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x*0.22F, -setNew.k14y*0.22F, 0.0F);
					super.mesh.chunkVisible("Z_Z_RETICLE", true);
					super.mesh.chunkVisible("Z_Z_BulletdropL", false);
					super.mesh.chunkVisible("Z_Z_BulletdropR", false);
					super.mesh.chunkVisible("Z_Z_radarlock", false);
					super.mesh.chunkVisible("Z_Z_missilelock", false);
				}
			}
			if (i > 1) {
				super.mesh.chunkVisible("Z_Z_RETICLE", false);
				super.mesh.chunkVisible("Z_Z_Bombsteer", false);
				super.mesh.chunkVisible("Z_Z_Bombmark2", false);
				super.mesh.chunkVisible("Z_Z_Bombmark3", false);
				super.mesh.chunkVisible("Z_Z_Bombmark4", false);
				super.mesh.chunkVisible("Z_Z_BulletdropL", false);
				super.mesh.chunkVisible("Z_Z_BulletdropR", false);
			}
			if(i!=1){
			super.mesh.chunkVisible("Z_Z_radarlock", false);
			super.mesh.chunkVisible("Z_Z_missilelock", false);}
		}
    }
    
	public void radarclutter(float r) {//TODO: Radar		
		boolean radar = false;
		if(!setNew.isBatteryOn && !setNew.isGeneratorAllive || ((F_18) aircraft()).radartogle == false)			
		{	
			radar = false;
			start = false;
			super.mesh.chunkVisible("Z_Z_RADAR_AH", false);
			super.mesh.chunkVisible("Z_Z_lockgate", false);
			super.mesh.chunkVisible("Z_Z_RadarFrame", false);
			super.mesh.chunkVisible("Z_Z_Scan_1", false);
	        super.mesh.chunkVisible("Z_Z_Scan_2", false);
	        super.mesh.chunkVisible("HDDR", true);
	        	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
            	for(int j = 1; j < 4; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
            	super.mesh.chunkVisible("Z_Z_dif+", false);
				super.mesh.chunkVisible("Z_Z_dif-", false);
				super.mesh.chunkVisible("Z_Z_Radarbrg", false);
		} else	
		{
			radar = true;
			if(((F_18) aircraft()).k14Mode != 1){
				super.mesh.chunkVisible("Z_Z_radarlock", false);
				super.mesh.chunkVisible("Z_Z_missilelock", false);}
			super.mesh.chunkVisible("HDDR", false);
			super.mesh.chunkVisible("Z_Z_RadarFrame", true);
		}
		for(int ra = 1; ra<3;ra++)
			super.mesh.chunkVisible("Z_Z_RADAR_Mach_" + ra, radar);
		for(int ra = 1; ra<4;ra++)
			super.mesh.chunkVisible("Z_Z_RADAR_Speed_" + ra, radar);
		for(int ra = 1; ra<3;ra++)
			super.mesh.chunkVisible("Z_Z_RADAR_ALT_" + ra, radar);
		super.mesh.chunkVisible("Z_Z_RADAR_AH", radar);        
		if(!radar){
			for(int j = 0; j <= nTgts; j++)
            {
                String m = "Z_Z_RadarMark" + j;
                if(super.mesh.isChunkVisible(m))
                     super.mesh.chunkVisible(m, false);            
            }
			return;
		}
		float limit = 0F;
		long ts = Time.current();
		if(((F_18)aircraft()).v == 0 && ((F_18)aircraft()).h == 0)
		super.mesh.chunkVisible("Z_Z_lockgate", false); else
		super.mesh.chunkVisible("Z_Z_lockgate", true);	
        resetYPRmodifier();
        Cockpit.xyz[1] = -((F_18)aircraft()).v;
        Cockpit.xyz[2] = ((F_18)aircraft()).h;
        super.mesh.chunkSetLocate("Z_Z_lockgate", Cockpit.xyz, Cockpit.ypr);
		if(((F_18) aircraft()).radarmode == 0 && ((F_18) aircraft()).lockmode == 0)
		{
		radarselection();
		limit = 0.145F;
		if((((F_18)aircraft()).v != 0F || ((F_18)aircraft()).h != 0F) && t3  + 60000L < ts)		
		{
			((F_18)aircraft()).v = 0F;
			((F_18)aircraft()).h = 0F;
			t3 = ts;
		}
		}
		if(((F_18) aircraft()).radarmode == 0 && ((F_18) aircraft()).lockmode == 1)
		{
			radarselection();
			limit = 0.035F;
			radaracquire(r);
			radarlock();			
				for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, true);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, true);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, true);
            	for(int j = 1; j < 4; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, true);
            	super.mesh.chunkVisible("Z_Z_Radarbrg", true);
		} else
		{
				for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
            	for(int j = 1; j < 3; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
            	for(int j = 1; j < 4; j++)
                super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
            	super.mesh.chunkVisible("Z_Z_dif+", false);
				super.mesh.chunkVisible("Z_Z_dif-", false);
				super.mesh.chunkVisible("Z_Z_Radarbrg", false);
		}
			super.mesh.chunkSetAngles("Z_Z_RADAR_AH", 0.0F, setNew.bank, 0.0F);			
			float f = calculateMach();
			float f1 = (float)(int)f * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_1", 0.0F, 0.0F, f1);
			float f2 = (float)((int)(f * 10F) % 10) * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_2", 0.0F, 0.0F, f2 - f1);
			f = (this.fm.getSpeedKMH() * 0.621371F)/100F;
		    f1 = (float)(int)f * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_1", 0.0F, 0.0F, f1);
			f2 = (float)((int)(f * 10F) % 10) * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_2", 0.0F, 0.0F, f2 - f1);
			float f3 = (float)((int)(f * 100F) % 10) * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_3", 0.0F, 0.0F, f3 - f1);
			f = (this.fm.getAltitude()*3.28084F)/10000F;
			f1 = (float)(int)f * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_1", 0.0F, 0.0F, f1);
			f2 = (float)((int)(f * 10F) % 10) * 36F;
			super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_2", 0.0F, 0.0F, f2 - f1);			
			super.mesh.chunkVisible("Z_Z_Scan_1", true);																
			if(!start)
			{
				right = true;
				left = false;						
			} 
			if(x<-limit)
			{
				right = true;
				left = false;			
			}
			if(x>limit)
			{
				right = false;
				left = true;
				start = true;
			}
			if(left && !right)
			if(ts > t2 + 5L)
			{	
				x-=0.01F;
				t2 = ts;
			}
			if(right && !left)
			if(ts > t2 + 5L)
			{	
				x+=0.01F;
				t2 = ts;
			}	
			//HUD.log(AircraftHotKeys.hudLogWeaponId, "scan " + x + " " + right + " " + left + start);
			resetYPRmodifier();
			if(((F_18) aircraft()).lockmode == 0)
		    Cockpit.xyz[1] = x;
			if(((F_18) aircraft()).lockmode == 1)
			Cockpit.xyz[1] = x - ((F_18)aircraft()).v;
		    super.mesh.chunkSetLocate("Z_Z_Scan_1", Cockpit.xyz, Cockpit.ypr);
	}
	
	private ArrayList radarPlane;
	private ArrayList radarLock;
	private ArrayList radarmissile;
	private ArrayList victim;
	private long t2;
	private float x;
	double FOV;
    double ScX;
    double ScY;
    double ScZ;
    float FOrigX;
    float FOrigY;
    int nTgts;
    float RRange;
    float RClose;
    float BRange;
    int BRefresh;
    int BSteps;
    float BDiv;
    long tBOld;
    boolean start;
    boolean left;
	boolean right;
	long t3;
    
    private int iLockState()
    {
        if(!(super.aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)super.aircraft()).getGuidedMissileUtils().getMissileLockState();
    }
    
    public void radarlock() //TODO radarlock
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();           
            radarLock.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                  
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();                   
                    List list = Engine.targets();
                    int i = list.size();                   
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {
                        	
                        	Vector3d vector3d = new Vector3d();                       	
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().curCloudsType();
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && (((Tuple3d) (pointOrtho)).y < ((Tuple3d) (pointOrtho)).x * 0.57735026919 && ((Tuple3d) (pointOrtho)).y > -((Tuple3d) (pointOrtho)).x * 0.57735026919) && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                            	radarLock.add(pointOrtho);                                                     
                        }               	
                    }                  
                int i1 = radarLock.size();
                if(i1>0)
                {	
                int nt = 0;
                targetnum = ((F_18) aircraft()).targetnum;
                if(((F_18) aircraft()).targetnum>=i1)
                	((F_18) aircraft()).targetnum = i1;
                int k = this.targetnum;
                if(i>0)
                {              	
                	Actor actor = (Actor)list.get(k);
                	Vector3d tarvec = new Vector3d();
                	double d = actor.getSpeed(tarvec);
                	//HUD.log(AircraftHotKeys.hudLogWeaponId, "target speed" + d);
                }               
                if(i1>0)
                {
                	double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;                   
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).z * FOV; //distance
                        float f = FOrigX + (float)(NewX * 0.0099999997764825821D) + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getKren())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * 0.0099999997764825821D);                                                                    
                        String m = "Z_Z_radarlock";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        //if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	//super.mesh.chunkVisible(m, false);
                        //else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);                            
                        if(iLockState() == 2)
                        	super.mesh.chunkVisible("Z_Z_missilelock", true); else super.mesh.chunkVisible("Z_Z_missilelock", false);
                    }
                }                                               
                } else              
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_radarlock"))
                        super.mesh.chunkVisible("Z_Z_radarlock", false);
                	if(super.mesh.isChunkVisible("Z_Z_missilelock"))
                        super.mesh.chunkVisible("Z_Z_missilelock", false);
                    
                } 
            }                 
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
	
	public void radarselection() //TODO scan selection
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();           
            radarPlane.clear();
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                  
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();                   
                    List list = Engine.targets();
                    int i = list.size();                   
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {
                        	
                        	Vector3d vector3d = new Vector3d();                       	
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);
                            float f = Mission.cur().curCloudsType();
                            double v = ((x + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getRoll())) * 0.011F))/ScX)/(30D/((Tuple3d) (pointOrtho)).x);
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 9000D && ((Tuple3d) (pointOrtho)).y > v - 9000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                                radarPlane.add(pointOrtho);                                                     
                        }               	
                    }                  
                int i1 = radarPlane.size();
                if(i1>0)
                {	
                int nt = 0;           
                int k = 0;               
                //HUD.log(AircraftHotKeys.hudLogWeaponId, "target num" + k);                
                for(int j = 0; j < i1; j++)
                {
                    double x = ((Tuple3d) ((Point3d)radarPlane.get(j))).x;                   
                    if(x > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 30D / x; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(j))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(j))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY);
                        if(f1 < 0)
                        	f1 = 0;
                        nt++; // number of marks, from 0 -> 10
                        String m = "Z_Z_RadarMark" + nt;
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }	
                }  
                for(int j = nt + 1; j <= nTgts; j++)
                {
                    String m = "Z_Z_RadarMark" + j;
                    if(super.mesh.isChunkVisible(m))
                        super.mesh.chunkVisible(m, false);
                }                
                } else              
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    for(int j = 0; j <= nTgts + 1; j++)
                    {
                        String m = "Z_Z_RadarMark" + j;
                        if(super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, false);
                    }
                    
                } 
            } 
                
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
	
	public void radaracquire(float r) //TODO scan acquire
    {
        try
        {
            Aircraft ownaircraft = World.getPlayerAircraft();           
            radarPlane.clear();
            double v = -((((F_18)aircraft()).v + ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getRoll())) * 0.011F))/ScX);
            double h = ((((F_18)aircraft()).h)/(ScY * ((F_18) aircraft()).radarrange));
            float mach = 0F;
            float alt = 0F;
            float dif = 0F;
            float HDG = 0F;
            float brg = 0F;
            float test = 0F;
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                  
                    Point3d pointAC = ((Actor) (ownaircraft)).pos.getAbsPoint();
                    Orient orientAC = ((Actor) (ownaircraft)).pos.getAbsOrient();                   
                    List list = Engine.targets();
                    int i = list.size();                   
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                        {                        	                      	
                        	Vector3d vector3d = new Vector3d();                       	
                        	vector3d.set(pointAC);                       	
                        	Point3d pointOrtho = new Point3d();
                            pointOrtho.set(actor.pos.getAbsPoint());
                            pointOrtho.sub(pointAC);
                            orientAC.transformInv(pointOrtho);                           
                        	//HUD.log(AircraftHotKeys.hudLogWeaponId, "target heading" + HDG);
                            float f = Mission.cur().curCloudsType();                                                       
                            if(((Tuple3d) (pointOrtho)).x > h - 500D && ((Tuple3d) (pointOrtho)).x < h + 500D && ((Tuple3d) (pointOrtho)).y < v/(30D/((Tuple3d) (pointOrtho)).x) + 500D && ((Tuple3d) (pointOrtho)).y > v/(30D/((Tuple3d) (pointOrtho)).x) - 500D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.36397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.36397023426))
                            {
                            	radarPlane.add(pointOrtho);
                            	victim.add(actor);
                            }	
                        }
                    }
                 i = victim.size();
                 if(i>0)
                 {	
                    for(int j = 0; j < i; j++)
                    {	
                        Actor avictim = (Actor)victim.get(j);
                        Vector3d tarvec = new Vector3d();
                        tarvec.set(avictim.pos.getAbsPoint());
                        Point3d target = new Point3d();
                        target.set(avictim.pos.getAbsPoint());                            
                    	mach = ((float)avictim.getSpeed(tarvec)*3.6F/getMachForAlt((float)((Tuple3d) (target)).z))*1.621371F;
                    	alt = ((float)((Tuple3d) (target)).z*3.28084F)/10000F;
                    	dif = ((this.fm.getAltitude() - (float)((Tuple3d) (target)).z)*3.28084F)/1000F;
                    	Orient tgt = ((Actor) (avictim)).pos.getAbsOrient();
                    	Orient orient = ((Actor) (ownaircraft)).pos.getAbsOrient();
                    	HDG = (normalizeDegree(tgt.getAzimut() - 270F))/100F;
                    	brg = normalizeDegree(-normalizeDegree(setNew.azimuth.getDeg(r) + 90F) + (normalizeDegree(tgt.getAzimut() - 270F)));                    	
                    }
                }
                int i1 = radarPlane.size();
                if(i1>0)
                {	
                int nt = 0;
                int k = 0;               
                float fm = (float)(int)mach * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_1", 0.0F, 0.0F, fm);
    			float f2 = (float)((int)(mach * 10F) % 10) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_2", 0.0F, 0.0F, f2 - fm);
    			fm = (float)(int)alt * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_1", 0.0F, 0.0F, fm);
    			f2 = (float)((int)(alt * 10F) % 10) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_2", 0.0F, 0.0F, f2 - fm);
    			fm = (float)(int)dif * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, fm);
    			f2 = (float)((int)(dif * 10F) % 10) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_2", 0.0F, 0.0F, f2 - fm);
    			fm = (float)(int)HDG * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_1", 0.0F, 0.0F, fm);
    			f2 = (float)((int)(HDG * 10F) % 10) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_2", 0.0F, 0.0F, f2 - fm);
    			float f3 = (float)((int)(HDG * 100F) % 10) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_3", 0.0F, 0.0F, f3 - fm);   			
    			super.mesh.chunkSetAngles("Z_Z_Radarbrg", 0.0F, brg, 0.0F);
    			if(dif>0)
    			{
    				super.mesh.chunkVisible("Z_Z_dif+", false);
    				super.mesh.chunkVisible("Z_Z_dif-", true);
    			} else
    			{
    				super.mesh.chunkVisible("Z_Z_dif+", true);
    				super.mesh.chunkVisible("Z_Z_dif-", false);
    			}
                	double x1 = ((Tuple3d) ((Point3d)radarPlane.get(k))).x;                   
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 30D / x1; 
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getRoll())) * 0.011F);
                        float f1 = FOrigY + (float)(NewY * ScY) * ((F_18) aircraft()).radarrange;
                        ((F_18)aircraft()).v = f;
                        ((F_18)aircraft()).h = f1;
                        if(f1 < 0)
                        	f1 = 0;                      
                        String m = "Z_Z_lockgate";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 30D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                        double NewX = -((Tuple3d) ((Point3d)radarPlane.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarPlane.get(k))).x; //distance
                        float f = FOrigX + (float)(NewX * ScX) - ((float)Math.sin(Math.toRadians(((FlightModelMain) (super.fm)).Or.getRoll())) * 0.011F); //FOrigX currently do nothing
                        float f1 = FOrigY + (float)(NewY * ScY) * ((F_18) aircraft()).radarrange;
                        if(f1 < 0)
                        	f1 = 0;                       
                        String m = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(m);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f;
                        Cockpit.xyz[2] = f1;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f>=0.18F || f<=-0.18F || f1>0.36F)
                        	super.mesh.chunkVisible(m, false);
                        else
                        if(!super.mesh.isChunkVisible(m))
                            super.mesh.chunkVisible(m, true);
                        
                    }            
                } else              
                {	// hide everything when there's no enemy
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                	if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                	if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                	for(int j = 1; j < 3; j++)
                    super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j, false);
                	for(int j = 1; j < 3; j++)
                    super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j, false);
                	for(int j = 1; j < 3; j++)
                    super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j, false);
                	for(int j = 1; j < 4; j++)
                    super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j, false);
                	super.mesh.chunkVisible("Z_Z_dif+", false);
    				super.mesh.chunkVisible("Z_Z_dif-", false);
    				super.mesh.chunkVisible("Z_Z_Radarbrg", false);
                    ((F_18)aircraft()).lockmode = 0;                  
                } 
            } 
                
        }    	
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }	
	
	protected float offset;
	protected int targetnum;
	

    protected void moveControls(float f)
    {
        super.mesh.chunkSetAngles("Z_Z_Stick", 0.0F, ((FlightModelMain) (super.fm)).CT.AileronControl * 10F, ((FlightModelMain) (super.fm)).CT.ElevatorControl * 10F);
        super.mesh.chunkSetAngles("Z_Z_Throttle1", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        super.mesh.chunkSetAngles("Z_Z_Throttle2", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * -0.07F;
        super.mesh.chunkSetLocate("PedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * 0.07F;
        super.mesh.chunkSetLocate("PedalR", Cockpit.xyz, Cockpit.ypr);
    }
    
    protected void backupGauges(float f)//TODO Gauges
    {
    	float f1 = (this.fm.getAltitude()*3.28084F);
        float f2 = cvt(f1, 0.0F, 50000F, 0.0F, 18000F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt", 0.0F, f2, 0.0F);
        f1 = (this.fm.getAltitude()*3.28084F)/10000F;
        f2 = (float)(int)f1 * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 10F) % 10) * 36F;      
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_2", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 100F) % 10) * 36F;       
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_3", 0.0F, 0.0F, f2);
    	float f4 = 0.0F;
        float f6 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        f6 *= 0.53996F;
        if(f6 <= 50F)
            f4 = (f6 / 50F) * 15F;
        else
        if(f6 <= 100F)
            f4 = (f6 - 50F) + 15F;
        else
        if(f6 <= 150F)
            f4 = (f6 - 100F) * 1.2F + 65F;
        else
        if(f6 <= 200F)
            f4 = (f6 - 150F) * 0.9F + 125F;
        else
        if(f6 <= 300F)
            f4 = (f6 - 200F) * 0.6F + 170F;
        else
        if(f6 <= 400F)
            f4 = (f6 - 300F) * 0.45F + 230F;
        else
        if(f6 <= 700F)
            f4 = (f6 - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Speed", 0.0F, f4, 0.0F);
        float f7 = setNew.vspeed2 * 3.48F;
        f7 *= 60F;
        float f8 = Math.abs(f7);
        boolean flag = f7 < 0.0F;
        if(f8 <= 1000F)
            f2 = f8 * 0.07F;
        else
        if(f8 <= 2000F)
            f2 = (f8 - 1000F) * 0.035F + 70F;
        else
        if(f8 <= 4000F)
            f2 = (f8 - 2000F) * 0.0175F + 105F;
        else
        if(f8 <= 6000F)
            f2 = (f8 - 4000F) * 0.00875F + 140F;
        else
            f2 = 157.5F;
        if(flag)
            f2 = -f2;
        super.mesh.chunkSetAngles("Z_Z_Ins_VSpeed", 0.0F, f2, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_AH", 0.0F, setNew.bank, setNew.pitch);
        float f3 = setNew.vspeed2 * 3.48F;
        f3 *= 60F;
        if(f3 > 2000F)
        {
            f3 = 2000F;
        }
        if(f3 < -2000F)
        {
            f3 = -2000F;
        }
        f3 /= 1000F;
        Cockpit.xyz[0] = f3 * 0.00001F;
        super.mesh.chunkSetLocate("Z_Z_Ins_Climb", Cockpit.xyz, Cockpit.ypr);
        f3 = fm.getAOS();
        Cockpit.xyz[1] = f3 * 0.00001F;
        super.mesh.chunkSetLocate("Z_Z_Ins_Bank", Cockpit.xyz, Cockpit.ypr);
        f2 = (((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Ins_Fuel_1", false);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Ins_Fuel_2", false);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Ins_Fuel_3", false);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_3", 0.0F, 0.0F, f5);       
        float bingo = ((F_18)aircraft()).Bingofuel/1000F;
        f3 = (float)(int)bingo * 36F;        	
        super.mesh.chunkSetAngles("Z_Z_Ins_Bingo_1", 0.0F, 0.0F, f3);
        float fuelinteltempL = ((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut;
    	float fuelinteltempR = ((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut;
    	f3 = (float)((int)(fuelinteltempL/1000F * 10F) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_Temp_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(fuelinteltempL/1000F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_2", 0.0F, 0.0F, f4);
    	float N1L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F)/10F;
    	float N1R = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F)/10F;
    	f3 = (float)((int)(N1L * 10F) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_RPM_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(N1L * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_2", 0.0F, 0.0F, f4);
    	float FFL = this.fm.EI.engines[0].tmpF;
    	float FFR = this.fm.EI.engines[0].tmpF;
    	f3 = (float)((int)(FFL*10F * 10F) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_FF_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(FFL*10F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_2", 0.0F, 0.0F, f4);
    	float extempL = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 0F, 120F);
    	float extempR = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 0F, 120F);
    	super.mesh.chunkSetAngles("Z_Z_Ins_EGT_1", -extempL, 0.0F, 0.0F);
    	super.mesh.chunkSetAngles("Z_Z_Ins_EGT_2", extempL, 0.0F, 0.0F);
    	float oilpressL = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
    	float oilpressR = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
    	f3 = (float)(int)oilpressL/10F * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_Oil_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(oilpressL/10F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_1", 0.0F, 0.0F, f4);
        //HUD.log(AircraftHotKeys.hudLogWeaponId, "Temp " + extempL + "FF " + FFL);
    }

    protected void moveGauge(float f)
    {
    	super.mesh.chunkSetAngles("Gauge_ADI1", setNew.bank, 0.0F, setNew.pitch);
        super.mesh.chunkSetAngles("Gauge_StdByADI1", setNew.bank, 0.0F, setNew.pitch);
        super.mesh.chunkSetAngles("GaugeMove_RPM_L", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_RPM_R", floatindex(cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 300F) / 100F, rpmScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_TGT_L", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_TGT_R", cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 200F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FF_L", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_FF_R", cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 3828F, 0.0F, 330F), 0.0F, 0.0F);
        float f1 = interp(setNew.altimeter, setOld.altimeter, f);
        float f2 = cvt(f1, 0.0F, 30480F, 0.0F, 36000F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_N", f2, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("GaugeMove_ALT_3", 0.0F, 0.0F, -f2);
        f2 = cvt(f1, 0.0F, 30480F, 0.0F, 3600F);
        float f3 = (float)(int)(f2 / 36F) * 36F;
        if(f2 - f3 > 34.2F)
            f3 += (1.0F - ((f3 + 36F) - f2) / 1.8F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_4", 0.0F, 0.0F, -f3);
        f2 = cvt(f1, 0.0F, 30480F, 0.0F, 360F);
        f3 = (float)(int)(f2 / 36F) * 36F;
        if(f2 - f3 > 35.82F)
            f3 += (1.0F - ((f3 + 36F) - f2) / 0.18F) * 36F;
        super.mesh.chunkSetAngles("GaugeMove_ALT_5", 0.0F, 0.0F, -f3);
        f2 = setNew.azimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_HDG", -f2, 0.0F, 0.0F);
        f2 = setNew.waypointAzimuth.getDeg(f) + 90F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_CRS", f2, 0.0F, 0.0F);
        f2 = setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F + 180F;
        super.mesh.chunkSetAngles("GaugeMove_HSI_TACAN", f2, 0.0F, 0.0F);
        resetYPRmodifier();
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
        {
            f2 = setNew.ilsLoc;
            Cockpit.xyz[0] = -f2 * 0.001F;
        } else
        if(((FlightModelMain) (super.fm)).AS.listenNDBeacon || ((FlightModelMain) (super.fm)).AS.listenRadioStation)
        {
            f2 = setNew.navDiviation0;
            Cockpit.xyz[0] = -f2 * 0.002F;
        }
        if(Cockpit.xyz[0] < -0.02F)
            Cockpit.xyz[0] = -0.02F;
        else
        if(Cockpit.xyz[0] > 0.02F)
            Cockpit.xyz[0] = 0.02F;
        super.mesh.chunkSetLocate("GaugeMove_HSI_DIV", Cockpit.xyz, Cockpit.ypr);
        float f4 = 0.0F;
        float f5 = calculateMach();
        f4 = f5 * 180F;
        super.mesh.chunkSetAngles("GaugeMove_SPD_MACH", f4, 0.0F, 0.0F);
        f4 = 0.0F;
        float f6 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        f6 *= 0.53996F;
        if(f6 <= 50F)
            f4 = (f6 / 50F) * 15F;
        else
        if(f6 <= 100F)
            f4 = (f6 - 50F) + 15F;
        else
        if(f6 <= 150F)
            f4 = (f6 - 100F) * 1.2F + 65F;
        else
        if(f6 <= 200F)
            f4 = (f6 - 150F) * 0.9F + 125F;
        else
        if(f6 <= 300F)
            f4 = (f6 - 200F) * 0.6F + 170F;
        else
        if(f6 <= 400F)
            f4 = (f6 - 300F) * 0.45F + 230F;
        else
        if(f6 <= 700F)
            f4 = (f6 - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("GaugeMove_SPD", f4, 0.0F, 0.0F);
        float f7 = setNew.vspeed2 * 3.48F;
        f7 *= 60F;
        float f8 = Math.abs(f7);
        boolean flag = f7 < 0.0F;
        if(f8 <= 1000F)
            f2 = f8 * 0.07F;
        else
        if(f8 <= 2000F)
            f2 = (f8 - 1000F) * 0.035F + 70F;
        else
        if(f8 <= 4000F)
            f2 = (f8 - 2000F) * 0.0175F + 105F;
        else
        if(f8 <= 6000F)
            f2 = (f8 - 4000F) * 0.00875F + 140F;
        else
            f2 = 157.5F;
        if(flag)
            f2 = -f2;
        super.mesh.chunkSetAngles("GaugeMove_VS", f2, 0.0F, 0.0F);
        f2 = super.fm.getAOA() * 9F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        else
        if(f2 > 270F)
            f2 = 270F;
        f2 *= -1F;
        super.mesh.chunkSetAngles("GaugeMove_AOA", f2, 0.0F, 0.0F);
        float f9 = super.fm.getOverload();
        f9--;
        f2 = f9 * 20F;
        super.mesh.chunkSetAngles("GaugeMove_G", f2, 0.0F, 0.0F);
        
        if(((FlightModelMain) (super.fm)).CT.getGear() > 0.0F)
            super.mesh.chunkSetAngles("Z_Z_Gear", 0.0F, 0.0F, 0.0F);
        else
            super.mesh.chunkSetAngles("Z_Z_Gear", 0.0F, 0.0F, -15.5F);
    }
    
    protected void movescreenfuel()//TODO fuel
    {
    	for(int m = 1; m < 6; m++)
    	{
    	for(int n = 1; n <4; n++)
    	{
    		super.mesh.chunkVisible("Z_Z_HDD_Mach1_" + n, false);
    		super.mesh.chunkVisible("Z_Z_HDD_Time1_" + n, false);
    		
    	}    	
    	super.mesh.chunkVisible("Z_Z_HDD_Range_" + m, false);    	    	   	
    	}
    	resetYPRmodifier();
    	Cockpit.xyz[0] = 0.41000F;
        Cockpit.xyz[1] = 0.420F;
        Cockpit.xyz[2] = 0.043F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_Fuel", Cockpit.xyz, Cockpit.ypr);  
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.03000F;
        Cockpit.xyz[1] = 0.390F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_Tank", Cockpit.xyz, Cockpit.ypr); 
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.380F;
        Cockpit.xyz[1] = 0.070F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_TankL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.340F;
        Cockpit.xyz[1] = 0.070F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_TankR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.030F;
        Cockpit.xyz[1] = -0.120F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_Tank4", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.030F;
        Cockpit.xyz[1] = -0.40F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_ETC", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.170F;
        Cockpit.xyz[1] = -0.40F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_ETR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.235F;
        Cockpit.xyz[1] = -0.40F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_ETL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.03F;
        Cockpit.xyz[1] = 0.07F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_FeedR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.03F;
        Cockpit.xyz[1] = 0.207F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_FeedL", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.38000F;
        Cockpit.xyz[1] = 0.410F;
        Cockpit.xyz[2] = 0.043F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_bingo", Cockpit.xyz, Cockpit.ypr);
    	float f3 = 0F;
    	float f2 = (((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F;
        float tankW = 0F;
        if(f2 > 10.115F)
        	tankW = (f2 - 10.115F)/2F;        
        float f4 = (float)((int)(tankW * 10F) % 10) * 36F;
        if(f4 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_TankL_1", false);
        	super.mesh.chunkVisible("Z_Z_HDD_TankR_1", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_TankL_1", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_TankR_1", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(tankW * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_TankL_2", false);
        	super.mesh.chunkVisible("Z_Z_HDD_TankR_2", false);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_TankL_2", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_TankR_2", 0.0F, 0.0F, f5);        
        float f6 = (float)((int)(tankW * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_TankL_3", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_TankR_3", 0.0F, 0.0F, f6);
        float tank4 = 0F;
        if (f2 > 6.030F)
        	tank4 = f2 - 6.030F;
        if (tank4 > 4.085F)
        	tank4 = 4.085F;
        f3 = (float)(int)tank4 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank4_1", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank4_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank4 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank4_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank4_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank4 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank4_3", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank4_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank4 * 1000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank4_4", 0.0F, 0.0F, f6);
        float tank1 = f2 - 3.190F;
        if (f2 > 3.190F)
        	tank1=f2 - 3.190F;
        if (tank1 > 2.840F)
        	tank1 = 2.840F;
        f3 = (float)(int)tank1 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank_1", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank1 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank1 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Tank_3", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank1 * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_Tank_4", 0.0F, 0.0F, f6);                
        float tankL = 0F;
        if(fm.CT.Weapons[9] != null)
        {	
        for(int j = 0; j < fm.CT.Weapons[9].length && j < 2; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
        	tankL = (((F_18)aircraft()).checkfuel(0)/1000)*2.20462262F; 
        } else
        {	
        	tankL = 0F;
        }
        }
        f3 = (float)(int)tankL * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_ETL_1", false);
        	super.mesh.chunkVisible("Z_Z_HDD_ETR_1", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_ETL_1", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_HDD_ETR_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_ETL_2", false);
        	super.mesh.chunkVisible("Z_Z_HDD_ETR_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_ETL_2", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_ETR_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankL* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_ETL_3", false);
        	super.mesh.chunkVisible("Z_Z_HDD_ETR_3", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_ETL_3", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_ETR_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tankL * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_ETL_4", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_ETR_4", 0.0F, 0.0F, f6);
        float feedL = 1.790F;
        if(f2<3.190F)
        	feedL = f2 - 1.400F;
        if(feedL<0F)
        	feedL = 0F;
        f3 = (float)(int)feedL * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_FeedL_1", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedL_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_FeedL_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedL_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedL* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_FeedL_3", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedL_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedL * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedL_4", 0.0F, 0.0F, f6);
        float feedR = 1.400F;
        if(f2<3.190F)
        	feedR = f2 - 1.790F;
        if(feedR<0F)
        	feedR = 0F;
        f3 = (float)(int)feedR * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_FeedR_1", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedR_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedR * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_FeedR_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedR_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedR* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_FeedR_3", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedR_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedR * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_FeedR_4", 0.0F, 0.0F, f6);
        f2 = (((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F + tankL*2F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_1", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_3", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_4", 0.0F, 0.0F, f6);
        if(f2>10F)
        {
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_5", 0.0F, 0.0F, 36F);
        } else
        {
        super.mesh.chunkVisible("Z_Z_HDD_Fuel_5", false);
        }
        float bingo = ((F_18)aircraft()).Bingofuel/1000F;
        f3 = (float)(int)bingo * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_bingo_1", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_bingo_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(bingo * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_bingo_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_bingo_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(bingo* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_bingo_3", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_bingo_3", 0.0F, 0.0F, f5);
        f6 = (float)((int)(bingo * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_bingo_4", 0.0F, 0.0F, f6);
    }
    
    protected void movescreenfuelflow()
    {
    	resetYPRmodifier();
    	Cockpit.xyz[0] = 0.41000F;
        Cockpit.xyz[1] = 0.420F;
        Cockpit.xyz[2] = 0.043F;
        super.mesh.chunkSetLocate("Z_Z_HDD_Fuel", Cockpit.xyz, Cockpit.ypr);
		float tankL = 0F;
		if(fm.CT.Weapons[9] != null)
        {	
        for(int j = 0; j < fm.CT.Weapons[9].length && j < 2; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
        	tankL = (((F_18)aircraft()).checkfuel(0)/1000)*2.20462262F; 
        } else
        {	
        	tankL = 0F;
        }
        }               
		float Sumfuel = (((FlightModelMain) (super.fm)).M.fuel) + tankL*2F;
		float Sfuel = Sumfuel;
		if(Sumfuel > 2500F) Sfuel = Sumfuel - 2000F; else Sfuel = Sumfuel;		
		float flowrate = this.fm.EI.engines[0].tmpF + this.fm.EI.engines[0].tmpF;
		float Duration = (Sfuel/flowrate)/3600;
		float f3 = (float)(int)Duration * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_5", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_5", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(Duration * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_1", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_1", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(Duration * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_2", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(Duration * 1000F) % 10) * 36F;
        if(f5 == 0F && f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_Fuel_3", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(Duration * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_Fuel_4", 0.0F, 0.0F, f7);
        float Currange = Duration * super.fm.getSpeedKMH() * 0.621371192F;        
        float weight = this.fm.M.getFullMass()*9.8F;
        float duspeed = ((float)Math.pow((this.fm.Sq.dragProducedCx/this.fm.Fusel.CxMin_0), 0.25D)) * (float)Math.sqrt((2*weight)/(((F_18)aircraft()).getAirPressure(this.fm.getAltitude()) * this.fm.Sq.squareWing));
        float thrustrequired = ((1/(this.fm.Fusel.K_max)*weight)/10600F)*100F;
        float bestspeed = (float)((thrustrequired * (2.17F * (Math.log(thrustrequired)))) + (27.44F * Math.log(this.fm.getAltitude())));
        float temperature = Atmosphere.temperature(this.fm.HofVmax);
        float density = Atmosphere.density(this.fm.HofVmax);
        float MRalt = this.fm.HofVmax - 0.05F * temperature + 0.02F * density;
        float MRaltspeed = (float)((thrustrequired * (2.17F * (Math.log(thrustrequired)))) + (27.44F * Math.log(MRalt)));
        flowrate = (1/(this.fm.Fusel.K_max)*weight)*0.065F/3600F;
        Duration = (Sfuel/flowrate)/3600;
        Currange = Duration * super.fm.getSpeedKMH() * 0.621371192F;       
        float MDalt = this.fm.HofVmax - 0.06F * temperature + 0.01F * density;
        float MDaltspeed = ((float)Math.pow((this.fm.Sq.dragProducedCx/this.fm.Fusel.CxMin_0), 0.25D)) * (float)Math.sqrt((2*weight)/(((F_18)aircraft()).getAirPressure(MDalt) * this.fm.Sq.squareWing));
        thrustrequired = (this.fm.Fusel.CxMin_0 * (((F_18)aircraft()).getAirPressure(MDalt)/2F) * (float)Math.pow(MDaltspeed, 2D) * this.fm.Sq.squareWing) + (2F * this.fm.Sq.dragProducedCx * weight)/(((F_18)aircraft()).getAirPressure(MDalt) * (float)Math.pow(MDaltspeed, 2D) * this.fm.Sq.squareWing);
        flowrate = (thrustrequired * 0.065F)/3600F;
        float MDaltDuration = (Sfuel/flowrate)/3600;   
    }
    
    protected void movescreenengines()//TODO Engine
    {
    	float inlettempL = Atmosphere.temperature(this.fm.getAltitude());
    	float inlettempR = inlettempL;
    	float fuelinteltempL = ((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut;
    	float fuelinteltempR = ((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut;
    	float N1L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F);
    	float N1R = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F);
    	float N2L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
    	float N2R = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
    	float FFL = this.fm.EI.engines[0].tmpF;
    	float FFR = this.fm.EI.engines[0].tmpF;
    	float extempL = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 92F, 10F, 1838F);
    	float extempR = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 92F, 10F, 1838F);
    	float nozposL = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput() * 100F;
    	float nozposR = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput() * 100F;
    	float oilpressL = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
    	float oilpressR = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
    	float thrustL = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput();
    	float thrustR = ((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput();
    	float shake = (((FlightModelMain) (super.fm)).EI.engines[0].w / ((FlightModelMain) (super.fm)).EI.engines[0].wMax) * ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax * (float)Math.sqrt(this.fm.getSpeed() / 94F);
    	float enginevibL = cvt(shake, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax, 0.0F, 0.21F);
    	shake = (((FlightModelMain) (super.fm)).EI.engines[0].w / ((FlightModelMain) (super.fm)).EI.engines[0].wMax) * ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax * (float)Math.sqrt(this.fm.getSpeed() / 94F);
    	float enginevibR = cvt(shake, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax, 0.0F, 0.21F);
    	float CPRL = ((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure();
    	float CPRR = ((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure();
    	float TDPL = ((FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor();
    	float TDPR = ((FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor();
    }
    
    protected void HUD(float A)//TODO HUD
    {   	
    	boolean flag = false;
        boolean flag1 = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive)
            flag1 = false;
        else
            flag1 = true;
        super.mesh.chunkVisible("Z_Z_HUD_Screen", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_PITCH", flag1);
        for(int j = 1; j < 5; j++)
        super.mesh.chunkVisible("Z_Z_HUD_Speed_" + j, flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA_2", flag1);
        for(int j = 1; j < 5; j++)
        super.mesh.chunkVisible("Z_Z_HUD_VP_" + j, flag1);
        for(int j = 1; j < 5; j++)
        super.mesh.chunkVisible("Z_Z_HUD_Alt_" + j, flag1);
        super.mesh.chunkVisible("Z_Z_HUD_G_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_G_2", flag1);        
        super.mesh.chunkVisible("Z_Z_HUD_HDG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Bank", flag1);
        super.mesh.chunkVisible("Z_Z_RETICLECROSS", flag1);
        if(!flag1)
        {
        	return;
        }   
    	float f10 = calculateMach();
        float f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, f3);       
		float f = (this.fm.getSpeedKMH() * 0.621371F)/1000F;
	    float f1 = (float)(int)f * 36F;
	    if(f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Speed_1", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Speed_1", 0.0F, 0.0F, f1);
		float f2 = (float)((int)(f * 10F) % 10) * 36F;
		if(f2 == 0 && f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Speed_2", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Speed_2", 0.0F, 0.0F, f2);
		f3 = (float)((int)(f * 100F) % 10) * 36F;
		if(f3 == 0 && f2 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Speed_3", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Speed_3", 0.0F, 0.0F, f3);
		f3 = (float)((int)(f * 1000F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Speed_4", 0.0F, 0.0F, f3);
		f = (this.fm.getAltitude()*3.28084F)/10000F;
		f1 = (float)(int)f * 36F;
		if(f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Alt_1", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_1", 0.0F, 0.0F, f1);
		f2 = (float)((int)(f * 10F) % 10) * 36F;
		if(f2 == 0 && f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Alt_2", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_2", 0.0F, 0.0F, f2);
		f3 = (float)((int)(f * 100F) % 10) * 36F;
		if(f3 == 0 && f2 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Alt_3", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_3", 0.0F, 0.0F, f3);
		f2 = (float)((int)(f * 1000F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_4", 0.0F, 0.0F, f2);
		f = setNew.vspeed2 * 3.48F/1000F;
		f1 = (float)(int)f * 36F;
		if(f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_VP_1", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_VP_1", 0.0F, 0.0F, f1);
		f2 = (float)((int)(f * 10F) % 10) * 36F;
		if(f2 == 0 && f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_VP_2", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_VP_2", 0.0F, 0.0F, f2);
		f3 = (float)((int)(f * 100F) % 10) * 36F;
		if(f3 == 0 && f2 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_VP_3", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_VP_3", 0.0F, 0.0F, f3);
		float f4 = (float)((int)(f * 1000F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_VP_4", 0.0F, 0.0F, f4);
		float f8 = super.fm.getOverload();
        f3 = (float)(int)Math.abs(f8) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(Math.abs(f8) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_2", 0.0F, 0.0F, f3);
        float f9 = normalizeDegree(setNew.azimuth.getDeg(A) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);
        f2 = setNew.pitch;
        if(f2 > 90F)
            for(; f2 > 90F; f2 -= 360F);
        else
        if(f2 < -90F)
            for(; f2 < -90F; f2 += 360F);
        f2 -= 90F;
        f2 = -f2;
        int j = (int)f2 / 5;
        for(int k = j - 3; k <= j + 2; k++)
            if(k >= 0 && k < 37)
            {
                super.mesh.chunkVisible("Z_Z_HUD_PITCH", true);
                super.mesh.chunkSetAngles("Z_Z_HUD_PITCH", 0.0F, setNew.bank, -setNew.pitch);
            }
        Cockpit.xyz[0] = 0.0F;
        boolean flag2 = false;
        if(((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1 || f1 > 10F)
        {
            Cockpit.xyz[1] = -setNew.fpmYaw;
            if(Cockpit.xyz[1] < -6.5F)
            {
                Cockpit.xyz[1] = -6.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[1] > 6.5F)
            {
                Cockpit.xyz[1] = 6.5F;
                flag2 = true;
            }
            Cockpit.xyz[2] = -setNew.fpmPitch;
            if(Cockpit.xyz[2] < -11.5F)
            {
                Cockpit.xyz[2] = -11.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[2] > 7F)
            {
                Cockpit.xyz[2] = 7F;
                flag2 = true;
            }
        } else
        {
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        }
        super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag2);
        f3 = normalizeDegree(setNew.bank);
        if(f3 > 50F && f3 < 310F)
        	super.mesh.chunkVisible("Z_Z_HUD_Bank", false); else
        		super.mesh.chunkVisible("Z_Z_HUD_Bank", true);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", 0.0F, f3, 0.0F);
    }

    protected void moveHUD(float f)
    {
        blinkCounter++;
        float f1 = Pitot.Indicator((float)((Tuple3d) (((FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        f1 *= 0.53996F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.001613948F * f1;
        Cockpit.xyz[2] = 0.0F;
        boolean flag = false;
        boolean flag1 = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive)
            flag1 = false;
        else
            flag1 = true;
        for(int i = 0; i < 37; i++)
            super.mesh.chunkVisible(hudPitchRudderStr[i], false);

        super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
        super.mesh.chunkVisible("Z_Z_ALT_1", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_2", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_3", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_4", flag1);
        super.mesh.chunkVisible("Z_Z_ALT_5", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", flag1);
        super.mesh.chunkVisible("Z_Z_G_1", flag1);
        super.mesh.chunkVisible("Z_Z_G_2", flag1);        
        super.mesh.chunkVisible("Z_Z_HUD_HDG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_3", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_Mach_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_SPD", flag1);
        //super.mesh.chunkVisible("Z_Z_HUD_HIDE1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_MISC", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_VS", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_VSBG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOABG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_BANK", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_0", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_2", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DST_3", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DIR", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_GS", flag1);
        super.mesh.chunkVisible("Z_Z_RETICLECROSS", flag1);
        if(!flag1)
            return;
        super.mesh.chunkSetLocate("Z_Z_HUD_SPD", Cockpit.xyz, Cockpit.ypr);
        float f2 = setNew.pitch;
        if(f2 > 90F)
            for(; f2 > 90F; f2 -= 360F);
        else
        if(f2 < -90F)
            for(; f2 < -90F; f2 += 360F);
        f2 -= 90F;
        f2 = -f2;
        int j = (int)f2 / 5;
        for(int k = j - 3; k <= j + 2; k++)
            if(k >= 0 && k < 37)
            {
                super.mesh.chunkVisible(hudPitchRudderStr[k], true);
                super.mesh.chunkSetAngles(hudPitchRudderStr[k], setNew.bank, 0.0F, -setNew.pitch);
            }

        if(((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F)
        {
            super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", false);
        } else
        {
            super.mesh.chunkSetAngles("Z_Z_HUD_PITCHN3", setNew.bank, 0.0F, -setNew.pitch);
            super.mesh.chunkVisible("Z_Z_HUD_PITCHN3", true);
        }
        Cockpit.xyz[0] = 0.0F;
        boolean flag2 = false;
        if(((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1 || f1 > 10F)
        {
            Cockpit.xyz[1] = -setNew.fpmYaw;
            if(Cockpit.xyz[1] < -6.5F)
            {
                Cockpit.xyz[1] = -6.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[1] > 6.5F)
            {
                Cockpit.xyz[1] = 6.5F;
                flag2 = true;
            }
            Cockpit.xyz[2] = -setNew.fpmPitch;
            if(Cockpit.xyz[2] < -11.5F)
            {
                Cockpit.xyz[2] = -11.5F;
                flag2 = true;
            } else
            if(Cockpit.xyz[2] > 7F)
            {
                Cockpit.xyz[2] = 7F;
                flag2 = true;
            }
        } else
        {
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        }
        super.mesh.chunkSetAngles("Z_Z_HUD_FPM", 0.0F, Cockpit.xyz[1], Cockpit.xyz[2]);
        super.mesh.chunkVisible("Z_Z_HUD_FPM_NO", flag2);
        float f3 = normalizeDegree(setNew.bank);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", f3, 0.0F, 0.0F);
        if(f3 > 90F && f3 < 270F)
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", true);
        else
            super.mesh.chunkVisible("Z_Z_HUD_LOWER_ROLL", false);
        float f7 = setNew.altimeter;
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 3600000F);
        f3 = 0.0F;
        super.mesh.chunkSetAngles("Z_Z_ALT_1", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 360000F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_2", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 36000F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_3", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 3600F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_4", 0.0F, 0.0F, f3);
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 1800F);
        if(f3 <= 360F)
        {
            super.mesh.chunkSetAngles("Z_Z_HUD_ALT_CY", 0.0F, 0.0F, f3);
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", true);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_ALT_CY", false);
        }
        f3 = cvt(f7, 0.0F, 30480F, 0.0F, 360F);
        f3 = (float)(int)(f3 / 36F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_ALT_5", 0.0F, 0.0F, f3);
        float f8 = super.fm.getOverload();
        f3 = (float)(int)Math.abs(f8) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(Math.abs(f8) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_G_2", 0.0F, 0.0F, f3);
        float f9 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);       
        f3 = super.fm.getAOA();
        flag = false;
        if(f3 < 0.0F)
        {
            f3 = 0.0F;
            flag = true;
        } else
        if(f3 > 20F)
        {
            f3 = 20F;
            flag = true;
        }
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = f3 * 0.01F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_AOA", Cockpit.xyz, Cockpit.ypr);
        if(flag && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_AOA", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_AOA", true);
        f3 = setNew.vspeed2 * 3.48F;
        f3 *= 60F;
        flag = false;
        if(f3 > 2000F)
        {
            f3 = 2000F;
            flag = true;
        }
        if(f3 < -2000F)
        {
            f3 = -2000F;
            flag = true;
        }
        f3 /= 1000F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = f3 * 0.05F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HUD_VS", Cockpit.xyz, Cockpit.ypr);
        if(flag && blinkCounter % 10 < 5)
            super.mesh.chunkVisible("Z_Z_HUD_VS", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_VS", true);
        float f10 = calculateMach();
        f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_3", 0.0F, 0.0F, f3);
        if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding || ((FlightModelMain) (super.fm)).AS.listenRadioStation || ((FlightModelMain) (super.fm)).AS.listenNDBeacon)
        {
            super.mesh.chunkVisible("Z_Z_HUD_DST_0", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_1", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_2", true);
            super.mesh.chunkVisible("Z_Z_HUD_DST_3", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR", true);
            super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", true);
            float f11;
            if(((FlightModelMain) (super.fm)).AS.listenLorenzBlindLanding)
            {
                f11 = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", true);
                float f4 = setNew.ilsGS;
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = f4 * 0.05F;
                Cockpit.xyz[2] = 0.0F;
                if(Cockpit.xyz[1] < -0.1F)
                    Cockpit.xyz[1] = -0.1F;
                else
                if(Cockpit.xyz[1] > 0.1F)
                    Cockpit.xyz[1] = 0.1F;
                super.mesh.chunkSetLocate("Z_Z_HUD_GS", Cockpit.xyz, Cockpit.ypr);
                f4 = setNew.ilsLoc;
                Cockpit.xyz[0] = -f4 * 0.005F;
            } else
            {
                f11 = getNDBDist();
                super.mesh.chunkVisible("Z_Z_HUD_GS", false);
                float f5 = setNew.navDiviation0;
                Cockpit.xyz[0] = -f5 * 0.01F;
            }
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = 0.0F;
            if(Cockpit.xyz[0] < -0.1F)
                Cockpit.xyz[0] = -0.1F;
            else
            if(Cockpit.xyz[0] > 0.1F)
                Cockpit.xyz[0] = 0.1F;
            float f6 = ((float)(int)f11 / 100F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_3", 0.0F, 0.0F, f6);
            f6 = (float)(((int)f11 % 100) / 10) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_2", 0.0F, 0.0F, f6);
            f6 = (float)(int)(f11 % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_1", 0.0F, 0.0F, f6);
            f6 = ((f11 * 10F) % 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_DST_0", 0.0F, 0.0F, f6);
            super.mesh.chunkSetLocate("Z_Z_HUD_DIR", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_DST_0", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_1", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_2", false);
            super.mesh.chunkVisible("Z_Z_HUD_DST_3", false);
            super.mesh.chunkVisible("Z_Z_HUD_DIR", false);
            super.mesh.chunkVisible("Z_Z_HUD_DIR_BG", false);
            super.mesh.chunkVisible("Z_Z_HUD_GS", false);
        }
    }

    protected void drawSound(float f)
    {       	
    	if(aoaWarnFX != null)
            if(setNew.fpmPitch >= 9.7F && setNew.fpmPitch < 12F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX.isPlaying())
                    aoaWarnFX.play();
            } else
            {
                aoaWarnFX.cancel();
            }
        if(aoaWarnFX2 != null)
            if(setNew.fpmPitch >= 12F && setNew.fpmPitch < 14F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX2.isPlaying())
                    aoaWarnFX2.play();
            } else
            {
                aoaWarnFX2.cancel();
            }
        if(aoaWarnFX3 != null)
            if(setNew.fpmPitch >= 14F && setNew.fpmPitch < 15.5F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX3.isPlaying())
                    aoaWarnFX3.play();
            } else
            {
                aoaWarnFX3.cancel();
            }
        if(aoaWarnFX4 != null)
            if(setNew.fpmPitch >= 15.4F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX4.isPlaying())
                    aoaWarnFX4.play();
            } else
            {
                aoaWarnFX4.cancel();
            }
        if(AltitudeWarn != null)
        {	
        if((((FlightModelMain) (fm)).getAltitude()<550F && ((FlightModelMain) (fm)).getVertSpeed() < -40F) || (((FlightModelMain) (fm)).getAltitude()<100F && ((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F))
        {
        	if((((FlightModelMain) (fm)).Or.getPitch() - 360F) < -22F)
        	{
        			PullupWarn.start();
        			AltitudeWarn.stop();
        	} else
        	{	
        			AltitudeWarn.start();
        			PullupWarn.stop();
        	}
        } else
        {
        	AltitudeWarn.stop();
        	PullupWarn.stop();
        }
        }       
    }
    
    private long tw;

    public float normalizeDegree(float f)
    {
        if(f < 0.0F)
            do
                f += 360F;
            while(f < 0.0F);
        else
        if(f > 360F)
            do
                f -= 360F;
            while(f >= 360F);
        return f;
    }

    public float normalizeDegree180(float f)
    {
        if(f < -180F)
            do
                f += 360F;
            while(f < -180F);
        else
        if(f > 180F)
            do
                f -= 360F;
            while(f > 180F);
        return f;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return super.fm.getSpeedKMH() / getMachForAlt(super.fm.getAltitude());
    }

    public void reflectCockpitState()
    {
    }

    public void toggleDim()
    {
        isDimmer = !isDimmer;
    }

    public void toggleLight()
    {
    	super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
        {
            super.mesh.chunkVisible("Z_Z_NVision", true);
            //light1.light.setEmit(0.0060F, 0.6F);
            //((F_18)aircraft()).FLIR = true;
        } else
        {
            super.mesh.chunkVisible("Z_Z_NVision", false);
            //light1.light.setEmit(0.0F, 0.0F);
            //((F_18)aircraft()).FLIR = false;
        }
    }   

    private void retoggleLight()
    {
    }

    protected float getNDBDist()
    {
        int i = ((FlightModelMain) (super.fm)).AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.sub(actor.pos.getAbsPoint(), ((FlightModelMain) (super.fm)).Loc);
            return (float)(tmpV.length() * 0.0010000000474974513D) / 1.853F;
        }
    }

    protected float getNDBDirection()
    {
        int i = ((FlightModelMain) (super.fm)).AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (super.fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.x = ((Tuple3d) (actor.pos.getAbsPoint())).x;
            tmpV.y = ((Tuple3d) (actor.pos.getAbsPoint())).y;
            tmpV.z = ((Tuple3d) (actor.pos.getAbsPoint())).z + 20D;
            ((Actor) (aircraft())).pos.getAbs(tmpP);
            tmpP.sub(tmpV);
            float f = 57.32484F * (float)Math.atan2(-((Tuple3d) (tmpP)).y, -((Tuple3d) (tmpP)).x);
            return 360F - f;
        }
    }

    public boolean useRealisticNavigationInstruments()
    {
        return World.cur().diffCur.RealisticNavigationInstruments;
    }
    
    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
//        HierMesh hiermesh = aircraft().hierMesh();
//        mesh.chunkVisible("Nose_D0", hiermesh.isChunkVisible("Nose_D0"));
//    	mesh.chunkVisible("Refillrod1", hiermesh.isChunkVisible("Refillrod1"));
//    	mesh.chunkVisible("Refillrod2", hiermesh.isChunkVisible("Refillrod2"));
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private Matrix3d tmpMat;
    private boolean bNeedSetUp;
    private float pictAiler;
    private float pictElev;
    private boolean bU4;
    private boolean isDimmer;
    private int blinkCounter;
    private SoundFX aoaWarnFX;
    private SoundFX aoaWarnFX2;
    private SoundFX aoaWarnFX3;
    private SoundFX aoaWarnFX4;
    private SoundFX PullupWarn;
    private SoundFX AltitudeWarn;
    private String hudPitchRudderStr[];
    private Gun gun[];
    private float alpha;
    protected Polares dragfs;
    private static final float rpmScale[] = {
        0.0F, 190F, 220F, 300F
    };
    private static final float k14TargetMarkScale[] = {
        0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F
    };
    private static final float k14TargetWingspanScale[] = {
        9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F
    };
    private static final float k14BulletDrop[] = {
        5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 
        8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 
        10.603F, 10.704F, 10.739F, 10.782F, 10.789F
    };
    private static final float k14RocketDrop[] = {
        7.812F, 8.168F, 8.508F, 8.978F, 9.24F, 9.576F, 9.849F, 10.108F, 10.473F, 10.699F, 
        10.911F, 11.111F, 11.384F, 11.554F, 11.787F, 11.928F, 11.992F, 12.282F, 12.381F, 12.513F, 
        12.603F, 12.704F, 12.739F, 12.782F, 12.789F
    };
    private long to;    
    float ElevationMaxPositive;
    float ElevationMinNegative;
    public boolean radaron;
    
   







}