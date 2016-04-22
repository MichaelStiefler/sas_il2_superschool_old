////////////////////////////////////////////////////////////////////////////////////////
// By PAL, from MiG-21 Source - ***3rd Generation***
// Removed MoveSteering(float f) method to SuperClass, for proper Passive Steering (DiffBrakes=3)
// Fail Safe version of SirenaWarning(), now is Range dependant SirenaWarning(float range)
// Method playSirenaWarning was not doing anything. Corrected.   
////////////////////////////////////////////////////////////////////////////////////////

// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 05/10/2015 02:13:47 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MIG_21S.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            MIG_21, Chute, PaintSchemeFMParMiG21, TypeGuidedMissileCarrier, 
//            TypeCountermeasure, TypeThreatDetector, Cockpit, Aircraft, 
//            NetAircraft, EjectionSeat

public class MIG_21S extends MIG_21
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public MIG_21S()
    {
        counter = 0;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        pylonOccupied = false;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        super.bHasSK1Seat = false;
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();
        if((getBulletEmitterByHookName("_ExternalDev17") instanceof PylonGP9) || (getBulletEmitterByHookName("_ExternalDev02") instanceof MiG21Pylon))
        {
            pylonOccupied = true;
            ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx = ((FlightModelMain) (super.FM)).Sq.dragAirbrakeCx / 2.0F;
        }
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        super.k14Mode++;
        if(super.k14Mode > 1)
            super.k14Mode = 0;
        if(super.k14Mode == 0)
        {
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed");
        } else
        if(super.k14Mode == 1)
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Gyro");
            else
            if(super.k14Mode == 2 && ((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Gyro");
        return true;
    }

    public void rareAction(float f, boolean flag)
    {
        int i = counter++ % 5;
        super.rareAction(f, flag);
    }

    public void update(float f)
    {
        super.update(f);
        computeR11F2S300_AB();
        if(Config.isUSE_RENDER())
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null && Main3D.cur3D().cockpits[0].cockpitDimControl)
            {
                hierMesh().chunkVisible("Head1_D0", false);
                hierMesh().chunkVisible("Glass_Head1_D0", true);
            } else
            {
                hierMesh().chunkVisible("Head1_D0", true);
                hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteMiG21/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-7D, 0.0D, 0.59999999999999998D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl)
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() > 600F || ((FlightModelMain) (super.FM)).CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                ((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        guidedMissileUtils.update();
    }

    public void computeR11F2S300_AB()
    {
        if(((FlightModelMain) (super.FM)).EI.engines[0].getThrustOutput() > 1.001F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() > 5)
            ((FlightModelMain) (super.FM)).producedAF.x += 22800D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            if(f > 17F)
            {
                f1 = 20F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (0.0329824F * f3 - 0.647042F * f2) + 2.64429F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1500F, -140F);
        float f2 = Math.max(-f * 1500F, -100F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);			
//By PAL, center wheel when deploying or retracting:
		hiermesh.chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F);		
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearTelescopeL", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearTelescopeR", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.4F, 0.0F, 0.3F);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 50F * f, 0.0F);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 1.0F, 0.0F, -0.5F);
        Aircraft.xyz[0] = f;
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        if(!pylonOccupied)
        {
            hierMesh().chunkSetAngles("AirbrakeRear", 0.0F, 40F * f, 0.0F);
            hierMesh().chunkSetAngles("AirbrakeTelescope", 0.0F, -40F * f, 0.0F);
        }
    }

//	//By PAL, edited due to 30F making wheel twist absurdly!!! Used default in Superclass
//    public void moveSteering(float f)
//    {
//        if((double)((FlightModelMain) (super.FM)).CT.getGear() < 1D)
//            hierMesh().chunkSetAngles("GearC_D0", 0.0F * f, 0.0F, 0.0F);
//        else
//            hierMesh().chunkSetAngles("GearC_D0", 30F * f, 0.0F, 0.0F);
//    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        ((FlightModelMain) (super.FM)).CT.WeaponControl[0] = false;
        ((FlightModelMain) (super.FM)).CT.WeaponControl[1] = false;
        ((FlightModelMain) (super.FM)).CT.bHasAileronControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasRudderControl = false;
        ((FlightModelMain) (super.FM)).CT.bHasElevatorControl = false;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private boolean pylonOccupied;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    protected boolean bHasSK1Seat;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_21S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG21");
        Property.set(class1, "meshName", "3DO/Plane/MiG-21S/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-21S.fmd:MIG21");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMIG_21MF.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 
            2, 2, 9, 9, 2, 2, 3, 3, 3, 3, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 
            2, 2, 7, 7, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_Flare01", "_Flare02", "_ExternalDev02", "_ExternalDev01", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", 
            "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", 
            "_ExternalRock09", "_ExternalRock10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalDev13", "_ExternalDev14", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", 
            "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", 
            "_ExternalRock41", "_ExternalRock42", "_CANNON01", "_CANNON02", "_ExternalDev17"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 65;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3R+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2USf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2USf+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRS-2US+1x490Lf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2US";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2US+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2US+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2USf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2USf+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xRS-2USf+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3Sf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3S+1xGP-9f";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRS-2US+2xR-3S+1x490Lf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunR3R", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-3R";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-3R+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2xR-3R+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(5, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+2x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+2x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+2x490Lf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+2x490L+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+2x490L+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+2x490L+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+2x490L+1xGP-9f";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3S+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-3R+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunR3R", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5M", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xR-2US+3x490Lf";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK5Mf", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2x490L+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh-66+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunKh66", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS-24+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS-24+R-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunS24", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2x490L+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(5, "RocketGunS5M", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+3x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunS5M", 32);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonUB32", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-100+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-100+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250m46+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250m46+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250m46+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB250m46", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZB-360+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZB-360+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunZB360", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK-250+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK-250+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunRBK250", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xFAB-100+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+2xR-3S+1xGP-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(0, "MGunGSh23Ls", 100);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonGP9", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xFAB-100+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB-100+2xR-3S+1x490L";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "MBDPylon", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100m54", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry+2xR-3S";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[62] = new Aircraft._WeaponSlot(0, "MGunNull", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "MiG21WingPylon", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(2, "RocketGunK13A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "MiG21Pylon", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB800L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_PTB490", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}