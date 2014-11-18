// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/3/2012 10:11:10 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Yak_36S.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.FuelTankGun_Tankyak;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunAGM65D;
import com.maddox.il2.objects.weapons.RocketGunAGM65L;
import com.maddox.il2.objects.weapons.RocketGunAIM9L;
import com.maddox.il2.objects.weapons.RocketGunAIM120A;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Yak_36, Aircraft, TypeFighterAceMaker, TypeRadarGunsight, 
//            Chute, PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, 
//            NetAircraft

public class AV_8A extends AV_8
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeDockable
{

    public AV_8A()
    {
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        sirenaSoundPlaying = false;
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
        removeChuteTimer = -1L;
        smplSirena.setInfinite(true);
        trgtlongAI = null;
        missilesList = new ArrayList();
        hasIR = false;
        hasPRHM = false;
        a2a = false;
		IR = false;
		tX4Prev = 0L;
    }   

    
    

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
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

    private boolean sirenaWarning()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(World.getPlayerAircraft() == null)
            return false;
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if((aircraft1 instanceof Aircraft) && aircraft1.getArmy() != World.getPlayerArmy() && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && aircraft1 != World.getPlayerAircraft() && aircraft1.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft1)).pos.getAbsPoint())).z;
            new String();
            new String();
            double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float)Math.atan2(d8, -d7);
            int k = (int)(Math.floor((int)f) - 90D);
            if(k < 0)
                k += 360;
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(i1, d11);
            int j1 = (int)(Math.floor((int)f1) - 90D);
            if(j1 < 0)
                j1 += 360;
            int k1 = j1 - j;
            int l1 = (int)(Math.ceil(((double)i1 * 3.2808399000000001D) / 100D) * 100D);
            if(l1 >= 5280)
                l1 = (int)Math.floor(l1 / 5280);
            bRadarWarning = (double)i1 <= 3000D && (double)i1 >= 50D && k1 >= 195 && k1 <= 345 && Math.sqrt(l * l) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag)
    {
        if(flag && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AN/APR-36: Enemy at Six!");
        } else
        if(!flag && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(3.5F, 1.5F, 2.5F, 8.2F, 3.0F, 3.5F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        droptank();
        ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;   
        checkmesh();
    }
    
    
    
    private void checkmesh()// TODO: Checkmesh
    {
    	boolean flag1 = false;
    	boolean flag2 = false;
    	boolean flag3 = false;
    	if(thisWeaponsName.startsWith("6xMk82") || thisWeaponsName.startsWith("8xMk82"))
        {
            hierMesh().chunkVisible("pylonBTerL", true);
            hierMesh().chunkVisible("pylonBTerR", true);
        }
        if(thisWeaponsName.endsWith("AN/AAQ-28V"))
        {
            hierMesh().chunkVisible("FLIRPOD", true);            
        }
        if(thisWeaponsName.startsWith("4xAIM-9+ 2xAIM-120") || thisWeaponsName.startsWith("2xAIM-9+ 4xAIM-120") || thisWeaponsName.startsWith("6xAIM-120"))
        {
        	flag1 = true;
        	flag2 = true;
        	flag3 = true;
        }      
        if(thisWeaponsName.startsWith("2xDT + 2xAIM-9") || thisWeaponsName.startsWith("2xDT + 2xAIM-120"))
        {
        	flag1 = true;
        }       
        if(thisWeaponsName.startsWith("4xAIM-9+ 2xDT") || thisWeaponsName.startsWith("4xAIM-120 + 2xDT") || thisWeaponsName.startsWith("2xAIM-120 + 2xAIM-9+ 2xDT"))
        {
        	flag1 = true;
        	flag2 = true;
        }
        if(thisWeaponsName.startsWith("2xRocketpod + 2xAGM65L + 2xAIM-9 + AN/AAQ-28V") || thisWeaponsName.startsWith("2xRocketpod + 2xAGM65D + 2xAIM-9 + AN/AAQ-28V"))
        {
        	flag1 = true;
        	flag3 = true;
        }
        if(hasIR)
        	flag1 = true;
        if(HasAGM)
        	flag2 = true;
        hierMesh().chunkVisible("PylonLOut", flag1);
        hierMesh().chunkVisible("PylonROut", flag1);
        hierMesh().chunkVisible("PylonLMid", flag2);
        hierMesh().chunkVisible("PylonRMid", flag2);
        hierMesh().chunkVisible("PylonLIn", flag3);
        hierMesh().chunkVisible("PylonRIn", flag3);
    }
    
    
    
    private final void doRemoveTankL()
    {
        if(hierMesh().chunkFindCheck("TankL") != -1)
        {
            hierMesh().hideSubTrees("TankL");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankL"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private final void doRemoveTankR()
    {
        if(hierMesh().chunkFindCheck("TankR") != -1)
        {
            hierMesh().hideSubTrees("TankR");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("TankR"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            this.getSpeed(vector3d);
            vector3d.z -= 10D;
            vector3d.set(vector3d);
            wreckage.setSpeed(vector3d);
        }
    }
    
    private boolean HasAGM;
    
    private void droptank()
    {
    	//missilesList.clear();       	
    	for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
        if(FM.CT.Weapons[i] == null)
              continue;
          for(int j = 0; j < FM.CT.Weapons[i].length; j++)
        {
        if(!FM.CT.Weapons[i][j].haveBullets())
               continue;
        if(FM.CT.Weapons[i][j] instanceof FuelTankGun_Tankyak)
        {	
        	havedroptank = true;
        	super.hierMesh().chunkVisible("TankL", true);
        	super.hierMesh().chunkVisible("TankR", true);
        }
        if(FM.CT.Weapons[i][j] instanceof RocketGunAIM9L)
        {	
        	hasIR = true;
        	//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if(FM.CT.Weapons[i][j] instanceof RocketGunAIM120A)
        {	
        	hasPRHM = true;
        	//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if (FM.CT.Weapons[i][j] instanceof RocketGunAGM65L) {
        	HasAGM = true;
			//missilesList.add(FM.CT.Weapons[i][j]);
        }
        if (FM.CT.Weapons[i][j] instanceof RocketGunAGM65D) {
        	IR = true;
			missilesList.add(FM.CT.Weapons[i][j]);
        }
    } 
    }
    }
    //------------------------------------------------------
    
    public void launchMsl()
    {
        if(missilesList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGunAGM65L)missilesList.remove(0)).shots(1);
            return;
        }
    }
    
    private long tfire;
    public BulletEmitter Weapons[][];
    private boolean hasIR;
    private boolean hasPRHM;   

    public void update(float f)
    {        
        if(bNeedSetup)
            checkAsDrone();
    	guidedMissileUtils.update();
    	//AIswitchmissile();
    	int i = aircIndex();
        if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);
                    if(((FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel)
                    ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(((FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
                if(dtime > 0L && ((Maneuver)super.FM).Group != null)
                {
                    ((Maneuver)super.FM).Group.leaderGroup = null;
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Pilot)super.FM).setDumbTime(3000L);
                    if(Time.current() > dtime + 3000L)
                    {
                        dtime = -1L;
                        ((Maneuver)super.FM).clear_stack();
                        ((Maneuver)super.FM).set_maneuver(0);
                        ((Pilot)super.FM).setDumbTime(0L);
                    }
                } else
                if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
        sirenaWarning();
        super.update(f);
        if(havedroptank == true)
        {	
        if(!FM.CT.Weapons[9][1].haveBullets())	       	
        {
        	havedroptank = false;
        	doRemoveTankL();
        	doRemoveTankR();
        }
        }
        if(((FlightModelMain) (super.FM)).CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(-5D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
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
                //((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(((FlightModelMain) (super.FM)).CT.DragChuteControl == 1.0F && super.FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                ((FlightModelMain) (super.FM)).CT.DragChuteControl = 0.0F;
                ((FlightModelMain) (super.FM)).CT.bHasDragChuteControl = false;
                //((FlightModelMain) (super.FM)).Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !((FlightModelMain) (super.FM)).CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (FM instanceof Maneuver) && ((Maneuver)FM).get_task() == 7 && !((FlightModelMain) (super.FM)).AP.way.isLanding()) //TODO straft
        {           
        	if(missilesList.isEmpty())
        	{        	
        	Pilot pilot = (Pilot)FM;           
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            float f1 = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
            if(f1 < 55F && vector3d.z < 0.0D){
                vector3d.z = 0.0D;} else               	
            if(pilot != null && isAlive(pilot.target_ground))
            {
                Point3d point3d2 = new Point3d();
                pilot.target_ground.pos.getAbs(point3d2);
                pilot.set_maneuver(43);
                if(pos.getAbsPoint().distance(point3d2) < 2000D)
                {
                    point3d2.sub(FM.Loc);
                    FM.Or.transformInv(point3d2);
                    ((FlightModelMain) (super.FM)).CT.PowerControl = 0.55F;                                                                             	
                }
            }
            setSpeed(vector3d);
        } else
        	if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {       		
        		Pilot pilot = (Pilot)FM;
                if(pilot.get_maneuver() == 43 && pilot.target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    pilot.target_ground.pos.getAbs(point3d);
                    point3d.sub(FM.Loc);
                    FM.Or.transformInv(point3d);
                    if(point3d.x > 1000D && point3d.x < (IR ? 2250D : 1250D) + 250D * (double)FM.Skill)
                    {
                        if(!IR)
                            point3d.x /= 2 - FM.Skill / 3;
                        if(point3d.y < point3d.x && point3d.y > -point3d.x && point3d.z * 1.5D < point3d.x && point3d.z * 1.5D > -point3d.x)
                        {
                            launchMsl();
                            tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            }
        }	       
    }
    
    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
            if(Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing)target_;
                int i = aircIndex();
                if(Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if(isNetMaster())
                ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
            {
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 1F;
            } else  
            {
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 0F;
            }	
        }
    }
    

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((FlightModelMain) (super.FM)).CT.setGearAirborne();
        moveGear(0.0F);
        com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            com.maddox.il2.engine.ActorNet actornet = null;
            if(Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive = 0L;         // Last Time when a common threat was reported
	private long intervalCommonThreat = 1000L;        // Interval (milliseconds) at which common threats should be dealt with (i.e. duration of warning sound / light)
	private long lastRadarLockThreatActive = 0L;      // Last Time when a radar lock threat was reported
	private long intervalRadarLockThreat = 1000L;     // Interval (milliseconds) at which radar lock threats should be dealt with (i.e. duration of warning sound / light)
	private long lastMissileLaunchThreatActive = 0L;  // Last Time when a missile launch threat was reported
	private long intervalMissileLaunchThreat = 1000L; // Interval (milliseconds) at which missile launch threats should be dealt with (i.e. duration of warning sound / light)
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    private boolean havedroptank;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private ArrayList missilesList;
    private Actor trgtlongAI;
    private long tX4Prev;
	private boolean a2a;
    private boolean IR;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Harrier");
        Property.set(class1, "meshName", "3DO/Plane/AV-8B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParMiG21());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1997F);
        Property.set(class1, "yearExpired", 2014F);
        Property.set(class1, "FlightModel", "FlightModels/AV-8B.fmd:AV8B");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitAV_8B.class, com.maddox.il2.objects.air.CockpitAV8FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 9, 9, 2, 2, 2, 2, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 
            3, 9, 9, 9, 9, 3, 3, 9, 9, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 0, 0, 0, 0, 0,
            0, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev03", 
            "_ExternalDev04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", 
            "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev05", "_ExternalDev06", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", 
            "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", 
            "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_CANNON04", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08",
            "_CANNON09", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 67;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDT + 2xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM120A", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-9+ 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-120 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-120 + 2xAIM-9+ 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM-9+ 2xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM-9+ 4xAIM-120";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83 + 2xMk84 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83 + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84 + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);            
            s = "4xMk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82SnakeEye + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82SnakeEye + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xCBU87 + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xCBU87 + 2xDT";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82LGB + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82LGB + 2xDT + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27); 
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);          
            s = "2xRocketpod + 2xDT + 2xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27); 
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRocketpod + 2xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xMk82LGB + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xCBU87 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 4xMk82SnakeEye";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);           
            s = "2xRocketpod + 2xAGM65L + 2xAIM-9 + AN/AAQ-28V";//TODO
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);            
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xDT + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xDT + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xMk83 + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xMk84 + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk84", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xCBU87 + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xCBU87 + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunCBU87", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65L + 2xMk82LGB + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65D + 2xMk82LGB + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRocketpod + 2xAGM-84 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM-84 + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM-84 + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunJDAM84", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunJDAM84", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHARM + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(2, "RocketGunHARM", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xMk83 + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xAGM65L + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xAGM65D + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82 + 2xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82SnakeEye + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xAGM65L + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xAGM65D + 2xAIM-9 + AN/AAQ-28V";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunAGM65D", 1);          
            a_lweaponslot[15] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82SnakeEye + 2xRocketpod + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleA8", 27);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "Pylon_LAU130white", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(1, "MGunbackfire", 27);         
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);            
            s = "2xMk20Rockeye + 2xDT + 2xAIM-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tankyak", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk20", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
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