// Source File Name:   CockpitF_18C.java

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
        float hsiLoc;

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
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f);
                setOld.waypointAzimuth.setDeg(f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
                if(((FlightModelMain) (fm)).AS.listenLorenzBlindLanding && ((FlightModelMain) (fm)).AS.isILSBL)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    bHSIILS = true;
                    bHSIDL = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                } else if(((FlightModelMain) (fm)).AS.listenTACAN)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSITAC = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITGT = bHSIUHF = false;
                } else if(((FlightModelMain) (fm)).AS.listenNDBeacon || ((FlightModelMain) (fm)).AS.listenYGBeacon)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSIUHF = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = false;
                } else
                {
                    setNew.hsiLoc = 0.0F;
                    setNew.ilsLoc = 0.0F;
                    setNew.ilsGS = 0.0F;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                }
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(0.1F) - 90F);
                bHSINAV = true;
                bHSIDL = bHSIILS = bHSIMAN = bHSITAC = bHSITGT = bHSIUHF = false;
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
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
        return super.waypointAzimuthInvertMinus(30F);
    }
    
    protected String[] HUD1 = null;

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
        bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
        printCompassHeading = true;
        super.cockpitNightMats = (new String[] {
            "Instrument", "Gauges","AH"
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

        ((F_18)aircraft()).bWantBeaconKeys = true; //TODO:Radar parameter
		t2 = 0;
		t3 = 0L;
		FOV = 1.0D;
        ScX = 0.0059999997764825821D;
        ScY = 0.0000045;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 74000F;
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
        RWRA = new ArrayList();
        RWRM = new ArrayList();
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(168F, 83.0F,0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(168F, 83.0F,0.0F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        tw = 0L;
        x = 0F;
        start = false;
        left = false;
    	right = false;
    	becondistance = 0F;
    }
    
    private float testfuel;
    
    private LightPointActor light1;
    private LightPointActor light2;

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
		if(((F_18)aircraft()).Nvision == true)
			super.mesh.chunkVisible("Z_Z_NVision", true); else super.mesh.chunkVisible("Z_Z_NVision", false);	
    	int j = ((F_18)aircraft()).leftscreen;
    	if(j == 0)
    	{
    		movescreenfuel();
    		super.mesh.chunkVisible("HDD_Fuel", true);        	
        	super.mesh.chunkVisible("HDD_FuelFlow", false);
        	super.mesh.chunkVisible("HDD_Engine", false);
    	}
    	if(j == 1)
    	{
    		movescreenfuelflow();
    		super.mesh.chunkVisible("HDD_Fuel", false);        	
        	super.mesh.chunkVisible("HDD_FuelFlow", true);
        	super.mesh.chunkVisible("HDD_Engine", false);
    	}
    	if(j == 2)
    	{
    		movescreenengines();
    		super.mesh.chunkVisible("HDD_Fuel", false);        	
        	super.mesh.chunkVisible("HDD_FuelFlow", false);
        	super.mesh.chunkVisible("HDD_Engine", true);
    	}
    	if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        ((F_18)aircraft()).bWantBeaconKeys = true;
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
        AircraftLH aircraftlh = (AircraftLH)aircraft();
        aircraftlh.printCompassHeading = true;
        resetYPRmodifier();
        moveControls(f);
        HUD(f);
        drawSound(f); 
        Navscreen(f);
        backupGauges(f);
        Warninglight();
        ILS();
        RWR();
    }
    
    private void RWR()
    {
    	if(((F_18)aircraft()).bMissileWarning == true)
    	{
    		super.mesh.chunkVisible("Z_Z_RWR_M", true);
    		resetYPRmodifier();
    		float f = ((F_18)aircraft()).misslebrg;
    		Cockpit.xyz[0] = -(float) Math.sin(Math.toRadians(f)) * 0.01F;
    		Cockpit.xyz[2] = (float) Math.cos(Math.toRadians(f)) * 0.01F;
    		super.mesh.chunkSetLocate("Z_Z_RWR_M", Cockpit.xyz, Cockpit.ypr);
    	} else
    	{
    		super.mesh.chunkVisible("Z_Z_RWR_M", false);
    	}
    	if(((F_18)aircraft()).bRadarWarning == true)
    	{
    		super.mesh.chunkVisible("Z_Z_RWR_U", true);
    		resetYPRmodifier();
    		float f = ((F_18)aircraft()).aircraftbrg;
    		Cockpit.xyz[0] = -(float) Math.sin(Math.toRadians(f)) * 0.02F;
    		Cockpit.xyz[2] = (float) Math.cos(Math.toRadians(f)) * 0.02F;
    		super.mesh.chunkSetLocate("Z_Z_RWR_U", Cockpit.xyz, Cockpit.ypr);
    	} else
    	{
    		super.mesh.chunkVisible("Z_Z_RWR_U", false);
    	}
    }
    
    private void ILS()//TODO ILS
    {
    	boolean flag = false;   	
    	if(((F_18)aircraft()).ILS != true)
    	{	
    		flag = false;
    		super.mesh.chunkVisible("Z_Z_ILS_Hor", false);
    		super.mesh.chunkVisible("Z_Z_ILS_Ver", false);
    		super.mesh.chunkVisible("Z_Z_ILS_Pitch", false);
    		super.mesh.chunkVisible("Z_Z_ILS_AOA", false);
    		return;
    	} else
    	{
    		super.mesh.chunkVisible("Z_Z_ILS_Hor", true);
    		super.mesh.chunkVisible("Z_Z_ILS_Ver", true);
    		super.mesh.chunkVisible("Z_Z_ILS_Pitch", true);
    		super.mesh.chunkVisible("Z_Z_ILS_AOA", true);
    	}
    	resetYPRmodifier();
    	float ilsloctmp = setNew.ilsLoc * setNew.ilsLoc * ((setNew.ilsLoc < 0)? -1F : 1F);
        Cockpit.xyz[0] = -cvt(ilsloctmp, -10000F, 10000F, -0.590F, 0.590F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_ILS_Hor", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float ilsgstmp = setNew.ilsGS * setNew.ilsGS * ((setNew.ilsGS < 0)? -1F : 1F);
        Cockpit.xyz[2] = cvt(ilsgstmp, -0.25F, 0.25F, -0.590F, 0.590F);
        Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
        mesh.chunkSetLocate("Z_Z_ILS_Ver", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_Z_ILS_Hor", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        //mesh.chunkVisible("Z_Z_ILS_Ver", bHSIILS);
        resetYPRmodifier();
        float speed = cvt(((FlightModelMain) (fm)).getSpeedKMH(), -100F, 250F, -0.09F, 0.09F);
        Cockpit.xyz[2] = speed;
        mesh.chunkSetLocate("Z_Z_ILS_Pitch", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[2] = -cvt(((FlightModelMain) (fm)).getAOA(), 6.4F, 8.9F, -0.08F, 0.08F);
        Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
        mesh.chunkSetLocate("Z_Z_ILS_AOA", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
    }
    
    
    
    private void HUDgunsight()//TODO Gunsight
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
			if (i == 1) {
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
	
	private ArrayList RWRA;
	private ArrayList RWRM;
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
            if(Actor.isValid(ownaircraft) && Actor.isAlive(ownaircraft))
            {                                                                        
                int i1 = radarLock.size();
                if(i1>0)
                {	
                int nt = 0;               
                int k = 0;                              
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
                            if(((Tuple3d) (pointOrtho)).x > (double)RClose && ((Tuple3d) (pointOrtho)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (pointOrtho)).y < v + 6000D && ((Tuple3d) (pointOrtho)).y > v - 6000D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.46397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.46397023426))
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
            radarLock.clear();
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
                            if(((Tuple3d) (pointOrtho)).x > h - 500D && ((Tuple3d) (pointOrtho)).x < h + 500D && ((Tuple3d) (pointOrtho)).y < v/(30D/((Tuple3d) (pointOrtho)).x) + 500D && ((Tuple3d) (pointOrtho)).y > v/(30D/((Tuple3d) (pointOrtho)).x) - 500D && (((Tuple3d) (pointOrtho)).z < ((Tuple3d) (pointOrtho)).x * 0.56397023426 && ((Tuple3d) (pointOrtho)).z > -((Tuple3d) (pointOrtho)).x * 0.56397023426))
                            {
                            	radarLock.add(pointOrtho);
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
                int i1 = radarLock.size();
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
    			fm = (float)(int)Math.abs(dif) * 36F;
    			super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, fm);
    			f2 = (float)((int)(Math.abs(dif) * 10F) % 10) * 36F;
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
                	double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;                   
                    if(x1 > (double)RClose && nt <= nTgts)
                    {
                    	FOV = 30D / x1; 
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
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
                        double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                        double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).x; //distance
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
    	super.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, 20F * ((FlightModelMain) (super.fm)).CT.getCockpitDoor());
    	super.mesh.chunkSetAngles("Z_Z_Stick", 0.0F, ((FlightModelMain) (super.fm)).CT.AileronControl * 10F, ((FlightModelMain) (super.fm)).CT.ElevatorControl * 10F);
        super.mesh.chunkSetAngles("Z_Z_Throttle1", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        super.mesh.chunkSetAngles("Z_Z_Throttle2", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * -0.07F;
        super.mesh.chunkSetLocate("PedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = ((FlightModelMain) (super.fm)).CT.getRudder() * 0.07F;
        super.mesh.chunkSetLocate("PedalR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_Z_Gear", 0.0F, 0.0F, cvt(((FlightModelMain) (super.fm)).CT.GearControl,0.0F,1.0F,0.0F,20F));
        float f1 = 0F;
        if(((FlightModelMain) (super.fm)).CT.FlapsControl<0.6F)
        {	
        	f1 = -15F;
        	super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f1);
        }
        if(((FlightModelMain) (super.fm)).CT.FlapsControl>=0.66F && ((FlightModelMain) (super.fm)).CT.FlapsControl<0.75F)
        {	
        	f1 = 0F;
        	super.mesh.chunkSetAngles("Z_Z_Flap", f1, 0.0F, 0.0F);
        }
        if(((FlightModelMain) (super.fm)).CT.FlapsControl>=0.75F)
        {	
        	f1 = 15F;
        	super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f1);
        }
        super.mesh.chunkSetAngles("Z_Z_Hook", 0.0F, 0.0F, (int)((FlightModelMain) (super.fm)).CT.arrestorControl * 20F);
        super.mesh.chunkSetAngles("Z_Z_WingFold",(int)((FlightModelMain) (super.fm)).CT.wingControl * -80F, 0.0F, 0.0F);
    }
    
    protected void backupGauges(float f)//TODO Gauges
    {
    	float f1 = (this.fm.getAltitude()*3.28084F);
        float f2 = cvt(f1, 0.0F, 50000F, 0.0F, 1800F);
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
        float ilsloctmp = setNew.ilsLoc * setNew.ilsLoc * ((setNew.ilsLoc < 0)? -1F : 1F);
        Cockpit.xyz[0] = -cvt(ilsloctmp, -10000F, 10000F, -0.020F, 0.020F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(((FlightModelMain) (fm)).Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Bank", Cockpit.xyz, Cockpit.ypr);
        float ilsgstmp = setNew.ilsGS * setNew.ilsGS * ((setNew.ilsGS < 0)? -1F : 1F);
        Cockpit.xyz[1] = -cvt(ilsgstmp, -0.25F, 0.25F, -0.020F, 0.020F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(((FlightModelMain) (fm)).Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Climb", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_Z_Ins_Bank", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        mesh.chunkVisible("Z_Z_Ins_Climb", bHSIILS);       
        f2 = (((FlightModelMain) (super.fm)).M.fuel/10000F)*2.20462262F;
        float f3 = (float)(int)f2 * 36F;
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
    	float fuelinteltempR = ((FlightModelMain) (super.fm)).EI.engines[1].tWaterOut;
        if(fuelinteltempL < fuelinteltempR)  fuelinteltempL = fuelinteltempR;
    	f3 = (float)((int)(fuelinteltempL/1000F * 10F) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_Temp_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(fuelinteltempL/1000F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_2", 0.0F, 0.0F, f4);
    	float N1L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4090F, 0F, 100F)/10F;
    	float N1R = cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4090F, 0F, 100F)/10F;
        if(N1L < N1R)  N1L = N1R;
    	f3 = (float)((int)(N1L) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_RPM_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(N1L * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_2", 0.0F, 0.0F, f4);
    	float FFL = this.fm.EI.engines[0].tmpF;
    	float FFR = this.fm.EI.engines[1].tmpF;
        if(FFL < FFR)  FFL = FFR;
    	f3 = (float)((int)(FFL*10F * 10F) % 10) * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_FF_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(FFL*10F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_2", 0.0F, 0.0F, f4);
    	float extempL = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 0F, 80F);
    	float extempR = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 0F, 80F);
    	super.mesh.chunkSetAngles("Z_Z_Ins_EGT_1", -extempL, 0.0F, 0.0F);
    	super.mesh.chunkSetAngles("Z_Z_Ins_EGT_2", extempL, 0.0F, 0.0F);
    	float oilpressL = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
    	float oilpressR = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[1].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();
        if(oilpressL < oilpressR)  oilpressL = oilpressR;
    	f3 = (float)(int)oilpressL/10F * 36F;
    	super.mesh.chunkSetAngles("Z_Z_Ins_Oil_1", 0.0F, 0.0F, f3);
    	f4 = (float)((int)(oilpressL/10F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_2", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_Ins_H", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_M", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_S", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        f2 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Compass", f2, 0.0F, 0.0F);
        //HUD.log(AircraftHotKeys.hudLogWeaponId, "Temp " + extempL + "FF " + FFL);
    }   
    
    protected void movescreenfuel()//TODO fuel
    {   	
    	resetYPRmodifier();
    	Cockpit.xyz[0] = 0.046F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.04F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr);//TOTAL 1 
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.037F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr);//TOTAL 2        
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.031F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.007F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr);//L WING
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.042F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.007F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr);//R WING
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0065F;
        Cockpit.xyz[2] = -0.011F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr);//TANK 4 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001F;
        Cockpit.xyz[1] = -0.0065F;
        Cockpit.xyz[2] = -0.011F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr);//TANK 4 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.005F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL CENTER 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.007F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL CENTER 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.021F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL RIGHT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.033F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL RIGHT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.032F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.039F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL LEFT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.020F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.039F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr);//EXTERNAL LEFT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.007F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr);//FEED R 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.002F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.007F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr);//FEED R 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.021F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr);//FEED L 1
        Cockpit.xyz[0] = -0.002F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.021F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr);//FEED L 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.037F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.039F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr);//BINGO 1
        Cockpit.xyz[0] = -0.044F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.039F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr);//BINGO 2
        resetYPRmodifier();
    	Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.04F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr);//TANK 1 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.002F;
        Cockpit.xyz[1] = -0.0062F;
        Cockpit.xyz[2] = 0.04F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr);//TANK 1 2
        float f3 = 0F;
    	float f2 = (((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F;
        float tankW = 0F;
        if(f2 > 10.115F)
        	tankW = (f2 - 10.115F)/2F;        
        float f4 = (float)((int)(tankW * 10F) % 10) * 36F;
        if(f4 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        }	else
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(tankW * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        }	else
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f5);        
        float f6 = (float)((int)(tankW * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", true);
    	super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f6);
        float tank4 = 0F;
        if (f2 > 6.030F)
        	tank4 = f2 - 6.030F;
        if (tank4 > 4.085F)
        	tank4 = 4.085F;
        f3 = (float)(int)tank4 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank4 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank4 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank4 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f6);
        float tank1 = f2 - 3.190F;
        if (f2 > 3.190F)
        	tank1=f2 - 3.190F;
        if (tank1 > 2.840F)
        	tank1 = 2.840F;
        f3 = (float)(int)tank1 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank1 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank1 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tank1 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f6);        
        float tankC = 0F;
        for(int j = 0; j < fm.CT.Weapons[9].length && j < 2; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
            	tankC = (((F_18)aircraft()).checkfuel(0)/1000)*2.20462262F; 
        } else
        {	
            	tankC = 0F;
        }
        f3 = (float)(int)tankC * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankC * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankC* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tankC * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f6);
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        float tankL = 0F;
        for(int j = 2; j < fm.CT.Weapons[9].length && j < 7; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
        	tankL = (((F_18)aircraft()).checkfuel(1)/1000)*2.20462262F; 
        } else
        {	
        	tankL = 0F;
        }
        f3 = (float)(int)tankL * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        }	else
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        }	else
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankL* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", false);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", false);
        }	else
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f5);
        f6 = (float)((int)(tankL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
    	super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f6);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f6);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false);
    	super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false);
    	super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
    	super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
        float feedL = 1.790F;
        if(f2<3.190F)
        	feedL = f2 - 1.400F;
        if(feedL<0F)
        	feedL = 0F;
        f3 = (float)(int)feedL * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false);	else super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedL * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false);	else super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedL* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false);	else super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f6);
        float feedR = 1.400F;
        if(f2<3.190F)
        	feedR = f2 - 1.790F;
        if(feedR<0F)
        	feedR = 0F;
        f3 = (float)(int)feedR * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false); else	super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedR * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);	else	super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedR* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false);	else	super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(feedR * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f6);
        f2 = ((((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F + tankL*2F + tankC)/10F;
        totalfuel = f2;
        timefuel = Time.current();
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f7);
        
        float bingo = ((F_18)aircraft()).Bingofuel/1000F;
        f3 = (float)(int)bingo * 36F;
        if(f3 == 0F)
        {
        	super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(bingo * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(bingo* 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        {	
        	super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        }	
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(bingo * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f6);       
    }
    
    private float totalfuel;
    private long timefuel;
    
    protected void movescreenfuelflow()//TODO fuel flow
    {
    	resetYPRmodifier();
    	Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr);//BINGO RANGE 1 
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr);//BINGO RANGE 2        
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.025F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr);//BINGO DURATION 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.033F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr);//BINGO DURATION 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.007F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr);//BEST M RANGE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr);//BEST M RANGE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.025F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr);//BEST M ENDURANCE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.033F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr);//BEST M ENDURANCE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.025F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.004F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr);//TIME 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.017F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.004F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr);//TIME 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.001F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.004F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr);//FUEL REMAIN 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.007F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.004F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr);//FUEL REMAIN 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.013F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.015F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr);//BASIC 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.005F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.015F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr);//BASIC 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.013F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.022F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr);//FUEL 1
        Cockpit.xyz[0] = 0.005F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.022F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr);//FUEL 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.013F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.027F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr);//STORE 1
        Cockpit.xyz[0] = 0.005F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.027F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr);//STORE 2
        resetYPRmodifier();
    	Cockpit.xyz[0] = 0.013F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.037F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr);//TOTAL 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.005F;
        Cockpit.xyz[1] = -0.007F;
        Cockpit.xyz[2] = -0.037F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr);//TOTAL 2
        float tankL = 0F;
        for(int j = 2; j < fm.CT.Weapons[9].length && j < 7; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
        	tankL = (((F_18)aircraft()).checkfuel(1)/1000)*2.20462262F; 
        } else
        {	
        	tankL = 0F;
        }
        float tankC = 0F;
        for(int j = 0; j < fm.CT.Weapons[9].length && j < 2; j++)
        if(fm.CT.Weapons[9][j].haveBullets())
        {
            	tankC = (((F_18)aircraft()).checkfuel(0)/1000)*2.20462262F; 
        } else
        {	
            	tankC = 0F;
        }               
		float Sumfuel = ((((FlightModelMain) (super.fm)).M.fuel/1000F)*2.20462262F + tankL*2F + tankC)/10F;//FUEL REMAIN
		float Sfuel = Sumfuel*10000F/2.20462262F;
		float flowrate = (totalfuel*10000F/2.20462262F - Sfuel)/((Time.current() - timefuel)/1000L);
		float distance = becondistance/1000F;		
		float Duration = (Sfuel/flowrate)/36000F;
		float Currange = Duration * 10F * super.fm.getSpeedKMH();
		float rangeremain = Currange - distance;
		if(rangeremain<0)
			rangeremain = 0;
		Duration = rangeremain/(super.fm.getSpeedKMH()*10F);
		Sumfuel = Duration * flowrate * 36000F;
		float f2 = Sumfuel/10000F*2.20462262F;		
		float f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f7);
        float clock = (distance / super.fm.getSpeedKMH());//Clock
        //HUD.log(AircraftHotKeys.hudLogWeaponId, "ETA " + clock + "range " + Currange + "distance " + distance);
        f2 = clock;
        if(clock<0F)
        	clock = 0F;
        f3 = (float)(int)f2 * 36F;
		super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(f2 * 100F) % 10) * 0.6F;
        f5 = (float)(int)f4 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(f2 * 1000F) % 10) * 0.6F;
        f6 = (float)(int)f5 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f6);
        float bingo = ((F_18)aircraft()).Bingofuel/2.20462262F;
        Duration = ((Sfuel - bingo)/flowrate)/36000F;
		if(Duration<0F)
			Duration = 0F;
		f2 = Duration;
		f3 = (float)(int)f2 * 36F;
		super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(f2 * 100F) % 10) * 0.6F;
        f5 = (float)(int)f4 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(f2 * 1000F) % 10) * 0.6F;
        f6 = (float)(int)f5 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f6);        
        Currange = Duration * 10F * super.fm.getSpeedKMH() * 0.621371192F;//Bingo Range
        f2 = Currange/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f7);                
        float duspeed = 700F * cvt(((FlightModelMain) (super.fm)).getAltitude(), 0F, 10000F, 1.0F, 1.3F);
        float thrustCo = cvt(((FlightModelMain) (super.fm)).getAltitude(), 0F, 10000F, 1.0F, 0.7F);
        float duthrust = cvt(duspeed, 600F, 800F, 0.35F, 0.50F)*thrustCo;        
        float fuelflowvalue = cvt(duthrust, 0.0F , 1.00F, 0.09F, 1.20F);
        //HUD.log(AircraftHotKeys.hudLogWeaponId," " + fuelflowvalue);
        Duration = (Sfuel - bingo)/fuelflowvalue/36000;//best mach duration
        f2 = Duration;
        f3 = (float)(int)f2 * 36F;
		super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f4);
        f4 = (float)((int)(f2 * 100F) % 10) * 0.6F;
        f5 = (float)(int)f4 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f5);
        f5 = (float)((int)(f2 * 1000F) % 10) * 0.6F;
        f6 = (float)(int)f5 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f6);
        Currange = Duration * 10F * duspeed * 0.621371192F;//best mach range
        f2 = Currange/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f7);
        
        float basic = ((FlightModelMain) (super.fm)).M.massEmpty;
        f2 = basic*2.20462262F/10000F;//basic
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f7);
        float fuel = ((FlightModelMain) (super.fm)).M.fuel;//fuel
        f2 = fuel*2.20462262F/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;        
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f7);
        float store = ((FlightModelMain) (super.fm)).M.mass - basic - fuel;//store
        f2 = store*2.20462262F/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F; 
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f7);
        float total = basic + fuel + store;//total
        f2 = total*2.20462262F/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_4", 0.0F, 0.0F, f7);
    }
    
    protected void movescreenengines()//TODO Engine
    {
    	resetYPRmodifier();
    	Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr);//Inlet Temp 1 
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.03F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr);//Inlet Temp 2        
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr);//N1 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.025F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr);//N1 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr);//N2 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr);//N2 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.015F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr);//EGT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0063F;
        Cockpit.xyz[2] = 0.015F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr);//EGT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.010F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr);//FF 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.010F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr);//FF 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.005F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr);//NOZ POS 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.005F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr);//NOZ POS 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.00F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr);//OIL PRES 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0064F;
        Cockpit.xyz[2] = 0.00F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr);//OIL PRES 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.038F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.005F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr);//THRUST 1
        Cockpit.xyz[0] = 0.030F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.005F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr);//THRUST 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.030F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.005F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr);//THRUST2 1
        Cockpit.xyz[0] = -0.038F;
        Cockpit.xyz[1] = -0.0067F;
        Cockpit.xyz[2] = -0.005F; 
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr);//THRUST2 2
        resetYPRmodifier();
    	Cockpit.xyz[0] = 0.034F;
        Cockpit.xyz[1] = -0.0066F;
        Cockpit.xyz[2] = -0.008F;      
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr);//FAN VIB 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.034F;
        Cockpit.xyz[1] = -0.0066F;
        Cockpit.xyz[2] = -0.008F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr);//FAN VIB 2
    	float inlettempL = Atmosphere.temperature(this.fm.getAltitude());
    	float inlettempR = inlettempL;
    	float f2 = inlettempL/10F;
		float f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4); 
    	float fuelinteltempL = ((FlightModelMain) (super.fm)).EI.engines[0].tWaterOut;   	 
    	float fuelinteltempR = ((FlightModelMain) (super.fm)).EI.engines[1].tWaterOut;
    	float N1L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM(), 0.0F, 4080F, 0F, 100F);
    	f2 = N1L/10F;
    	if(f2>9.9F)
    		f2 = 9.0F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f4);
    	float N1R = cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM(), 0.0F, 4080F, 0F, 100F);
    	f2 = N1R/10F;
    	if(f2>9.9F)
    		f2 = 9.0F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
    	float N2L = cvt(((FlightModelMain) (super.fm)).EI.engines[0].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
    	f2 = N2L/10F;
    	if(f2>9.9F)
    		f2 = 9.0F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f4);
    	float N2R = cvt(((FlightModelMain) (super.fm)).EI.engines[1].getRPM() * 0.85294117647058823529411764705882F, 0.0F, 3480F, 0F, 100F);
    	f2 = N2R/10F;
    	if(f2>9.9F)
    		f2 = 9.0F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f4);    	
    	float extempL = cvt(((FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0F, 120F, 10F, 838F);
    	f2 = extempL/100F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f5);
    	float extempR = cvt(((FlightModelMain) (super.fm)).EI.engines[1].tOilOut, 0F, 120F, 10F, 838F);
    	f2 = extempR/100F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f5);
    	float FFL = this.fm.EI.engines[0].tmpF;
    	f2 = FFL*100F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f5);
    	float FFR = this.fm.EI.engines[1].tmpF;
    	f2 = FFR * 100F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F && f5 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f5);
    	f2 = -((F_18) aircraft()).fNozzleOpenL * 1.1F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f4);
    	f2 = -((F_18) aircraft()).fNozzleOpenR * 1.1F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f4);
    	float oilpressL = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[0].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[0].getReadyness();  	
    	f2 = oilpressL;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f4);
    	float oilpressR = 1.0F + 0.05F * ((FlightModelMain) (super.fm)).EI.engines[1].tOilOut * ((FlightModelMain) (super.fm)).EI.engines[1].getReadyness();
    	f2 = oilpressR;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
    	float thrustL = (((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput() < 1.0F ? cvt(((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(),0F,1.0F,0F,4800F) : cvt(((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(),1.00F,1.10F,4800F,7950F));
    	f2 = thrustL/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f7);
    	float thrustR = (((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput() < 1.00F ? cvt(((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(),0F,1.0F,0F,4800F) : cvt(((FlightModelMain) (super.fm)).EI.engines[0].getPowerOutput(),1.00F,1.10F,4800F,7950F));
    	f2 = thrustR/10000F;
		f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f5);
        f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f6);
        f7 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f7);
    	float shake = (((FlightModelMain) (super.fm)).EI.engines[0].w / ((FlightModelMain) (super.fm)).EI.engines[0].wMax) * ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax * (float)Math.sqrt(this.fm.getSpeed() / 94F);
    	float enginevibL = cvt(shake, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[0].thrustMax, 0.05F, 0.21F);    	
    	f2 = enginevibL * 10F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
    	shake = (((FlightModelMain) (super.fm)).EI.engines[1].w / ((FlightModelMain) (super.fm)).EI.engines[1].wMax) * ((FlightModelMain) (super.fm)).EI.engines[1].thrustMax * (float)Math.sqrt(this.fm.getSpeed() / 94F);
    	float enginevibR = cvt(shake, 0.0F, ((FlightModelMain) (super.fm)).EI.engines[1].thrustMax, 0.05F, 0.21F);
    	f2 = enginevibR * 10F;
    	f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false); else super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
    	float CPRL = ((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure();
    	float CPRR = ((FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure();
    	float TDPL = ((FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor();
    	float TDPR = ((FlightModelMain) (super.fm)).EI.engines[0].getControlCompressor();
    }
    
    protected void Navscreen(float A)
    {
    	float f = -normalizeDegree(setNew.azimuth.getDeg(A) + 90F);
        super.mesh.chunkSetAngles("HDD_Nav_Comp", 0.0F, f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_N", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_W", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_E", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_S", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_3", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_6", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_12", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_15", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_21", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_24", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_30", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_33", 0.0F, -f, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_CRS", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
        resetYPRmodifier();
        float hsiloctmp = setNew.hsiLoc * setNew.hsiLoc * ((setNew.hsiLoc < 0)? -1F : 1F);
        Cockpit.xyz[1] = cvt(hsiloctmp, -20000F, 20000F, -0.036F, 0.036F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("HDD_Nav_DIV", Cockpit.xyz, Cockpit.ypr);
        if(useRealisticNavigationInstruments())
        {
        	super.mesh.chunkSetAngles("HDD_Nav_Tacan", setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);           
        }       
        resetYPRmodifier();
        float BeaconDistanceInNM = getBeaconDistance();       
        if(!useRealisticNavigationInstruments())
        {
            WayPoint waypoint = ((FlightModelMain) (fm)).AP.way.curr();
            if(waypoint != null)
            {
                Point3d P1 = new Point3d();
                Vector3d V = new Vector3d();
                waypoint.getP(P1);
                V.sub(P1, ((FlightModelMain) (fm)).Loc);
                BeaconDistanceInNM = (float)Math.sqrt(V.x * V.x + V.y * V.y);
            }
        }
        becondistance = BeaconDistanceInNM;
        float f2 = (BeaconDistanceInNM / 1852) / 1000F;		
		float f3 = (float)(int)f2 * 36F;
        if(f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Nav_SDT_1", false); else super.mesh.chunkVisible("Z_Z_Nav_SDT_1", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_1", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Nav_SDT_2", false); else super.mesh.chunkVisible("Z_Z_Nav_SDT_2", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_2", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F)
        	super.mesh.chunkVisible("Z_Z_Nav_SDT_3", false); else super.mesh.chunkVisible("Z_Z_Nav_SDT_3", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_3", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f5 == 0F && f4 == 0F && f3 == 0F && f6 == 0F)
        	super.mesh.chunkVisible("Z_Z_Nav_SDT_4", false); else super.mesh.chunkVisible("Z_Z_Nav_SDT_4", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_4", 0.0F, 0.0F, f6);    
    }
    
    private float becondistance;
    
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
        for(int j = 1; j < 6; j++)
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
		if(f3 == 0 && f2 == 0 && f1 == 0)
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
		if(f3 == 0 && f2 == 0 && f1 == 0)
	    	super.mesh.chunkVisible("Z_Z_HUD_Alt_3", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_3", 0.0F, 0.0F, f3);
		float f4 = (float)((int)(f * 1000F) % 10) * 36F;
		if(f4 == 0 && f3 == 0 && f2 == 0 && f1 == 0)
			super.mesh.chunkVisible("Z_Z_HUD_Alt_4", false);
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_4", 0.0F, 0.0F, f2);
		float f5 = (float)((int)(f * 10000F) % 10) * 36F;
		super.mesh.chunkSetAngles("Z_Z_HUD_Alt_5", 0.0F, 0.0F, f2);
		f = Math.abs(setNew.vspeed2 * 3.48F/1000F);
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
		f4 = (float)((int)(f * 1000F) % 10) * 36F;
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
        f10 = setNew.fpmPitch/10;
        f3 = (float)(int)f10 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_1", 0.0F, 0.0F, f3);
        f3 = (float)((int)(f10 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_2", 0.0F, 0.0F, f3); 
    }   

    protected void drawSound(float f)//TODO SOUND
    {       	
    	boolean flag = false;
    	if(aoaWarnFX != null)
            if(setNew.fpmPitch >= 9.7F && setNew.fpmPitch < 12F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX.isPlaying())
                    aoaWarnFX.play();
                flag = true;
            } else
            {
                aoaWarnFX.cancel();
                flag = false;
            }
        if(aoaWarnFX2 != null)
            if(setNew.fpmPitch >= 12F && setNew.fpmPitch < 14F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX2.isPlaying())
                    aoaWarnFX2.play();
                flag = true;
            } else
            {
                aoaWarnFX2.cancel();
                flag = false;
            }
        if(aoaWarnFX3 != null)
            if(setNew.fpmPitch >= 14F && setNew.fpmPitch < 15.5F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX3.isPlaying())
                    aoaWarnFX3.play();
                flag = true;
            } else
            {
                aoaWarnFX3.cancel();
                flag = false;
            }
        if(aoaWarnFX4 != null)
            if(setNew.fpmPitch >= 15.4F && ((FlightModelMain) (super.fm)).Gears.nOfGearsOnGr < 1)
            {
                if(!aoaWarnFX4.isPlaying())
                    aoaWarnFX4.play();
                flag = true;
            } else
            {
                aoaWarnFX4.cancel();
                flag = false;
            }
        if(AltitudeWarn != null)
        {	
        float radioaltnow = ((FlightModelMain) (fm)).getAltitude() - (float)Engine.land().HQ_Air(((FlightModelMain) (fm)).Loc.x, ((FlightModelMain) (fm)).Loc.y);
        radioaltnow = radioaltnow / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getKren()))) / (float)Math.cos(Math.toRadians(Math.abs(((FlightModelMain) (fm)).Or.getTangage())));	
        if((radioaltnow<550F && ((FlightModelMain) (fm)).getVertSpeed() < -40F) || (radioaltnow<100F && ((FlightModelMain) (super.fm)).CT.getGear() < 0.999999F))
        {
        	if((((FlightModelMain) (fm)).Or.getPitch() - 360F) < -22F)
        	{
        			PullupWarn.start();
        			AltitudeWarn.stop();
        			flag = true;
        	} else
        	{	
        			AltitudeWarn.start();
        			PullupWarn.stop();
        			flag = true;
        	}
        } else
        {
        	AltitudeWarn.stop();
        	PullupWarn.stop();
        	flag = false;
        }
        }
        super.mesh.chunkVisible("L_MC", flag);
    }
    
    protected void Warninglight()
    {
    	super.mesh.chunkVisible("L_Gear", ((FlightModelMain) (super.fm)).CT.getGear() > 0.95F);
        super.mesh.chunkVisible("L_SpBr", ((FlightModelMain) (super.fm)).CT.getAirBrake() > 0.1F);
        super.mesh.chunkVisible("L_LFire", ((FlightModelMain) (super.fm)).AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_RFire", ((FlightModelMain) (super.fm)).AS.astateEngineStates[1] > 2);
        boolean H = false;
        boolean M = false;
        boolean L = false;
        if(this.fm.CT.getGear()<0.1F)
        {
            H = false;
            M = false;
            L = false;
        } else
        {
            if(((FlightModelMain) (fm)).getAOA() > 8.9F)
            {
                H = true;
                M = false;
                L = false;
            } else
            if(((FlightModelMain) (fm)).getAOA() > 8.1F)
            {
                H = true;
                M = true;
                L = false;
            } else
            if(((FlightModelMain) (fm)).getAOA() > 6.9F)
            {
                H = false;
                M = true;
                L = false;
            } else
            if(((FlightModelMain) (fm)).getAOA() > 6.4F)
            {
                H = false;
                M = true;
                L = true;
            } else
            {
                H = false;
                M = false;
                L = true;
            }
        }
        super.mesh.chunkVisible("L_AOAH", H);
        super.mesh.chunkVisible("L_AOAL", L);
        super.mesh.chunkVisible("L_AOAM", M);
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
            //super.mesh.chunkVisible("Z_Z_NVision", true);
            light1.light.setEmit(0.0075F, 0.7F);
            light2.light.setEmit(0.0075F, 0.7F);
            //((F_18)aircraft()).FLIR = true;
            setNightMats(true);
        } else
        {
            //super.mesh.chunkVisible("Z_Z_NVision", false);
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            //((F_18)aircraft()).FLIR = false;
            setNightMats(false);
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
    private boolean bHSIDL;
    private boolean bHSIILS;
    private boolean bHSIMAN;
    private boolean bHSINAV;
    private boolean bHSITAC;
    private boolean bHSITGT;
    private boolean bHSIUHF;
    
   







}