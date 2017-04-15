
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.CrossVersion;
import com.maddox.sound.*;
import com.maddox.util.HashMapExt;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, F_14, AircraftLH, TypeGuidedMissileCarrier, 
//            Aircraft, Cockpit, TypeSemiRadar, TypeGroundRadar, 
//            TypeSupersonic

public class CockpitF_14D extends CockpitPilot
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

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + fm.EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = 200F;
            if(((F_14)aircraft()).radargunsight == 0 && ((F_14)aircraft()).k14Mode == 1)
                f = 200F;
            if(((F_14)aircraft()).radargunsight == 1 && ((F_14)aircraft()).k14Mode == 1)
                f = ((F_14)aircraft()).k14Distance;
            Point3d point3d = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
            float f1 = (float)((Tuple3d) (point3d)).z;
            if((((F_14)aircraft()).radargunsight == 2 || ((F_14)aircraft()).radargunsight == 3) && ((F_14)aircraft()).k14Mode == 1)
                f = f1 / (float)Math.cos(Math.toRadians(fm.Or.getPitch() - 270F)) + 200F;
            setNew.k14w = (5F * CockpitF_14D.k14TargetWingspanScale[((F_14)aircraft()).k14WingspanType]) / f;
            setNew.k14w = 0.9F * setOld.k14w + 0.1F * setNew.k14w;
            setNew.k14wingspan = 0.9F * setOld.k14wingspan + 0.1F * CockpitF_14D.k14TargetMarkScale[((F_14)aircraft()).k14WingspanType];
            setNew.k14mode = 0.8F * setOld.k14mode + 0.2F * (float)((F_14)aircraft()).k14Mode;
            Vector3d vector3d = ((SndAircraft) (aircraft())).FM.getW();
            double d = 0.00125D * (double)f;
            float f2 = (float)Math.toDegrees(d * ((Tuple3d) (vector3d)).z);
            float f3 = -(float)Math.toDegrees(d * ((Tuple3d) (vector3d)).y);
            float f4 = floatindex((f - 200F) * 0.04F, CockpitF_14D.k14BulletDrop) - CockpitF_14D.k14BulletDrop[0];
            if(((F_14)aircraft()).radargunsight == 2)
                f4 = floatindex((f - 200F) * 0.04F, CockpitF_14D.k14RocketDrop) - CockpitF_14D.k14RocketDrop[0];
            f3 += (float)Math.toDegrees(Math.atan(f4 / f));
            setNew.k14x = 0.92F * setOld.k14x + 0.08F * f2;
            setNew.k14y = 0.92F * setOld.k14y + 0.08F * f3;
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
                if(fm.AS.listenLorenzBlindLanding && fm.AS.isILSBL)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = (10F * setOld.ilsGS + getGlidePath()) / 11F;
                    bHSIILS = true;
                    bHSIDL = bHSIMAN = bHSINAV = bHSITAC = bHSITGT = bHSIUHF = false;
                } else
                if(fm.AS.listenTACAN)
                {
                    setNew.hsiLoc = (10F * setOld.hsiLoc + getBeaconDirection()) / 11F;
                    setNew.ilsLoc = (10F * setOld.ilsLoc + getBeaconDirection()) / 11F;
                    setNew.ilsGS = 0.0F;
                    bHSITAC = true;
                    bHSIDL = bHSIILS = bHSIMAN = bHSINAV = bHSITGT = bHSIUHF = false;
                } else
                if(fm.AS.listenNDBeacon || fm.AS.listenYGBeacon)
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
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            setNew.vspeed2 = (49F * setOld.vspeed2 + fm.getVertSpeed()) / 50F;
            setNew.pitch = fm.Or.getPitch();
            setNew.bank = fm.Or.getRoll();
            Vector3d vector3d1 = new Vector3d();
            fm.Or.transformInv(fm.Vwld, vector3d1);
            setNew.fpmPitch = FMMath.RAD2DEG(-(float)Math.atan2(((Tuple3d) (vector3d1)).z, ((Tuple3d) (vector3d1)).x));
            setNew.fpmYaw = FMMath.RAD2DEG((float)Math.atan2(((Tuple3d) (vector3d1)).y, ((Tuple3d) (vector3d1)).x));
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            if(fm.EI.engines[0].getRPM() > 200F || fm.EI.engines[1].getRPM() > 200F)
                setNew.isGeneratorAllive = true;
            else
                setNew.isGeneratorAllive = false;
            f = ((F_14)aircraft()).fSightCurForwardAngle;
            f2 = ((F_14)aircraft()).fSightCurSideslip;
            f3 = fm.getAltitude();
            f4 = (float)(-(Math.abs(fm.Vwld.length()) * Math.sin(Math.toRadians(Math.abs(fm.Or.getTangage())))) * 0.10189999639987946D);
            f4 += (float)Math.sqrt(f4 * f4 + 2.0F * f3 * 0.1019F);
            float f5 = Math.abs((float)fm.Vwld.length()) * (float)Math.cos(Math.toRadians(Math.abs(fm.Or.getTangage())));
            float f6 = (f5 * f4 + 10F) - 10F;
            alpha = 90F - Math.abs(fm.Or.getTangage()) - (float)Math.toDegrees(Math.atan(f6 / f3));
            return true;
        }

        Interpolater()
        {
        }
    }


    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
            aircraft().hierMesh().chunkVisible("HMask1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Seat1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        aircraft().hierMesh().chunkVisible("Head1_D0", true);
        aircraft().hierMesh().chunkVisible("HMask1_D0", true);
        aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
        aircraft().hierMesh().chunkVisible("Seat1_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(30F);
    }

    public CockpitF_14D()
    {
        super("3DO/Cockpit/F-14D/hier.him", "bf109");
        HUD1 = null;
        y = 0.0F;
        ground = false;
        t4 = 0L;
        range = 0.0F;
        ts = 0L;
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
        CrossVersion.setPrintCompassHeading(this, true);
        super.cockpitNightMats = (new String[] {
            "Instrument", "Gauges", "AH"
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

        ((F_14)aircraft()).bWantBeaconKeys = true;
        t2 = 0L;
        t3 = 0L;
        FOV = 1.0D;
        ScX = 0.005999999776482582D;
        ScY = 4.5000000000000001E-006D;
        ScZ = 0.0010000000474974513D;
        FOrigX = 0.0F;
        FOrigY = 0.0F;
        nTgts = 10;
        RRange = 370000F;
        RClose = 50F;
        BRange = 0.1F;
        BRefresh = 1300;
        BSteps = 12;
        BDiv = BRefresh / BSteps;
        tBOld = 0L;
        radarPlane = new ArrayList();
        radarLock = new ArrayList();
        radarground = new ArrayList();
        victim = new ArrayList();
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(168F, 83F, 0.0F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(168F, 83F, 0.0F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK2", light2);
        tw = 0L;
        x = 0.0F;
        start = false;
        left = false;
        right = false;
        becondistance = 0.0F;
        oldleftscreen = 0;
        oldrightscreen = 0;
        oldhddnav = 0;
        LFlapStat = '\0';
        LGearStat = '\0';
        if(!useRealisticNavigationInstruments())
            super.mesh.materialReplace("Nav_Tacan", "Nav_WPT");
    }

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
        if(((F_14)aircraft()).Nvision)
            super.mesh.chunkVisible("Z_Z_NVision", true);
        else
            super.mesh.chunkVisible("Z_Z_NVision", false);
        int i = ((F_14)aircraft()).leftscreen;
        if(i != oldleftscreen)
        {
            if(i == 0)
                super.mesh.materialReplace("HDD_Fuel", "HDD_Fuel");
            if(i == 1)
                super.mesh.materialReplace("HDD_Fuel", "HDD_FuelFlow");
            if(i == 2)
                super.mesh.materialReplace("HDD_Fuel", "HDD_Engine");
            oldleftscreen = i;
        }
        if(i == 0)
            movescreenfuel();
        if(i == 1)
            movescreenfuelflow();
        if(i == 2)
            movescreenengines();
        if(bHSIILS)
        {
            if(oldhddnav != 1)
            {
                super.mesh.materialReplace("HDD_Nav", "HDD_NavILS");
                oldhddnav = 1;
            }
        } else
        if(bHSITAC)
        {
            if(oldhddnav != 2)
            {
                super.mesh.materialReplace("HDD_Nav", "HDD_NavTCN");
                oldhddnav = 2;
            }
        } else
        if(oldhddnav != 0)
        {
            super.mesh.materialReplace("HDD_Nav", "HDD_Nav");
            oldhddnav = 0;
        }
        if(bNeedSetUp)
        {
            super.mesh.chunkVisible("Int_Marker", false);
            bNeedSetUp = false;
        }
        ((F_14)aircraft()).bWantBeaconKeys = true;
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
        if(((F_14)aircraft()).bMissileWarning)
        {
            super.mesh.chunkVisible("Z_Z_RWR_M", true);
            super.mesh.chunkVisible("L_AI", true);
            resetYPRmodifier();
            float f = ((F_14)aircraft()).misslebrg;
            Cockpit.xyz[0] = -(float)Math.sin(Math.toRadians(f)) * 0.013333F;
            Cockpit.xyz[2] = (float)Math.cos(Math.toRadians(f)) * 0.013333F;
            super.mesh.chunkSetLocate("Z_Z_RWR_M", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            super.mesh.chunkVisible("Z_Z_RWR_M", false);
            super.mesh.chunkVisible("L_AI", false);
        }
        if(((F_14)aircraft()).bRadarWarning)
        {
            super.mesh.chunkVisible("Z_Z_RWR_U", true);
            super.mesh.chunkVisible("L_AI", true);
            resetYPRmodifier();
            float f1 = ((F_14)aircraft()).aircraftbrg;
            Cockpit.xyz[0] = -(float)Math.sin(Math.toRadians(f1)) * 0.026666F;
            Cockpit.xyz[2] = (float)Math.cos(Math.toRadians(f1)) * 0.026666F;
            super.mesh.chunkSetLocate("Z_Z_RWR_U", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            super.mesh.chunkVisible("Z_Z_RWR_U", false);
            super.mesh.chunkVisible("L_AI", false);
        }
    }

    private void ILS()
    {
        if(!((F_14)aircraft()).ILS || !setNew.isBatteryOn && !setNew.isGeneratorAllive)
        {
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
            resetYPRmodifier();
            float f = setNew.ilsLoc * setNew.ilsLoc * (setNew.ilsLoc < 0.0F ? -1F : 1.0F);
            Cockpit.xyz[0] = -cvt(f, -10000F, 10000F, -0.59F, 0.59F);
            Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
            mesh.chunkSetLocate("Z_Z_ILS_Hor", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
            float f1 = setNew.ilsGS * setNew.ilsGS * (setNew.ilsGS < 0.0F ? -1F : 1.0F);
            Cockpit.xyz[2] = cvt(f1, -0.25F, 0.25F, -0.59F, 0.59F);
            Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
            mesh.chunkSetLocate("Z_Z_ILS_Ver", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkVisible("Z_Z_ILS_Hor", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
            resetYPRmodifier();
            float f2 = cvt(fm.getSpeedKMH(), 200F, 450F, -0.09F, 0.09F);
            Cockpit.xyz[2] = f2;
            mesh.chunkSetLocate("Z_Z_ILS_Pitch", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
            Cockpit.xyz[2] = -cvt(fm.getAOA(), 6.4F, 8.9F, -0.08F, 0.08F);
            Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
            mesh.chunkSetLocate("Z_Z_ILS_AOA", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
            return;
        }
    }

    private void HUDgunsight()
    {
        if((fm.AS.astateCockpitState & 2) == 0)
        {
            int i = ((F_14)aircraft()).k14Mode;
            resetYPRmodifier();
            Cockpit.xyz[0] = setNew.k14w;
            if(i == 0)
            {
                resetYPRmodifier();
                super.mesh.chunkSetAngles("Z_Z_Bomb", 0.0F, 0.0F, -fm.Or.getRoll());
                super.mesh.chunkSetAngles("Z_Z_Bombsteer", -setNew.k14x, 0.0F, 0.0F);
                super.mesh.chunkVisible("Z_Z_Bombsteer", true);
                super.mesh.chunkVisible("Z_Z_Bombmark3", true);
                super.mesh.chunkSetAngles("Z_Z_Bombmark", 0.0F, -1.4F * alpha - setNew.k14y, 0.0F);
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
                super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                super.mesh.chunkVisible("Z_Z_BulletdropR", false);
            }
            if(i == 1)
            {
                super.mesh.chunkVisible("Z_Z_Bombsteer", false);
                super.mesh.chunkVisible("Z_Z_Bombmark3", false);
                if(((F_14)aircraft()).radargunsight == 1)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x, -setNew.k14y, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                } else
                if(((F_14)aircraft()).radargunsight == 0)
                {
                    super.mesh.chunkVisible("Z_Z_RETICLE", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", true);
                    super.mesh.chunkSetAngles("Z_Z_Bulletdrop", setNew.k14x * 27F, 0.0F, setNew.k14y * 10F);
                    resetYPRmodifier();
                    Cockpit.xyz[1] = -setNew.k14y / 100F;
                    super.mesh.chunkSetLocate("Z_Z_BulletdropL", Cockpit.xyz, Cockpit.ypr);
                    Cockpit.xyz[1] = setNew.k14y / 100F;
                    super.mesh.chunkSetLocate("Z_Z_BulletdropR", Cockpit.xyz, Cockpit.ypr);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
                if(((F_14)aircraft()).radargunsight == 2)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x * 0.75F, -setNew.k14y * 0.75F, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
                if(((F_14)aircraft()).radargunsight == 3)
                {
                    super.mesh.chunkSetAngles("Z_Z_RETICLE", -setNew.k14x * 0.22F, -setNew.k14y * 0.22F, 0.0F);
                    super.mesh.chunkVisible("Z_Z_RETICLE", true);
                    super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                    super.mesh.chunkVisible("Z_Z_BulletdropR", false);
                    super.mesh.chunkVisible("Z_Z_radarlock", false);
                    super.mesh.chunkVisible("Z_Z_missilelock", false);
                }
            }
            if(i > 1)
            {
                super.mesh.chunkVisible("Z_Z_RETICLE", false);
                super.mesh.chunkVisible("Z_Z_Bombsteer", false);
                super.mesh.chunkVisible("Z_Z_Bombmark3", false);
                super.mesh.chunkVisible("Z_Z_BulletdropL", false);
                super.mesh.chunkVisible("Z_Z_BulletdropR", false);
            }
            if(i != 1)
            {
                super.mesh.chunkVisible("Z_Z_radarlock", false);
                super.mesh.chunkVisible("Z_Z_missilelock", false);
            }
        }
    }

    public void radarclutter(float f)
    {
        ScY = 4.5E-006F * (float)((F_14)aircraft()).radarrange;
        boolean flag = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive || !((F_14)aircraft()).radartogle)
        {
            flag = false;
            start = false;
            ground = false;
            if(oldrightscreen != 0)
                super.mesh.materialReplace("HDDR", "HDDR");
            oldrightscreen = 0;
            super.mesh.chunkVisible("Z_Z_RADAR_AH", false);
            super.mesh.chunkVisible("Z_Z_lockgate", false);
            super.mesh.chunkVisible("Z_Z_Scan_1", false);
            for(int i = 1; i < 3; i++)
                super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + i, false);

            for(int k = 1; k < 3; k++)
                super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + k, false);

            for(int i1 = 1; i1 < 3; i1++)
                super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + i1, false);

            for(int k1 = 1; k1 < 4; k1++)
                super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + k1, false);

            super.mesh.chunkVisible("Z_Z_dif+", false);
            super.mesh.chunkVisible("Z_Z_dif-", false);
            super.mesh.chunkVisible("Z_Z_Radarbrg", false);
            super.mesh.chunkVisible("Z_Z_RADAR_MBRG", false);
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
            ((F_14)aircraft()).radarmode = 0;
            ((F_14)aircraft()).lockmode = 0;
        } else
        {
            flag = true;
            if(((F_14)aircraft()).k14Mode != 1)
            {
                super.mesh.chunkVisible("Z_Z_radarlock", false);
                super.mesh.chunkVisible("Z_Z_missilelock", false);
            }
        }
        for(int j = 1; j < 3; j++)
            super.mesh.chunkVisible("Z_Z_RADAR_Mach_" + j, flag);

        for(int l = 1; l < 4; l++)
            super.mesh.chunkVisible("Z_Z_RADAR_Speed_" + l, flag);

        for(int j1 = 1; j1 < 3; j1++)
            super.mesh.chunkVisible("Z_Z_RADAR_ALT_" + j1, flag);

        super.mesh.chunkVisible("Z_Z_RADAR_AH", flag);
        if(!flag)
        {
            for(int l1 = 0; l1 <= nTgts; l1++)
            {
                String s = "Z_Z_RadarMark" + l1;
                if(super.mesh.isChunkVisible(s))
                    super.mesh.chunkVisible(s, false);
            }

            return;
        }
        float f1 = 0.0F;
        long l2 = Time.current();
        if(((F_14)aircraft()).v == 0.0F && ((F_14)aircraft()).h == 0.0F)
            super.mesh.chunkVisible("Z_Z_lockgate", false);
        else
            super.mesh.chunkVisible("Z_Z_lockgate", true);
        resetYPRmodifier();
        Cockpit.xyz[1] = -((F_14)aircraft()).v * 1.3333F;
        Cockpit.xyz[2] = ((F_14)aircraft()).h * (float)((F_14)aircraft()).radarrange * 1.3333F;
        super.mesh.chunkSetLocate("Z_Z_lockgate", Cockpit.xyz, Cockpit.ypr);
        boolean flag1 = false;
        if(((F_14)aircraft()).radarmode == 0 && ((F_14)aircraft()).lockmode == 0)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            f1 = 0.03625F;
            super.mesh.chunkVisible("Z_Z_RADAR_MBRG", false);
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
            if((((F_14)aircraft()).v != 0.0F || ((F_14)aircraft()).h != 0.0F) && t3 + 60000L < l2)
            {
                ((F_14)aircraft()).v = 0.0F;
                ((F_14)aircraft()).h = 0.0F;
                t3 = l2;
            }
        }
        if(((F_14)aircraft()).radarmode == 0 && ((F_14)aircraft()).lockmode == 1)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            f1 = 0.00875F;
            radaracquire(f);
            radarlock();
            flag1 = true;
        } else
        if(((F_14)aircraft()).radarmode == 1)
        {
            if(oldrightscreen != 1)
                super.mesh.materialReplace("HDDR", "radarframe");
            oldrightscreen = 1;
            radarselection();
            f1 = 0.01375F;
            radarboresight(f);
            radarlock();
            if(((F_14)aircraft()).lockmode == 1)
                flag1 = true;
            else
                flag1 = false;
        } else
        {
            flag1 = false;
        }
        for(int i2 = 1; i2 < 3; i2++)
            super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + i2, flag1);

        for(int j2 = 1; j2 < 3; j2++)
            super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + j2, flag1);

        for(int k2 = 1; k2 < 3; k2++)
            super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + k2, flag1);

        for(int i3 = 1; i3 < 4; i3++)
            super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + i3, flag1);

        super.mesh.chunkVisible("Z_Z_dif+", flag1);
        super.mesh.chunkVisible("Z_Z_dif-", flag1);
        super.mesh.chunkVisible("Z_Z_Radarbrg", flag1);
        super.mesh.chunkVisible("Z_Z_RADAR_MBRG", flag1);
        super.mesh.chunkSetAngles("Z_Z_RADAR_AH", 0.0F, setNew.bank, 0.0F);
        float f2 = calculateMach();
        float f3 = (float)(int)f2 * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_1", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Mach_2", 0.0F, 0.0F, f4 - f3);
        f2 = (fm.getSpeedKMH() * 0.5399568F) / 100F;
        f3 = (float)(int)f2 * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_2", 0.0F, 0.0F, f4 - f3);
        float f5 = (float)((int)(f2 * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_Speed_3", 0.0F, 0.0F, f5 - f3);
        f2 = (fm.getAltitude() * 3.28084F) / 10000F;
        f3 = (float)(int)f2 * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_1", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_RADAR_ALT_2", 0.0F, 0.0F, f4 - f3);
        if(((F_14)aircraft()).radarmode < 2)
        {
            super.mesh.chunkVisible("Z_Z_Scan_1", true);
            ground = false;
            if(!start)
            {
                right = true;
                left = false;
            }
            if(x < -f1)
            {
                right = true;
                left = false;
            }
            if(x > f1)
            {
                right = false;
                left = true;
                start = true;
            }
            if(left && !right && l2 > t2 + 5L)
            {
                x -= 0.0025F;
                t2 = l2;
            }
            if(right && !left && l2 > t2 + 5L)
            {
                x += 0.0025F;
                t2 = l2;
            }
            resetYPRmodifier();
            if(((F_14)aircraft()).lockmode == 0 || ((F_14)aircraft()).radarmode == 1)
                Cockpit.xyz[0] = x * 1.3333F;
            if(((F_14)aircraft()).lockmode == 1 && ((F_14)aircraft()).radarmode == 0)
                Cockpit.xyz[0] = (x - ((F_14)aircraft()).v / 4F) * 1.3333F;
            super.mesh.chunkSetLocate("Z_Z_Scan_1", Cockpit.xyz, Cockpit.ypr);
        } else
        if(((F_14)aircraft()).radarmode == 2)
        {
            if(((F_14)aircraft()).lockmode == 0)
            {
                radarground();
                super.mesh.chunkVisible("Z_Z_Scan_1", true);
            }
            if(((F_14)aircraft()).lockmode == 1)
            {
                radarlock();
                radaracquireground(f);
                super.mesh.chunkVisible("Z_Z_Scan_1", false);
            }
            start = false;
            if(!ground)
            {
                right = true;
                left = false;
            }
            if(y < -57F)
            {
                right = true;
                left = false;
            }
            if(y > 57F)
            {
                right = false;
                left = true;
                ground = true;
            }
            if(left && !right && l2 > t2 + 5L)
            {
                y -= 2.0F;
                t2 = l2;
            }
            if(right && !left && l2 > t2 + 5L)
            {
                y += 2.0F;
                t2 = l2;
            }
            x = cvt(y, -50F, 50F, 0.145F, -0.145F);
            super.mesh.chunkSetAngles("Z_Z_Scan_1", 0.0F, y, 0.0F);
            if(oldrightscreen != 2)
                super.mesh.materialReplace("HDDR", "Groundradar");
            oldrightscreen = 2;
            super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
        }
    }

    private int iLockState()
    {
        if(!(aircraft() instanceof TypeGuidedMissileCarrier))
            return 0;
        else
            return ((TypeGuidedMissileCarrier)aircraft()).getGuidedMissileUtils().getMissileLockState();
    }

    public void radarlock()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                int i = radarLock.size();
                if(i > 0)
                {
                    int nt = 0;
                    int k = 0;
                    if(i > 0)
                    {
                        double x1 = ((Tuple3d) ((Point3d)radarLock.get(k))).x;
                        if(x1 > (double)RClose && nt <= nTgts)
                        {
                            FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                            double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).z * FOV; //distance
                            float f = FOrigX + (float)(NewX * 0.0099999997764825821D) + (float)Math.sin(Math.toRadians(fm.Or.getKren())) * 0.011F;
                            float f2 = FOrigY + (float)(NewY * 0.0099999997764825821D);
                            if(f2 > 0.3F)
                                f2 = 0.3F;
                            if(f2 < -0.3F)
                                f2 = -0.3F;
                            if(f > 0.3F)
                                f = 0.3F;
                            if(f < -0.3F)
                                f = -0.3F;
                            String s = "Z_Z_radarlock";
                            super.mesh.setCurChunk(s);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f;
                            Cockpit.xyz[2] = f2;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(!super.mesh.isChunkVisible(s))
                                super.mesh.chunkVisible(s, true);
                            if(iLockState() == 2)
                                super.mesh.chunkVisible("Z_Z_missilelock", true);
                            else
                                super.mesh.chunkVisible("Z_Z_missilelock", false);
                        }
                        if(x1 > (double)RClose && nt <= nTgts)
                        {
                            FOV = 300D / x1; // distance relationship, to adjust the deviation of radar mark when getting closer to target planes
                            double NewX = -((Tuple3d) ((Point3d)radarLock.get(k))).y * FOV; // spanning
                            double NewY = ((Tuple3d) ((Point3d)radarLock.get(k))).z * FOV; //distance
                            float f1 = FOrigX + (float)(NewX * 9.9999997764825814E-005D) + (float)Math.sin(Math.toRadians(fm.Or.getKren())) * 0.011F;
                            float f3 = FOrigY + (float)(NewY * 9.9999997764825814E-005D);
                            String s1 = "Z_Z_RADAR_TBRG";
                            super.mesh.setCurChunk(s1);
                            resetYPRmodifier();
                            Cockpit.xyz[0] = -f1 * 1.3333F;
                            Cockpit.xyz[2] = f3 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(!super.mesh.isChunkVisible(s1))
                                super.mesh.chunkVisible(s1, true);
                        }
                    }
                } else
                {    // hide everything when there's no enemy
                    if(super.mesh.isChunkVisible("Z_Z_radarlock"))
                        super.mesh.chunkVisible("Z_Z_radarlock", false);
                    if(super.mesh.isChunkVisible("Z_Z_missilelock"))
                        super.mesh.chunkVisible("Z_Z_missilelock", false);
                    if(super.mesh.isChunkVisible("Z_Z_RADAR_TBRG"))
                        super.mesh.chunkVisible("Z_Z_RADAR_TBRG", false);
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarselection()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            radarPlane.clear();
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        Mission.cur();
                        float f = Mission.curCloudsType();
                        double d = ((double)(x + (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F) / ScX / (30D / ((Tuple3d) (point3d1)).x)) * 4D;
                        if(right && ((Tuple3d) (point3d1)).x > (double)RClose && ((Tuple3d) (point3d1)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (point3d1)).y < d + 1000D && ((Tuple3d) (point3d1)).y > d - 8000D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.46397023426D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.46397023426D)
                        {
                            radarPlane.add(point3d1);
                            tw = Time.current();
                        }
                        if(left && ((Tuple3d) (point3d1)).x > (double)RClose && ((Tuple3d) (point3d1)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (point3d1)).y < d + 8000D && ((Tuple3d) (point3d1)).y > d - 1000D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.46397023426D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.46397023426D)
                        {
                            radarPlane.add(point3d1);
                            tw = Time.current();
                        }
                    }
                }

                int k = radarPlane.size();
                if(k > 0)
                {
                    int l = 0;
                    boolean flag = false;
                    for(int j1 = 0; j1 < k; j1++)
                    {
                        double d1 = ((Tuple3d) ((Point3d)radarPlane.get(j1))).x;
                        if(d1 > (double)RClose && l <= nTgts)
                        {
                            FOV = 30D / d1;
                            double d2 = -((Tuple3d) ((Point3d)radarPlane.get(j1))).y * FOV;
                            double d3 = ((Tuple3d) ((Point3d)radarPlane.get(j1))).x;
                            float f1 = (FOrigX + (float)(d2 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                            float f2 = FOrigY + (float)(d3 * ScY);
                            if(f2 < 0.0F)
                                f2 = 0.0F;
                            l++;
                            String s2 = "Z_Z_RadarMark" + l;
                            super.mesh.setCurChunk(s2);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f1 * 1.3333F;
                            Cockpit.xyz[2] = f2 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(f1 >= 0.18F || f1 <= -0.18F || f2 > 0.36F)
                                super.mesh.chunkVisible(s2, false);
                            else
                            if(!super.mesh.isChunkVisible(s2))
                                super.mesh.chunkVisible(s2, true);
                        }
                    }

                    for(int k1 = l + 1; k1 <= nTgts; k1++)
                    {
                        String s1 = "Z_Z_RadarMark" + k1;
                        if(super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, false);
                    }

                } else
                {
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    for(int i1 = 0; i1 <= nTgts + 1; i1++)
                    {
                        String s = "Z_Z_RadarMark" + i1;
                        if(super.mesh.isChunkVisible(s))
                            super.mesh.chunkVisible(s, false);
                    }

                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radaracquire(float f)
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            double d = -((double)(((F_14)aircraft()).v + (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F) / ScX);
            double d1 = (double)(((F_14)aircraft()).h * (float)((F_14)aircraft()).radarrange) / ScY;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 0.0F;
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        Mission.cur();
                        float f7 = Mission.curCloudsType();
                        if(((Tuple3d) (point3d1)).x > d1 - 500D && ((Tuple3d) (point3d1)).x < d1 + 500D && ((Tuple3d) (point3d1)).x < 48000D && ((Tuple3d) (point3d1)).y < d / (30D / ((Tuple3d) (point3d1)).x) + 500D && ((Tuple3d) (point3d1)).y > d / (30D / ((Tuple3d) (point3d1)).x) - 500D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.56397023426000004D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.56397023426000004D)
                        {
                            radarLock.add(point3d1);
                            victim.add(actor);
                            ((TypeSemiRadar)((F_14)aircraft())).target.set(point3d1);
                        }
                    }
                }

                i = victim.size();
                if(i > 0)
                {
                    for(int k = 0; k < i; k++)
                    {
                        Actor actor1 = (Actor)victim.get(k);
                        Vector3d vector3d1 = new Vector3d();
                        vector3d1.set(actor1.pos.getAbsPoint());
                        Point3d point3d2 = new Point3d();
                        point3d2.set(actor1.pos.getAbsPoint());
                        f1 = (((float)actor1.getSpeed(vector3d1) * 3.6F) / getMachForAlt((float)((Tuple3d) (point3d2)).z)) * 1.621371F;
                        f2 = ((float)((Tuple3d) (point3d2)).z * 3.28084F) / 10000F;
                        f3 = ((fm.getAltitude() - (float)((Tuple3d) (point3d2)).z) * 3.28084F) / 1000F;
                        Orient orient1 = actor1.pos.getAbsOrient();
                        Orient orient2 = ((Actor) (aircraft)).pos.getAbsOrient();
                        f4 = normalizeDegree(orient1.getAzimut() - 270F) / 100F;
                        f5 = normalizeDegree(-normalizeDegree(setNew.azimuth.getDeg(f) + 90F) + normalizeDegree(orient1.getAzimut() - 270F));
                    }

                }
                int l = radarLock.size();
                if(l > 0)
                {
                    int i1 = 0;
                    int k1 = 0;
                    float f8 = (float)(int)f1 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_1", 0.0F, 0.0F, f8);
                    float f9 = (float)((int)(f1 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_2", 0.0F, 0.0F, f9 - f8);
                    f8 = (float)(int)f2 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_1", 0.0F, 0.0F, f8);
                    f9 = (float)((int)(f2 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_2", 0.0F, 0.0F, f9 - f8);
                    f8 = (float)(int)Math.abs(f3) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, f8);
                    f9 = (float)((int)(Math.abs(f3) * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_2", 0.0F, 0.0F, f9 - f8);
                    f8 = (float)(int)f4 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_1", 0.0F, 0.0F, f8);
                    f9 = (float)((int)(f4 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_2", 0.0F, 0.0F, f9 - f8);
                    float f10 = (float)((int)(f4 * 100F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_3", 0.0F, 0.0F, f10 - f8);
                    super.mesh.chunkSetAngles("Z_Z_Radarbrg", 0.0F, f5, 0.0F);
                    if(f3 > 0.0F)
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", false);
                        super.mesh.chunkVisible("Z_Z_dif-", true);
                    } else
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", true);
                        super.mesh.chunkVisible("Z_Z_dif-", false);
                    }
                    double d2 = ((Tuple3d) ((Point3d)radarLock.get(k1))).x;
                    if(d2 > (double)RClose && i1 <= nTgts)
                    {
                        FOV = 30D / d2;
                        double d3 = -((Tuple3d) ((Point3d)radarLock.get(k1))).y * FOV;
                        double d5 = ((Tuple3d) ((Point3d)radarLock.get(k1))).x;
                        float f11 = (FOrigX + (float)(d3 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f13 = FOrigY + (float)(d5 * ScY);
                        ((F_14)aircraft()).v = f11;
                        ((F_14)aircraft()).h = f13 / (float)((F_14)aircraft()).radarrange;
                        if(((F_14)aircraft()).v > 0.03625F || ((F_14)aircraft()).v < -0.03625F)
                            super.mesh.chunkVisible("Z_Z_Scan_1", false);
                        if(f13 < 0.0F)
                            f13 = 0.0F;
                        String s = "Z_Z_lockgate";
                        super.mesh.setCurChunk(s);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f11 * 1.3333F;
                        Cockpit.xyz[2] = f13 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f11 >= 0.18F || f11 <= -0.18F || f13 > 0.36F)
                            super.mesh.chunkVisible(s, false);
                        else
                        if(!super.mesh.isChunkVisible(s))
                            super.mesh.chunkVisible(s, true);
                    }
                    if(d2 > (double)RClose && i1 <= nTgts)
                    {
                        FOV = 30D / d2;
                        double d4 = -((Tuple3d) ((Point3d)radarLock.get(k1))).y * FOV;
                        double d6 = ((Tuple3d) ((Point3d)radarLock.get(k1))).x;
                        float f12 = (FOrigX + (float)(d4 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f14 = FOrigY + (float)(d6 * ScY);
                        if(f14 < 0.0F)
                            f14 = 0.0F;
                        String s1 = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(s1);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f12 * 1.3333F;
                        Cockpit.xyz[2] = f14 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f12 >= 0.18F || f12 <= -0.18F || f14 > 0.36F)
                            super.mesh.chunkVisible(s1, false);
                        else
                        if(!super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, true);
                    }
                } else
                {
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    for(int j1 = 1; j1 < 3; j1++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + j1, false);

                    for(int l1 = 1; l1 < 3; l1++)
                        super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + l1, false);

                    for(int i2 = 1; i2 < 3; i2++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + i2, false);

                    for(int j2 = 1; j2 < 4; j2++)
                        super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + j2, false);

                    super.mesh.chunkVisible("Z_Z_dif+", false);
                    super.mesh.chunkVisible("Z_Z_dif-", false);
                    super.mesh.chunkVisible("Z_Z_Radarbrg", false);
                    ((F_14)aircraft()).lockmode = 0;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarboresight(float f)
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            long l = Time.current();
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if((actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        if(l > t4 + 5L)
                        {
                            range += 1000F;
                            t4 = l;
                        }
                        Mission.cur();
                        float f6 = Mission.curCloudsType();
                        if((double)(range + 700F) >= 16000D - (double)(350F * f6))
                            range = 0.0F;
                        double d = ((double)(x + (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F) / ScX / (30D / ((Tuple3d) (point3d1)).x)) * 4D;
                        if(((Tuple3d) (point3d1)).x < (double)(range + 700F) && ((Tuple3d) (point3d1)).y < d + 8000D && ((Tuple3d) (point3d1)).y > d - 8000D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.56397023426000004D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.56397023426000004D)
                        {
                            radarLock.add(point3d1);
                            victim.add(actor);
                            range = (float)((Tuple3d) (point3d1)).x;
                            ((F_14)aircraft()).lockmode = 1;
                            ((TypeSemiRadar)((F_14)aircraft())).target.set(point3d1);
                        }
                    }
                }

                i = victim.size();
                if(i > 0)
                {
                    for(int k = 0; k < i; k++)
                    {
                        Actor actor1 = (Actor)victim.get(k);
                        Vector3d vector3d1 = new Vector3d();
                        vector3d1.set(actor1.pos.getAbsPoint());
                        Point3d point3d2 = new Point3d();
                        point3d2.set(actor1.pos.getAbsPoint());
                        f1 = (((float)actor1.getSpeed(vector3d1) * 3.6F) / getMachForAlt((float)((Tuple3d) (point3d2)).z)) * 1.621371F;
                        f2 = ((float)((Tuple3d) (point3d2)).z * 3.28084F) / 10000F;
                        f3 = ((fm.getAltitude() - (float)((Tuple3d) (point3d2)).z) * 3.28084F) / 1000F;
                        Orient orient1 = actor1.pos.getAbsOrient();
                        Orient orient2 = ((Actor) (aircraft)).pos.getAbsOrient();
                        f4 = normalizeDegree(orient1.getAzimut() - 270F) / 100F;
                        f5 = normalizeDegree(-normalizeDegree(setNew.azimuth.getDeg(f) + 90F) + normalizeDegree(orient1.getAzimut() - 270F));
                    }

                }
                int i1 = radarLock.size();
                if(i1 > 0)
                {
                    int j1 = 0;
                    int l1 = 0;
                    float f7 = (float)(int)f1 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_1", 0.0F, 0.0F, f7);
                    float f8 = (float)((int)(f1 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Mach_2", 0.0F, 0.0F, f8 - f7);
                    f7 = (float)(int)f2 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_1", 0.0F, 0.0F, f7);
                    f8 = (float)((int)(f2 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_ALT_2", 0.0F, 0.0F, f8 - f7);
                    f7 = (float)(int)Math.abs(f3) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_1", 0.0F, 0.0F, f7);
                    f8 = (float)((int)(Math.abs(f3) * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_Dif_2", 0.0F, 0.0F, f8 - f7);
                    f7 = (float)(int)f4 * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_1", 0.0F, 0.0F, f7);
                    f8 = (float)((int)(f4 * 10F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_2", 0.0F, 0.0F, f8 - f7);
                    float f9 = (float)((int)(f4 * 100F) % 10) * 36F;
                    super.mesh.chunkSetAngles("Z_Z_TARGET_HDG_3", 0.0F, 0.0F, f9 - f7);
                    super.mesh.chunkSetAngles("Z_Z_Radarbrg", 0.0F, f5, 0.0F);
                    if(f3 > 0.0F)
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", false);
                        super.mesh.chunkVisible("Z_Z_dif-", true);
                    } else
                    {
                        super.mesh.chunkVisible("Z_Z_dif+", true);
                        super.mesh.chunkVisible("Z_Z_dif-", false);
                    }
                    double d1 = ((Tuple3d) ((Point3d)radarLock.get(l1))).x;
                    if(d1 > (double)RClose && j1 <= nTgts)
                    {
                        FOV = 30D / d1;
                        double d2 = -((Tuple3d) ((Point3d)radarLock.get(l1))).y * FOV;
                        double d4 = ((Tuple3d) ((Point3d)radarLock.get(l1))).x;
                        float f10 = (FOrigX + (float)(d2 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f12 = FOrigY + (float)(d4 * ScY);
                        ((F_14)aircraft()).v = f10;
                        ((F_14)aircraft()).h = f12;
                        if(f12 < 0.0F)
                            f12 = 0.0F;
                        String s = "Z_Z_lockgate";
                        super.mesh.setCurChunk(s);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f10 * 1.3333F;
                        Cockpit.xyz[2] = f12 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f10 >= 0.18F || f10 <= -0.18F || f12 > 0.36F)
                            super.mesh.chunkVisible(s, false);
                        else
                        if(!super.mesh.isChunkVisible(s))
                            super.mesh.chunkVisible(s, true);
                    }
                    if(d1 > (double)RClose && j1 <= nTgts)
                    {
                        FOV = 30D / d1;
                        double d3 = -((Tuple3d) ((Point3d)radarLock.get(l1))).y * FOV;
                        double d5 = ((Tuple3d) ((Point3d)radarLock.get(l1))).x;
                        float f11 = (FOrigX + (float)(d3 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f13 = FOrigY + (float)(d5 * ScY);
                        if(f13 < 0.0F)
                            f13 = 0.0F;
                        String s1 = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(s1);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f11 * 1.3333F;
                        Cockpit.xyz[2] = f13 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f11 >= 0.18F || f11 <= -0.18F || f13 > 0.36F)
                            super.mesh.chunkVisible(s1, false);
                        else
                        if(!super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, true);
                    }
                } else
                {
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    for(int k1 = 1; k1 < 3; k1++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Mach_" + k1, false);

                    for(int i2 = 1; i2 < 3; i2++)
                        super.mesh.chunkVisible("Z_Z_TARGET_ALT_" + i2, false);

                    for(int j2 = 1; j2 < 3; j2++)
                        super.mesh.chunkVisible("Z_Z_TARGET_Dif_" + j2, false);

                    for(int k2 = 1; k2 < 4; k2++)
                        super.mesh.chunkVisible("Z_Z_TARGET_HDG_" + k2, false);

                    super.mesh.chunkVisible("Z_Z_dif+", false);
                    super.mesh.chunkVisible("Z_Z_dif-", false);
                    super.mesh.chunkVisible("Z_Z_Radarbrg", false);
                    ((F_14)aircraft()).lockmode = 0;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radarground()
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            radarPlane.clear();
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    Vector3d vector3d = new Vector3d();
                    if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy() && actor.getSpeed(vector3d) > 5D)
                    {
                        Vector3d vector3d1 = new Vector3d();
                        vector3d1.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        Mission.cur();
                        float f = Mission.curCloudsType();
                        double d1 = (double)(x + (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F) / ScX / (30D / ((Tuple3d) (point3d1)).x);
                        if(right && ((Tuple3d) (point3d1)).x > (double)RClose && ((Tuple3d) (point3d1)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (point3d1)).y < d1 + 6000D && ((Tuple3d) (point3d1)).y > d1 - 1000D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.46397023426D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.46397023426D)
                        {
                            radarPlane.add(point3d1);
                            tw = Time.current();
                        }
                        if(left && ((Tuple3d) (point3d1)).x > (double)RClose && ((Tuple3d) (point3d1)).x < (double)RRange - (double)(350F * f) && ((Tuple3d) (point3d1)).y < d1 + 1000D && ((Tuple3d) (point3d1)).y > d1 - 6000D && ((Tuple3d) (point3d1)).z < ((Tuple3d) (point3d1)).x * 0.46397023426D && ((Tuple3d) (point3d1)).z > -((Tuple3d) (point3d1)).x * 0.46397023426D)
                        {
                            radarPlane.add(point3d1);
                            tw = Time.current();
                        }
                    }
                }

                int k = radarPlane.size();
                if(k > 0)
                {
                    int l = 0;
                    boolean flag = false;
                    for(int j1 = 0; j1 < k; j1++)
                    {
                        double d = ((Tuple3d) ((Point3d)radarPlane.get(j1))).x;
                        if(d > (double)RClose && l <= nTgts)
                        {
                            FOV = 30D / d;
                            double d2 = -((Tuple3d) ((Point3d)radarPlane.get(j1))).y * FOV;
                            double d3 = ((Tuple3d) ((Point3d)radarPlane.get(j1))).x;
                            float f1 = (FOrigX + (float)(d2 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                            float f2 = FOrigY + (float)(d3 * ScY);
                            if(f2 < 0.0F)
                                f2 = 0.0F;
                            l++;
                            String s2 = "Z_Z_RadarMark" + l;
                            super.mesh.setCurChunk(s2);
                            resetYPRmodifier();
                            Cockpit.xyz[1] = -f1 * 1.3333F;
                            Cockpit.xyz[2] = f2 * 1.3333F;
                            super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                            super.mesh.render();
                            if(f1 >= 0.18F || f1 <= -0.18F || f2 > 0.36F)
                                super.mesh.chunkVisible(s2, false);
                            else
                            if(!super.mesh.isChunkVisible(s2))
                                super.mesh.chunkVisible(s2, true);
                        }
                    }

                    for(int k1 = l + 1; k1 <= nTgts; k1++)
                    {
                        String s1 = "Z_Z_RadarMark" + k1;
                        if(super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, false);
                    }

                } else
                {
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    for(int i1 = 0; i1 <= nTgts + 1; i1++)
                    {
                        String s = "Z_Z_RadarMark" + i1;
                        if(super.mesh.isChunkVisible(s))
                            super.mesh.chunkVisible(s, false);
                    }

                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void radaracquireground(float f)
    {
        try
        {
            Aircraft aircraft = World.getPlayerAircraft();
            radarLock.clear();
            victim.clear();
            double d = -((double)(((F_14)aircraft()).v + (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F) / ScX);
            double d1 = (double)(((F_14)aircraft()).h * (float)((F_14)aircraft()).radarrange) / ScY;
            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;
            float f5 = 0.0F;
            float f6 = 0.0F;
            if(Actor.isValid(aircraft) && Actor.isAlive(aircraft))
            {
                Point3d point3d = ((Actor) (aircraft)).pos.getAbsPoint();
                Orient orient = ((Actor) (aircraft)).pos.getAbsOrient();
                List list = Engine.targets();
                int i = list.size();
                for(int j = 0; j < i; j++)
                {
                    Actor actor = (Actor)list.get(j);
                    if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                    {
                        Vector3d vector3d = new Vector3d();
                        vector3d.set(point3d);
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        Point3d point3d2 = new Point3d();
                        point3d2.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        Mission.cur();
                        float f7 = Mission.curCloudsType();
                        if(((Tuple3d) (point3d1)).x > d1 - 500D && ((Tuple3d) (point3d1)).x < d1 + 500D && ((Tuple3d) (point3d1)).y < d / (30D / ((Tuple3d) (point3d1)).x) + 500D && ((Tuple3d) (point3d1)).y > d / (30D / ((Tuple3d) (point3d1)).x) - 500D)
                        {
                            radarLock.add(point3d1);
                            ((TypeGroundRadar)((F_14)aircraft())).groundtarget.set(point3d2);
                        }
                    }
                }

                int k = radarLock.size();
                if(k > 0)
                {
                    int l = 0;
                    int i1 = 0;
                    double d2 = ((Tuple3d) ((Point3d)radarLock.get(i1))).x;
                    if(d2 > (double)RClose && l <= nTgts)
                    {
                        FOV = 30D / d2;
                        double d3 = -((Tuple3d) ((Point3d)radarLock.get(i1))).y * FOV;
                        double d5 = ((Tuple3d) ((Point3d)radarLock.get(i1))).x;
                        float f8 = (FOrigX + (float)(d3 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f10 = FOrigY + (float)(d5 * ScY);
                        ((F_14)aircraft()).v = f8;
                        ((F_14)aircraft()).h = f10 / (float)((F_14)aircraft()).radarrange;
                        if(f10 < 0.0F)
                            f10 = 0.0F;
                        String s = "Z_Z_lockgate";
                        super.mesh.setCurChunk(s);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f8 * 1.3333F;
                        Cockpit.xyz[2] = f10 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f8 >= 0.18F || f8 <= -0.18F || f10 > 0.36F)
                            super.mesh.chunkVisible(s, false);
                        else
                        if(!super.mesh.isChunkVisible(s))
                            super.mesh.chunkVisible(s, true);
                    }
                    if(d2 > (double)RClose && l <= nTgts)
                    {
                        FOV = 30D / d2;
                        double d4 = -((Tuple3d) ((Point3d)radarLock.get(i1))).y * FOV;
                        double d6 = ((Tuple3d) ((Point3d)radarLock.get(i1))).x;
                        float f9 = (FOrigX + (float)(d4 * ScX)) - (float)Math.sin(Math.toRadians(fm.Or.getRoll())) * 0.011F;
                        float f11 = FOrigY + (float)(d6 * ScY);
                        if(f11 < 0.0F)
                            f11 = 0.0F;
                        String s1 = "Z_Z_RadarMark0";
                        super.mesh.setCurChunk(s1);
                        resetYPRmodifier();
                        Cockpit.xyz[1] = -f9 * 1.3333F;
                        Cockpit.xyz[2] = f11 * 1.3333F;
                        super.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        super.mesh.render();
                        if(f9 >= 0.18F || f9 <= -0.18F || f11 > 0.36F)
                            super.mesh.chunkVisible(s1, false);
                        else
                        if(!super.mesh.isChunkVisible(s1))
                            super.mesh.chunkVisible(s1, true);
                        if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                            super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                        for(int j1 = 1; j1 <= nTgts + 1; j1++)
                        {
                            String s2 = "Z_Z_RadarMark" + j1;
                            if(super.mesh.isChunkVisible(s2))
                                super.mesh.chunkVisible(s2, false);
                        }

                    }
                } else
                {
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark0"))
                        super.mesh.chunkVisible("Z_Z_RadarMark0", false);
                    if(super.mesh.isChunkVisible("Z_Z_RadarMark11"))
                        super.mesh.chunkVisible("Z_Z_RadarMark11", false);
                    if(super.mesh.isChunkVisible("Z_Z_lockgate"))
                        super.mesh.chunkVisible("Z_Z_lockgate", false);
                    ((F_14)aircraft()).lockmode = 0;
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void moveControls(float f)
    {
        super.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, 20F * fm.CT.getCockpitDoor());
        super.mesh.chunkSetAngles("Z_Z_Stick", 0.0F, fm.CT.AileronControl * 10F, fm.CT.ElevatorControl * 10F);
        super.mesh.chunkSetAngles("Z_Z_Throttle1", 0.0F, 0.0F, -42F * interp(setNew.throttler, setOld.throttler, f));
        super.mesh.chunkSetAngles("Z_Z_Throttle2", 0.0F, 0.0F, -42F * interp(setNew.throttlel, setOld.throttlel, f));
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = fm.CT.getRudder() * -0.07F;
        super.mesh.chunkSetLocate("PedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = fm.CT.getRudder() * 0.07F;
        super.mesh.chunkSetLocate("PedalR", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_Z_Gear", 0.0F, 0.0F, cvt(fm.CT.GearControl, 0.0F, 1.0F, 0.0F, 20F));
        float f1 = 0.0F;
        if(fm.CT.FlapsControlSwitch == 0)
        {
            float f2 = -15F;
            super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f2);
        }
        if(fm.CT.FlapsControlSwitch == 1)
        {
            float f3 = 0.0F;
            super.mesh.chunkSetAngles("Z_Z_Flap", f3, 0.0F, 0.0F);
        }
        if(fm.CT.FlapsControlSwitch == 2)
        {
            float f4 = 15F;
            super.mesh.chunkSetAngles("Z_Z_Flap", 0.0F, 0.0F, f4);
        }
        super.mesh.chunkSetAngles("Z_Z_Hook", 0.0F, 0.0F, (float)(int)fm.CT.arrestorControl * 20F);
        super.mesh.chunkSetAngles("Z_Z_WingFold", (float)(int)fm.CT.wingControl * -80F, 0.0F, 0.0F);
    }

    protected void backupGauges(float f)
    {
        float f1 = fm.getAltitude() * 3.28084F;
        float f2 = cvt(f1, 0.0F, 50000F, 0.0F, 1800F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt", 0.0F, f2, 0.0F);
        f1 = (fm.getAltitude() * 3.28084F) / 10000F;
        f2 = (float)(int)f1 * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_2", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Alt_3", 0.0F, 0.0F, f2);
        float f3 = 0.0F;
        float f4 = Pitot.Indicator((float)((Tuple3d) (fm.Loc)).z, fm.getSpeedKMH());
        f4 *= 0.53996F;
        if(f4 <= 50F)
            f3 = (f4 / 50F) * 15F;
        else
        if(f4 <= 100F)
            f3 = (f4 - 50F) + 15F;
        else
        if(f4 <= 150F)
            f3 = (f4 - 100F) * 1.2F + 65F;
        else
        if(f4 <= 200F)
            f3 = (f4 - 150F) * 0.9F + 125F;
        else
        if(f4 <= 300F)
            f3 = (f4 - 200F) * 0.6F + 170F;
        else
        if(f4 <= 400F)
            f3 = (f4 - 300F) * 0.45F + 230F;
        else
        if(f4 <= 700F)
            f3 = (f4 - 400F) * 0.3F + 275F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Speed", 0.0F, f3, 0.0F);
        float f5 = setNew.vspeed2 * 3.48F;
        f5 *= 60F;
        float f6 = Math.abs(f5);
        boolean flag = f5 < 0.0F;
        if(f6 <= 1000F)
            f2 = f6 * 0.07F;
        else
        if(f6 <= 2000F)
            f2 = (f6 - 1000F) * 0.035F + 70F;
        else
        if(f6 <= 4000F)
            f2 = (f6 - 2000F) * 0.0175F + 105F;
        else
        if(f6 <= 6000F)
            f2 = (f6 - 4000F) * 0.00875F + 140F;
        else
            f2 = 157.5F;
        if(flag)
            f2 = -f2;
        super.mesh.chunkSetAngles("Z_Z_Ins_VSpeed", 0.0F, f2, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_AH", 0.0F, setNew.bank, setNew.pitch);
        float f7 = setNew.ilsLoc * setNew.ilsLoc * (setNew.ilsLoc < 0.0F ? -1F : 1.0F);
        Cockpit.xyz[0] = -cvt(f7, -10000F, 10000F, -0.02F, 0.02F);
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Bank", Cockpit.xyz, Cockpit.ypr);
        float f8 = setNew.ilsGS * setNew.ilsGS * (setNew.ilsGS < 0.0F ? -1F : 1.0F);
        Cockpit.xyz[1] = -cvt(f8, -0.25F, 0.25F, -0.02F, 0.02F);
        Cockpit.xyz[0] = Cockpit.xyz[2] = 0.0F;
        Cockpit.ypr[0] = cvt(fm.Or.getKren(), -35F, 35F, -35F, 35F);
        Cockpit.ypr[1] = Cockpit.ypr[2] = 0.0F;
        mesh.chunkSetLocate("Z_Z_Ins_Climb", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkVisible("Z_Z_Ins_Bank", bHSIDL || bHSIILS || bHSIMAN || bHSINAV || bHSITAC || bHSITGT || bHSIUHF);
        mesh.chunkVisible("Z_Z_Ins_Climb", bHSIILS);
        f2 = (fm.M.fuel / 10000F) * 2.204623F;
        float f9 = (float)(int)f2 * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_1", f9 != 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_1", 0.0F, 0.0F, f9);
        f3 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_2", f3 != 0.0F || f9 != 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_2", 0.0F, 0.0F, f3);
        float f10 = (float)((int)(f2 * 100F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_Ins_Fuel_3", f10 != 0.0F || f3 != 0.0F || f9 != 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Fuel_3", 0.0F, 0.0F, f10);
        float f11 = (float)((F_14)aircraft()).Bingofuel / 1000F;
        f9 = (float)(int)f11 * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Bingo_1", 0.0F, 0.0F, f9);
        float f12 = fm.EI.engines[0].tWaterOut;
        float f13 = fm.EI.engines[1].tWaterOut;
        if(f12 < f13)
            f12 = f13;
        f9 = (float)((int)((f12 / 1000F) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_1", 0.0F, 0.0F, f9);
        f3 = (float)((int)((f12 / 1000F) * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Temp_2", 0.0F, 0.0F, f3);
        float f14 = cvt(fm.EI.engines[0].getRPM(), 0.0F, 4080F, 0.0F, 100F) / 10F;
        float f15 = cvt(fm.EI.engines[1].getRPM(), 0.0F, 4080F, 0.0F, 100F) / 10F;
        if(f14 < f15)
            f14 = f15;
        if(f14 > 9.9F)
            f14 = 9.9F;
        f9 = (float)((int)f14 % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_1", 0.0F, 0.0F, f9);
        f3 = (float)((int)(f14 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_RPM_2", 0.0F, 0.0F, f3);
        Motor motor = fm.EI.engines[0];
        float f16 = Motor.tmpF;
        Motor motor1 = fm.EI.engines[1];
        float f17 = Motor.tmpF;
        if(f16 < f17)
            f16 = f17;
        f9 = (float)((int)(f16 * 10F * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_1", 0.0F, 0.0F, f9);
        f3 = (float)((int)(f16 * 10F * 100F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_FF_2", 0.0F, 0.0F, f3);
        float f18 = cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 80F);
        float f19 = cvt(fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 80F);
        super.mesh.chunkSetAngles("Z_Z_Ins_EGT_1", -f18, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_EGT_2", f19, 0.0F, 0.0F);
        float f20 = 1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness();
        float f21 = 1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness();
        if(f20 < f21)
            f20 = f21;
        f9 = (float)(int)(f20 / 10F) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_1", 0.0F, 0.0F, f9);
        f3 = (float)((int)((f20 / 10F) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_Ins_Oil_2", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_Ins_H", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_M", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        super.mesh.chunkSetAngles("Z_Z_Ins_S", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        f2 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_Ins_Compass", f2, 0.0F, 0.0F);
    }

    protected void movescreenfuel()
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.06133F;
        Cockpit.xyz[1] = -0.00827F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04933F;
        Cockpit.xyz[1] = -0.00827F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.05600F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.00933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //L WING
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04133F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.00933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //R WING
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00933F;
        Cockpit.xyz[1] = -0.00866F;
        Cockpit.xyz[2] = -0.01466F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //TANK 4 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.00133F;
        Cockpit.xyz[1] = -0.00866F;
        Cockpit.xyz[2] = -0.01466F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //TANK 4 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL CENTER 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.009333F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL CENTER 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0280F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL RIGHT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0440F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL RIGHT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.042665F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL LEFT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.026666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //EXTERNAL LEFT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.009333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //FEED R 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0026666F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.009333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //FEED R 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0280F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //FEED L 1
        Cockpit.xyz[0] = -0.00266F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0280F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //FEED L 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.049332F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //BINGO 1
        Cockpit.xyz[0] = -0.05866F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.0520F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //BINGO 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //TANK 1 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.002666F;
        Cockpit.xyz[1] = -0.008266F;
        Cockpit.xyz[2] = 0.05333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //TANK 1 2
        float f = 0.0F;
        totalfuelInt = (fm.M.fuel / 1000F) * 2.204623F;  // 1000 lbs unit
        float tankW = 0.0F;  // Tank Wing (one side)
        if(totalfuelInt > 12.2F)
            tankW = (totalfuelInt - 12.2F) / 2.0F;
        float f3 = (float)((int)(tankW * 10F) % 10) * 36F;
        if(f3 == 0.0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(tankW * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(tankW * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", true);
        super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f5);
        float tank1 = 0F;  // tank1, forward fuselage
        float tank4 = 0F;  // tank4, aft fuselage
        if(totalfuelInt > 12.2F)
        {
            tank1 = 4.7F;
            tank4 = 4.4F;
        }
        else if(totalfuelInt > 11.9F)
        {
            tank1 = 4.7F - (totalfuelInt - 11.9F);
            tank4 = 4.4F;
        }
        else if(totalfuelInt > 3.1F)
        {
            tank1 = tank4 = (totalfuelInt - 3.1F) / 2F;
        }
        f = (float)(int)tank4 * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(tank4 * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank4 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank4 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f5);
        f = (float)(int)tank1 * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(tank1 * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tank1 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tank1 * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f5);
        // NO Center external Droptank
        super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
//        float f8 = 0.0F;
//        if(((F_14)aircraft()).bHasCenterTank && !fm.M.bFuelTanksDropped)
//            f8 = (((F_14)aircraft()).checkfuel(0) / 1000F) * 2.204623F;
//        f = (float)(int)f8 * 36F;
//        if(f == 0.0F)
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
//        else
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
//        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f);
//        f3 = (float)((int)(f8 * 10F) % 10) * 36F;
//        if(f3 == 0.0F && f == 0.0F)
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
//        else
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
//        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f3);
//        f4 = (float)((int)(f8 * 100F) % 10) * 36F;
//        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false);
//        else
//            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", true);
//        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f4);
//        f5 = (float)((int)(f8 * 1000F) % 10) * 36F;
//        super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
//        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f5);
//        super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        float tankExL = 0.0F;  // Wing external Droptank (one side)
        if(((F_14)aircraft()).bHasWingTank && !fm.M.bFuelTanksDropped)
//            if(((F_14)aircraft()).bHasCenterTank)
//                f9 = (((F_14)aircraft()).checkfuel(1) / 1000F) * 2.204623F;
//            else
                tankExL = (((F_14)aircraft()).checkfuel(0) / 1000F) * 2.204623F;
        f = (float)(int)tankExL * 36F;
        if(f == 0.0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(tankExL * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f3);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(tankExL * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", false);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", false);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        }
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f4);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f4);
        f5 = (float)((int)(tankExL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f5);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f5);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
        float feedL = 1.5F;  // Feed tank Left
        if(totalfuelInt < 3.0F)
            feedL = totalfuelInt / 2F;
        if(feedL < 0.0F)
            feedL = 0.0F;
        f = (float)(int)feedL * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(feedL * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedL * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedL * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f5);
        float feedR = 1.6F;  // Feed tank Right
        if(totalfuelInt < 3.1F)
        {
            if(totalfuelInt > 3.0F)
                feedR = totalfuelInt - 1.5F;
            else
                feedR = totalfuelInt / 2F;
        }
        if(feedR < 0.0F)
            feedR = 0.0F;
        f = (float)(int)feedR * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(feedR * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(feedR * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(feedR * 1000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f5);
//        totalfuelExt = totalfuelInt + tankExL * 2.0F + tankExC;
        totalfuelExt = totalfuelInt + tankExL * 2.0F;
        timefuel = Time.current();
        f = (float)(int)(totalfuelExt / 10F) * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(totalfuelExt) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(totalfuelExt * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(totalfuelExt * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F && f5 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f5);
        float f12 = (float)((int)(totalfuelExt * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f12);
        float f13 = (float)((F_14)aircraft()).Bingofuel / 1000F;
        f = (float)(int)f13 * 36F;
        if(f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f);
        f3 = (float)((int)(f13 * 10F) % 10) * 36F;
        if(f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f13 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f13 * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f5);
    }

    protected void movescreenfuelflow()
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.009333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //BINGO RANGE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //BINGO RANGE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //BINGO DURATION 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.0440F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.0400F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //BINGO DURATION 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00933F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //BEST M RANGE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.001333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //BEST M RANGE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03333F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //BEST M ENDURANCE 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04400F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //BEST M ENDURANCE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.03333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //TIME 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.022666F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //TIME 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.001333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //FUEL REMAIN 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.009333F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.005333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //FUEL REMAIN 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.017333F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.0200F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //BASIC 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.006666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.0200F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //BASIC 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.017333F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.02933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //FUEL 1
        Cockpit.xyz[0] = 0.006666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.02933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //FUEL 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.01733F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.03600F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //STORE 1
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.03600F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //STORE 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.01733F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.04933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 1
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.00666F;
        Cockpit.xyz[1] = -0.009333F;
        Cockpit.xyz[2] = -0.04933F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //TOTAL 2
        float tankExL = 0.0F;
        if(((F_14)aircraft()).bHasWingTank && !fm.M.bFuelTanksDropped)
//            if(((F_14)aircraft()).bHasCenterTank)
//                tankExL = (((F_14)aircraft()).checkfuel(1) / 1000F) * 2.204623F;
//            else
                tankExL = (((F_14)aircraft()).checkfuel(0) / 1000F) * 2.204623F;
//        float tankExC = 0.0F;
//        if(((F_14)aircraft()).bHasCenterTank && !fm.M.bFuelTanksDropped)
//            tankExC = (((F_14)aircraft()).checkfuel(0) / 1000F) * 2.204623F;
//        float Sumfuel = ((fm.M.fuel / 1000F) * 2.204623F + f * 2.0F + f1) / 10F;
        float Sumfuel = ((fm.M.fuel / 1000F) * 2.204623F + tankExL * 2.0F) / 10F;
        float Sfuel = (Sumfuel * 10000F) / 2.204623F;
        float flowrate = ((totalfuelInt * 10000F) / 2.204623F - Sfuel) / (float)((Time.current() - timefuel) / 1000L);
        float distance = becondistance / 1000F;
        float Duration = Sfuel / flowrate / 36000F;
        float Currange = Duration * 10F * fm.getSpeedKMH();
        float rangeremain = Currange - distance;
        if(rangeremain < 0.0F)
            rangeremain = 0.0F;
        Duration = rangeremain / (fm.getSpeedKMH() * 10F);
        Sumfuel = Duration * flowrate * 36000F;
        float f9 = (Sumfuel / 10000F) * 2.204623F;
        float f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f10);
        float f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f11);
        float f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f12);
        float f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f13);
        float f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f14);
        float clock = (distance / fm.getSpeedKMH()); //Clock
        f9 = clock;
        if(clock < 0.0F)
            clock = 0.0F;
        f10 = (float)((int)f9 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f11);
        f11 = (float)((int)(f9 * 100F) % 10) * 0.6F;
        f12 = (float)((int)f11 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f12);
        f12 = (float)((int)(f9 * 1000F) % 10) * 0.6F;
        f13 = (float)((int)f12 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f13);
        float bingo = (float)((F_14)aircraft()).Bingofuel / 2.204623F;
        Duration = ((Sfuel - bingo) / flowrate) / 36000F;
        if(Duration < 0F)
            Duration = 0F;
        f9 = Duration;
        f10 = (float)((int)f9 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f11);
        f11 = (float)((int)(f9 * 100F) % 10) * 0.6F;
        f12 = (float)((int)f11 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f12);
        f12 = (float)((int)(f9 * 1000F) % 10) * 0.6F;
        f13 = (float)(int)f12 * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f13);
        Currange = Duration * 10F * fm.getSpeedKMH() * 0.539956803455F; //Bingo Range
        f9 = Currange / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f11);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f14);
        float duspeed = 700F * cvt(fm.getAltitude(), 0F, 10000F, 1.0F, 1.3F);
        float thrustCo = cvt(fm.getAltitude(), 0F, 10000F, 1.0F, 0.7F);
        float duthrust = cvt(duspeed, 600F, 800F, 0.35F, 0.50F) * thrustCo;
        float fuelflowvalue = cvt(duthrust, 0.0F , 1.00F, 0.09F, 1.20F);
        Duration = (Sfuel - bingo) / fuelflowvalue/36000; //best mach duration
        f9 = Duration;
        f10 = (float)((int)f9 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f11);
        f11 = (float)((int)(f9 * 100F) % 10) * 0.6F;
        f12 = (float)((int)f11 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f12);
        f12 = (float)((int)(f9 * 1000F) % 10) * 0.6F;
        f13 = (float)((int)f12 % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f13);
        Currange = Duration * 10F * duspeed * 0.539956803455F; //best mach range
        f9 = Currange / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f11);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f14);
        float basic = fm.M.massEmpty; // basic weight
        f9 = (basic * 2.204623F) / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f11);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f14);
        float fuel = fm.M.fuel; // fuel internal
        f9 = (fuel * 2.204623F) / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f11);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f14);
        float store = fm.M.mass - basic - fuel; //store
        f9 = (store * 2.204623F) / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f11);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f14);
        float total = fm.M.mass; //total
        f9 = (total * 2.204623F) / 10000F;
        f10 = (float)((int)f9 % 10) * 36F;
        if(f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f10);
        f11 = (float)((int)(f9 * 10F) % 10) * 36F;
        if(f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f11);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        f12 = (float)((int)(f9 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f12);
        f13 = (float)((int)(f9 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f11 == 0.0F && f10 == 0.0F && f13 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f13);
        f14 = (float)((int)(f9 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_4", 0.0F, 0.0F, f14);
    }

    protected void movescreenengines()
    {
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03999F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM21_1", Cockpit.xyz, Cockpit.ypr); //Inlet Temp 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03999F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM22_1", Cockpit.xyz, Cockpit.ypr); //Inlet Temp 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM23_1", Cockpit.xyz, Cockpit.ypr); //N1 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.03333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM24_1", Cockpit.xyz, Cockpit.ypr); //N1 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.026666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM25_1", Cockpit.xyz, Cockpit.ypr); //N2 RPM 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.026666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM26_1", Cockpit.xyz, Cockpit.ypr); //N2 RPM 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM31_1", Cockpit.xyz, Cockpit.ypr); //EGT 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00840F;
        Cockpit.xyz[2] = 0.020F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM32_1", Cockpit.xyz, Cockpit.ypr); //EGT 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.013333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM33_1", Cockpit.xyz, Cockpit.ypr); //FF 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.013333F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM34_1", Cockpit.xyz, Cockpit.ypr); //FF 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM27_1", Cockpit.xyz, Cockpit.ypr); //NOZ POS 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM28_1", Cockpit.xyz, Cockpit.ypr); //NOZ POS 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM29_1", Cockpit.xyz, Cockpit.ypr); //OIL PRES 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.008533F;
        Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM210_1", Cockpit.xyz, Cockpit.ypr); //OIL PRES 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.05066F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM211_1", Cockpit.xyz, Cockpit.ypr); //THRUST 1
        Cockpit.xyz[0] = 0.03999F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM35_1", Cockpit.xyz, Cockpit.ypr); //THRUST 2
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.03999F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM212_1", Cockpit.xyz, Cockpit.ypr); //THRUST2 1
        Cockpit.xyz[0] = -0.05066F;
        Cockpit.xyz[1] = -0.008933F;
        Cockpit.xyz[2] = -0.006666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM36_1", Cockpit.xyz, Cockpit.ypr); //THRUST2 2
        resetYPRmodifier();
        Cockpit.xyz[0] = 0.04533F;
        Cockpit.xyz[1] = -0.00880F;
        Cockpit.xyz[2] = -0.010666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM37_1", Cockpit.xyz, Cockpit.ypr); //FAN VIB 1
        resetYPRmodifier();
        Cockpit.xyz[0] = -0.04533F;
        Cockpit.xyz[1] = -0.00880F;
        Cockpit.xyz[2] = -0.010666F;
        super.mesh.chunkSetLocate("Z_Z_HDD_NUM38_1", Cockpit.xyz, Cockpit.ypr); //FAN VIB 2
        float inlettempL = Atmosphere.temperature(fm.getAltitude());
        float inlettempR = inlettempL;
        float f2 = inlettempL / 10F;
        float f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_2", 0.0F, 0.0F, f3);
        float f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM21_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM21_3", 0.0F, 0.0F, f4);
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM22_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM22_3", 0.0F, 0.0F, f4);
        float fuelinteltempL = fm.EI.engines[0].tWaterOut;
        float fuelinteltempR = fm.EI.engines[1].tWaterOut;
        float N1L = cvt(fm.EI.engines[0].getRPM(), 0.0F, 4080F, 0.0F, 100F);
        f2 = N1L / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM23_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM23_3", 0.0F, 0.0F, f4);
        float N1R = cvt(fm.EI.engines[1].getRPM(), 0.0F, 4080F, 0.0F, 100F);
        f2 = N1R / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM24_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM24_3", 0.0F, 0.0F, f4);
        float N2L = cvt(fm.EI.engines[0].getRPM() * 0.8529412F, 0.0F, 3480F, 0.0F, 100F);
        f2 = N2L / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM25_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM25_3", 0.0F, 0.0F, f4);
        float f10 = cvt(fm.EI.engines[1].getRPM() * 0.8529412F, 0.0F, 3480F, 0.0F, 100F);
        f2 = f10 / 10F;
        if(f2 > 9.9F)
            f2 = 9.9F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM26_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM26_3", 0.0F, 0.0F, f4);
        float extempL = cvt(fm.EI.engines[0].tOilOut, 0.0F, 120F, 10F, 838F);
        f2 = extempL / 100F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_3", 0.0F, 0.0F, f4);
        float f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f12 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM31_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM31_4", 0.0F, 0.0F, f12);
        float extempR = cvt(fm.EI.engines[1].tOilOut, 0.0F, 120F, 10F, 838F);
        f2 = extempR / 100F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_3", 0.0F, 0.0F, f4);
        f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f12 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM32_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM32_4", 0.0F, 0.0F, f12);
        float FFL = fm.EI.engines[0].tmpF;
        f2 = FFL * 100F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_3", 0.0F, 0.0F, f4);
        f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f12 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM33_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM33_4", 0.0F, 0.0F, f12);
        float FFR = fm.EI.engines[1].tmpF;
        f2 = FFR * 100F;
        f3 = (float)((int)f2 % 10) * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_3", 0.0F, 0.0F, f4);
        f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F && f12 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM34_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM34_4", 0.0F, 0.0F, f12);
        f2 = -((F_14)aircraft()).fNozzleOpenL * 1.1F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM27_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM27_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM27_3", 0.0F, 0.0F, f4);
        f2 = -((F_14)aircraft()).fNozzleOpenR * 1.1F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM28_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM28_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM28_3", 0.0F, 0.0F, f4);
        float oilpressL = 1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness();
        f2 = oilpressL;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM29_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM29_3", 0.0F, 0.0F, f4);
        float oilpressR = 1.0F + 0.05F * fm.EI.engines[1].tOilOut * fm.EI.engines[1].getReadyness();
        f2 = oilpressR;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM210_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM210_3", 0.0F, 0.0F, f4);
        float thrustL = fm.EI.engines[0].getPowerOutput() < 1.0F ? cvt(fm.EI.engines[0].getPowerOutput(), 0.0F, 1.0F, 0.0F, 4800F) : cvt(fm.EI.engines[0].getPowerOutput(), 1.0F, 1.1F, 4800F, 7950F);
        f2 = thrustL / 10000F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM211_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM211_3", 0.0F, 0.0F, f4);
        f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_2", 0.0F, 0.0F, f12);
        float f19 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f4 == 0.0F && f3 == 0.0F && f19 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM35_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_3", 0.0F, 0.0F, f19);
        float f20 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM35_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM35_4", 0.0F, 0.0F, f20);
        float thrustR = fm.EI.engines[1].getPowerOutput() < 1.0F ? cvt(fm.EI.engines[1].getPowerOutput(), 0.0F, 1.0F, 0.0F, 4800F) : cvt(fm.EI.engines[1].getPowerOutput(), 1.0F, 1.1F, 4800F, 7950F);
        f2 = thrustR / 10000F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        if(f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM212_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM212_3", 0.0F, 0.0F, f4);
        f12 = (float)((int)(f2 * 100F) % 10) * 36F;
        if(f12 == 0.0F && f4 == 0.0F && f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_2", 0.0F, 0.0F, f12);
        f19 = (float)((int)(f2 * 1000F) % 10) * 36F;
        if(f12 == 0.0F && f4 == 0.0F && f3 == 0.0F && f19 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM36_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_3", 0.0F, 0.0F, f19);
        f20 = (float)((int)(f2 * 10000F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM36_4", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM36_4", 0.0F, 0.0F, f20);
        float shake = (fm.EI.engines[0].w / fm.EI.engines[0].wMax) * fm.EI.engines[0].thrustMax * (float)Math.sqrt(fm.getSpeed() / 94F);
        float enginevibL = cvt(shake, 0.0F, fm.EI.engines[0].thrustMax, 0.05F, 0.21F);
        f2 = enginevibL * 10F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM37_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM37_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM37_4", false);
        shake = (fm.EI.engines[1].w / fm.EI.engines[1].wMax) * fm.EI.engines[1].thrustMax * (float)Math.sqrt(fm.getSpeed() / 94F);
        float enginevibR = cvt(shake, 0.0F, fm.EI.engines[1].thrustMax, 0.05F, 0.21F);
        f2 = enginevibR * 10F;
        f3 = (float)(int)f2 * 36F;
        if(f3 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", false);
        else
            super.mesh.chunkVisible("Z_Z_HDD_NUM38_2", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_2", 0.0F, 0.0F, f3);
        f4 = (float)((int)(f2 * 10F) % 10) * 36F;
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_3", true);
        super.mesh.chunkSetAngles("Z_Z_HDD_NUM38_3", 0.0F, 0.0F, f4);
        super.mesh.chunkVisible("Z_Z_HDD_NUM38_4", false);
        float CPRL = fm.EI.engines[0].getManifoldPressure();
        float CPRR = fm.EI.engines[1].getManifoldPressure();
        float TDPL = fm.EI.engines[0].getControlCompressor();
        float TDPR = fm.EI.engines[1].getControlCompressor();
    }

    protected void Navscreen(float f)
    {
        float f1 = -normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("HDD_Nav_Comp", 0.0F, f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_N", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_W", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_E", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_S", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_3", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_6", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_12", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_15", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_21", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_24", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_30", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_33", 0.0F, -f1, 0.0F);
        super.mesh.chunkSetAngles("HDD_Nav_Pointer", 0.0F, setNew.radioCompassAzimuth.getDeg(f1 * 0.02F) + 90F, 0.0F);
        resetYPRmodifier();
        float f2 = setNew.hsiLoc * setNew.hsiLoc * (setNew.hsiLoc < 0.0F ? -1F : 1.0F);
        if(bHSIILS)
            Cockpit.xyz[0] = cvt(f2, -20000F, 20000F, 0.02F, -0.02F);
        else
            Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = Cockpit.xyz[2] = 0.0F;
        super.mesh.chunkSetLocate("HDD_Nav_DIV", Cockpit.xyz, Cockpit.ypr);
        super.mesh.chunkVisible("HDD_Nav_DIV", bHSIILS || bHSIMAN || bHSINAV);
        resetYPRmodifier();
        float f3 = getBeaconDistance();
        if(!useRealisticNavigationInstruments())
        {
            WayPoint waypoint = fm.AP.way.curr();
            if(waypoint != null)
            {
                Point3d point3d = new Point3d();
                Vector3d vector3d = new Vector3d();
                waypoint.getP(point3d);
                vector3d.sub(point3d, fm.Loc);
                f3 = (float)Math.sqrt(vector3d.x * vector3d.x + vector3d.y * vector3d.y);
            }
        }
        becondistance = f3;
        float f4 = f3 / 1852F / 1000F;
        float f5 = (float)(int)f4 * 36F;
        if(f5 == 0.0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_1", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_1", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_1", 0.0F, 0.0F, f5);
        float f6 = (float)((int)(f4 * 10F) % 10) * 36F;
        if(f6 == 0.0F && f5 == 0.0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_2", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_2", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_2", 0.0F, 0.0F, f6);
        float f7 = (float)((int)(f4 * 100F) % 10) * 36F;
        if(f7 == 0.0F && f6 == 0.0F && f5 == 0.0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_3", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_3", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_3", 0.0F, 0.0F, f7);
        float f8 = (float)((int)(f4 * 1000F) % 10) * 36F;
        if(f7 == 0.0F && f6 == 0.0F && f5 == 0.0F && f8 == 0.0F)
            super.mesh.chunkVisible("Z_Z_Nav_SDT_4", false);
        else
            super.mesh.chunkVisible("Z_Z_Nav_SDT_4", true);
        super.mesh.chunkSetAngles("Z_Z_Nav_SDT_4", 0.0F, 0.0F, f8);
        if(useRealisticNavigationInstruments())
        {
            super.mesh.chunkVisible("HDD_Nav_Tacan", bHSIILS || bHSITAC);
            resetYPRmodifier();
            Cockpit.xyz[2] = cvt(f3, 1852F, 92600F, -0.044F, -0.01F);
            Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
            super.mesh.chunkSetLocate("HDD_Nav_Tacan", Cockpit.xyz, Cockpit.ypr);
        } else
        {
            super.mesh.chunkVisible("HDD_Nav_Tacan", bHSINAV);
            resetYPRmodifier();
            Cockpit.xyz[2] = cvt(f3, 1852F, 92600F, -0.04F, 0.0F);
            Cockpit.xyz[0] = Cockpit.xyz[1] = 0.0F;
            super.mesh.chunkSetLocate("HDD_Nav_Tacan", Cockpit.xyz, Cockpit.ypr);
        }
    }

    protected void HUD(float f)
    {
        boolean flag = false;
        boolean flag1 = false;
        if(!setNew.isBatteryOn && !setNew.isGeneratorAllive)
            flag1 = false;
        else
            flag1 = true;
        super.mesh.chunkVisible("Z_Z_HUD_Screen", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_PITCH", flag1);
        for(int i = 1; i < 5; i++)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_" + i, flag1);

        super.mesh.chunkVisible("Z_Z_HUD_AOA_1", flag1);
        super.mesh.chunkVisible("Z_Z_HUD_AOA_2", flag1);
        for(int j = 1; j < 5; j++)
            super.mesh.chunkVisible("Z_Z_HUD_VP_" + j, flag1);

        for(int k = 1; k < 6; k++)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_" + k, flag1);

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
            return;
        float f1 = calculateMach();
        float f2 = (float)(int)f1 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Mach_2", 0.0F, 0.0F, f2);
        float f3 = (fm.getSpeedKMH() * 0.5399568F) / 1000F;
        float f4 = (float)(int)f3 * 36F;
        if(f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_1", 0.0F, 0.0F, f4);
        float f5 = (float)((int)(f3 * 10F) % 10) * 36F;
        if(f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_2", 0.0F, 0.0F, f5);
        f2 = (float)((int)(f3 * 100F) % 10) * 36F;
        if(f2 == 0.0F && f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Speed_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_3", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f3 * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Speed_4", 0.0F, 0.0F, f2);
        f3 = (fm.getAltitude() * 3.28084F) / 10000F;
        f4 = (float)(int)f3 * 36F;
        if(f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_1", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f3 * 10F) % 10) * 36F;
        if(f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_2", 0.0F, 0.0F, f5);
        f2 = (float)((int)(f3 * 100F) % 10) * 36F;
        if(f2 == 0.0F && f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_3", 0.0F, 0.0F, f2);
        float f6 = (float)((int)(f3 * 1000F) % 10) * 36F;
        if(f6 == 0.0F && f2 == 0.0F && f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_Alt_4", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_4", 0.0F, 0.0F, f5);
        float f7 = (float)((int)(f3 * 10000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_Alt_5", 0.0F, 0.0F, f5);
        f3 = Math.abs((setNew.vspeed2 * 3.48F) / 1000F);
        f4 = (float)(int)f3 * 36F;
        if(f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_VP_1", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_1", 0.0F, 0.0F, f4);
        f5 = (float)((int)(f3 * 10F) % 10) * 36F;
        if(f5 == 0.0F && f4 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_VP_2", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_2", 0.0F, 0.0F, f5);
        f2 = (float)((int)(f3 * 100F) % 10) * 36F;
        if(f2 == 0.0F && f5 == 0.0F)
            super.mesh.chunkVisible("Z_Z_HUD_VP_3", false);
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_3", 0.0F, 0.0F, f2);
        f6 = (float)((int)(f3 * 1000F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_VP_4", 0.0F, 0.0F, f6);
        float f8 = fm.getOverload();
        f2 = (float)(int)Math.abs(f8) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(Math.abs(f8) * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_G_2", 0.0F, 0.0F, f2);
        float f9 = normalizeDegree(setNew.azimuth.getDeg(f) + 90F);
        super.mesh.chunkSetAngles("Z_Z_HUD_HDG", 0.0F, f9, 0.0F);
        f5 = setNew.pitch;
        if(f5 > 90F)
            for(; f5 > 90F; f5 -= 360F);
        else
        if(f5 < -90F)
            for(; f5 < -90F; f5 += 360F);
        f5 -= 90F;
        f5 = -f5;
        int l = (int)f5 / 5;
        for(int i1 = l - 3; i1 <= l + 2; i1++)
            if(i1 >= 0 && i1 < 37)
            {
                super.mesh.chunkVisible("Z_Z_HUD_PITCH", true);
                super.mesh.chunkSetAngles("Z_Z_HUD_PITCH", 0.0F, setNew.bank, -setNew.pitch);
            }

        Cockpit.xyz[0] = 0.0F;
        boolean flag2 = false;
        if(fm.Gears.nOfGearsOnGr < 1 || f4 > 10F)
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
        f2 = normalizeDegree(setNew.bank);
        if(f2 > 50F && f2 < 310F)
            super.mesh.chunkVisible("Z_Z_HUD_Bank", false);
        else
            super.mesh.chunkVisible("Z_Z_HUD_Bank", true);
        super.mesh.chunkSetAngles("Z_Z_HUD_BANK", 0.0F, f2, 0.0F);
        f1 = setNew.fpmPitch / 10F;
        f2 = (float)(int)f1 * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_1", 0.0F, 0.0F, f2);
        f2 = (float)((int)(f1 * 10F) % 10) * 36F;
        super.mesh.chunkSetAngles("Z_Z_HUD_AOA_2", 0.0F, 0.0F, f2);
        float f10 = getBeaconDistance() / 1852F;
        if(f10 > 99.9F)
            f10 = 99.9F;
        if((bHSIILS || bHSITAC) && flag1)
        {
            super.mesh.chunkVisible("Z_Z_HUD_TCN_1", f10 >= 10F);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_2", true);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_3", true);
            float f11 = (float)Math.floor(f10 / 10F) * 36F;
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_1", 0.0F, 0.0F, f11);
            f11 = (float)(Math.floor(f10) * 36D);
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_2", 0.0F, 0.0F, f11);
            f11 = (float)(Math.floor((double)(f10 * 10F) % 10D) * 36D);
            super.mesh.chunkSetAngles("Z_Z_HUD_TCN_3", 0.0F, 0.0F, f11);
        } else
        {
            super.mesh.chunkVisible("Z_Z_HUD_TCN_1", false);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_2", false);
            super.mesh.chunkVisible("Z_Z_HUD_TCN_3", false);
        }
    }

    protected void drawSound(float f)
    {
        boolean flag = false;
        if(aoaWarnFX != null)
            if(setNew.fpmPitch >= 9.7F && setNew.fpmPitch < 12F && fm.Gears.nOfGearsOnGr < 1)
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
            if(setNew.fpmPitch >= 12F && setNew.fpmPitch < 14F && fm.Gears.nOfGearsOnGr < 1)
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
            if(setNew.fpmPitch >= 14F && setNew.fpmPitch < 15.5F && fm.Gears.nOfGearsOnGr < 1)
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
            if(setNew.fpmPitch >= 15.4F && fm.Gears.nOfGearsOnGr < 1)
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
            float f1 = fm.getAltitude() - (float)Engine.land().HQ_Air(fm.Loc.x, fm.Loc.y);
            if(f1 < 550F && fm.getVertSpeed() < -40F || f1 < 100F && fm.CT.getGear() < 0.999999F)
            {
                if(fm.Or.getPitch() - 360F < -22F)
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
        super.mesh.chunkVisible("L_SpBr", fm.CT.getAirBrake() > 0.1F);
        super.mesh.chunkVisible("L_LFire", fm.AS.astateEngineStates[0] > 2);
        super.mesh.chunkVisible("L_RFire", fm.AS.astateEngineStates[1] > 2);
        if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != '\007')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearA");
            LGearStat = '\007';
        } else
        if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != '\006')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearNR");
            LGearStat = '\006';
        } else
        if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != '\005')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearNL");
            LGearStat = '\005';
        } else
        if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() > 0.95F && LGearStat != '\004')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearN");
            LGearStat = '\004';
        } else
        if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != '\003')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearLR");
            LGearStat = '\003';
        } else
        if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() > 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != '\002')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearR");
            LGearStat = '\002';
        } else
        if(fm.CT.getGearL() > 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != '\001')
        {
            super.mesh.materialReplace("WlightGear", "WlightGearL");
            LGearStat = '\001';
        } else
        if(fm.CT.getGearL() < 0.95F && fm.CT.getGearR() < 0.95F && fm.CT.getGearC() < 0.95F && LGearStat != 0)
        {
            super.mesh.materialReplace("WlightGear", "WlightGear");
            LGearStat = '\0';
        }
        if(fm.CT.FlapsControlSwitch == 0 && LFlapStat != 0)
        {
            super.mesh.materialReplace("WlightFlap", "WlightFlap");
            LFlapStat = '\0';
        } else
        if(fm.CT.FlapsControlSwitch == 1)
        {
            if(LFlapStat != '\001' && !((F_14)aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapH");
                LFlapStat = '\001';
            } else
            if(LFlapStat != '\005' && ((F_14)aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapHY");
                LFlapStat = '\005';
            }
        } else
        if(fm.CT.FlapsControlSwitch == 2)
            if(LFlapStat != '\002' && !((F_14)aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapF");
                LFlapStat = '\002';
            } else
            if(LFlapStat != '\n' && ((F_14)aircraft()).bForceFlapmodeAuto)
            {
                super.mesh.materialReplace("WlightFlap", "WlightFlapFY");
                LFlapStat = '\n';
            }
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        if(fm.CT.getGear() < 0.1F)
        {
            flag = false;
            flag1 = false;
            flag2 = false;
        } else
        if(fm.getAOA() > 8.9F)
        {
            flag = true;
            flag1 = false;
            flag2 = false;
        } else
        if(fm.getAOA() > 8.1F)
        {
            flag = true;
            flag1 = true;
            flag2 = false;
        } else
        if(fm.getAOA() > 6.9F)
        {
            flag = false;
            flag1 = true;
            flag2 = false;
        } else
        if(fm.getAOA() > 6.4F)
        {
            flag = false;
            flag1 = true;
            flag2 = true;
        } else
        {
            flag = false;
            flag1 = false;
            flag2 = true;
        }
        super.mesh.chunkVisible("L_AOAH", flag);
        super.mesh.chunkVisible("L_AOAL", flag2);
        super.mesh.chunkVisible("L_AOAM", flag1);
    }

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
        for(i = 0; i < TypeSupersonic.fMachAltX.length && TypeSupersonic.fMachAltX[i] <= f; i++);
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
        return fm.getSpeedKMH() / getMachForAlt(fm.getAltitude());
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
            light1.light.setEmit(0.0075F, 0.7F);
            light2.light.setEmit(0.0075F, 0.7F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    private void retoggleLight()
    {
    }

    protected float getNDBDist()
    {
        int i = fm.AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (fm)).actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            tmpV.sub(actor.pos.getAbsPoint(), fm.Loc);
            return (float)(tmpV.length() * 0.0010000000474974513D) / 1.853F;
        }
    }

    protected float getNDBDirection()
    {
        int i = fm.AS.getBeacon();
        if(i == 0)
        {
            return 0.0F;
        } else
        {
            ArrayList arraylist = Main.cur().mission.getBeacons(((Interpolate) (fm)).actor.getArmy());
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
    }

    protected String HUD1[];
    private float testfuel;
    private LightPointActor light1;
    private LightPointActor light2;
    private ArrayList radarPlane;
    private ArrayList radarLock;
    private ArrayList radarground;
    private ArrayList victim;
    private long t2;
    private float x;
    private float y;
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
    boolean ground;
    boolean left;
    boolean right;
    long t3;
    private long tw;
    private long t4;
    private float range;
    protected float offset;
    protected int targetnum;
    private long ts;
    private float totalfuelInt;  // Total fuel Internal
    private float totalfuelExt;  // Total fuel Internal + External Droptanks
    private long timefuel;
    private float becondistance;
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
    private int oldleftscreen;
    private int oldrightscreen;
    private int oldhddnav;
    private char LFlapStat;
    private char LGearStat;



}