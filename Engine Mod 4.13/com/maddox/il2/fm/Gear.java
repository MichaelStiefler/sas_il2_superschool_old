// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Gear.java

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.il2.objects.effects.MiscEffects;
import com.maddox.il2.objects.ships.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;
import java.util.*;
import com.maddox.il2.ai.air.CellAirField;

// Referenced classes of package com.maddox.il2.fm:
//            RealFlightModel, FlightModel, Mass, Atmosphere, 
//            AircraftState, Controls, Arm, EnginesInterface, 
//            Motor

public class Gear
{
    private static class PlateFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d1)
        {
            if(!(actor instanceof Plate))
                return true;
            Mesh mesh = ((ActorMesh)actor).mesh();
            mesh.getBoundBox(Gear.plateBox);
            Gear.corn1.set(Gear.corn);
            Loc loc = actor.pos.getAbs();
            loc.transformInv(Gear.corn1);
            if((double)(Gear.plateBox[0] - 2.5F) < Gear.corn1.x && Gear.corn1.x < (double)(Gear.plateBox[3] + 2.5F) && (double)(Gear.plateBox[1] - 2.5F) < Gear.corn1.y && Gear.corn1.y < (double)(Gear.plateBox[4] + 2.5F))
            {
                Gear.plateFilterGear.bPlateExist = true;
                Gear.plateFilterGear.bPlateGround = ((Plate)actor).isGround();
                Gear.plateFilterGear.bPlateConcrete = ((Plate)actor).isConcrete();
                Gear.plateFilterGear.bPlateSand = ((Plate)actor).isSand();
            }
            return true;
        }

        private PlateFilter()
        {
        }

    }

    static class ClipFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d1)
        {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter()
        {
        }
    }


    public Gear()
    {
        weakPoints = null;
        onGround = false;
        nearGround = false;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        oldNOfGearsOnGr = 0;
        oldNOfPoiOnGr = 0;
        nP = 0;
        gearsChanged = false;
        clpEff = new Eff3DActor[64];
        clpEngineEff = new Eff3DActor[8][2];
        effectName = "";
        bTheBabysGonnaBlow = false;
        lgear = true;
        rgear = true;
        cgear = true;
        bIsHydroOperable = true;
        bIsOperable = true;
        bTailwheelLocked = false;
        steerAngle = 0.0F;
        roughness = 0.5D;
        arrestorVAngle = 0.0F;
        arrestorVSink = 0.0F;
        arrestorHook = null;
        waterList = null;
        isGearColl = false;
        MassCoeff = 1.0D;
        bFrontWheel = false;
        bUnderDeck = false;
        bIsMaster = true;
        fatigue = new int[2];
        p0 = new Point3d();
        p1 = new Point3d();
        l0 = new Loc();
        v0 = new Vector3d();
        tmpV = new Vector3d();
        tmpV1 = new Vector3d();
        fric = 0.0D;
        fricF = 0.0D;
        fricR = 0.0D;
        maxFric = 0.0D;
        screenHQ = 0.0D;
        canDoEffect = true;
        bPlateExist = false;
        bPlateGround = false;
        bPlateConcrete = false;
        bPlateSand = false;
        currentBornPlace = null;
        zutiHasPlaneSkisOnWinterCamo = false;
        tempLoc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        tempPoint = new Point3f();
        //
        dCatapultOffsetX = new double[4];
        dCatapultOffsetY = new double[4];
        dCatapultYaw = new double[4];
        
        for(int i = 0; i < 4; i++)
        {
            dCatapultOffsetX[i] = 0.0D;
            dCatapultOffsetY[i] = 0.0D;
            dCatapultYaw[i] = 0.0D;
        }

        iCatapults = 0;
        bCatapultAllow = true;
        bCatapultBoost = false;
        bCatapultAI = true;
        bCatapultAllowAI = true;
        bCatapultSet = false;
        bAlreadySetCatapult = false;
        bStandardDeckCVL = false;
        bSteamCatapult = false;
        iCatapultAlreadySetNum = -1;
        bHasBlastDeflector = false;
        onGround = false;
        nearGround = false;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        oldNOfGearsOnGr = 0;
        oldNOfPoiOnGr = 0;
        nP = 0;
        gearsChanged = false;
        catEff = null;
        clpEff = new Eff3DActor[64];
        clpEngineEff = new Eff3DActor[8][2];
        effectName = new String();
        bTheBabysGonnaBlow = false;
        lgear = true;
        rgear = true;
        cgear = true;
        bIsHydroOperable = true;
        bIsOperable = true;
        bTailwheelLocked = false;
        steerAngle = 0.0F;
        roughness = 0.5D;
        arrestorVAngle = 0.0F;
        arrestorVSink = 0.0F;
        arrestorHook = null;
        waterList = null;
        isGearColl = false;
        MassCoeff = 1.0D;
        bFrontWheel = false;
        bUnderDeck = false;
        bIsMaster = true;
        fatigue = new int[2];
        p0 = new Point3d();
        p1 = new Point3d();
        l0 = new Loc();
        v0 = new Vector3d();
        tmpV = new Vector3d();
        fric = 0.0D;
        fricF = 0.0D;
        fricR = 0.0D;
        maxFric = 0.0D;
        screenHQ = 0.0D;
        canDoEffect = true;
        if(Mission.cur().sectFile().get("Mods", "CatapultAllow", 1) == 0)
        {
            bCatapultAllow = false;
            bCatapultAllowAI = false;
        }
        if(Mission.cur().sectFile().get("Mods", "CatapultBoost", 1) == 1 && !Mission.isCoop())
            bCatapultBoost = true;
        if(Config.cur.ini.get("Mods", "CatapultAllowAI", 1) == 0)
            bCatapultAllowAI = false;
        if(Mission.cur().sectFile().get("Mods", "CatapultAllowAI", 1) == 0)
            bCatapultAllowAI = false;
        if(Config.cur.ini.get("Mods", "StandardDeckCVL", 0) == 1)
            bStandardDeckCVL = true;
        if(Mission.cur().sectFile().get("Mods", "StandardDeckCVL", 0) == 1)
            bStandardDeckCVL = true;
    }

    public boolean onGround()
    {
        return onGround;
    }

    public boolean nearGround()
    {
        return nearGround;
    }

    public boolean isHydroOperable()
    {
        return bIsHydroOperable;
    }

    public void setHydroOperable(boolean flag)
    {
        bIsHydroOperable = flag;
    }

    public boolean isOperable()
    {
        return bIsOperable;
    }

    public void setOperable(boolean flag)
    {
        bIsOperable = flag;
    }

    public float getSteeringAngle()
    {
        return steerAngle;
    }

    public void setSteeringAngle(float f)
    {
        steerAngle = f;
    }

    public boolean isUnderDeck()
    {
        return bUnderDeck;
    }

    public boolean getWheelsOnGround()
    {
        boolean flag = isGearColl;
        isGearColl = false;
        return flag;
    }

    private void setWeakPoints()
    {
        ArrayList arraylist = new ArrayList();
        for(int i = 3; i < pnti.length; i++)
        {
            String s1 = "_Clip" + s(i - 3);
            String s2 = FM.actor.findHook(s1).chunkName();
            if(s2.startsWith("Turret"))
                arraylist.add(new Integer(i));
        }

        weakPoints = new int[arraylist.size()];
        for(int j = 0; j < arraylist.size(); j++)
            weakPoints[j] = ((Integer)arraylist.get(j)).intValue();

    }

    public void set(HierMesh hiermesh)
    {
        HM = hiermesh;
        if(pnti == null)
        {
            int i;
            for(i = 0; i < 61 && HM.hookFind("_Clip" + s(i)) >= 0; i++);
            pnti = new int[i + 3];
            pnti[0] = HM.hookFind("_ClipLGear");
            pnti[1] = HM.hookFind("_ClipRGear");
            pnti[2] = HM.hookFind("_ClipCGear");
            for(int j = 3; j < pnti.length; j++)
                pnti[j] = HM.hookFind("_Clip" + s(j - 3));

        }
        if(arrestorHook == null && hiermesh.hookFind("_ClipAGear") != -1)
            arrestorHook = new HookNamed(hiermesh, "_ClipAGear");
        int k = pnti[2];
        if(k > 0)
        {
            HM.hookMatrix(k, M4);
            if(M4.m03 > -1D)
                bFrontWheel = true;
        }
    }

    public void computePlaneLandPose(FlightModel flightmodel)
    {
        FM = flightmodel;
        for(int i = 0; i < 3; i++)
            if(pnti[i] < 0)
                return;

        HM.hookMatrix(pnti[0], M4);
        double d1 = M4.m03;
        double d2 = M4.m23;
        double d3 = M4.m13;
        HM.hookMatrix(pnti[2], M4);
        double d4 = M4.m03;
        double d5 = M4.m23;
        HM.hookMatrix(pnti[1], M4);
        d1 = (d1 + M4.m03) * 0.5D;
        d2 = (d2 + M4.m23) * 0.5D;
        double d6 = d1 - d4;
        double d7 = d2 - d5;
        if(H == 0.0F || Pitch == 0.0F)
        {
            Pitch = -Geom.RAD2DEG((float)Math.atan2(d7, d6));
            if(d6 < 0.0D)
                Pitch += 180F;
            Line2f line2f = new Line2f();
            line2f.set(new Point2f((float)d1, (float)d2), new Point2f((float)d4, (float)d5));
            H = line2f.distance(new Point2f(0.0F, 0.0F));
            H -= (double)((FM.M.massEmpty + FM.M.maxFuel + FM.M.maxNitro) * Atmosphere.g()) / 2700000D;
        }
        double d8 = Math.sqrt(d6 * d6 + d7 * d7);
        d1 = (d6 / d8) * d1 + (d7 / d8) * d2;
        if(d6 < 0.0D)
            d1 = -d1;
        mgx = (float)d1;
        mgy = (float)d3;
        sgx = (float)d4;
        mgh = (float)Math.sqrt(mgx * mgx + mgy * mgy);
        mgalpha = (float)Math.atan2(mgx, mgy);
    }

    public void set(Gear gear)
    {
        if(gear.pnti == null)
            return;
        pnti = new int[gear.pnti.length];
        if(gear.waterList != null)
        {
            waterList = new int[gear.waterList.length];
            for(int i = 0; i < waterList.length; i++)
                waterList[i] = gear.waterList[i];

        }
        for(int j = 0; j < pnti.length; j++)
            pnti[j] = gear.pnti[j];

        bIsSail = gear.bIsSail;
        sinkFactor = gear.sinkFactor;
        springsStiffness = gear.springsStiffness;
        H = gear.H;
        Pitch = gear.Pitch;
        bFrontWheel = gear.bFrontWheel;
    }

    public void ground(FlightModel flightmodel, boolean flag)
    {
        ground(flightmodel, flag, false);
    }

    public void ground(FlightModel flightmodel, boolean flag, boolean flag1)
    {
        FM = flightmodel;
        bIsMaster = flag;
        onGround = flag1;
        FM.Vrel.x = -FM.Vwld.x;
        FM.Vrel.y = -FM.Vwld.y;
        FM.Vrel.z = -FM.Vwld.z;
        for(int i = 0; i < 2; i++)
            if(fatigue[i] > 0)
                fatigue[i]--;

        if(weakPoints == null)
            setWeakPoints();
        Pn.set(FM.Loc);
        Pn.z = Engine.cur.land.HQ(Pn.x, Pn.y);
        double d1 = Pn.z;
        screenHQ = d1;
        if(FM.Loc.z - d1 > 50D && !bFlatTopGearCheck)
        {
            turnOffEffects();
            arrestorVSink = -50F;
            return;
        }
        isWater = Engine.cur.land.isWater(Pn.x, Pn.y);
        if(isWater)
            roughness = 0.5D;
        D0 = Engine.cur.land.EQN(Pn.x, Pn.y, Normal);
        bUnderDeck = false;
        BigshipGeneric bigshipgeneric = null;
        if(bFlatTopGearCheck)
        {
            corn.set(FM.Loc);
            corn1.set(FM.Loc);
            corn1.z -= 20D;
            Actor actor = Engine.collideEnv().getLine(corn, corn1, false, clipFilter, Pship);
            if(actor instanceof BigshipGeneric)
            {
                Pn.z = Pship.z + 0.5D;
                d1 = Pn.z;
                isWater = false;
                bUnderDeck = true;
                actor.getSpeed(Vship);
                FM.Vrel.add(Vship);
                bigshipgeneric = (BigshipGeneric)actor;
                bigshipgeneric.addRockingSpeed(FM.Vrel, Normal, FM.Loc);
                if(flightmodel.AS.isMaster() && bigshipgeneric.getAirport() != null && flightmodel.CT.bHasArrestorControl)
                {
                    if(!bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor) && FM.Vrel.lengthSquared() > 10D && flightmodel.CT.getArrestor() > 0.1F)
                    {
                        bigshipgeneric.requestTowAircraft((Aircraft)flightmodel.actor);
                        if(bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
                        {
                            flightmodel.AS.setFlatTopString(bigshipgeneric, bigshipgeneric.towPortNum);
                            if((FM instanceof RealFlightModel) && bIsMaster && ((RealFlightModel)FM).isRealMode())
                                ((RealFlightModel)FM).producedShakeLevel = 5F;
                            ((Aircraft)flightmodel.actor).sfxTow();
                        }
                    }
                    if(bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor) && FM.Vrel.lengthSquared() < 1.0D && World.Rnd().nextFloat() < 0.008F)
                    {
                        bigshipgeneric.requestDetowAircraft((Aircraft)flightmodel.actor);
                        flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
                    }
                }
                if(bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
                {
                    int k = bigshipgeneric.towPortNum;
                    Point3d apoint3d[] = bigshipgeneric.getShipProp().propAirport.towPRel;
                    bigshipgeneric.pos.getAbs(l0);
                    l0.transform(apoint3d[k * 2], p0);
                    l0.transform(apoint3d[k * 2 + 1], p1);
                    p0.x = 0.5D * (p0.x + p1.x);
                    p0.y = 0.5D * (p0.y + p1.y);
                    p0.z = 0.5D * (p0.z + p1.z);
                    flightmodel.actor.pos.getAbs(l0);
                    l0.transformInv(p0);
                    l0.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(flightmodel.actor, new Loc(l0), l0);
                    v0.sub(p0, l0.getPoint());
                    if(v0.x > 0.0D)
                    {
                        if(bigshipgeneric.isTowAircraft((Aircraft)flightmodel.actor))
                        {
                            bigshipgeneric.requestDetowAircraft((Aircraft)flightmodel.actor);
                            flightmodel.AS.setFlatTopString(bigshipgeneric, -1);
                        }
                    } else
                    {
                        tmpV.set(FM.Vrel);
                        flightmodel.actor.pos.getAbsOrient().transformInv(tmpV);
                        if(tmpV.x < 0.0D)
                        {
                            double d3 = v0.length();
                            v0.normalize();
                            arrestorVAngle = (float)Math.toDegrees(Math.asin(v0.z));
                            //
                            double d4 = 1000D;
                            Class class1 = null;
                            Class class2 = null;
                            try
                            {
                                class1 = Class.forName("com.maddox.il2.objects.air.TypeSupersonic");
                            }
                            catch(Exception exception) { }
                            try
                            {
                                class2 = Class.forName("com.maddox.il2.objects.air.TypeFastJet");
                            }
                            catch(Exception exception1) { }
                            if(Mission.curYear() > 1948)
                            {
                                if(class1 != null && FM.actor != null && FM.actor.getClass().isInstance(class1))
                                    d4 = 3000D;
                                if(class2 != null && FM.actor != null && FM.actor.getClass().isInstance(class2))
                                    d4 = 3000D;
                            }
                            if(FM.Vmin > 190F)
                                d4 += (FM.Vmin - 190F) * 50F;
                            if(FM.M.getFullMass() > 4500F)
                                d4 += FM.M.getFullMass() - 4500F;
                            v0.scale(d4 * d3);
                            //
                            flightmodel.GF.add(v0);
                            v0.scale(0.29999999999999999D);
                            v0.cross(l0.getPoint(), v0);
                            flightmodel.GM.add(v0);
                        }
                    }
                } else
                {
                    arrestorVAngle = 0.0F;
                }
            }
        }
        if(flightmodel.CT.bHasArrestorControl)
        {
            flightmodel.actor.pos.getAbs(Aircraft.tmpLoc1);
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            arrestorHook.computePos(flightmodel.actor, Aircraft.tmpLoc1, loc);
            arrestorVSink = (float)(Pn.z - loc.getPoint().z);
        }
        Fd.set(FM.Vrel);
        Vnf.set(Normal);
        FM.Or.transformInv(Normal);
        FM.Or.transformInv(Fd);
        Fd.normalize();
        Pn.x = 0.0D;
        Pn.y = 0.0D;
        Pn.z -= FM.Loc.z;
        FM.Or.transformInv(Pn);
        D = -Normal.dot(Pn);
        if(!bIsMaster)
            D -= 0.014999999999999999D;
        if(D > 50D)
        {
            nearGround = false;
            return;
        }
        nearGround = true;
        gWheelSinking[0] = gWheelSinking[1] = gWheelSinking[2] = 0.0F;
        for(int j = 0; j < pnti.length; j++)
        {
            int l = pnti[j];
            if(l <= 0)
            {
                Pnt[j].set(0.0F, 0.0F, 0.0F);
            } else
            {
                HM.hookMatrix(l, M4);
                Pnt[j].set((float)M4.m03, (float)M4.m13, (float)M4.m23);
            }
        }

        nP = 0;
        nOfGearsOnGr = 0;
        nOfPoiOnGr = 0;
        tmpV.set(0.0D, 1.0D, 0.0D);
        Forward.cross(tmpV, Normal);
        Forward.normalize();
        Right.cross(Normal, Forward);
        boolean flag2 = false;
        for(int i1 = 0; i1 < pnti.length; i1++)
        {
            clp[i1] = false;
            if(i1 <= 2)
            {
                bIsGear = true;
                if(i1 == 0 && (!lgear || FM.CT.getGearL() < 0.01F) || i1 == 1 && (!rgear || FM.CT.getGearR() < 0.01F) || i1 == 2 && !cgear)
                    continue;
            } else
            {
                bIsGear = false;
            }
            PnT.set(Pnt[i1]);
            d = Normal.dot(PnT) + D;
            Fx.set(Fd);
            MassCoeff = 0.00040000000000000002D * (double)FM.M.getFullMass();
            if(MassCoeff > 1.0D)
                MassCoeff = 1.0D;
            if(d < 0.0D)
            {
                if(!testPropellorCollision(i1))
                    continue;
                if(isWater)
                {
                    if(!testWaterCollision(i1))
                        continue;
                } else
                {
                    if(!flag2 && !flightmodel.isPlayers() && FM.isTick(4, 0) && FM.Vrel.length() > 0.10000000149011612D)
                    {
                        plateCheck(flightmodel);
                        flag2 = true;
                    }
                    if(bIsGear ? !testGearCollision(i1) : !testNonGearCollision(i1))
                        continue;
                }
                clp[i1] = true;
                nP++;
            } else
            {
                if(d >= 0.10000000000000001D || isWater || bIsGear || !testNonGearCollision(i1))
                    continue;
                clp[i1] = true;
                nP++;
            }
            PnT.x += FM.Arms.GC_GEAR_SHIFT;
            Fx.cross(PnT, Tn);
            Fv.set(Fx);
            if(bIsSail && bInWaterList(i1))
            {
                tmpV.scale(fricF * 0.5D, Forward);
                Tn.add(tmpV);
                tmpV.scale(fricR * 0.5D, Right);
                Tn.add(tmpV);
            }
            if(bIsMaster)
            {
                FM.GF.add(Tn);
                FM.GM.add(Fx);
            }
        }

        if(oldNOfGearsOnGr != nOfGearsOnGr || oldNOfPoiOnGr != nOfPoiOnGr)
            gearsChanged = true;
        else
            gearsChanged = false;
        oldNOfGearsOnGr = nOfGearsOnGr;
        oldNOfPoiOnGr = nOfPoiOnGr;
        onGround = nP > 0;
        if(Config.isUSE_RENDER())
            drawEffects();
        if(bIsMaster)
        {
            FM.canChangeBrakeShoe = false;
            BigshipGeneric bigshipgeneric1 = bigshipgeneric;
            if(bigshipgeneric1 != null)
                FM.brakeShoeLastCarrier = bigshipgeneric1;
            else
            if((Mission.isCoop() || Mission.isDogfight()) && !Mission.isServer() && Actor.isAlive(FM.brakeShoeLastCarrier) && Time.current() < 60000L)
                bigshipgeneric1 = (BigshipGeneric)FM.brakeShoeLastCarrier;
            if(bigshipgeneric1 != null)
            {
                if(FM.brakeShoe)
                {
                    if(!isAnyDamaged())
                    {
                        L.set(FM.brakeShoeLoc);
                        L.add(FM.brakeShoeLastCarrier.pos.getAbs());
                        FM.Loc.set(L.getPoint());
                        FM.Or.set(L.getOrient());
                        //
                        if(!bAlreadySetCatapult)
                        {
                            bCatapultSet = setCatapultOffset(bigshipgeneric1, new SectFile("com/maddox/il2/objects/Catapults.ini"));
                            bAlreadySetCatapult = true;                            
                        }
                        if(bCatapultAllow && !FM.EI.getCatapult() && bCatapultSet)
                        {                        	
                            bigshipgeneric1.getAirport().pos.getCurrent();
                            Point3d point3d = L.getPoint();
                            CellAirField cellairfield = bigshipgeneric1.getCellTO();
                            boolean flag3 = false;
                            boolean flag4 = false;
                            boolean flag5 = false;
                            if(((Interpolate) (FM)).actor instanceof TypeSupersonic)
                                flag3 = true;
                            if(((Interpolate) (FM)).actor instanceof TypeFastJet)
                                flag4 = true;
                            if(FM.EI.engines[0].getType() == 2)
                                flag5 = true;
                            int k1 = 0;
                            do
                            {
                                if(k1 >= iCatapults)
                                    break;
                                if(dCatapultOffsetX[k1] != 0.0D && dCatapultOffsetY[k1] != 0.0D)
                                {
                                    double d5 = -cellairfield.leftUpperCorner().x - dCatapultOffsetX[k1];
                                    double d6 = cellairfield.leftUpperCorner().y - dCatapultOffsetY[k1];
                                    Loc loc1 = new Loc(d6, d5, 0.0D, 0.0F, 0.0F, 0.0F);
                                    loc1.add(FM.brakeShoeLastCarrier.pos.getAbs());
                                    Point3d point3d1 = loc1.getPoint();
                                    double d7 = Math.abs((point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y));
                                    if(d7 <= 100D)
                                    {
                                        L.set(d6, d5, FM.brakeShoeLoc.getZ(), FM.brakeShoeLoc.getAzimut(), FM.brakeShoeLoc.getTangage(), FM.brakeShoeLoc.getKren());
                                        L.add(FM.brakeShoeLastCarrier.pos.getAbs());
                                        FM.Loc.set(L.getPoint());
                                        FM.Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw[k1], Pitch, FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                                        FM.actor.pos.setAbs(FM.Loc, FM.Or);
                                                                                                                        
                                        if(bCatapultBoost)
                                        {                                        	
                                            if((flag3 || flag4 || flag5) && Mission.curYear() > 1945 && Mission.curYear() < 1953)
                                                FM.EI.setCatapult(FM.M.getFullMass(), catapultPowerJets, k1);
                                            else
                                            if((flag3 || flag4 || flag5) && Mission.curYear() > 1952)
                                            {
                                                int l1 = (int)Math.ceil((((FM.M.getFullMass() - FM.M.massEmpty) / 900F) * catapultPowerJets) / 10F);
                                                FM.EI.setCatapult(FM.M.getFullMass(), catapultPowerJets + (float)l1, k1);
                                            } else
                                            if(Mission.curYear() < 1953)
                                            {
                                                FM.EI.setCatapult(FM.M.getFullMass(), catapultPower, k1);
                                            } else
                                            {
                                                int i2 = (int)Math.ceil(((FM.M.getFullMass() - FM.M.massEmpty) / 900F) * 2.0F);
                                                FM.EI.setCatapult(FM.M.getFullMass(), catapultPower + (float)i2, k1);
                                            }
                                        } else
                                        {
                                            FM.EI.setCatapult(FM.M.getFullMass(), 10F, k1);
                                        }
                                        FM.brakeShoeLoc.set(FM.actor.pos.getAbs());
                                        FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getAbs());
                                        iCatapultAlreadySetNum = k1;
                                        if(bHasBlastDeflector)
                                        {
                                            if(bLogDetailBD)
                                                System.out.println("Gear: 876 - bHasBlastDeflector = true , setBlastDeflector(i4=" + Integer.toString(k1) + ", 1)");
                                            ((TypeBlastDeflector)bigshipgeneric1).setBlastDeflector(k1, 1);
                                        }
                                        break;
                                    }
                                }
                                k1++;
                            } while(true);
                        } else
                        if(FM.EI.getCatapult())
                            FM.EI.resetCatapultTime();
                        //
                        FM.brakeShoeLastCarrier.getSpeed(FM.Vwld);
                        FM.Vrel.set(0.0D, 0.0D, 0.0D);
                        for(int j1 = 0; j1 < 3; j1++)
                            gVelocity[j1] = 0.0D;

                        onGround = true;
                        FM.canChangeBrakeShoe = true;
                    } else
                    {
                        FM.brakeShoe = false;
                    }
                } else
                //
                {                           	
                    if(FM.EI.getCatapult())
                        FM.Or.setYPR(FM.brakeShoeLastCarrier.pos.getAbsOrient().getYaw() + (float)dCatapultYaw[FM.EI.getCatapultNumber()], FM.Or.getPitch(), FM.brakeShoeLastCarrier.pos.getAbsOrient().getRoll());
                   
                    if(nOfGearsOnGr == 3 && nP == 3 && FM.Vrel.lengthSquared() < 1.0D)
                    {
                        FM.brakeShoeLoc.set(FM.actor.pos.getCurrent());
                        FM.brakeShoeLoc.sub(FM.brakeShoeLastCarrier.pos.getCurrent());
                        FM.canChangeBrakeShoe = true;
                    }
                    bAlreadySetCatapult = false;
                    if(bHasBlastDeflector)
                    {
                        if(bLogDetailBD)
                            System.out.println("Gear: 915 - bHasBlastDeflector = true , setBlastDeflector(FM.EI.getCatapultNumber()=" + Integer.toString(FM.EI.getCatapultNumber()) + ", 0)");
                        ((TypeBlastDeflector)bigshipgeneric1).setBlastDeflector(FM.EI.getCatapultNumber(), 0);
                    }
                }	
                //	
            } else
            if(nOfGearsOnGr == 3 && nP == 3 && FM.Vrel.lengthSquared() < 1.5D)
            {
                FM.brakeShoeLoc.set(FM.actor.pos.getCurrent());
                FM.Vrel.set(0.0D, 0.0D, 0.0D);
                FM.canChangeBrakeShoe = true;
                onGround = true;
                if(FM.brakeShoe)
                {
                    FM.GF.set(0.0D, 0.0D, 0.0D);
                    FM.GM.set(0.0D, 0.0D, 0.0D);
                    FM.Vwld.set(0.0D, 0.0D, 0.0D);
                }
            }
            if((FM.actor instanceof TypeSeaPlane) || (FM.actor instanceof HE_LERCHE3))
                FM.canChangeBrakeShoe = false;
        }
        if(!bIsMaster)
            return;
        if(onGround && !isWater)
            processingCollisionEffect();
        double d2 = Engine.cur.land.HQ_ForestHeightHere(FM.Loc.x, FM.Loc.y);
        if(d2 > 0.0D && FM.Loc.z <= d1 + d2 && ((Aircraft)FM.actor).isEnablePostEndAction(0.0D))
            ((Aircraft)FM.actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
    }

    private boolean testNonGearCollision(int i)
    {
        if(FM.isTick(4, 0) && i >= 7 && FM.Vrel.length() > 1.0D)
        {
            tempLoc.set(Pnt[i].x, Pnt[i].y, Pnt[i].z + 0.05F, 0.0F, 0.0F, 0.0F);
            if(bPlateConcrete)
            {
                MiscEffects.bellyLandingConcrete(FM, tempLoc, i, pnti[i]);
                if((double)World.Rnd().nextFloat() > 0.98999999999999999D)
                {
                    for(int j = 0; j < FM.AS.astateTankStates.length; j++)
                        if(FM.AS.astateTankStates[j] == 1 || FM.AS.astateTankStates[j] == 2)
                        {
                            Hook hook = FM.actor.findHook("_Tank" + (j + 1) + "Leak");
                            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                            Loc loc1 = new Loc();
                            loc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                            hook.computePos(FM.actor, loc, loc1);
                            double d3 = tempLoc.getPoint().distance(loc1.getPoint());
                            if(d3 < 2D)
                                FM.AS.hitTank(Engine.actorLand(), j, 6);
                        }

                }
            } else
            if(bPlateSand)
            {
                MiscEffects.bellyLandingSand(FM, tempLoc, i, pnti[i]);
            } else
            {
                int k = World.cur().camouflage;
                if(k == 1)
                    MiscEffects.bellyLandingSnow(FM, tempLoc, i, pnti[i]);
                else
                if(k == 2)
                    MiscEffects.bellyLandingSand(FM, tempLoc, i, pnti[i]);
                else
                    MiscEffects.bellyLandingDirt(FM, tempLoc, i, pnti[i]);
            }
        }
        nOfPoiOnGr++;
        Vs.set(FM.Vrel);
        Vs.scale(-1D);
        FM.Or.transformInv(Vs);
        tmpV.set(Pnt[i]);
        tmpV.cross(FM.getW(), tmpV);
        Vs.add(tmpV);
        ForwardVPrj = Forward.dot(Vs);
        NormalVPrj = Normal.dot(Vs);
        RightVPrj = Right.dot(Vs);
        if(NormalVPrj > 0.0D)
        {
            NormalVPrj -= 3D;
            if(NormalVPrj < 0.0D)
                NormalVPrj = 0.0D;
        }
        double d1 = 1.0D;
        double d2 = d - 0.059999999999999998D;
        double d4 = d + 0.040000000000000008D;
        if(d2 > 0.0D)
            d2 = 0.0D;
        if(d2 < -2D)
            d2 = -2D;
        if(d4 > 0.0D)
            d4 = 0.0D;
        if(d4 < -0.25D)
            d4 = -0.25D;
        d1 = Math.max(-120000D * d2, -360000D * d4);
        d1 *= MassCoeff;
        Tn.scale(d1, Normal);
        fric = -40000D * NormalVPrj;
        if(fric > 100000D)
            fric = 100000D;
        if(fric < -100000D)
            fric = -100000D;
        tmpV.scale(fric, Normal);
        Tn.add(tmpV);
        double d5 = 1.0D - (0.5D * (double)Math.abs(Pnt[i].y)) / (double)FM.Arms.WING_END;
        fricF = -8000D * ForwardVPrj;
        fricR = -50000D * RightVPrj;
        fric = Math.sqrt(fricF * fricF + fricR * fricR);
        if(fric > 20000D * d5)
        {
            fric = (20000D * d5) / fric;
            fricF *= fric;
            fricR *= fric;
        }
        tmpV.scale(fricF * 0.80000001192092896D, Forward);
        Tn.add(tmpV);
        tmpV.scale(fricR * 0.80000001192092896D, Right);
        Tn.add(tmpV);
        if(i > 6 && bIsMaster)
        {
            World.cur();
            if(FM.actor == World.getPlayerAircraft() && FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) < 2D && !bTheBabysGonnaBlow)
            {
                for(int l = 0; l < FM.CT.Weapons.length; l++)
                {
                    if(FM.CT.Weapons[l] == null || FM.CT.Weapons[l].length <= 0)
                        continue;
                    for(int i1 = 0; i1 < FM.CT.Weapons[l].length; i1++)
                        if(((FM.CT.Weapons[l][i1] instanceof BombGun) || (FM.CT.Weapons[l][i1] instanceof RocketGun) || (FM.CT.Weapons[l][i1] instanceof RocketBombGun)) && FM.CT.Weapons[l][i1].haveBullets() && FM.getSpeed() > 38F && FM.CT.Weapons[l][i1].getHookName().startsWith("_External"))
                            bTheBabysGonnaBlow = true;

                }

                if(bTheBabysGonnaBlow && (!FM.isPlayers() || World.cur().diffCur.Vulnerability) && ((Aircraft)FM.actor).isEnablePostEndAction(0.0D))
                {
                    ((Aircraft)FM.actor).postEndAction(0.0D, Engine.actorLand(), 2, null);
                    if(FM.isPlayers())
                        HUD.log("FailedBombsDetonate");
                }
            }
        }
        if(bIsMaster && NormalVPrj < 0.0D)
        {
            boolean flag = isWeakPart(i);
            if(flag)
            {
                landHit(i, 100000D);
            } else
            {
                double d6 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.00023668639053254438D + NormalVPrj * NormalVPrj * 0.020408163265306121D;
                if(d6 > 1.0D)
                    landHit(i, (float)d6);
            }
        }
        return true;
    }

    private boolean isWeakPart(int i)
    {
        for(int j = 0; j < weakPoints.length; j++)
            if(weakPoints[j] == i)
                return true;

        return false;
    }

    private boolean testGearCollision(int i)
    {
        if((double)FM.CT.getGearR() < 0.01D && (double)FM.CT.getGearL() < 0.01D && (double)FM.CT.getGearC() < 0.01D)
            return false;
        double d1 = 1.0D;
        gWheelSinking[i] = (float)(-d);
        Vs.set(FM.Vrel);
        Vs.scale(-1D);
        FM.Or.transformInv(Vs);
        tmpV.set(Pnt[i]);
        tmpV.cross(FM.getW(), tmpV);
        Vs.add(tmpV);
        ForwardVPrj = Forward.dot(Vs);
        NormalVPrj = Normal.dot(Vs);
        RightVPrj = Right.dot(Vs);
        if(NormalVPrj > 0.0D)
            NormalVPrj = 0.0D;
        double d2 = (FM.Vrel.x * FM.Vrel.x + FM.Vrel.y * FM.Vrel.y) - 2D;
        if(d2 < 0.0D)
            d2 = 0.0D;
        double d3 = 0.01D * d2;
        if(d3 < 0.0D)
            d3 = 0.0D;
        if(d3 > 4.5D)
            d3 = 4.5D;
        double d4 = 0.40000000596046448D * Math.max(roughness * roughness, roughness);
        if(d3 > d4)
            d3 = d4;
        if(roughness > d3)
            roughness = d3;
        if(roughness < 0.20000000298023224D)
            roughness = 0.20000000298023224D;
        if(i < 2)
        {
            d += (double)World.Rnd().nextFloat(-2F, 1.0F) * 0.040000000000000001D * d3 * MassCoeff;
            d1 = Math.max(-9500D * (d - 0.10000000000000001D), -950000D * d);
            d1 *= springsStiffness;
        } else
        {
            d += (double)(World.Rnd().nextFloat(-2F, 1.0F) * 0.04F) * d3 * MassCoeff;
            d1 = Math.max(-9500D * (d - 0.10000000000000001D), -950000D * d);
            if(Pnt[i].x > 0.0F && Fd.dot(Normal) >= 0.0D)
                d1 *= 0.44999998807907104D;
            else
                d1 *= tailStiffness;
        }
        d1 -= 40000D * NormalVPrj;
        Tn.scale(d1, Normal);
        double d5 = 0.0001D * d1;
        double d6 = 0.0D;
        double d7 = 0.0D;
        if(FM.CT.bHasDiffBrakes && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
        {
            float f = FM.CT.getBrakeL();
            float f1 = FM.CT.getBrakeR();
            if(FM.AS.gearDamType[1] == 8)
                f1 = 0.0F;
            if(FM.AS.gearDamType[0] == 8)
                f = 0.0F;
            d6 = Math.max(f, f1);
            d7 = -f + f1;
        } else
        {
            d6 = FM.CT.getBrake();
            d7 = FM.CT.getRudder();
            if(FM.AS.gearDamType[1] == 8)
                d7 = -1D;
            if(FM.AS.gearDamType[0] == 8)
                d7 = 1.0D;
        }
        switch(i)
        {
        case 0: // '\0'
        case 1: // '\001'
            double d8 = 1.2D;
            if(i == 0)
                d8 = -1.2D;
            nOfGearsOnGr++;
            isGearColl = true;
            gVelocity[i] = ForwardVPrj;
            if(d6 > 0.10000000000000001D)
            {
                if(d7 > 0.10000000000000001D)
                    d6 += d8 * d6 * (d7 - 0.10000000000000001D);
                if(d7 < -0.10000000000000001D)
                    d6 += d8 * d6 * (d7 + 0.10000000000000001D);
                if(d6 > 1.0D)
                    d6 = 1.0D;
                if(d6 < 0.0D)
                    d6 = 0.0D;
                if(FM.AS.gearDamType[0] == 8 && FM.AS.gearDamType[1] == 8)
                    d6 = 0.0D;
            }
            double d9 = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
            if(d9 < 0.01D)
                d9 = 0.01D;
            double d11 = 1.0D / d9;
            double d13 = ForwardVPrj * d11;
            if(d13 < 0.0D)
                d13 *= -1D;
            double d14 = RightVPrj * d11;
            if(d14 < 0.0D)
                d14 *= -1D;
            double d15 = 5D;
            if(PnT.y * RightVPrj > 0.0D)
            {
                if(PnT.y > 0.0D)
                    d15 += 7D * RightVPrj;
                else
                    d15 -= 7D * RightVPrj;
                if(d15 > 20D)
                    d15 = 20D;
            }
            double d16 = 15000D;
            if(d9 < 3D)
            {
                double d17 = -0.33333299999999999D * (d9 - 3D);
                d16 += 3000D * d17 * d17;
            }
            fricR = -d15 * 100000D * RightVPrj * d5;
            maxFric = d16 * d5 * d14;
            if(fricR > maxFric)
                fricR = maxFric;
            if(fricR < -maxFric)
                fricR = -maxFric;
            fricF = -d15 * 600D * ForwardVPrj * d5;
            maxFric = d15 * Math.max(200D * (1.0D - 0.040000000000000001D * d9), 5D) * d5 * d13;
            if(fricF > maxFric)
                fricF = maxFric;
            if(fricF < -maxFric)
                fricF = -maxFric;
            double d18 = 0.029999999999999999D;
            if(Pnt[2].x > 0.0F)
                d18 = 0.059999999999999998D;
            double d19 = Math.abs(ForwardVPrj);
            if(d19 < 1.0D)
                d18 += 3D * (1.0D - d19);
            d18 *= 0.029999999999999999D * d6;
            fricF += -300000D * d18 * ForwardVPrj * d5;
            maxFric = d16 * d5 * d13;
            if(fricF > maxFric)
                fricF = maxFric;
            if(fricF < -maxFric)
                fricF = -maxFric;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > maxFric)
            {
                fric = maxFric / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR, Right);
            Tn.add(tmpV);
            float f2 = 1.0F;
            boolean flag = false;
            if(FM.AS.gearDamType[i] == 7)
                flag = true;
            if(i == 0)
                f2 = FM.CT.getGearL();
            else
                f2 = FM.CT.getGearR();
            if(bIsMaster && (NormalVPrj < 0.0D || f2 < 0.99F))
            {
                double d20 = ForwardVPrj * ForwardVPrj * 8.0000000000000007E-005D + RightVPrj * RightVPrj * 0.0067999999999999996D;
                if(FM.CT.bHasArrestorControl)
                    d20 += NormalVPrj * NormalVPrj * 0.025000000000000001D;
                else
                    d20 += NormalVPrj * NormalVPrj * 0.070000000000000007D;
                if(d20 > 1.0D || f2 < 0.99F)
                {
                    if(d20 > 1.0D)
                        fatigue[i] += 10;
                    double d22 = 38000D + (double)FM.M.massEmpty * 6D;
                    if(f2 < 0.95F)
                        d22 *= 0.0099999997764825821D;
                    else
                    if((double)f2 < 0.98999999999999999D)
                        d22 *= 0.10000000149011612D;
                    double d23 = (Tn.x * Tn.x * 0.14999999999999999D + Tn.y * Tn.y * 0.14999999999999999D + Tn.z * Tn.z * 0.080000000000000002D) / (d22 * d22);
                    byte byte0 = 100;
                    if(flag)
                        byte0 = 25;
                    if(fatigue[i] > byte0 || d23 > 1.0D)
                    {
                        landHit(i, (float)d20);
                        Aircraft aircraft1 = (Aircraft)FM.actor;
                        if(i == 0)
                            aircraft1.msgCollision(aircraft1, "GearL2_D0", "GearL2_D0");
                        if(i == 1)
                            aircraft1.msgCollision(aircraft1, "GearR2_D0", "GearR2_D0");
                    }
                }
            }
            break;

        case 2: // '\002'
            nOfGearsOnGr++;
            if(bTailwheelLocked && steerAngle > -5F && steerAngle < 5F)
            {
                gVelocity[i] = ForwardVPrj;
                steerAngle = 0.0F;
                fric = -400D * ForwardVPrj;
                maxFric = 400D;
                if(fric > maxFric)
                    fric = maxFric;
                if(fric < -maxFric)
                    fric = -maxFric;
                tmpV.scale(fric, Forward);
                Tn.add(tmpV);
                fric = -10000D * RightVPrj;
                maxFric = 40000D;
                if(fric > maxFric)
                    fric = maxFric;
                if(fric < -maxFric)
                    fric = -maxFric;
                tmpV.scale(fric, Right);
                Tn.add(tmpV);
            } else
            if(bFrontWheel)
            {
                gVelocity[i] = ForwardVPrj;
                tmpV.set(1.0D, -0.5D * (double)FM.CT.getRudder(), 0.0D);
                steerAngleFork.setDeg(steerAngle, (float)Math.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
                steerAngle = steerAngleFork.getDeg(0.115F);
                nwRight.cross(Normal, tmpV);
                nwRight.normalize();
                nwForward.cross(nwRight, Normal);
                ForwardVPrj = nwForward.dot(Vs);
                RightVPrj = nwRight.dot(Vs);
                double d10 = Math.sqrt(ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj);
                if(d10 < 0.01D)
                    d10 = 0.01D;
                double d12 = 1.0D / d10;
                fricF = -100D * ForwardVPrj;
                maxFric = 4000D;
                if(fricF > maxFric)
                    fricF = maxFric;
                if(fricF < -maxFric)
                    fricF = -maxFric;
                fricR = -500D * RightVPrj;
                maxFric = 4000D;
                if(fricR > maxFric)
                    fricR = maxFric;
                if(fricR < -maxFric)
                    fricR = -maxFric;
                maxFric = 1.0D - 0.02D * d10;
                if(maxFric < 0.10000000000000001D)
                    maxFric = 0.10000000000000001D;
                maxFric = 5000D * maxFric;
                fric = Math.sqrt(fricF * fricF + fricR * fricR);
                if(fric > maxFric)
                {
                    fric = maxFric / fric;
                    fricF *= fric;
                    fricR *= fric;
                }
                tmpV.scale(fricF, Forward);
                Tn.add(tmpV);
                tmpV.scale(fricR, Right);
                Tn.add(tmpV);
            } else
            {
                gVelocity[i] = Vs.length();
                if(Vs.lengthSquared() > 0.040000000000000001D)
                {
                    steerAngleFork.setDeg(steerAngle, (float)Math.toDegrees(Math.atan2(Vs.y, Vs.x)));
                    steerAngle = steerAngleFork.getDeg(0.115F);
                }
                fricF = -1000D * ForwardVPrj;
                fricR = -1000D * RightVPrj;
                fric = Math.sqrt(fricF * fricF + fricR * fricR);
                maxFric = 1500D;
                if(fric > maxFric)
                {
                    fric = maxFric / fric;
                    fricF *= fric;
                    fricR *= fric;
                }
                tmpV.scale(fricF, Forward);
                Tn.add(tmpV);
                tmpV.scale(fricR, Right);
                Tn.add(tmpV);
            }
            float f3 = FM.CT.getGearC();
            if(!bIsMaster || NormalVPrj >= 0.0D && f3 >= 0.99F)
                break;
            double d21 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.0001D;
            if((double)f3 < 0.94999999999999996D)
                d21 = 4D;
            else
            if((double)f3 < 0.98999999999999999D)
                d21 *= 2D;
            if(FM.CT.bHasArrestorControl)
                d21 += NormalVPrj * NormalVPrj * 0.0040000000000000001D;
            else
                d21 += NormalVPrj * NormalVPrj * 0.02D;
            if(d21 > 1.0D)
            {
                landHit(i, (float)d21);
                Aircraft aircraft = (Aircraft)FM.actor;
                aircraft.msgCollision(aircraft, "GearC2_D0", "GearC2_D0");
            }
            break;

        default:
            fricF = -4000D * ForwardVPrj;
            fricR = -4000D * RightVPrj;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > 10000D)
            {
                fric = 10000D / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR, Right);
            Tn.add(tmpV);
            break;
        }
        Tn.scale(MassCoeff);
        if(!bPlateConcrete && FM.Vrel.length() > 0.10000000149011612D && World.cur().camouflage == 1)
        {
            tempLoc.set(Pnt[i].x, Pnt[i].y, Pnt[i].z + 0.05F, 0.0F, 0.0F, 0.0F);
            MiscEffects.gearMarksSnow(FM, tempLoc, i, pnti[i]);
        }
        return true;
    }

    private boolean testWaterCollision(int i)
    {
        Vs.set(FM.Vrel);
        Vs.scale(-1D);
        FM.Or.transformInv(Vs);
        tmpV.set(Pnt[i]);
        tmpV.cross(FM.getW(), tmpV);
        Vs.add(tmpV);
        ForwardVPrj = Forward.dot(Vs);
        NormalVPrj = Normal.dot(Vs);
        RightVPrj = Right.dot(Vs);
        double d1 = ForwardVPrj;
        if(d1 < 0.0D)
            d1 = 0.0D;
        if((!bIsSail || !bInWaterList(i)) && d < -2D)
            d = -2D;
        double d2 = -(1.0D + 0.29999999999999999D * d1) * (double)sinkFactor * d * Math.abs(d) * (1.0D + 0.29999999999999999D * Math.sin((double)((long)(1 + i % 3) * Time.current()) * 0.001D));
        double d3 = 0.0001D * d2;
        if(bIsSail && bInWaterList(i))
        {
            if(NormalVPrj > 0.0D)
                NormalVPrj = 0.0D;
            Tn.scale(d2, Normal);
            fric = -1000D * NormalVPrj;
            if(fric > 4000D)
                fric = 4000D;
            if(fric < -4000D)
                fric = -4000D;
            tmpV.scale(fric, Normal);
            Tn.add(tmpV);
            fricF = -40D * ForwardVPrj;
            fricR = -300D * RightVPrj;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > 50000D)
            {
                fric = 50000D / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF * 0.5D, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR * 0.5D, Right);
            Tn.add(tmpV);
        } else
        {
            Tn.scale(d2, Normal);
            fric = -1000D * NormalVPrj;
            if(fric > 4000D)
                fric = 4000D;
            if(fric < -4000D)
                fric = -4000D;
            tmpV.scale(fric, Normal);
            Tn.add(tmpV);
            fricF = -500D * ForwardVPrj;
            fricR = -800D * RightVPrj;
            fric = Math.sqrt(fricF * fricF + fricR * fricR);
            if(fric > 50000D)
            {
                fric = 50000D / fric;
                fricF *= fric;
                fricR *= fric;
            }
            tmpV.scale(fricF, Forward);
            Tn.add(tmpV);
            tmpV.scale(fricR, Right);
            Tn.add(tmpV);
            if(sinkFactor > 1.0F && !bIsSail)
            {
                sinkFactor -= 0.4F * Time.tickLenFs();
                if(sinkFactor < 1.0F)
                    sinkFactor = 1.0F;
            }
        }
        if(bIsMaster && NormalVPrj < 0.0D)
        {
            double d4 = (ForwardVPrj * ForwardVPrj + RightVPrj * RightVPrj) * 0.00040000000000000002D + NormalVPrj * NormalVPrj * 0.0011111111111111111D;
            if(d4 > 1.0D)
                landHit(i, (float)d4);
        }
        return true;
    }

    private boolean testPropellorCollision(int i)
    {
        if(bIsMaster && i >= 3 && i <= 6)
        {
            if(FM.actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings)
                return false;
            FM.setCapableOfTaxiing(false);
            switch(FM.Scheme)
            {
            default:
                break;

            case 1: // '\001'
                ((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
                break;

            case 2: // '\002'
            case 3: // '\003'
                if(i < 5)
                    ((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
                else
                    ((Aircraft)FM.actor).hitProp(1, 0, Engine.actorLand());
                break;

            case 4: // '\004'
            case 5: // '\005'
                ((Aircraft)FM.actor).hitProp(i - 3, 0, Engine.actorLand());
                break;

            case 6: // '\006'
                switch(i)
                {
                case 3: // '\003'
                    ((Aircraft)FM.actor).hitProp(0, 0, Engine.actorLand());
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    ((Aircraft)FM.actor).hitProp(1, 0, Engine.actorLand());
                    break;

                case 6: // '\006'
                    ((Aircraft)FM.actor).hitProp(2, 0, Engine.actorLand());
                    break;
                }
                break;
            }
            return false;
        } else
        {
            return true;
        }
    }

    private void landHit(int i, double d1)
    {
        if(FM.Vrel.length() < 13D || pnti[i] < 0)
            return;
        if(FM.actor == World.getPlayerAircraft() && !World.cur().diffCur.Realistic_Landings)
            return;
        ActorHMesh actorhmesh = (ActorHMesh)FM.actor;
        if(!Actor.isValid(actorhmesh))
            return;
        Mesh mesh = actorhmesh.mesh();
        long l = Time.tick();
        String s1 = actorhmesh.findHook(mesh.hookName(pnti[i])).chunkName();
        if(s1.compareTo("CF_D0") == 0)
        {
            if(d1 > 2D)
                MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
        } else
        if(s1.compareTo("Tail1_D0") == 0)
        {
            if(d1 > 1.3D)
                MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
        } else
        if((FM.actor instanceof Scheme1) && s1.compareTo("Engine1_D0") == 0)
        {
            MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
            if(d1 > 5D)
                MsgCollision.post(l, actorhmesh, Engine.actorLand(), "CF_D0", "Body");
        } else
        {
            MsgCollision.post(l, actorhmesh, Engine.actorLand(), s1, "Body");
        }
    }

    public void hitLeftGear()
    {
        lgear = false;
        FM.brakeShoe = false;
    }

    public void hitRightGear()
    {
        rgear = false;
        FM.brakeShoe = false;
    }

    public void hitCentreGear()
    {
        cgear = false;
        FM.brakeShoe = false;
    }

    public boolean isAnyDamaged()
    {
        return !lgear || !rgear || !cgear;
    }

    private void drawEffects()
    {
        boolean flag = FM.isTick(16, 0);
        for(int i = 0; i < 3; i++)
        {
            if(bIsSail && flag && isWater && clp[i] && FM.getSpeedKMH() > 10F)
            {
                if(clpGearEff[i][0] == null)
                {
                    clpGearEff[i][0] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WakeSmaller.eff", -1F);
                    clpGearEff[i][1] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[i]), new Orient(0.0F, 0.0F, 0.0F)), 1.0F, "3DO/Effects/Tracers/ShipTrail/WaveSmaller.eff", -1F);
                }
                continue;
            }
            if(flag && clpGearEff[i][0] != null)
            {
                Eff3DActor.finish(clpGearEff[i][0]);
                Eff3DActor.finish(clpGearEff[i][1]);
                clpGearEff[i][0] = null;
                clpGearEff[i][1] = null;
            }
        }

        for(int j = 0; j < pnti.length; j++)
        {
            if(clp[j] && FM.Vrel.length() > 16.666666670000001D && !isUnderDeck())
            {
                if(clpEff[j] != null)
                    continue;
                if(isWater)
                    effectName = "EFFECTS/Smokes/SmokeAirSplat.eff";
                else
                if(World.cur().camouflage == 1)
                    effectName = "EFFECTS/Smokes/SmokeAirTouchW.eff";
                else
                    effectName = "EFFECTS/Smokes/SmokeAirTouch.eff";
                clpEff[j] = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(Pnt[j]), new Orient(0.0F, 90F, 0.0F)), 1.0F, effectName, -1F);
                continue;
            }
            if(flag && clpEff[j] != null)
            {
                Eff3DActor.finish(clpEff[j]);
                clpEff[j] = null;
            }
        }

        //
        if(FM.EI.getCatapult() && isUnderDeck() && !FM.brakeShoe)
        {        	
            if(FM.brakeShoeLastCarrier != null && catEff == null && Mission.curYear() > 1952)
            {            	
                FM.actor.pos.getAbs(Aircraft.tmpLoc1);
                FM.brakeShoeLoc.get(Pn);
                Aircraft.tmpLoc1.transform(Pn, PnT);
                float f = Atmosphere.temperature((float)FM.Loc.z) - 273.16F;
                float f1 = f - World.wind().curGust() - (float)(Mission.curCloudsType() * 3);
                if(f1 > 20F)
                    effectName = "3DO/Effects/Ships/CatapultSteam.eff";
                else
                if(f1 > 10F)
                    effectName = "3DO/Effects/Ships/CatapultSteam2.eff";
                else
                if(f1 > 0.0F)
                    effectName = "3DO/Effects/Ships/CatapultSteam3.eff";
                else
                if(f1 > -10F)
                    effectName = "3DO/Effects/Ships/CatapultSteam4.eff";
                else
                    effectName = "3DO/Effects/Ships/CatapultSteam5.eff";
                catEff = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(-2D, 0.0D, -1.5D), new Orient(0.0F, 0.0F, -70F)), 1.0F, effectName, -1F);
            }
        } else
        if(flag && catEff != null)
        {
            Eff3DActor.finish(catEff);
            catEff = null;
        }
        //
        
        if(FM.EI.getNum() > 0)
        {
            for(int k = 0; k < FM.EI.getNum(); k++)
            {
                FM.actor.pos.getAbs(Aircraft.tmpLoc1);
                Pn.set(FM.EI.engines[k].getPropPos());
                Aircraft.tmpLoc1.transform(Pn, PnT);
                float f = (float)(PnT.z - Engine.cur.land.HQ(PnT.x, PnT.y));
                if(f < 16.2F && FM.EI.engines[k].getThrustOutput() > 0.5F)
                {
                    Pn.x -= f * Aircraft.cvt(FM.Or.getTangage(), -30F, 30F, 8F, 2.0F);
                    Aircraft.tmpLoc1.transform(Pn, PnT);
                    PnT.z = Engine.cur.land.HQ(PnT.x, PnT.y);
                    if(clpEngineEff[k][0] == null)
                    {
                        Aircraft.tmpLoc1.transformInv(PnT);
                        if(isWater)
                        {
                            clpEngineEff[k][0] = Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust2.eff", -1F);
                            clpEngineEff[k][1] = Eff3DActor.New(new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/WhiteEngineWaveTSPD.eff", -1F);
                        } else
                        {
                            clpEngineEff[k][0] = Eff3DActor.New(FM.actor, null, new Loc(PnT), 1.0F, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage != 1 ? "1" : "2") + ".eff", -1F);
                        }
                        continue;
                    }
                    if(isWater)
                    {
                        if(clpEngineEff[k][1] == null)
                        {
                            Eff3DActor.finish(clpEngineEff[k][0]);
                            clpEngineEff[k][0] = null;
                            continue;
                        }
                    } else
                    if(clpEngineEff[k][1] != null)
                    {
                        Eff3DActor.finish(clpEngineEff[k][0]);
                        clpEngineEff[k][0] = null;
                        Eff3DActor.finish(clpEngineEff[k][1]);
                        clpEngineEff[k][1] = null;
                        continue;
                    }
                    Aircraft.tmpOr.set(FM.Or.getAzimut() + 180F, 0.0F, 0.0F);
                    clpEngineEff[k][0].pos.setAbs(PnT);
                    clpEngineEff[k][0].pos.setAbs(Aircraft.tmpOr);
                    clpEngineEff[k][0].pos.resetAsBase();
                    if(clpEngineEff[k][1] != null)
                    {
                        PnT.z = 0.0D;
                        clpEngineEff[k][1].pos.setAbs(PnT);
                    }
                    continue;
                }
                if(clpEngineEff[k][0] != null)
                {
                    Eff3DActor.finish(clpEngineEff[k][0]);
                    clpEngineEff[k][0] = null;
                }
                if(clpEngineEff[k][1] != null)
                {
                    Eff3DActor.finish(clpEngineEff[k][1]);
                    clpEngineEff[k][1] = null;
                }
            }

        }
    }

    private void turnOffEffects()
    {
        if(FM.isTick(69, 0))
        {
            for(int i = 0; i < pnti.length; i++)
                if(clpEff[i] != null)
                {
                    Eff3DActor.finish(clpEff[i]);
                    clpEff[i] = null;
                }

            for(int j = 0; j < FM.EI.getNum(); j++)
            {
                if(clpEngineEff[j][0] != null)
                {
                    Eff3DActor.finish(clpEngineEff[j][0]);
                    clpEngineEff[j][0] = null;
                }
                if(clpEngineEff[j][1] != null)
                {
                    Eff3DActor.finish(clpEngineEff[j][1]);
                    clpEngineEff[j][1] = null;
                }
            }
            
            if(catEff != null)
            {
                Eff3DActor.finish(catEff);
                catEff = null;
            }

        }
    }

    private void processingCollisionEffect()
    {
        if(!canDoEffect)
            return;
        Vnorm = FM.Vwld.dot(Normal);
        if(FM.actor == World.getPlayerAircraft() && World.cur().diffCur.Realistic_Landings && Vnorm < -20D && (double)World.Rnd().nextFloat() < 0.02D)
        {
            canDoEffect = false;
            int i = 20 + (int)(30F * World.Rnd().nextFloat());
            if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][0].countBullets() != 0)
                i = 0;
            if(((Aircraft)FM.actor).isEnablePostEndAction(i))
            {
                Eff3DActor eff3dactor = null;
                if(i > 0)
                {
                    Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/FireGND.eff", i);
                    eff3dactor = Eff3DActor.New(FM.actor, null, new Loc(new Point3d(0.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F)), 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", i + 10);
                    ((NetAircraft)FM.actor).sfxSmokeState(0, 0, true);
                }
                ((Aircraft)FM.actor).postEndAction(i, Engine.actorLand(), 2, eff3dactor);
            }
        }
    }

    public void load(SectFile sectfile)
    {
        bIsSail = sectfile.get("Aircraft", "Seaplane", 0) != 0;
        sinkFactor = sectfile.get("Gear", "SinkFactor", 1.0F);
        springsStiffness = sectfile.get("Gear", "SpringsStiffness", 1.0F);
        tailStiffness = sectfile.get("Gear", "TailStiffness", 0.6F);
        if(sectfile.get("Gear", "FromIni", 0) == 1)
        {
            H = sectfile.get("Gear", "H", 2.0F);
            Pitch = sectfile.get("Gear", "Pitch", 10F);
        } else
        {
            H = Pitch = 0.0F;
        }
        String s1 = sectfile.get("Gear", "WaterClipList", "-");
        if(!s1.startsWith("-"))
        {
            waterList = new int[3 + s1.length() / 2];
            waterList[0] = 0;
            waterList[1] = 1;
            waterList[2] = 2;
            for(int i = 0; i < waterList.length - 3; i++)
            {
                waterList[3 + i] = 10 * (s1.charAt(i + i) - 48) + 1 * (s1.charAt(i + i + 1) - 48);
                waterList[3 + i] += 3;
            }

        }
    }

    public float getLandingState()
    {
        if(!onGround)
            return 0.0F;
        float f = 0.4F + ((float)roughness - 0.2F) * 0.5F;
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public void plateCheck(FlightModel flightmodel)
    {
        Actor actor = flightmodel.actor;
        if(bUnderDeck)
            return;
        if(!Actor.isValid(actor))
        {
            return;
        } else
        {
            float f = 200F;
            actor.pos.getAbs(corn);
            plateFilterGear = this;
            Engine.drawEnv().getFiltered((AbstractCollection)null, corn.x - (double)f, corn.y - (double)f, corn.x + (double)f, corn.y + (double)f, 1, plateFilter);
            return;
        }
    }

    public double plateFriction(FlightModel flightmodel)
    {
        Actor actor = flightmodel.actor;
        if(bUnderDeck)
            return 0.0D;
        if(!Actor.isValid(actor))
            return 0.20000000000000001D;
        if(!World.cur().diffCur.Realistic_Landings)
            return 0.20000000000000001D;
        float f = 200F;
        actor.pos.getAbs(corn);
        bPlateExist = false;
        bPlateGround = false;
        bPlateConcrete = false;
        bPlateSand = false;
        plateFilterGear = this;
        Engine.drawEnv().getFiltered((AbstractCollection)null, corn.x - (double)f, corn.y - (double)f, corn.x + (double)f, corn.y + (double)f, 1, plateFilter);
        if(bPlateExist)
            return !bPlateGround ? 0.0D : 0.80000000000000004D;
        int i = Engine.cur.land.HQ_RoadTypeHere(flightmodel.Loc.x, flightmodel.Loc.y);
        switch(i)
        {
        case 1: // '\001'
            return 0.80000000000000004D;

        case 2: // '\002'
            return 0.0D;

        case 3: // '\003'
            return 5D;
        }
        if(currentBornPlace != null)
        {
            double d1 = currentBornPlace.getBornPlaceFriction(flightmodel.Loc.x, flightmodel.Loc.y);
            if(d1 != -1D)
                if(zutiHasPlaneSkisOnWinterCamo && d1 > 2.3999999999999999D)
                    return 2.4000000953674316D;
                else
                    return d1;
        }
        currentBornPlace = BornPlace.getCurrentBornPlace(flightmodel.Loc.x, flightmodel.Loc.y);
        return !zutiHasPlaneSkisOnWinterCamo ? 3.7999999999999998D : 2.4000000953674316D;
    }

    private String s(int i)
    {
        return i >= 10 ? "" + i : "0" + i;
    }

    private boolean bInWaterList(int i)
    {
        if(waterList != null)
        {
            for(int j = 0; j < waterList.length; j++)
                if(waterList[j] == i)
                    return true;

        }
        return false;
    }

    public void zutiCheckPlaneForSkisAndWinterCamo(String s1)
    {
        for(int i = 0; i < ZUTI_SKIS_AC_CLASSES.length; i++)
            if(s1.endsWith(ZUTI_SKIS_AC_CLASSES[i]))
            {
                if("WINTER".equals(Engine.land().config.camouflage))
                    zutiHasPlaneSkisOnWinterCamo = true;
                else
                    zutiHasPlaneSkisOnWinterCamo = false;
                return;
            }

        zutiHasPlaneSkisOnWinterCamo = false;
    }

    //
    public boolean setCatapultOffset(BigshipGeneric bigshipgeneric, SectFile sectfile)
    {
        boolean flag = false;
        bCatapultAI = false;
        String s1 = bigshipgeneric.getClass().getName();
        int i = s1.lastIndexOf('.');
        int j = s1.lastIndexOf('$');
        if(i < j)
            i = j;
        String s2 = s1.substring(i + 1);
        if(!flag)
            if(!sectfile.sectionExist(s2))
            {
                s2 = "Default";
            } else
            {
                if(!bStandardDeckCVL && sectfile.exist(s2, "dCatapultOffsetXAlt"))
                {
                    dCatapultOffsetX[0] = sectfile.get(s2, "dCatapultOffsetXAlt", 0.0F);
                    dCatapultOffsetY[0] = sectfile.get(s2, "dCatapultOffsetYAlt", 0.0F);
                    dCatapultYaw[0] = sectfile.get(s2, "dCatapultYawAlt", 0.0F);
                    if(iCatapults < 1 && (dCatapultOffsetX[0] != 0.0D || dCatapultOffsetY[0] != 0.0D))
                        iCatapults = 1;
                } else
                {
                    dCatapultOffsetX[0] = sectfile.get(s2, "dCatapultOffsetX", 0.0F);
                    dCatapultOffsetY[0] = sectfile.get(s2, "dCatapultOffsetY", 0.0F);
                    dCatapultYaw[0] = sectfile.get(s2, "dCatapultYaw", 0.0F);
                    if(iCatapults < 1 && (dCatapultOffsetX[0] != 0.0D || dCatapultOffsetY[0] != 0.0D))
                        iCatapults = 1;
                }
                if(iCatapults > 0)
                {
                    for(int k = 1; k < 4 && sectfile.exist(s2, "dCatapultOffsetX" + Integer.toString(k + 1)); k++)
                    {
                        dCatapultOffsetX[k] = sectfile.get(s2, "dCatapultOffsetX" + Integer.toString(k + 1), 0.0F);
                        dCatapultOffsetY[k] = sectfile.get(s2, "dCatapultOffsetY" + Integer.toString(k + 1), 0.0F);
                        dCatapultYaw[k] = sectfile.get(s2, "dCatapultYaw" + Integer.toString(k + 1), 0.0F);
                        iCatapults = k + 1;
                    }

                }
                catapultPower = sectfile.get(s2, "catapultPower", 0.0F);
                catapultPowerJets = sectfile.get(s2, "catapultPowerJets", 0.0F);
                if(sectfile.get(s2, "bSteamCatapult", 0) == 1)
                    bSteamCatapult = true;
                if(iCatapults > 0)
                {
                    flag = true;
                    bCatapultAI = bCatapultAllowAI;
                }
            }
        bHasBlastDeflector = bigshipgeneric instanceof TypeBlastDeflector;
        return flag;
    }

    public double getCatapultOffsetX(int i)
    {
        return dCatapultOffsetX[i];
    }

    public double getCatapultOffsetY(int i)
    {
        return dCatapultOffsetY[i];
    }

    public double getCatapultOffsetX()
    {
        return getCatapultOffsetX(0);
    }

    public double getCatapultOffsetY()
    {
        return getCatapultOffsetY(0);
    }

    public double getCatapultOffsetX2()
    {
        return getCatapultOffsetX(1);
    }

    public double getCatapultOffsetY2()
    {
        return getCatapultOffsetY(1);
    }

    public boolean getCatapultAI()
    {
        return bCatapultAI;
    }

    public int getCatapultAlreadySetNum()
    {
        return iCatapultAlreadySetNum;
    }
    //
    
    public int pnti[];
    public int weakPoints[];
    boolean onGround;
    boolean nearGround;
    public float H;
    public float Pitch;
    public float sinkFactor;
    public float springsStiffness;
    public float tailStiffness;
    public boolean bIsSail;
    public int nOfGearsOnGr;
    public int nOfPoiOnGr;
    private int oldNOfGearsOnGr;
    private int oldNOfPoiOnGr;
    private int nP;
    public boolean gearsChanged;
    private static final double landSlot = 0.10000000000000001D;
    HierMesh HM;
    public boolean bFlatTopGearCheck;
    public static final int MP = 64;
    public static final double maxVf_gr = 65D;
    public static final double maxVn_gr = 7D;
    public static final double maxVf_wt = 50D;
    public static final double maxVn_wt = 30D;
    public static final double maxVf_wl = 110D;
    public static final double maxVn_wl = 7D;
    public static final double _1_maxVf_gr2 = 0.00023668639053254438D;
    public static final double _1_maxVn_gr2 = 0.020408163265306121D;
    public static final double _1_maxVf_wt2 = 0.00040000000000000002D;
    public static final double _1_maxVn_wt2 = 0.0011111111111111111D;
    public static final double _1_maxVf_wl2 = 8.264462809917356E-005D;
    public static final double _1_maxVn_wl2 = 0.020408163265306121D;
    private static Point3f Pnt[];
    private static boolean clp[] = new boolean[64];
    private Eff3DActor clpEff[];
    public Eff3DActor clpGearEff[][] = {
        {
            null, null
        }, {
            null, null
        }, {
            null, null
        }
    };
    public Eff3DActor clpEngineEff[][];
    private String effectName;
    private boolean bTheBabysGonnaBlow;
    public boolean lgear;
    public boolean rgear;
    public boolean cgear;
    public boolean bIsHydroOperable;
    private boolean bIsOperable;
    public boolean bTailwheelLocked;
    public double gVelocity[] = {
        0.0D, 0.0D, 0.0D
    };
    public float gWheelAngles[] = {
        0.0F, 0.0F, 0.0F
    };
    public float gWheelSinking[] = {
        0.0F, 0.0F, 0.0F
    };
    public float steerAngle;
    public double roughness;
    public float arrestorVAngle;
    public float arrestorVSink;
    public HookNamed arrestorHook;
    public int waterList[];
    private boolean isGearColl;
    private double MassCoeff;
    public boolean bFrontWheel;
    private static AnglesFork steerAngleFork = new AnglesFork();
    private double d;
    private double poiDrag;
    private double D0;
    private double D;
    private double Vnorm;
    private boolean isWater;
    private boolean bUnderDeck;
    private boolean bIsGear;
    private FlightModel FM;
    private boolean bIsMaster;
    private int fatigue[];
    private Point3d p0;
    private Point3d p1;
    private Loc l0;
    private Vector3d v0;
    private Vector3d tmpV;
    private Vector3d tmpV1;
    private double fric;
    private double fricF;
    private double fricR;
    private double maxFric;
    public double screenHQ;
    static ClipFilter clipFilter = new ClipFilter();
    private boolean canDoEffect;
    private static Vector3d Normal = new Vector3d();
    private static Vector3d Forward = new Vector3d();
    private static Vector3d Right = new Vector3d();
    private static Vector3d nwForward = new Vector3d();
    private static Vector3d nwRight = new Vector3d();
    private static double NormalVPrj;
    private static double ForwardVPrj;
    private static double RightVPrj;
    private static Vector3d Vnf = new Vector3d();
    private static Vector3d Fd = new Vector3d();
    private static Vector3d Fx = new Vector3d();
    private static Vector3d Vship = new Vector3d();
    private static Vector3d Fv = new Vector3d();
    private static Vector3d Tn = new Vector3d();
    private static Point3d Pn = new Point3d();
    private static Point3d PnT = new Point3d();
    private static Point3d Pship = new Point3d();
    private static Vector3d Vs = new Vector3d();
    private static Vector3d normal = new Vector3d();
    private static Matrix4d M4 = new Matrix4d();
    private static Gear plateFilterGear = null;
    private static PlateFilter plateFilter = new PlateFilter();
    private static Point3d corn = new Point3d();
    private static Point3d corn1 = new Point3d();
    private static Loc L = new Loc();
    private static float plateBox[] = new float[6];
    public boolean bPlateExist;
    public boolean bPlateGround;
    public boolean bPlateConcrete;
    public boolean bPlateSand;
    private BornPlace currentBornPlace;
    private static String ZUTI_SKIS_AC_CLASSES[] = {
        "DXXI_SARJA3_EARLY", "DXXI_SARJA3_LATE", "DXXI_SARJA3_SARVANTO", "DXXI_SARJA4", "R_5_SKIS", "BLENHEIM1", "BLENHEIM4", "GLADIATOR1", "GLADIATOR1J8A", "GLADIATOR2", 
        "I_15BIS_SKIS", "I_16TYPE5_SKIS", "I_16TYPE6_SKIS"
    };
    private boolean zutiHasPlaneSkisOnWinterCamo;
    Loc tempLoc;
    Point3f tempPoint;
    public float mgx;
    public float mgy;
    public float mgh;
    public float mgalpha;
    public float sgx;
    //
    private boolean bCatapultAllow;
    private boolean bCatapultBoost;
    private float catapultPower;
    private float catapultPowerJets;
    private boolean bCatapultAI;
    private boolean bCatapultAllowAI;
    private boolean bCatapultSet;
    private boolean bAlreadySetCatapult;
    private boolean bStandardDeckCVL;
    private boolean bSteamCatapult;
    private int iCatapultAlreadySetNum;
    private boolean bHasBlastDeflector;
    private Eff3DActor catEff;
    public ZutiAirfieldPoint zutiCurrentZAP;
    private double dCatapultOffsetX[];
    private double dCatapultOffsetY[];
    private double dCatapultYaw[];
    private int iCatapults;
    private static boolean bLogDetailBD = false;
    //
    static 
    {
        Pnt = new Point3f[64];
        for(int i = 0; i < Pnt.length; i++)
            Pnt[i] = new Point3f();

    }




}
