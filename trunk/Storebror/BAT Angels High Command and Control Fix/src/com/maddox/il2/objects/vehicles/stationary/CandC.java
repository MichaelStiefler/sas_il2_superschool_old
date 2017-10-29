package com.maddox.il2.objects.vehicles.stationary;

import java.lang.reflect.Method;
import java.util.List;
//import java.util.Random;
import java.util.Map.Entry;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.NearestRadarTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.Selector;
import com.maddox.il2.game.VisibilityChecker;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.B_17;
import com.maddox.il2.objects.air.B_24;
import com.maddox.il2.objects.air.Paratrooper;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeScout;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.humans.Soldier;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.Ship.SubTypeVIIC_Srf;
import com.maddox.il2.objects.ships.Ship.SubTypeVIIC_Sub;
import com.maddox.il2.objects.ships.Ship.USSGatoSS212_Srf;
import com.maddox.il2.objects.ships.Ship.USSGatoSS212_Sub;
import com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Srf;
import com.maddox.il2.objects.ships.Ship.USSGreenlingSS213_Sub;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.AAA;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryCY6;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.ObjState;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public abstract class CandC
{
    public static class DynamicStrikeUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if((float)Time.current() <= delay)
                return true;
            List list = Engine.targets();
            int i = list.size();
            if(l < i)
            {
                Actor actor = (Actor)list.get(l);
                if(actor != null && ((actor instanceof TypeStormovik) || (actor instanceof TypeFighter) || (actor instanceof TypeDiveBomber)) && !(actor instanceof TypeBomber) && actor.getArmy() == myArmy && ((Maneuver)((Aircraft)actor).FM).hasBombs())
                    findtarget(actor);
                else
                if(actor != null && (actor instanceof TypeFighter) && !(actor instanceof TypeBomber) && actor.getArmy() == myArmy && !((Maneuver)((Aircraft)actor).FM).hasBombs())
                    fightertarget(actor);
                l++;
            } else
            {
                super.resetTimer(0.1F);
                listen();
            }
            return true;
        }

        public void listen()
        {
            List list = Engine.targets();
            int i;
            for(i = list.size(); m < i; m++)
            {
                Actor actor = (Actor)list.get(m);
                if(actor != null && ((actor instanceof TypeStormovik) || (actor instanceof TypeFighter) || (actor instanceof TypeDiveBomber)) && !(actor instanceof TypeBomber) && actor.getArmy() == myArmy && ((Maneuver)((Aircraft)actor).FM).hasBombs())
                {
                    Pilot pilot = (Pilot)((Aircraft)actor).FM;
//                    if(pilot.AP.way.curr().Action == 3 && pilot.get_task() != 7)
                    if(pilot.Group != null && pilot.AP.way.curr().Action == 3 && pilot.get_task() != 7) // TODO: Fixed by SAS~Storebror, added pilot Group null check!
                    {
                        pilot.Group.setGroupTask(4);
                        Point3d point3d = actor.pos.getAbsPoint();
                        point3d.z = World.land().HQ(point3d.x, point3d.y);
                        pilot.Group.setGTargMode(point3d, 8000F);
                    }
                }
            }

            if(m >= i)
                m = 0;
        }

        public void findtarget(Actor actor)
        {
            boolean flag = false;
            do
            {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = World.Rnd().nextInt(i);
                Actor actor1 = (Actor)list.get(j);
                if(actor1 != null && !(actor1 instanceof Aircraft) && ((actor1 instanceof TgtVehicle) || (actor1 instanceof TgtTank) || (actor1 instanceof TgtTrain)) && (actor1 instanceof StationaryGeneric) && !(actor1 instanceof BigshipGeneric))
                    if(actor1.getArmy() == actor.getArmy());
                Point3d point3d = actor1.pos.getAbsPoint();
                if(actor.pos.getAbsPoint().distance(point3d) > 10000D)
                {
                    if(actor instanceof TypeDiveBomber)
                        assigntarget(point3d, actor, 5000F, true);
                    else
                        assigntarget(point3d, actor, 1500F, true);
                    flag = true;
                }
            } while(!flag);
        }

        public void fightertarget(Actor actor)
        {
            boolean flag = false;
            do
            {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = World.Rnd().nextInt(i);
                Actor actor1 = (Actor)list.get(j);
                if(((actor1 instanceof TgtVehicle) || (actor1 instanceof TgtTank) || (actor1 instanceof TgtTrain)) && (actor1 instanceof StationaryGeneric))
                    if(flag);
                Point3d point3d = actor1.pos.getAbsPoint();
                flag = true;
                if(actor.pos.getAbsPoint().distance(point3d) > 10000D)
                {
                    assigntarget(point3d, actor, 5000F, false);
                    flag = true;
                }
            } while(!flag);
        }

        public void assigntarget(Point3d point3d, Actor actor, float f, boolean flag)
        {
            Pilot pilot = (Pilot)((Aircraft)actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = ((int)f + World.Rnd().nextInt(1000)) - 500;
            Point3d point3d2 = new Point3d(point3d.x, point3d.y, i);
            Point3d point3d3 = new Point3d(point3d2.x - 5000D, point3d2.y, i);
            if(point3d1.x - point3d2.x > 0.0D)
                point3d3.set(point3d2.x + 5000D, point3d2.y, i);
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 5000D, i);
            if(point3d1.y - point3d2.y > 0.0D)
                point3d4.set(point3d2.x, point3d2.y + 5000D, i);
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 5000D, i);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = i;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            float f1 = pilot.Vmax * 0.8F; // TODO: Fixed by SAS~Storebror: Useless double conversion removed
            waypoint.set(f1);
            waypoint1.set(f1);
            waypoint2.set(f1);
            waypoint3.set(f1);
            waypoint4.set(f1);
            waypoint.Action = 0;
            waypoint1.Action = 3;
            if(!flag)
                waypoint1.Action = 0;
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if(pilot.AP.way.first().Action == 1)
            {
                waypoint4.Action = 2;
                if(pilot.AP.way.first().getTargetName() != null)
                    waypoint4.setTarget(pilot.AP.way.first().getTargetName());
            }
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            if(!flag)
            {
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
                pilot.AP.way.add(waypoint3);
                pilot.AP.way.add(waypoint1);
            }
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        private int l;
        private int m;

        public DynamicStrikeUnit()
        {
            l = 0;
            m = 0;
            Timer1 = Timer2 = 0.01F;
            delay = 10F;
        }
    }

    public static class DynamicStreamUnit extends CandCGeneric
    {

        public boolean danger()
        {
            List list = Engine.targets();
            int i = list.size();
            if(l < i)
            {
                Actor actor = (Actor)list.get(l);
                if(actor != null && (actor instanceof TypeBomber) && actor.getArmy() == myArmy)
                    findtarget(actor);
                if(actor != null && (actor instanceof TypeFighter) && actor.getArmy() == myArmy)
                {
                    // // Random random = new Random();
                    int j = World.Rnd().nextInt(100);
                    if(j > 50)
                        findtarget(actor);
                    else
                        intrudertarget(actor);
                }
                l++;
            } else
            {
                cleanup();
//                ObjState.destroy(this); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            return true;
        }

        public void findtarget(Actor actor)
        {
            for(boolean flag = false; !flag;)
            {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = World.Rnd().nextInt(i);
                Actor actor1 = (Actor)list.get(j);
                if((actor1 instanceof Stationary.Wagon8) && actor1.getArmy() != myArmy && !flag)
                {
//                    int k = World.Rnd().nextInt(100);
                    Point3d point3d = new Point3d();
                    actor1.pos.getAbs(point3d);
                    falsetarget = point3d;
                    flag = true;
                    if(!first)
                    {
                        maintarget = point3d;
                        ObjState.destroy(actor1); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor1.postDestroy();
                        first = true;
                    }
                }
            }

            if(((Maneuver)((Aircraft)actor).FM).Skill > 2)
                assigntarget(falsetarget, actor);
            else
                assigntarget(maintarget, actor);
        }

        public void intrudertarget(Actor actor)
        {
            for(boolean flag = false; !flag;)
            {
                List list = Engine.targets();
                int i = list.size();
                // // Random random = new Random();
                int j = World.Rnd().nextInt(i);
                Actor actor1 = (Actor)list.get(j);
                if((actor1 instanceof Stationary.OpelBlitz6700A_fuel) && actor1.getArmy() != myArmy && !flag)
                {
                    actor1.pos.getAbs(falsetarget);
                    flag = true;
                }
            }

            assignintrudertarget(falsetarget, actor);
        }

        public void cleanup()
        {
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(actor != null && ((actor instanceof OBOEUnit) || (actor instanceof H2SUnit) || (actor instanceof AimpointUnit) || (actor instanceof ErrorUnit)) && actor.pos.getAbsPoint().distance(maintarget) > 1000D && actor.getArmy() == myArmy)
//                    ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    actor.postDestroy();
            }

        }

        public void assignintrudertarget(Point3d point3d, Actor actor)
        {
            Pilot pilot = (Pilot)((Aircraft)actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = World.Rnd().nextInt(4000) - 2000;
            int j = World.Rnd().nextInt(4000) - 2000;
            int k = (3000 + World.Rnd().nextInt(2000)) - 1000;
            Point3d point3d2 = new Point3d(point3d.x + (double)i, point3d.y + (double)j, k);
            Point3d point3d3 = new Point3d(point3d2.x - 10000D, point3d2.y, k);
            if(point3d1.x - point3d2.x > 0.0D)
                point3d3.set(point3d2.x + 10000D, point3d2.y, k);
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 10000D, k);
            if(point3d1.y - point3d2.y > 0.0D)
                point3d4.set(point3d2.x, point3d2.y + 10000D, k);
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 10000D, k);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = k;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            waypoint.set(400F);
            waypoint1.set(400F);
            waypoint2.set(400F);
            waypoint3.set(400F);
            waypoint4.set(400F);
            waypoint.Action = 0;
            waypoint1.Action = 0;
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if(pilot.AP.way.first().Action == 1)
                waypoint4.Action = 2;
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        public void assigntarget(Point3d point3d, Actor actor)
        {
            Pilot pilot = (Pilot)((Aircraft)actor).FM;
            Point3d point3d1 = new Point3d();
            pilot.AP.way.curr().getP(point3d1);
            // // Random random = new Random();
            int i = World.Rnd().nextInt(4000) - 2000;
            int j = World.Rnd().nextInt(4000) - 2000;
            int k = (6000 + World.Rnd().nextInt(2000)) - 1000;
            Point3d point3d2 = new Point3d(point3d.x + (double)i, point3d.y + (double)j, k);
            Point3d point3d3 = new Point3d(point3d2.x - 10000D, point3d2.y, k);
            if(point3d1.x - point3d2.x > 0.0D)
                point3d3.set(point3d2.x + 10000D, point3d2.y, k);
            Point3d point3d4 = new Point3d(point3d2.x, point3d2.y - 10000D, k);
            if(point3d1.y - point3d2.y > 0.0D)
                point3d4.set(point3d2.x, point3d2.y + 10000D, k);
            Point3d point3d5 = new Point3d(point3d1.x + 10000D, point3d1.y + 10000D, k);
            Point3d point3d6 = new Point3d();
            pilot.AP.way.first().getP(point3d6);
            point3d6.z = k;
            WayPoint waypoint = new WayPoint(point3d3);
            WayPoint waypoint1 = new WayPoint(point3d2);
            WayPoint waypoint2 = new WayPoint(point3d4);
            WayPoint waypoint3 = new WayPoint(point3d5);
            WayPoint waypoint4 = new WayPoint(point3d6);
            waypoint.set(400F);
            waypoint1.set(400F);
            waypoint2.set(400F);
            waypoint3.set(400F);
            waypoint4.set(400F);
            waypoint.Action = 0;
            waypoint1.Action = 3;
            if(actor instanceof TypeFighter)
                waypoint1.Action = 0;
            waypoint2.Action = 0;
            waypoint3.Action = 0;
            waypoint4.Action = 0;
            if(pilot.AP.way.first().Action == 1)
                waypoint4.Action = 2;
            pilot.AP.way.add(waypoint);
            pilot.AP.way.add(waypoint1);
            pilot.AP.way.add(waypoint2);
            pilot.AP.way.add(waypoint3);
            pilot.AP.way.add(waypoint4);
        }

        private static Point3d falsetarget = new Point3d();
        private static Point3d maintarget = new Point3d();
        private boolean first;
        private int l;


        public DynamicStreamUnit()
        {
            first = false;
            l = 0;
            Timer1 = Timer2 = 0.01F;
            delay = 1.0F;
        }
    }

    public static class LandingUnit extends CandCGeneric
    {

        public boolean danger()
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if((actor instanceof Aircraft) && ((Maneuver)((Aircraft)actor).FM).AP.way.isLandingOnShip() && ((Maneuver)((Aircraft)actor).FM).get_maneuver() == 25 && actor != World.getPlayerAircraft())
                {
                    Pilot pilot = (Pilot)((Aircraft)actor).FM;
//                    Point3d point3d = new Point3d();
                    actor.pos.getAbs(point3d);
                    float f = ((Pilot)((Aircraft)actor).FM).getAltitude() - (float)World.land().HQ(point3d.x, point3d.y); // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    if((float)pilot.Vwld.length() > pilot.VminFLAPS && f <= 60F) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        pilot.Vwld.scale(0.955D);
//                    Vector3d vector3d = new Vector3d();
                    actor.getSpeed(vector3d);
                    if(f <= 10F && vector3d.z < 0.0D)
                        vector3d.z = 0.0D;
                }
            }

            return true;
        }
        
        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public LandingUnit()
        {
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class LevelUnit extends CandCGeneric
    {

        public boolean danger()
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                
                // TODO: Fixed by SAS~Storebror: Completely rewritten code part to clean up the mess...
//                if(actor != null && (actor instanceof Aircraft) && !(actor instanceof TypeTransport) && ((Pilot)((Aircraft)actor).FM).get_task() == 7 && actor != World.getPlayerAircraft())
//                {
//                    
//                    if(!((Maneuver)((Aircraft)actor).FM).hasRockets() && !((Maneuver)((Aircraft)actor).FM).hasBombs())
//                    {
//                        ((Maneuver)((Aircraft)actor).FM).AP.way.next();
//                        ((Maneuver)((Aircraft)actor).FM).target_ground = null;
//                        ((Maneuver)((Aircraft)actor).FM).Group.setGroupTask(1);
//                        ((Maneuver)((Aircraft)actor).FM).Group.setGroupTask(1);
//                    }
//                    ((Maneuver)((Aircraft)actor).FM).CT.dropFuelTanks();
//                    Vector3d vector3d = new Vector3d();
//                    actor.getSpeed(vector3d);
//                    Point3d point3d = new Point3d();
//                    actor.pos.getAbs(point3d);
//                    float f = (float)((double)((Pilot)((Aircraft)actor).FM).getAltitude() - World.land().HQ(point3d.x, point3d.y));
//                    int k = 100;
//                    if(((Maneuver)((Aircraft)actor).FM).hasBombs())
//                        k = 300;
//                    if(f <= (float)k && vector3d.z < 0.0D)
//                        vector3d.z = 0.0D;
//                    actor.setSpeed(vector3d);
//                    if(f >= 1500F && vector3d.z > 0.0D)
//                        vector3d.z = 0.0D;
//                    actor.setSpeed(vector3d);
//                }
                
                if (actor == null) continue;
                if (!(actor instanceof Aircraft)) continue;
                Aircraft actorAircraft = (Aircraft)actor;
                if ((actorAircraft instanceof TypeTransport) || actorAircraft == World.getPlayerAircraft()) continue;
                if (!(actorAircraft.FM instanceof Pilot)) continue;
                Pilot actorPilot = (Pilot)actorAircraft.FM;
                if (actorPilot.get_task() != 7) continue;
                if (actorPilot.hasRockets() && !actorPilot.hasBombs()) {
                    actorPilot.AP.way.next();
                    actorPilot.target_ground = null;
                    if (actorPilot.Group != null) {
                        actorPilot.Group.setGroupTask(1); // FIXME: Why twice? No idea, but doesn't hurt and might
                        actorPilot.Group.setGroupTask(1); //        be necessary, code is impossible to see through!
                    }
                }
                actorPilot.CT.dropFuelTanks();
                actor.getSpeed(this.vector3d);
                float f = (float) (actorPilot.getAltitude() - World.land().HQ(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y));
                int k = 100;
                if (actorPilot.hasBombs())
                    k = 300;
                if (f <= (float) k && vector3d.z < 0D)
                    vector3d.z = 0D;
                if (f >= 1500F && vector3d.z > 0D)
                    vector3d.z = 0D;
                actor.setSpeed(vector3d);
            }

            return true;
        }

//        private int counter;
//        private boolean hadtarget;
        
        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        public LevelUnit()
        {
//            counter = 0;
//            hadtarget = false;
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class CityLightsUnit extends CandCGeneric
    {

        public boolean danger()
        {
            World.MaxVisualDistance = 50000F;
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            if(counter <= 100 && aircraft.pos.getAbsPoint().distance(point3d) < 20000D)
                while(counter <= 100 && !flag) 
                {
                    // // Random random = new Random();
                    int i = World.Rnd().nextInt(3000);
                    float f = i - 1500;
                    i = World.Rnd().nextInt(3000);
                    float f1 = i - 1500;
                    Engine.land();
                    int j = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(point3d.x + (double)f), Engine.land().WORLD2PIXY(point3d.y + (double)f1));
                    float f2 = (float)(World.land().HQ(point3d.x + (double)f, point3d.y + (double)f1) - point3d.z);
                    if(j >= 16 && j < 20)
                    {
                        Eff3DActor.New(new Loc(point3d.x + (double)f, point3d.y + (double)f1, point3d.z + (double)f2, 0.0F, 90F, 0.0F), 1.0F, "3DO/Effects/Fireworks/CityLight.eff", -1F);
                        flag = true;
                        counter++;
                    } else
                    {
                        flag = true;
                    }
                }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int counter;

        public CityLightsUnit()
        {
            counter = 0;
            Timer1 = Timer2 = delay = 0.01F;
        }
    }

    public static class SLD2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.pos.getAbsPoint().distance(point3d) < 5000D)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if(Actor.isAlive(actor) && (actor instanceof ArtilleryCY6.ProneInfantry))
                    {
                        // // Random random = new Random();
                        int i = World.Rnd().nextInt(360);
                        new Soldier(this, getArmy(), new Loc(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y, actor.pos.getAbsPoint().z, i, 0.0F, 0.0F));
                        super.setTimer(500);
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SLD2Unit()
        {
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class SLDUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.pos.getAbsPoint().distance(point3d) < 10000D)
            {
                // // Random random = new Random();
                int i = World.Rnd().nextInt(360);
                new Soldier(this, getArmy(), new Loc(pos.getAbsPoint().x, pos.getAbsPoint().y, pos.getAbsPoint().z, i, 0.0F, 0.0F));
                super.setTimer(500);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SLDUnit()
        {
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class NGS1Unit extends CandCGeneric
    {

        public boolean danger()
        {
            if(!hadtarget)
            {
                actor = Selector.getTarget();
                if(actor != null && !(actor instanceof Aircraft))
                {
                    hadtarget = true;
                    counter = 0;
                }
            }
            if(hadtarget)
            {
                if(counter >= 50)
                {
                    String s = "weapon.bomb_std";
//                    Point3d point3d = new Point3d();
                    actor.pos.getAbs(point3d);
                    // Random random = new Random();
                    int i = World.Rnd().nextInt(400);
                    int j = i - 200;
                    point3d.x += j;
                    i = World.Rnd().nextInt(400);
                    j = i - 200;
                    point3d.y += j;
                    float f = 25F;
                    float f1 = 136F;
                    i = World.Rnd().nextInt(100);
                    if(i > 50)
                    {
                        f = 50F;
                        f1 = 210F;
                    }
                    Explosions.generate(actor, point3d, f, 0, f1, !Mission.isNet());
                    MsgExplosion.send(actor, s, point3d, getOwner(), 0.0F, f, 0, f1);
                }
                if(counter >= 43 && counter < 45)
                    HUD.logCenter("                                                                             Splash!");
                if(counter > 21 && counter < 25)
                    HUD.logCenter("                                                                             Rounds Complete.");
                if(counter > 15 && counter < 25)
                    HUD.logCenter("                                                                             Firing.");
                if(counter > 5 && counter < 10)
                    HUD.logCenter("                                                                             Target Received.");
                counter++;
            }
            if(counter > 70)
            {
                hadtarget = false;
                counter = 0;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int counter;
        private boolean hadtarget;
        Actor actor;

        public NGS1Unit()
        {
            counter = 0;
            hadtarget = false;
            actor = null;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class NGSUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(!hadtarget)
            {
                actor = Selector.getTarget();
                if(actor != null && !(actor instanceof Aircraft))
                {
                    hadtarget = true;
                    counter = 0;
                }
            }
            if(hadtarget)
            {
                if(counter >= 50)
                {
                    String s = "weapon.bomb_std";
//                    Point3d point3d = new Point3d();
                    actor.pos.getAbs(point3d);
                    // Random random = new Random();
                    int i = World.Rnd().nextInt(200);
                    int j = i - 100;
                    point3d.x += j;
                    i = World.Rnd().nextInt(200);
                    j = i - 100;
                    point3d.y += j;
                    Explosions.generate(actor, point3d, 7F, 0, 30F, !Mission.isNet());
                    MsgExplosion.send(actor, s, point3d, getOwner(), 0.0F, 7F, 0, 30F);
                }
                if(counter >= 43 && counter < 45)
                    HUD.logCenter("                                                                             Splash!" + s1);
                if(counter > 21 && counter < 25)
                    HUD.logCenter("                                                                             Rounds Complete.");
                if(counter > 15 && counter < 25)
                    HUD.logCenter("                                                                             Firing.");
                if(counter > 5 && counter < 10)
                    HUD.logCenter("                                                                             Target Received.");
                counter++;
            }
            if(counter > 70)
            {
                hadtarget = false;
                counter = 0;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int counter;
        private boolean hadtarget;
        Actor actor;
        private static String s1 = null;


        public NGSUnit()
        {
            counter = 0;
            hadtarget = false;
            actor = null;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class BombUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
//            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(this, 10000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
            Aircraft aircraft = GetNearestEnemy(this, 10000F, 9);
            if(counter > 10)
            {
                counter = 0;
                startpoint.set(point3d.x + (double)(World.Rnd().nextInt(1000) - 500), point3d.y + (double)(World.Rnd().nextInt(1000) - 500), point3d.z);
            }
            if(aircraft != null && (aircraft instanceof TypeBomber) && aircraft.getArmy() != myArmy)
            {
                World.MaxVisualDistance = 50000F;
                counter++;
                String s = "weapon.bomb_std";
                startpoint.x += World.Rnd().nextInt(40) - 20;
                startpoint.y += World.Rnd().nextInt(40) - 20;
                Explosions.generate(this, startpoint, 7F, 0, 30F, !Mission.isNet());
                startpoint.z = World.land().HQ(startpoint.x, startpoint.y);
                MsgExplosion.send(this, s, startpoint, getOwner(), 0.0F, 7F, 0, 30F);
                Engine.land();
                int i = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(startpoint.x), Engine.land().WORLD2PIXY(startpoint.y));
                if(firecounter < 100 && i >= 16 && i < 20)
                {
                    Eff3DActor.New(null, null, new Loc(startpoint.x, startpoint.y, startpoint.z + 5D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/CityFire3.eff", 300F);
                    firecounter++;
                }
                super.setTimer(15);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private static Point3d startpoint = new Point3d();
        private int counter;
        private int firecounter;


        public BombUnit()
        {
            counter = 11;
            firecounter = 0;
            Timer1 = Timer2 = 0.05F;
        }
    }

    public static class BRGUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.pos.getAbsPoint().distance(point3d) < 10000D)
            {
                String s = "weapon.bomb_std";
                // Random random = new Random();
                int i = World.Rnd().nextInt(200);
                int j = i - 100;
                point3d.x += j;
                i = World.Rnd().nextInt(200);
                j = i - 100;
                point3d.y += j;
                i = World.Rnd().nextInt(5);
                point3d.z += i;
                Explosions.generate(this, point3d, 7F, 0, 30F, !Mission.isNet());
                MsgExplosion.send(this, s, point3d, getOwner(), 0.0F, 7F, 0, 30F);
                super.setTimer(15);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public BRGUnit()
        {
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class BoxUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Orient orient = new Orient();
            pos.getAbs(point3d, orient);
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(actor.pos.getAbsPoint().distance(point3d) < 10000D && actor.getArmy() != myArmy)
                {
                    String s = "weapon.bomb_std";
                    // Random random = new Random();
                    int k = World.Rnd().nextInt(1000);
                    int i1 = k - 500;
                    point3d.x += i1;
                    k = World.Rnd().nextInt(1000);
                    i1 = k - 500;
                    point3d.y += i1;
                    k = World.Rnd().nextInt(300);
                    i1 = k - 150;
                    point3d.z += actor.pos.getAbsPoint().z + (double)i1;
                    Explosions.AirFlak(point3d, 1);
                    MsgExplosion.send(this, s, point3d, getOwner(), 0.0F, 0.9F, 0, 30F);
                    l.set(point3d, orient);
                    Eff3DActor eff3dactor = Eff3DActor.New(l, 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Burn.eff", 1.0F);
                    eff3dactor.postDestroy(Time.current() + 1500L);
                    LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                    lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                    lightpointactor.light.setEmit(1.0F, 300F);
                    eff3dactor.draw.lightMap().put("light", lightpointactor);
                    super.setTimer(15);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

//        private int counter;
//        private boolean hadtarget;
        private static Loc l = new Loc();


        public BoxUnit()
        {
//            counter = 0;
//            hadtarget = false;
            Timer1 = Timer2 = 0.1F;
        }
    }

    public static class WindowUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeBomber) && actor.getArmy() == myArmy && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    int i = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                    if(i < 0)
                        i = 360 + i;
//                    boolean flag1 = false;
//                    Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, 4000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                    Aircraft aircraft = GetNearestEnemy(actor, 4000F, 9);
                    if (aircraft == null) continue; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                    if((aircraft instanceof TypeFighter) && aircraft.getSpeed(vector3d) > 20D)
                    {
                        if(aircraft == World.getPlayerAircraft())
                            flag = true;
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int)Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float)Math.atan2(d8, -d7);
                        int j = (int)(Math.floor((int)f) - 90D);
                        if(j < 0)
                            j = 360 + j;
                        int k = j - i;
                        if(k < 0)
                            k = 360 + k;
                        boolean flag2 = false;
                        if(k >= 90 && k <= 270)
                            flag2 = true;
                        double d9 = d - d3;
                        double d10 = d1 - d4;
                        boolean flag3 = false;
                        boolean flag4 = false;
                        int l = 0;
                        if(Mission.cur() != null)
                            l = Mission.cur().sectFile().get("Mods", "WindowEffect", 0);
                        if(l > 100)
                            l = 100;
                        if(l < 0)
                            l = 0;
                        // Random random = new Random();
                        int i1 = World.Rnd().nextInt(100);
                        if(i1 <= 3 + l)
                            flag3 = true;
                        if(i1 <= 11 + l)
                            flag4 = true;
                        int j1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                        if(j1 <= 4000 && Math.abs(d6) <= 500D && flag2 && flag3 && !flag) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                            ((Pilot)aircraft.FM).set_maneuver(14);
                        if(j1 <= 4000 && Math.abs(d6) <= 500D && flag2 && flag4 && flag) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                            HUD.logCenter("                                          RO: We're being jammed!");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();
        
        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public WindowUnit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class VectorUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            char c = '\u03E8';
//            Vector3d vector3d = new Vector3d();
            int i = 1000;
            int j = 1000;
            // Random random = new Random();
            int k = World.Rnd().nextInt(140);
            int l = k + 10;
//            Object obj = null;
            if(Mission.cur() != null)
                maxrange = Mission.cur().sectFile().get("Mods", "VectorRange", 100) * 1000;
            for(; i <= maxrange; i += 1000)
            {
                List list = Engine.targets();
                int i1 = list.size();
                for(int j1 = 0; j1 < i1; j1++)
                {
                    Actor actor = (Actor)list.get(j1);
                    if((actor instanceof Aircraft) && Mission.cur() != null && (Mission.cur().sectFile().get("Mods", "VectorVarAlt", 0) == 1 && Config.cur.ini.get("Mods", "VectorVarAlt", 0) != 1 || Mission.cur().sectFile().get("Mods", "VectorVarAlt", 0) == 1))
                    {
                        maxheight = (actor.pos.getAbsPoint().distance(point3d) / (double)maxrange) * 8000D;
                        if(maxheight < (double)l)
                            maxheight = l;
                    }
                    if(Config.cur.ini.get("Mods", "VectorMode", 0) == 1 && Mission.cur().sectFile().get("Mods", "VectorMode", 0) != 1 || Mission.cur().sectFile().get("Mods", "VectorMode", 0) == 1)
                        bombers = true;
                    if((bombers && (actor instanceof TypeBomber) || !bombers && (actor instanceof Aircraft)) && Actor.isAlive(actor) && actor.getArmy() != myArmy && actor.pos.getAbsPoint().distance(point3d) <= (double)i && actor.pos.getAbsPoint().z >= maxheight)
                        while(j <= maxrange) 
                        {
//                            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, j, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                            Aircraft aircraft = GetNearestEnemy(actor, j, 9);
//                            j = (int)((float)j + 1000F); // Fixed by SAS~Storebror: What the... ???
                            j += 1000;
                            if(aircraft != null && (aircraft instanceof TypeFighter) && aircraft.pos.getAbsPoint().distance(point3d) <= (double)maxrange)
                            {
                                if(World.Sun().ToSun.z < 0.0F && aircraft.FM.EI.getNum() > 1)
//                                    VisCheck.hasVector(aircraft, myArmy); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                                    HasVector(aircraft, myArmy);
                                AirGroup airgroup = ((Maneuver)((Aircraft)actor).FM).Group;
                                Pilot pilot = (Pilot)aircraft.FM;
                                if(airgroup.grTask == 1)
                                {
                                    pilot.targetAll();
                                    if (pilot.Group != null) { // TODO: Fixed by SAS~Storebror, added null check!
                                        pilot.Group.targetGroup = airgroup;
                                        pilot.Group.setGroupTask(3);
                                    }
                                }
                            }
                        }
                }

            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int maxrange;
        private double maxheight;
        private boolean bombers;

        public VectorUnit()
        {
            maxrange = 0x186a0;
            maxheight = 150D;
            bombers = false;
            Timer1 = Timer2 = 300F;
        }
    }

    public static class TakiUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil(k1);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof ShipGeneric)
                        byte0 = 40;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 60;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 5;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 15;
                    if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 20D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " km");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public TakiUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class StormFrontUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(Time.current() < (long)(Mission.cur().sectFile().get("Mods", "StormFrontDelay", 0) * 60))
                return false;
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.pos.getAbsPoint().distance(point3d) > 0.0D)
            {
                if(aircraft.pos.getAbsPoint().distance(point3d) > 250000D)
                    front = 1;
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) > 250000D)
                    front = 2;
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) > 200000D)
                    front = 3;
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) > 150000D)
                    front = 4;
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) > 100000D)
                    front = 5;
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) > 50000D)
                    front = 6;
                else
                    front = 7;
                int i = Mission.cur().sectFile().get("Mods", "WorstClouds", 7);
                if(front > i)
                    front = i;
            }
            if(front != lastfront)
            {
                Mission.createClouds(front, 1000F);
                World.land().cubeFullUpdate();
            }
            lastfront = front;
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int front;
        private int lastfront;

        public StormFrontUnit()
        {
            front = 1;
            lastfront = 1;
            Timer1 = Timer2 = 300F;
        }
    }

    public static class SPRUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            Pilot pilot = (Pilot)((Aircraft)aircraft).FM;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(aircraft.getArmy() == myArmy && (pilot.Wingman != null || pilot.crew > 1) && Actor.isAlive(actor) && ((actor instanceof TankGeneric) || (actor instanceof Wagon) || (actor instanceof ArtilleryGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric) || (actor instanceof CarGeneric)) && actor.getArmy() != myArmy)
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    double d6 = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    double d7 = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    double d8 = (d2 - d5) * 2D;
                    if(d8 > 6000D)
                        d8 = 6000D;
                    float f = World.getTimeofDay();
                    boolean flag1 = false;
                    if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
                        flag1 = true;
//                    // Random random = new Random();
//                    int j = World.Rnd().nextInt(100);
                    if(flag1)
                        d8 = 1500D - (d2 - d5);
                    String s = "units";
                    if(actor instanceof TankGeneric)
                        s = "armor";
                    if(actor instanceof ArtilleryGeneric)
                        s = "guns";
                    if(actor instanceof CarGeneric)
                        s = "vehicles";
                    if(actor instanceof Wagon)
                        s = "train";
                    if((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric))
                    {
                        s = "ship";
                        d8 *= 2D;
                    }
                    double d9 = d3 - d;
                    double d10 = d4 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d10, -d9);
                    double d11 = Math.floor((int)f1) - 90D;
                    if(d11 < 0.0D)
                        d11 = 360D + d11;
                    int k = (int)(d11 - (double)i);
                    if(k < 0)
                        k = 360 + k;
                    int l = (int)(Math.ceil((double)(k + 15) / 30D) - 1.0D);
                    if(l < 1)
                        l = 12;
                    double d12 = d - d3;
                    double d13 = d1 - d4;
                    double d14 = Math.ceil(Math.sqrt(d13 * d13 + d12 * d12));
                    if(d14 <= d8)
                        HUD.logCenter("                                          Enemy " + s + " spotted at " + l + " o'clock!");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SPRUnit()
        {
            Timer1 = Timer2 = 30F;
        }
    }

    public static class SpawnUnit extends CandCGeneric
    {

        public boolean danger()
        {
            count++;
            if(count > 100)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if(actor instanceof Stationary.Motorcycle)
//                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                }

//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            boolean flag1 = false;
            // Random random = new Random();
//            boolean flag2 = false;
            List list = Engine.targets();
            int j = list.size();
            while(!flag) 
            {
                int i = World.Rnd().nextInt(j);
                Actor actor1 = (Actor)list.get(i);
                if((actor1 instanceof Stationary.Motorcycle) && actor1.getArmy() == myArmy)
                {
                    int k = 0;
                    Engine.land();
                    int l = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor1.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor1.pos.getAbsPoint().y));
                    for(; k <= j && !flag; k++)
                    {
                        Actor actor2 = (Actor)list.get(k);
                        int i1 = World.Rnd().nextInt(100);
                        boolean flag3 = false;
                        if(((actor2 instanceof ShipGeneric) || (actor2 instanceof BigshipGeneric)) && l >= 28 && l <= 32)
                            flag3 = true;
                        if(((actor2 instanceof StationaryGeneric) || (actor2 instanceof ArtilleryGeneric)) && (l < 28 || l > 32))
                            flag3 = true;
                        if(i1 < 33 && actor2.pos.getAbsPoint().distance(point3d) < 500D && flag3 && actor2.getArmy() == myArmy)
                        {
                            Point3d point3d1 = new Point3d();
                            actor1.pos.getAbs(point3d1);
                            int j1 = World.Rnd().nextInt(1000) - 500;
                            point3d1.x += j1;
                            j1 = World.Rnd().nextInt(1000) - 500;
                            point3d1.y += j1;
                            j1 = World.Rnd().nextInt(30) - 15;
                            CandCGeneric.o.setYPR(actor1.pos.getAbsOrient().getYaw() + (float)j1, 0.0F, 0.0F);
                            Engine.land();
                            int k1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(point3d1.x), Engine.land().WORLD2PIXY(point3d1.y));
                            if(l == k1 && flag3)
                            {
                                actor2.pos.setRel(point3d1, CandCGeneric.o);
                                actor2.pos = new ActorPosMove(actor2.pos);
                                actor2.collide(true);
                                ((ActorAlign)actor2).align();
                                flag = true;
                            }
                        }
                    }

                }
                i++;
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int count;

        public SpawnUnit()
        {
            count = 0;
            Timer1 = Timer2 = delay = 1.0F;
        }
    }

    public static class AmbushUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(counter > 50)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            AmbushUnit ambushunit = this;
            Actor actor = War.GetNearestEnemy(ambushunit, ambushunit.getArmy(), 200F);
            // Random random = new Random();
            List list = Engine.targets();
            int i = 0;
            for(int j = list.size(); actor != null && ((actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && (counter <= 50 || i <= j); i++)
            {
                Actor actor1 = (Actor)list.get(i);
                if((actor1 instanceof ArtilleryGeneric) && actor1.pos.getAbsPoint().distance(point3d) > 5000D && actor1.getArmy() == ambushunit.getArmy())
                {
                    Point3d point3d1 = new Point3d();
                    point3d1 = point3d;
                    int k = World.Rnd().nextInt(200) - 100;
                    point3d1.x += k;
                    k = World.Rnd().nextInt(200) - 100;
                    point3d1.y += k;
                    actor1.pos.setRel(point3d1, CandCGeneric.o);
                    actor1.pos = new ActorPosMove(actor1.pos);
                    actor1.collide(true);
                    ((ActorAlign)actor1).align();
                    counter++;
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int counter;

        public AmbushUnit()
        {
            counter = 0;
            Timer1 = Timer2 = delay = 5F;
        }
    }

    public static class SN2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if(d3 < (double)(1.25F * f3) && !flag1)
                        f4 = (float)d3 * 0.8F;
                    if(d3 < (double)(1.25F * f3) && flag1)
                        if(d3 <= (double)(1.25F * f3 * 0.5F))
                            f4 = (float)(d3 * 0.8D * 2D);
                        else
                            f4 = f3;
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil((double)i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if(j3 < 0)
                        j3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                    int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if(j3 < 5)
                        s1 = "Dead ahead, ";
                    if(j3 >= 5 && j3 < 8) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 5, ";
                    if(j3 > 7 && j3 <13) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 10, ";
                    if(j3 > 12 && j3 <18) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 15, ";
                    if(j3 > 17 && j3 <= 25) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 20, ";
                    if(j3 > 25 && j3 <= 35)
                        s1 = "Right 30, ";
                    if(j3 > 35 && j3 <= 45)
                        s1 = "Right 40, ";
                    if(j3 > 45 && j3 <= 60)
                        s1 = "Turn right, ";
                    if(j3 > 355)
                        s1 = "Dead ahead, ";
                    if(j3 <= 355 && j3 > 352) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 5, ";
                    if(j3 < 353 && j3 > 347) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 10, ";
                    if(j3 < 348 && j3 > 342) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 15, ";
                    if(j3 < 343 && j3 >= 335) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 20, ";
                    if(j3 < 335 && j3 >= 325)
                        s1 = "Left 30, ";
                    if(j3 < 325 && j3 >= 315)
                        s1 = "Left 40, ";
                    if(j3 < 345 && j3 >= 300)
                        s1 = "Turn left, ";
                    String s2 = "  ";
                    if(k2 < -10)
                        s2 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s2 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s2 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s2 = "up a bit";
                    if(k2 > 10)
                        s2 = "pull up";
                    String s3 = "  ";
                    if(j3 < 5)
                        s3 = "dead ahead, ";
                    if(j3 >= 5 && j3 < 8) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "right by 5\260, ";
                    if(j3 > 7 && j3 < 13) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "right by 10\260, ";
                    if(j3 > 12 && j3 < 18) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "right by 15\260, ";
                    if(j3 > 17 && j3 <= 25) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "right by 20\260, ";
                    if(j3 > 25 && j3 <= 35)
                        s3 = "right by 30\260, ";
                    if(j3 > 35 && j3 <= 45)
                        s3 = "right by 40\260, ";
                    if(j3 > 45 && j3 <= 60)
                        s3 = "off our right, ";
                    if(j3 > 355)
                        s3 = "dead ahead, ";
                    if(j3 <= 355 && j3 > 352) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "left by 5\260, ";
                    if(j3 < 353 && j3 > 347) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "left by 10\260, ";
                    if(j3 < 348 && j3 > 342) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "left by 15\260, ";
                    if(j3 < 343 && j3 >= 335) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s3 = "left by 20\260, ";
                    if(j3 < 335 && j3 >= 325)
                        s3 = "left by 30\260, ";
                    if(j3 < 325 && j3 >= 315)
                        s3 = "left by 40\260, ";
                    if(j3 < 345 && j3 >= 300)
                        s3 = "off our left, ";
                    if(i2 <= k3 && i2 > 1500 && k2 >= -50 && k2 <= 50 && Math.abs(i3) <= 60) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        freq = 6F;
                    } else
                    if(i2 <= k3 && i2 <= 1500 && i2 >= 500 && k2 >= -50 && k2 <= 50 && Math.abs(i3) <= 60) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        freq = 4F;
                    } else
                    {
                        freq = 6F;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public SN2Unit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class SN2bUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if(d3 < (double)(1.25F * f3) && !flag1)
                        f4 = (float)d3 * 0.8F;
                    if(d3 < (double)(1.25F * f3) && flag1)
                        if(d3 <= (double)(1.25F * f3 * 0.5F))
                            f4 = (float)(d3 * 0.8D * 2D);
                        else
                            f4 = f3;
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil((double)i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if(j3 < 0)
                        j3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                    int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if(j3 < 5)
                        s1 = "Dead ahead, ";
                    if(j3 >= 5 && (double)j3 <= 7.5D)
                        s1 = "Right 5, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s1 = "Right 10, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                        s1 = "Right 15, ";
                    if((double)j3 > 17.5D && j3 <= 25)
                        s1 = "Right 20, ";
                    if(j3 > 25 && j3 <= 35)
                        s1 = "Right 30, ";
                    if(j3 > 35 && j3 <= 45)
                        s1 = "Right 40, ";
                    if(j3 > 45 && j3 <= 60)
                        s1 = "Turn right, ";
                    if(j3 > 355)
                        s1 = "Dead ahead, ";
                    if(j3 <= 355 && (double)j3 >= 352.5D)
                        s1 = "Left 5, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s1 = "Left 10, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                        s1 = "Left 15, ";
                    if((double)j3 < 342.5D && j3 >= 335)
                        s1 = "Left 20, ";
                    if(j3 < 335 && j3 >= 325)
                        s1 = "Left 30, ";
                    if(j3 < 325 && j3 >= 315)
                        s1 = "Left 40, ";
                    if(j3 < 345 && j3 >= 300)
                        s1 = "Turn left, ";
                    String s2 = "  ";
                    if(k2 < -10)
                        s2 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s2 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s2 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s2 = "up a bit";
                    if(k2 > 10)
                        s2 = "pull up";
                    String s3 = "  ";
                    if(j3 < 5)
                        s3 = "dead ahead, ";
                    if(j3 >= 5 && (double)j3 <= 7.5D)
                        s3 = "right by 5\260, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s3 = "right by 10\260, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                        s3 = "right by 15\260, ";
                    if((double)j3 > 17.5D && j3 <= 25)
                        s3 = "right by 20\260, ";
                    if(j3 > 25 && j3 <= 35)
                        s3 = "right by 30\260, ";
                    if(j3 > 35 && j3 <= 45)
                        s3 = "right by 40\260, ";
                    if(j3 > 45 && j3 <= 60)
                        s3 = "off our right, ";
                    if(j3 > 355)
                        s3 = "dead ahead, ";
                    if(j3 <= 355 && (double)j3 >= 352.5D)
                        s3 = "left by 5\260, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s3 = "left by 10\260, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                        s3 = "left by 15\260, ";
                    if((double)j3 < 342.5D && j3 >= 335)
                        s3 = "left by 20\260, ";
                    if(j3 < 335 && j3 >= 325)
                        s3 = "left by 30\260, ";
                    if(j3 < 325 && j3 >= 315)
                        s3 = "left by 40\260, ";
                    if(j3 < 345 && j3 >= 300)
                        s3 = "off our left, ";
                    if((double)i2 <= (double)k3 && (double)i2 > 1500D && k2 >= -50 && k2 <= 50 && Math.sqrt(i3 * i3) <= 60D)
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)k3 && (double)i2 <= 1500D && (double)i2 >= 200D && k2 >= -50 && k2 <= 50 && Math.sqrt(i3 * i3) <= 60D)
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        freq = 4F;
                    } else
                    {
                        freq = 6F;
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public SN2bUnit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class SerrateUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeFighter) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    Aircraft aircraft = World.getPlayerAircraft();
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    int i = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                    if(i < 0)
                        i = 360 + i;
                    int j = (int)(-((double)actor.pos.getAbsOrient().getPitch() - 90D));
                    if(j < 0)
                        j = 360 + j;
                    // new String();
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
//                    double d9 = d - d3;
//                    double d10 = d1 - d4;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    float f1 = 57.32484F * (float)Math.atan2(d8, -d7);
                    int k = (int)(Math.floor((int)f) - 90D);
                    if(k < 0)
                        k = 360 + k;
                    int l = (int)(Math.floor((int)f1) + 90D);
                    if(l < 0)
                        l = 360 + l;
                    int i1 = k - i;
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.sqrt(d6 * d6);
                    int j1 = (int)(Math.ceil(Math.sqrt(d12 * d12 + d11 * d11) / 10D) * 10D);
                    float f2 = 57.32484F * (float)Math.atan2(j1, d13);
                    int k1 = (int)(Math.floor((int)f2) - 90D);
                    if(k1 < 0)
                        k1 = 360 + k1;
                    int l1 = k1 - j;
                    int i2 = 16000;
                    if(l1 >= 220 && l1 <= 320 && (i1 <= 60 && i1 >= 0 || i1 >= 300 && i1 <= 360))
                        i2 = 0x13880;
                    if(j1 <= i2)
                        HUD.logCenter("                                         Serrate: Target bearing " + l + "\260");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();
        
        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public SerrateUnit()
        {
            Timer1 = Timer2 = delay = 15F;
        }
    }

    public static class SARUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(Time.current() < (long)(Mission.cur().sectFile().get("Mods", "SARDelay", 0) * 60))
                return false;
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                pos.getAbs(point3d);
                if((actor instanceof Aircraft) && actor.pos.getAbsPoint().distance(point3d) <= 1000D && actor.getArmy() == myArmy)
                {
                    if(!popped)
                    {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        popped = true;
                    }
                    if((actor instanceof Aircraft) && actor.pos.getAbsPoint().distance(point3d) <= 300D && actor.getSpeed(vector3d) < 10D)
                    {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if(!pickup)
                {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if(aircraft1.pos.getAbsPoint().distance(point3d) < 3000D && aircraft1.getArmy() == myArmy)
                        flag = true;
                    pos.getAbs(point3d);
                    double d2 = (Main3D.cur3D().land2D.worldOfsX() + point3d.x) / 10000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsY() + point3d.y) / 10000D;
                    double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
//                    double d5 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x / 10000D;
//                    double d6 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y / 10000D;
                    char c = (char)(int)(65D + Math.floor((d2 / 676D - Math.floor(d2 / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d2 / 26D - Math.floor(d2 / 26D)) * 26D));
                    double d7 = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d8 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                    // new String();
                    String s1;
                    if(d4 > 260D)
                        s1 = "" + c + c1;
                    else
                        s1 = "" + c1;
                    double d9 = d7 - d;
                    double d10 = d8 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d10, -d9);
                    double d11 = Math.floor((int)f1) - 90D;
                    if(d11 < 0.0D)
                        d11 = 360D + d11;
                    int j = (int)(d11 - (double)i);
                    if(j < 0)
                        j = 360 + j;
                    int k = (int)(Math.ceil((double)(j + 15) / 30D) - 1.0D);
                    if(k == 0)
                        k = 12;
                    // new String();
                    int l = (int)Math.ceil(d3);
                    if(flag && aircraft1.pos.getAbsPoint().distance(point3d) < 2000D && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             Man in the water! " + k + " o'clock!");
                    else
                    if(!flag && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             SAR required at map grid " + s1 + "-" + l);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean pickup;
        private boolean popped;
//        private float delay;

        public SARUnit()
        {
            pickup = false;
            popped = false;
            delay = 0.0F;
            Timer1 = Timer2 = 15F;
            setMesh("3do/humans/Paratroopers/Water/US_Dinghy/live.sim");
        }
    }

    public static class RESCAPUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(Time.current() < (long)(Mission.cur().sectFile().get("Mods", "RESCAPDelay", 0) * 60))
                return false;
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            Actor actor = War.GetNearestEnemy(this, getArmy(), 100F);
            Aircraft aircraft = World.getPlayerAircraft();
            if(actor != null && aircraft != null && aircraft.getArmy() == myArmy) // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                if(actor.pos.getAbsPoint().distance(point3d) > 10D)
                {
                    HUD.logCenter("                                                                             They're getting close! Hurry!");
                } else
                {
                    HUD.logCenter("                                                                             They're right on top of me!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor1 = (Actor)entry.getValue();
                pos.getAbs(point3d);
                if(actor1.getArmy() == myArmy && (actor1 instanceof Aircraft))
                {
                    if(actor1.pos.getAbsPoint().distance(point3d) <= 1000D && !popped)
                    {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        popped = true;
                    }
                    if(actor1 != World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 300D && actor1.getSpeed(vector3d) <= 27D || actor1 == World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 30D && actor1.getSpeed(vector3d) <= 27D)
                    {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if(!pickup)
                {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if(aircraft1.pos.getAbsPoint().distance(point3d) < 3000D)
                        flag = true;
                    pos.getAbs(point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x / 10000D;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y / 10000D;
                    char c = (char)(int)(65D + Math.floor((d / 676D - Math.floor(d / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d / 26D - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if(d2 > 260D)
                        s1 = "" + c + c1;
                    else
                        s1 = "" + c1;
                    double d5 = d3 - d;
                    double d6 = d4 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d6, d5);
                    int i = (int)f1;
                    i = (i + 180) % 360;
                    // new String();
                    String s2 = "east";
                    if((double)i <= 315D && (double)i >= 225D)
                        s2 = "south";
                    if((double)i <= 135D && (double)i >= 45D)
                        s2 = "north";
                    if((double)i <= 44D && (double)i >= 316D)
                        s2 = "east";
                    if((double)i <= 224D && (double)i >= 136D)
                        s2 = "west";
                    // new String();
                    double d7 = (int)Math.ceil(d1);
                    if(flag && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             Got you in sight! I'm to the " + s2 + "!");
                    else
                    if(!flag && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             RESCAP required at map grid " + s1 + "-" + (int)d7);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean pickup;
        private boolean popped;
//        private float delay;

        public RESCAPUnit()
        {
            pickup = false;
            popped = false;
            delay = 0.0F;
            Timer1 = Timer2 = 15F;
            setMesh("3do/Buildings/addobjects/Human_02/live.sim");
        }
    }

    public static class DynamicRescueUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                actor2 = (Actor)entry.getValue();
                if(!pilotdown && actor2 != null && Actor.isAlive(actor2) && actor2.getArmy() == myArmy && (actor2 instanceof Paratrooper) && actor2.pos.getAbsPoint().z - World.land().HQ(actor2.pos.getAbsPoint().x, actor2.pos.getAbsPoint().y) < 1.0D)
                {
                    o.set(0.0F, 0.0F, 0.0F);
                    pos.setRel(actor2.pos.getAbsPoint(), o);
                    pos = new ActorPosMove(pos);
                    collide(false);
                    align();
                    Engine.land();
                    int i = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor2.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor2.pos.getAbsPoint().y));
                    if(i >= 28 && i < 32)
                        setMesh("3do/humans/Paratroopers/Water/US_Dinghy/live.sim");
                    else
                        setMesh("3do/Buildings/addobjects/Human_02/live.sim");
//                    ObjState.destroy(actor2); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    actor2.postDestroy();
                    pilotdown = true;
                    callchopper();
                }
            }

            if(!pilotdown)
                return true;
            super.resetTimer(15F);
            pos.getAbs(point3d);
            float f = World.getTimeofDay();
            String s = "Effects/Smokes/Yellowsmoke.eff";
            if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
            Actor actor = War.GetNearestEnemy(this, getArmy(), 100F);
            Aircraft aircraft = World.getPlayerAircraft();
            if(actor != null && aircraft != null && aircraft.getArmy() == myArmy) // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                if(actor.pos.getAbsPoint().distance(point3d) > 10D)
                {
                    HUD.logCenter("                                                                             They're getting close! Hurry!");
                } else
                {
                    HUD.logCenter("                                                                             They're right on top of me!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
            for(Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
            {
                Actor actor1 = (Actor)entry1.getValue();
                pos.getAbs(point3d);
                if(actor1.getArmy() == myArmy && (actor1 instanceof Aircraft))
                {
                    if(actor1.pos.getAbsPoint().distance(point3d) <= 1000D && !popped)
                    {
                        Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, s, -1F);
                        popped = true;
                    }
                    if(actor1 != World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 200D && actor1.getSpeed(vector3d) <= 27D || actor1 == World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 30D && actor1.getSpeed(vector3d) <= 27D)
                    {
                        HUD.logCenter("                                                                             We got him! Heading home!");
                        pickup = true;
                        World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
                        setMesh("3do/primitive/siren/mono.sim");
//                        destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        this.postDestroy();
                    }
                }
                if(!pickup)
                {
                    boolean flag = false;
                    Aircraft aircraft1 = World.getPlayerAircraft();
                    if(aircraft1.pos.getAbsPoint().distance(point3d) < 3000D)
                        flag = true;
                    pos.getAbs(point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x / 10000D;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y / 10000D;
                    char c = (char)(int)(65D + Math.floor((d / 676D - Math.floor(d / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d / 26D - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if(d2 > 260D)
                        s1 = "" + c + c1;
                    else
                        s1 = "" + c1;
                    double d5 = d3 - d;
                    double d6 = d4 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d6, d5);
                    int j = (int)f1;
                    j = (j + 180) % 360;
                    // new String();
                    String s2 = "east";
                    if((double)j <= 315D && (double)j >= 225D)
                        s2 = "south";
                    if((double)j <= 135D && (double)j >= 45D)
                        s2 = "north";
                    if((double)j <= 44D && (double)j >= 316D)
                        s2 = "east";
                    if((double)j <= 224D && (double)j >= 136D)
                        s2 = "west";
                    // new String();
                    double d7 = (int)Math.ceil(d1);
                    if(flag && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             Got you in sight! I'm to the " + s2 + "!");
                    else
                    if(!flag && aircraft1.getArmy() == myArmy)
                        HUD.logCenter("                                                                             RESCAP required at map grid " + s1 + "-" + (int)d7);
                }
                if(inbound)
                    checkchopper();
            }

            return true;
        }

        private void callchopper()
        {
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                chopper = (Actor)entry.getValue();
//                if(chopper != null && (chopper instanceof HRS3AI) && chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) < 200000D)
                if(chopper != null && isHRS3AI(chopper) && chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) < 200000D)
                {
                    inbound = true;
                    Pilot pilot = (Pilot)((Aircraft)chopper).FM;
                    Point3d point3d = new Point3d();
                    Point3d point3d2 = new Point3d();
                    actor2.pos.getAbs(point3d);
                    chopper.pos.getAbs(point3d2);
                    Point3d point3d3 = new Point3d();
                    Point3d point3d4 = new Point3d();
                    point3d3.x = point3d.x + (point3d.x - point3d2.x) / 20D;
                    point3d3.y = point3d.y + (point3d.y - point3d2.y) / 20D;
                    point3d4.x = point3d.x - (point3d.x - point3d2.x) / 10D;
                    point3d4.y = point3d.y - (point3d.y - point3d2.y) / 10D;
                    point3d3.z = point3d.z = 100D;
                    point3d4.z = 300D;
                    WayPoint waypoint = new WayPoint(point3d4);
                    waypoint.set(220F);
                    waypoint.Action = 0;
                    pilot.AP.way.add(waypoint);
                    WayPoint waypoint1 = new WayPoint(point3d);
                    waypoint1.set(220F);
                    waypoint1.Action = 0;
                    pilot.AP.way.add(waypoint1);
                    WayPoint waypoint2 = new WayPoint(point3d3);
                    waypoint2.set(220F);
                    waypoint2.Action = 3;
                    pilot.AP.way.add(waypoint2);
                    WayPoint waypoint3 = new WayPoint(pilot.AP.way.first().getP());
                    waypoint3.set(220F);
                    waypoint3.Action = 0;
                    pilot.AP.way.add(waypoint3);
                    return;
                }
            }

        }

        private void checkchopper()
        {
            check++;
            if(check > 10)
                if(Actor.isAlive(chopper))
                {
                    int i = (int)((chopper.pos.getAbsPoint().distance(actor2.pos.getAbsPoint()) / 1000D / 220D) * 60D);
                    HUD.logCenter("                                                                             Chopper inbound. ETA " + i + " minutes!");
                    check = 0;
                } else
                {
                    HUD.logCenter("                                                                             Chopper down! Abort rescue!");
//                    destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                    this.postDestroy();
                }
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private static Actor chopper = null;
        private static Actor actor2 = null;
        private boolean pickup;
        private boolean popped;
//        private float delay;
        private boolean pilotdown;
//        private static Point3d point3d1 = new Point3d();
//        private static Orient o = new Orient();
        private boolean inbound;
        private int check;


        public DynamicRescueUnit()
        {
            pickup = false;
            popped = false;
            delay = 0.0F;
            pilotdown = false;
            inbound = false;
            check = 0;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RefuelUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            float f = 0.0F;
//            Object obj = null;
            if(Mission.cur() != null)
            {
                if(Mission.cur().sectFile().get("Mods", "RefuelAll", 0) != 1);
                refuelall = true;
                if(Config.cur.ini.get("Mods", "RefuelAlI", 0) != 1);
                refuelall = true;
                if(Mission.cur().sectFile().get("Mods", "RefuelPlayer", 0) != 1);
                refuelplayer = true;
                if(Config.cur.ini.get("Mods", "RefuelPlayer", 0) != 1);
                refuelplayer = true;
            }
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor1 = (Actor)entry.getValue();
//                if(((actor1 instanceof KB_29P) || (actor1 instanceof A1H_Tanker)) && actor1.getArmy() == myArmy && actor1.getSpeed(vector3d) > 20D)
                if((isKB_29P(actor1) || isA1H_Tanker(actor1)) && actor1.getArmy() == myArmy && actor1.getSpeed(vector3d) > 20D)
                    do
                    {
//                        Point3d point3d1 = actor1.pos.getAbsPoint();
                        List list = Engine.targets();
                        int i = list.size();
                        for(int j = 0; j < i; j++)
                        {
                            Actor actor = (Actor)list.get(j);
//                            if(!refuelall && (actor instanceof F84G3) || (actor instanceof FJ_3M) || refuelall && (actor instanceof Aircraft) && actor.getArmy() == myArmy)
                            if(!refuelall && isF84G3(actor) || isFJ_3M(actor) || refuelall && (actor instanceof Aircraft) && actor.getArmy() == myArmy)
                            {
                                int k = (int)(-((double)actor1.pos.getAbsOrient().getYaw() - 90D));
                                if(k < 0)
                                    k = 360 + k;
                                double d = Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x;
                                double d1 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y;
                                double d2 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().z;
                                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                                double d6 = d3 - d;
                                double d7 = d4 - d1;
                                double d8 = d - d3;
                                double d9 = d1 - d4;
                                double d10 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                                double d11 = (int)Math.sqrt((actor1.getSpeed(vector3d) - actor.getSpeed(vector3d)) * (actor1.getSpeed(vector3d) - actor.getSpeed(vector3d)));
                                int l = (int)Math.ceil(Math.sqrt(d9 * d9 + d8 * d8));
                                float f1 = 57.32484F * (float)Math.atan2(d7, -d6);
                                int i1 = (int)(Math.floor((int)f1) - 90D);
                                if(i1 < 0)
                                    i1 = 360 + i1;
                                int j1 = i1 - k;
                                if(j1 < 0)
                                    j1 = 360 + j1;
                                boolean flag1 = false;
                                if(j1 >= 150 && j1 <= 210)
                                    flag1 = true;
                                float f2 = World.getTimeofDay();
//                                boolean flag2 = false;
                                if(f2 >= 0.0F && f2 <= 5F || f2 >= 21F && f2 <= 24F)
                                {
                                    Pilot pilot = (Pilot)((Aircraft)actor1).FM;
                                    pilot.AS.setNavLightsState(true);
                                }
                                if(!refuelplayer && actor != World.getPlayerAircraft() && (float)l <= f && d10 <= 100D && d11 <= 30D && ((Aircraft)actor).FM.M.fuel < ((Aircraft)actor).FM.M.maxFuel - 100F)
                                {
                                    ((Aircraft)actor).FM.M.fuel += 20F;
                                    if(((Aircraft)actor).FM.M.fuel > ((Aircraft)actor).FM.M.maxFuel)
                                        ((Aircraft)actor).FM.M.fuel = ((Aircraft)actor).FM.M.maxFuel;
                                    flag = true;
                                }
                                if(refuelplayer && actor == World.getPlayerAircraft() && flag1 && l <= 100 && d2 - d5 < 50D && d11 <= 10D && ((Aircraft)actor).FM.M.fuel < ((Aircraft)actor).FM.M.maxFuel - 100F)
                                {
                                    ((Aircraft)actor).FM.M.fuel += 20F;
                                    if(((Aircraft)actor).FM.M.fuel > ((Aircraft)actor).FM.M.maxFuel)
                                        ((Aircraft)actor).FM.M.fuel = ((Aircraft)actor).FM.M.maxFuel;
                                    flag = true;
                                }
                            }
                        }

                        f += 10F;
                    } while(!flag && f <= 300F);
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean refuelall;
        private boolean refuelplayer;

        public RefuelUnit()
        {
            refuelall = false;
            refuelplayer = false;
            Timer1 = Timer2 = delay = 1.0F;
        }
    }

    public static class RCGCIUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            actor = null;
            if(mainforce != null && !((Maneuver)((Aircraft)mainforce).FM).hasBombs())
            {
                actor = mainforce;
                mainforcefound = true;
                mainforcewrong = false;
            }
            if(mainforcewrong && ((Maneuver)((Aircraft)spoof).FM).hasBombs())
            {
                actor = spoof;
                mainforcewrong = true;
                mainforcefound = false;
            }
            if(mainforcewrong && !((Maneuver)((Aircraft)spoof).FM).hasBombs())
            {
                spoofed = true;
                mainforcewrong = false;
                mainforcefound = false;
                actor = null;
            }
            for(counter = 0; !mainforcefound && !mainforcewrong && actor == null && counter <= 100;)
                dosearchmainforce();

            if(Mission.cur() != null)
                maxrange = Mission.cur().sectFile().get("Mods", "RCGCIRange", 200) * 1000;
            if(actor != null && actor.pos.getAbsPoint().distance(point3d) < (double)maxrange && actor.getSpeed(vector3d) > 20D && actor.pos.getAbsPoint().z >= 150D)
            {
                double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 10000D;
//                double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 1000D;
//                double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 1000D;
                char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
                // new String();
                String s;
                if(d > 260D)
                    s = "" + c + c1;
                else
                    s = "" + c1;
                // new String();
                int i = (int)Math.ceil(d2);
                int j = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                if(j < 0)
                    j = 360 + j;
                if(spoofed)
                    HUD.logCenter("                                          Mainforce was a spoof! Abort!");
                else
                if(mainforcefound || mainforcewrong)
                    HUD.logCenter("                                          Confirmed Mainforce at " + s + "-" + i + ", heading " + j + "\260");
                else
                    HUD.logCenter("                                          Incoming Raid at " + s + "-" + i + ", heading " + j + "\260");
                spoofed = false;
            }
            return true;
        }

        private void dosearchmainforce()
        {
//            actor = NearestRadarTargets.GetNearestEnemyEcho(World.getPlayerAircraft(), maxrange, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
            actor = GetNearestEnemy(World.getPlayerAircraft(), maxrange, 9);
            // Random random = new Random();
//            boolean flag = false;
            counter++;
            if(actor != null && (actor instanceof TypeBomber) && ((Maneuver)((Aircraft)actor).FM).Skill == 2 && ((Maneuver)((Aircraft)actor).FM).hasBombs())
            {
                int i = World.Rnd().nextInt(10);
                if(i == 1)
                {
                    mainforce = actor;
                    mainforcefound = true;
                }
            } else
            if(actor != null && (actor instanceof TypeBomber) && ((Maneuver)((Aircraft)actor).FM).Skill == 3 && ((Maneuver)((Aircraft)actor).FM).hasBombs())
            {
                int j = World.Rnd().nextInt(10);
                if(j == 1)
                {
                    spoof = actor;
                    mainforcewrong = true;
                }
            } else
            {
                actor = null;
            }
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean mainforcefound;
        private boolean mainforcewrong;
        private boolean spoofed;
        private int maxrange;
        private static Actor mainforce = null;
        private static Actor spoof = null;
        private static Actor actor = null;
        private int counter;


        public RCGCIUnit()
        {
            mainforcefound = false;
            mainforcewrong = false;
            spoofed = false;
            maxrange = 200000;
            counter = 0;
            Timer1 = Timer2 = 30F;
        }
    }

    public static class RandomWeatherUnit extends CandCGeneric
    {

        public boolean danger()
        {
            // Random random = new Random();
            int i = World.Rnd().nextInt(5);
            int j = World.Rnd().nextInt(1500) + 500;
            Mission.createClouds(i, j);
//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        public RandomWeatherUnit()
        {
            Timer1 = Timer2 = delay = 0.0F;
        }
    }

    public static class RandomVehiclesUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && actor.getArmy() == myArmy)
                {
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    if(i <= 1000)
                    {
                        // Random random = new Random();
                        int j = World.Rnd().nextInt(100);
                        if(j >= 50)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomVehiclesUnit()
        {
            Timer1 = Timer2 = delay = 0.0F;
        }
    }

    public static class RandomVehicles2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if(!sprung && counter < 1)
                randomInt = World.Rnd().nextInt(100);
            if(randomInt > 50)
                sprung = true;
            if(sprung)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if(((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && actor.getArmy() == myArmy)
                    {
                        double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                        if(i <= 1000)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }

            }
            counter++;
            if(counter > 60)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int counter;
        private int randomInt;

        public RandomVehicles2Unit()
        {
            sprung = false;
            counter = 0;
            randomInt = 0;
            Timer1 = Timer2 = delay = 0.0F;
        }
    }

    public static class RandomTrainsUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if(!sprung && counter < 1)
                randomInt = World.Rnd().nextInt(100);
            if(randomInt > 50)
                sprung = true;
            if(sprung)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if((actor instanceof Wagon) && actor.getArmy() == myArmy)
                    {
                        double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                        if(i <= 1000)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }

            }
            counter++;
            if(counter > 60)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int counter;
        private int randomInt;

        public RandomTrainsUnit()
        {
            sprung = false;
            counter = 0;
            randomInt = 0;
            Timer1 = Timer2 = delay = 0.0F;
        }
    }

    public static class RandomTimeUnit extends CandCGeneric
    {

        public boolean danger()
        {
            float f = World.getTimeofDay();
            // Random random = new Random();
            int i = World.Rnd().nextInt(6) - 3;
            f += i;
            if((double)f > 24D)
                f = (float)((double)f - 24D);
            if(f < 0.0F)
                f = (float)(24D + (double)f);
            World.setTimeofDay(f);
            if(Config.isUSE_RENDER())
                World.land().cubeFullUpdate();
            if(Mission.cur() != null)
                Mission.cur().replicateTimeofDay();
//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        public RandomTimeUnit()
        {
            Timer1 = Timer2 = delay = 0.0F;
        }
    }

    public static class RandomSkillUnit extends CandCGeneric
    {

        public boolean danger()
        {
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeFighter) && actor.getArmy() == myArmy && ((Maneuver)((Aircraft)actor).FM).Skill == 1)
                {
                    byte byte0 = 1;
                    // Random random = new Random();
                    int i = World.Rnd().nextInt(100);
                    if(i <= 20)
                        byte0 = 0;
                    if(i > 20 && i <= 30)
                        byte0 = 2;
                    if(i >= 50 && i <= 55)
                        byte0 = 3;
                    ((Aircraft)actor).FM.setSkill(byte0);
                }
            }

            counter++;
            if(counter > 60)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            return true;
        }

        private int counter;

        public RandomSkillUnit()
        {
            counter = 0;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RandomShipsUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if(!sprung && counter < 1)
                randomInt = World.Rnd().nextInt(100);
            if(randomInt > 50)
                sprung = true;
            if(sprung)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if((actor instanceof BigshipGeneric) && actor.getArmy() == myArmy)
                    {
                        double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                        if(i <= 500)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }

            }
            counter++;
            if(counter > 60)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int counter;
        private int randomInt;

        public RandomShipsUnit()
        {
            sprung = false;
            counter = 0;
            randomInt = 0;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RandomShips2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof BigshipGeneric) && actor.getArmy() == myArmy)
                {
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    if(i <= 1000)
                    {
                        // Random random = new Random();
                        int j = World.Rnd().nextInt(2);
                        if(j != 0)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomShips2Unit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RandomFlakUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof ArtilleryGeneric) && actor.getArmy() == myArmy)
                {
//                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
//                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
//                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d4 = d2 - d;
//                    double d5 = d3 - d1;
//                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    // Random random = new Random();
                    int j = World.Rnd().nextInt(100);
                    if(j >= 50)
//                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RandomFlakUnit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RandomAircraftUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            // Random random = new Random();
            if(!sprung && counter < 1)
                randomInt = World.Rnd().nextInt(100);
            if(randomInt > 50)
                sprung = true;
            if(sprung)
            {
                for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
                {
                    Actor actor = (Actor)entry.getValue();
                    if((actor instanceof Aircraft) && actor.getArmy() == myArmy)
                    {
                        double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                        double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d4 = d2 - d;
                        double d5 = d3 - d1;
                        int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                        if(i <= 1000)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }

            }
            counter++;
            if(counter > 60)
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean sprung;
        private int counter;
        private int randomInt;
//        private float delay;
//        protected int engineSTimer;

        public RandomAircraftUnit()
        {
            sprung = false;
            counter = 0;
            randomInt = 0;
            delay = 0.0F;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RandomAircraft2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() == myArmy)
                {
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    if(i <= 1000)
                    {
                        // Random random = new Random();
                        int j = World.Rnd().nextInt(100);
                        if(j >= 50)
//                            ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                            actor.postDestroy();
                    }
                }
            }

//            destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
            this.postDestroy();
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

//        private float delay;
//        protected int engineSTimer;

        public RandomAircraft2Unit()
        {
            delay = 0.0F;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class RadioOperatorUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeFighter) && actor.getArmy() == myArmy && actor.getSpeed(vector3d) > 20D)
                {
                    if(actor == World.getPlayerAircraft())
                        flag = true;
                    pos.getAbs(point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    int i = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                    if(i < 0)
                        i = 360 + i;
//                    boolean flag1 = false;
                    Aircraft aircraft = War.GetNearestEnemyAircraft(actor, 650F, 9);
                    if (aircraft == null) continue; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
                    if((aircraft instanceof TypeFighter) && aircraft.getSpeed(vector3d) > 20D)
                    {
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int)Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float)Math.atan2(d8, -d7);
                        int j = (int)(Math.floor((int)f) - 90D);
                        if(j < 0)
                            j = 360 + j;
                        int k = j - i;
                        if(k < 0)
                            k = 360 + k;
                        boolean flag2 = false;
                        if(k >= 90 && k <= 270)
                            flag2 = true;
                        String s = "left";
                        if(k <= 180 && k >= 90)
                            s = "right";
                        double d9 = d - d3;
                        double d10 = d1 - d4;
                        // Random random = new Random();
                        int l = World.Rnd().nextInt(100);
                        int i1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                        if((double)i1 <= 650D && Math.sqrt(d6 * d6) <= 650D && flag2 && flag && l <= 50)
                            HUD.logCenter("                                          Nightfighter! Break " + s + "!");
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public RadioOperatorUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class OBSUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(actor == World.getPlayerAircraft() && actor.pos.getAbsPoint().distance(point3d) < 10000D)
                    flag = true;
            }

            Aircraft aircraft = World.getPlayerAircraft();
            double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            Aircraft aircraft1 = World.getPlayerAircraft();
            for(Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
            {
                Actor actor1 = (Actor)entry1.getValue();
                if(aircraft1.getArmy() == myArmy && Actor.isAlive(actor1) && (actor1 instanceof Aircraft) && actor1.getArmy() != myArmy && actor1.pos.getAbsPoint().distance(point3d) < 15000D)
                {
                    pos.getAbs(point3d);
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                    char c = (char)(int)(65D + Math.floor((d3 / 676D - Math.floor(d3 / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d3 / 26D - Math.floor(d3 / 26D)) * 26D));
                    // new String();
                    String s;
                    if(d2 > 260D)
                        s = "" + c + c1;
                    else
                        s = "" + c1;
                    // new String();
                    double d5 = (double)(int)(Math.floor(actor1.pos.getAbsPoint().z) / 100D) * 100D;
//                    double d6 = (int)(Math.floor((actor1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    int i = (int)Math.ceil(d4);
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    double d9 = Math.floor((int)f) - 90D;
                    if(d9 < 0.0D)
                        d9 = 360D + d9;
                    String s1 = "aircraft";
                    if(actor1 instanceof TypeBomber)
                        s1 = "bombers";
                    else
                    if(actor1 instanceof TypeFighter)
                        s1 = "fighters";
                    if(!flag && Mission.cur().sectFile().get("Main", "CloudType", 2) <= 3)
                        HUD.logCenter("                                                                     Enemy " + s1 + " at map grid " + s + "-" + i + ", altitude " + d5 + "m");
                    if(!flag && Mission.cur().sectFile().get("Main", "CloudType", 2) > 3)
                        HUD.logCenter("                                                                     Enemy aircraft at map grid " + s + "-" + i);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public OBSUnit()
        {
            Timer1 = Timer2 = 30F;
        }
    }

    public static class OBOEUnit extends CandCGeneric
    {

        public boolean danger()
        {
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            int i = Mission.cur().sectFile().get("Mods", "OBOERange", 500) * 1000;
            if(aircraft.pos.getAbsPoint().distance(point3d) > (double)i || !active)
                return true;
            boolean flag = ((Maneuver)((Aircraft)aircraft).FM).hasBombs();
            if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 500D && flag)
            {
                double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                double d2 = World.land().HQ(point3d.x, point3d.y);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                double d6 = aircraft.getSpeed(vector3d);
                double d7 = ((Maneuver)((Aircraft)aircraft).FM).getAltitude();
                double d8 = d - d3;
                double d9 = d1 - d4;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                double d10 = Math.floor((int)f) - 90D;
                if(d10 < 0.0D)
                    d10 = 360D + d10;
                double d11 = (double)(-aircraft.pos.getAbsOrient().getYaw()) + 90D;
                if(d11 < 0.0D)
                    d11 += 360D;
                double d12 = d3 - d;
                double d13 = d4 - d1;
                double d14 = Math.sqrt(d13 * d13 + d12 * d12);
                double d15 = d7 - World.land().HQ(point3d.x, point3d.y);
                if(d15 < 0.0D)
                    d15 = 0.0D;
                double d16 = d10 - d11;
                String s = "<<<";
                if(d16 < 0.0D)
                    s = "<";
                if(d16 < -30D)
                    s = "<<";
                if(d16 < -60D)
                    s = "<<<";
                if(d16 > 0.0D)
                    s = ">";
                if(d16 > 30D)
                    s = ">>";
                if(d16 > 60D)
                    s = ">>>";
                double d17 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                int j = (int)((d14 - d17) / d6 / 60D);
                if(j < 1)
                    resetTimer(1.0F);
                else
                    resetTimer(5.5F);
                String s1 = "|" + j + "|";
                if(j < 1)
                {
                    j = (int)((d14 - d17) / d6);
                    s1 = "[" + j + "]";
                }
                if(d16 <= 1.0D || d16 >= -1D)
                    HUD.logCenter("                                                                             " + s1);
                if(d16 >= 1.0D || d16 <= -1D)
                    HUD.logCenter("                                                                             " + s);
                if((d16 <= 1.0D || d16 >= -1D) && (d14 <= d17 || j == 0))
                    HUD.logCenter("                                                                             Drop!");
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();
        
        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

//        private boolean flag2;
//        public static Point3d point3d = new Point3d();
        public boolean active;


        public OBOEUnit()
        {
//            flag2 = false;
            active = true;
            Timer1 = Timer2 = delay = 5.5F;
        }
    }

    public static class NaxosUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeBomber) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
//                    String s = "level with us";
//                    if(d2 - d5 - 200D >= 0.0D)
//                        s = "below us";
//                    if((d2 - d5) + 200D < 0.0D)
//                        s = "above us";
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
//                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
//                    int i2 = l1 - j;
                    // Random random = new Random();
                    boolean flag1 = false;
                    if(World.Rnd().nextInt(100) < 10)
                        flag1 = true;
                    if(Mission.cur().sectFile().get("Mods", "NaxosLate", 0) != 1)
                        flag1 = true;
                    if((double)k1 <= 50000D && (double)k1 >= 100D && d6 < 0.0D && flag1)
                        HUD.logCenter("                                          Naxos: Target bearing " + i1 + "\260");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public NaxosUnit()
        {
            Timer1 = Timer2 = 15F;
        }
    }

    public static class NavUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 150D && aircraft.getArmy() == myArmy)
            {
                pos.getAbs(point3d);
                if(Mission.cur() != null && Mission.cur().sectFile().get("Mods", "NavError", 0) == 1)
                {
                    error++;
                    if(error > 50)
                        error = 50;
                }
                int i = error + Mission.cur().sectFile().get("Main", "CloudType", 2) * 2;
                int j = i;
                // Random random = new Random();
                int k = World.Rnd().nextInt(100);
                if(k > 50)
                    i -= i * 2;
                k = World.Rnd().nextInt(100);
                if(k > 50)
                    j -= j * 2;
                double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d1 = ((Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D + (double)i) / 10D;
                double d2 = ((Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D + (double)j) / 10D;
                char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
                // new String();
                String s;
                if(d > 260D)
                    s = "" + c + c1;
                else
                    s = "" + c1;
                // new String();
                int l = (int)Math.ceil(d2);
                HUD.logCenter("                                          Position Fix: " + s + "-" + l);
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int error;

        public NavUnit()
        {
            error = 0;
            Timer1 = Timer2 = 300F;
        }
    }

    public static class MonicaUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            Aircraft aircraft1 = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(aircraft1.getArmy() == myArmy && (actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    int j2 = (int)(Math.ceil(((double)k1 * 3.28084D) / 100D) * 100D);
                    String s = "ft";
                    if(j2 >= 5280)
                    {
                        j2 = (int)Math.floor(j2 / 5280);
                        s = "mi";
                    }
//                    int k2 = (int)Math.ceil((double)l * 0.62137119200000002D);
                    if((double)k1 <= 6400D && (double)k1 >= 308D && i2 >= 255 && i2 <= 285 && Math.sqrt(j1 * j1) >= 120D)
                        HUD.logCenter("                                                                   (" + j2 + s + ")");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public MonicaUnit()
        {
            Timer1 = Timer2 = 10F;
        }
    }

    public static class MADUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(aircraft.getArmy() == myArmy && ((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
//                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
//                    int i2 = l1 - j;
                    int j2 = (int)(Math.ceil((double)k1 * 3.28084D) / 100D) * 100;
                    String s = "MAD: Contact";
                    if(k1 <= 1000 && d2 <= 500D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " feet");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public MADUnit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class JammerUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            char c = '\u03E8';
//            Vector3d vector3d = new Vector3d();
            int i = 1000;
//            Object obj = null;
            for(; i <= 0x30d40; i += 1000)
            {
                List list = Engine.targets();
                int j = list.size();
                for(int k = 0; k < j; k++)
                {
                    Actor actor = (Actor)list.get(k);
                    if(((actor instanceof B_17) || (actor instanceof B_24)) && Actor.isAlive(actor) && actor.getArmy() == myArmy && actor.pos.getAbsPoint().z >= 500D)
                    {
//                        Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(actor, 200000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
                        Aircraft aircraft = GetNearestEnemy(actor, 200000F, 9);
                        if(aircraft != null && (aircraft instanceof TypeFighter) && aircraft.getArmy() != myArmy)
                        {
                            Pilot pilot = (Pilot)aircraft.FM;
                            AirGroup airgroup = ((Maneuver)aircraft.FM).Group;
                            // Random random = new Random();
                            int l = World.Rnd().nextInt(100);
                            if(l < 10)
                            {
//                                if(airgroup.grTask == 3)
                                if(pilot.Group != null && airgroup.grTask == 3) // TODO: Fixed by SAS~Storebror, added pilot Group null check!
                                    pilot.Group.setGroupTask(1);
                                if(aircraft == World.getPlayerAircraft())
                                {
                                    int i1 = World.Rnd().nextInt(360);
                                    int j1 = (int)(actor.pos.getAbsPoint().z * 0.1D) * 10;
                                    int k1 = World.Rnd().nextInt(100);
                                    int l1 = World.Rnd().nextInt(360);
                                    if(Mission.cur().sectFile().get("Mods", "JammerMode", 0) == 1)
                                    {
                                        int i2 = World.Rnd().nextInt(50) + 30;
                                        int j2 = i2;
                                        int k2 = World.Rnd().nextInt(100);
                                        if(k2 > 50)
                                            i2 -= i2 * 2;
                                        k2 = World.Rnd().nextInt(100);
                                        if(k2 > 50)
                                            j2 -= j2 * 2;
                                        double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                                        double d1 = ((Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x) / 1000D + (double)i2) / 10D;
                                        double d2 = ((Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y) / 1000D + (double)j2) / 10D;
                                        char c1 = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
                                        char c2 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
                                        // new String();
                                        String s;
                                        if(d > 260D)
                                            s = "" + c1 + c2;
                                        else
                                            s = "" + c2;
                                        // new String();
                                        int l2 = (int)Math.ceil(d2);
                                        if(actor instanceof B_17)
                                            HUD.logCenter("                                          Incoming Raid at " + s + "-" + l2 + ", heading " + i1 + "\260");
                                        else
                                        if(actor instanceof B_24)
                                            HUD.logCenter("                                          Ground signals are being jammed!");
                                    } else
                                    if(actor instanceof B_17)
                                        HUD.logCenter("                                          Target bearing " + l1 + "\260" + ", range " + k1 + " km, height " + j1 + "m, heading " + i1 + "\260");
                                    else
                                    if(actor instanceof B_24)
                                        HUD.logCenter("                                          Ground signals are being jammed!");
                                }
                            }
                        }
                    }
                }

            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public JammerUnit()
        {
        }
    }

    public static class H2SUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(!active)
                return true;
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            boolean flag = ((Maneuver)((Aircraft)aircraft).FM).hasBombs();
            if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 500D && flag && aircraft.getArmy() == myArmy)
            {
                double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                double d2 = World.land().HQ(point3d.x, point3d.y);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
//                double d6 = aircraft.getSpeed(vector3d);
//                double d7 = ((Maneuver)((Aircraft)aircraft).FM).getAltitude();
                double d8 = d - d3;
                double d9 = d1 - d4;
                float f = 57.32484F * (float)Math.atan2(d9, -d8);
                int i = (int)(Math.floor((int)f) - 90D);
                if(i < 0)
                    i = 360 + i;
                int j = (int)((double)(-aircraft.pos.getAbsOrient().getYaw()) + 90D);
                if(j < 0)
                    j += 360;
                double d10 = d3 - d;
                double d11 = d4 - d1;
                double d12 = Math.sqrt(d11 * d11 + d10 * d10);
                // Random random = new Random();
                int k = World.Rnd().nextInt(100);
                Engine.land();
                int l = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(aircraft.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(aircraft.pos.getAbsPoint().y));
                if(d12 <= 5000D)
                    HUD.logCenter("                                                                                 H2S: Over Target Area");
                else
                if(aircraft.pos.getAbsPoint().distance(point3d) < 25000D || k < 10 || l >= 0 && l < 4 || l >= 16 && l < 29)
                    HUD.logCenter("                                                                                 H2S: Turn to heading " + i + " \260");
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

//        public static Point3d point3d = new Point3d();
        public boolean active;


        public H2SUnit()
        {
            active = true;
            Timer1 = Timer2 = 30F;
        }
    }

    public static class GTWUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
//                    String s = "level with us";
//                    if(d2 - d6 - 300D >= 0.0D)
//                        s = "below us";
//                    if((d2 - d6) + 300D <= 0.0D)
//                        s = "above us";
//                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
//                        s = "slightly below";
//                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
//                        s = "slightly above";
//                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 4000F;
                    float f4 = f3;
                    if(d3 < (double)(1.25F * f3) && !flag1)
                        f4 = (float)d3 * 0.8F;
                    if(d3 < (double)(1.25F * f3) && flag1)
                        if(d3 <= (double)(1.25F * f3 * 0.5F))
                            f4 = (float)(d3 * 0.8D * 2D);
                        else
                            f4 = f3;
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil((double)i2 / 100D) * 100D);
                    int i3 = (k1 + l1) - 180;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                    int j3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    if((double)i2 <= (double)j3 && (double)i2 > 400D && k2 >= -45 && k2 <= 30 && Math.sqrt(i3 * i3) <= 30D)
                    {
                        HUD.logCenter("                                                                   (" + l2 + "m" + ")");
                        freq = 6F;
                    } else
                    {
                        freq = 10F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public GTWUnit()
        {
            freq = 10F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class GNRUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            Pilot pilot = (Pilot)((Aircraft)aircraft).FM;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((pilot.Wingman != null || pilot.crew > 1) && (actor instanceof Aircraft) && actor != World.getPlayerAircraft() && actor.getArmy() != myArmy && aircraft.getArmy() == myArmy && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    double d6 = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    double d7 = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d8 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    String s = "";
                    if(d2 - d5 - 500D >= 0.0D)
                        s = " low";
                    if((d2 - d5) + 500D < 0.0D)
                        s = " high";
                    // new String();
                    double d9 = d3 - d;
                    double d10 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d10, -d9);
                    int j = (int)(Math.floor((int)f) - 90D);
                    if(j < 0)
                        j = 360 + j;
                    int k = j - i;
                    if(k < 0)
                        k = 360 + k;
                    int l = (int)(Math.ceil((double)(k + 15) / 30D) - 1.0D);
                    if(l < 1)
                        l = 12;
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.ceil(Math.sqrt(d12 * d12 + d11 * d11) / 10D) * 10D;
                    String s1 = "Aircraft ";
                    if(actor instanceof TypeFighter)
                        s1 = "Fighters ";
                    if(actor instanceof TypeBomber)
                        s1 = "Bombers ";
                    float f1 = World.getTimeofDay();
                    boolean flag = false;
                    if(f1 >= 0.0F && f1 <= 5F || f1 >= 21F && f1 <= 24F)
                        flag = true;
                    // Random random = new Random();
                    int i1 = World.Rnd().nextInt(100);
                    if(!flag && d13 <= 6000D && d13 >= 500D && Math.sqrt(d8 * d8) <= 2000D)
                        HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
                    if(flag && i1 <= 50 && d13 <= 1000D && d13 >= 100D && Math.sqrt(d8 * d8) <= 500D)
                        HUD.logCenter("                                          " + s1 + "at " + l + " o'clock" + s + "!");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public GNRUnit()
        {
            Timer1 = Timer2 = 30F;
        }
    }

    public static class GCIUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(Mission.cur() != null)
                maxrange = Mission.cur().sectFile().get("Mods", "GCIRange", 100);
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            float f = 1000F;
            // Random random = new Random();
            int i = World.Rnd().nextInt(140);
            int j = i + 10;
            Aircraft aircraft1 = World.getPlayerAircraft();
            if (aircraft1 == null) return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            
            // Get rid of endless recursion through all game's target list (a few hundred times)
            // New method introduced to get the same result with on single target list search!
            Aircraft aircraft = GetNearestEnemyInSteps(aircraft1, 1000F, maxrange, 1000F, 9);

////            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//            Aircraft aircraft = GetNearestEnemy(aircraft1, 1000F, 9);
//            for(int k = 0; aircraft == null && k < maxrange; k++)
//            {
////                aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F + f, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//                aircraft = GetNearestEnemy(aircraft1, 1000F + f, 9);
//                f += 1000F;
//            }
            if (aircraft == null) return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!

//            boolean flag1 = false;
            maxrange *= 1000;
            if(Mission.cur() != null && Mission.cur().sectFile().get("Mods", "GCIVarAlt", 0) == 1)
            {
                maxheight1 = (aircraft.pos.getAbsPoint().distance(point3d) / (double)maxrange) * 8000D;
                if(maxheight1 < (double)j)
                    maxheight1 = j;
                maxheight2 = (aircraft1.pos.getAbsPoint().distance(point3d) / (double)maxrange) * 8000D;
                if(maxheight2 < (double)j)
                    maxheight2 = j;
            }
            if(aircraft1.pos.getAbsPoint().distance(point3d) < (double)maxrange && aircraft1.pos.getAbsPoint().z >= maxheight2)
                flag = true;
            if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().distance(point3d) < (double)maxrange && aircraft.pos.getAbsPoint().z >= maxheight1 && aircraft != World.getPlayerAircraft() && aircraft.getArmy() != myArmy && aircraft1.getArmy() == myArmy)
            {
                double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 10000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 1000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 1000D;
                double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d5 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                double d6 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                double d7 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D;
                double d8 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D;
                char c = (char)(int)(65D + Math.floor((d5 / 676D - Math.floor(d5 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d5 / 26D - Math.floor(d5 / 26D)) * 26D));
                // new String();
                String s;
                if(d4 > 260D)
                    s = "" + c + c1;
                else
                    s = "" + c1;
                // new String();
                int l = (int)(Math.floor(aircraft.pos.getAbsPoint().z * 0.1D) * 10D);
//                int i1 = (int)(Math.floor((aircraft.getSpeed(vector3d) * 0.62137119200000002D * 60D * 60D) / 10000D) * 10D);
                // new String();
                int j1 = (int)Math.ceil(d6);
                double d9 = d5 - d;
                double d10 = d6 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d10, -d9);
                double d11 = Math.floor((int)f1) - 90D;
                if(d11 < 0.0D)
                    d11 = 360D + d11;
                int k1 = (int)d11;
                double d12 = d2 - d7;
                double d13 = d3 - d8;
                int l1 = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
                if(l1 < 0)
                    l1 = 360 + l1;
                int i2 = (int)Math.ceil(Math.sqrt(d13 * d13 + d12 * d12));
                if(flag)
                {
                    if(i2 > 4)
                        HUD.logCenter("                                          Target bearing " + k1 + "\260" + ", range " + i2 + "km, height " + l + "m, heading " + l1 + "\260");
                    else
                        HUD.logCenter(" ");
                } else
                {
                    HUD.logCenter("                                                                             Target at " + s + "-" + j1 + ", height " + l + "m, heading " + l1 + "\260");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int maxrange;
        private double maxheight1;
        private double maxheight2;

        public GCIUnit()
        {
            maxrange = 0x186a0;
            maxheight1 = 150D;
            maxheight2 = 150D;
        }
    }

    public static class FD2Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
//                    boolean flag1 = false;
                    Engine.land();
//                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
//                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
//                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
//                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 3000F;
                    float f4 = f3;
                    if(d3 < 1250D)
                        f4 = (float)(d3 * 0.8D * 3D);
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)f3;
                    if((float)i2 < f3)
                        if(i2 > 1200)
                            l2 = (int)(Math.ceil((double)i2 / 500D) * 500D);
                        else
                            l2 = (int)(Math.ceil((double)i2 / 200D) * 200D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if(j3 < 0)
                        j3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 5.15D)) * ((double)f4 * 0.25D));
                    int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    String s2 = "  ";
                    if(j3 < 3) // TODO: Fixed by SAS~Storebror: Useless double conversion removed, e.g. this was "(double)j3 < 2.5D" before
                        s1 = "Dead ahead, ";
                    if(j3 > 2 && j3 < 8) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 5, ";
                    if(j3 > 7 && j3 < 13) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 10, ";
                    if(j3 > 12 && j3 < 18) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 15, ";
                    if(j3 > 17 && j3 < 23) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Right 20, ";
                    if(j3 > 22 && j3 <= 30) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        s1 = "Right 25, ";
                        s2 = ", evading";
                    }
                    if(j3 > 357) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Dead ahead, ";
                    if(j3 < 358 && j3 > 352) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 5, ";
                    if(j3 < 353 && j3 > 347) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 10, ";
                    if(j3 < 348 && j3 > 342) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 15, ";
                    if(j3 < 343 && j3 > 332) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s1 = "Left 20, ";
                    if(j3 < 333 && j3 >= 330) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        s1 = "Left 25, ";
                        s2 = ", evading";
                    }
                    String s3 = "  ";
                    if(k2 < -10)
                        s3 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s3 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s3 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s3 = "up a bit";
                    if(k2 > 10)
                        s3 = "pull up";
                    String s4 = "  ";
                    if(j3 < 3) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "dead ahead, ";
                    if(j3 > 2 && j3 <8) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "right by 5\260, ";
                    if(j3 > 7 && j3 <13) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "right by 10\260, ";
                    if(j3 > 12 && j3 <18) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "right by 15\260, ";
                    if(j3 > 17 && j3 <23) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "right by 20\260, ";
                    if(j3 > 22 && j3 <= 30) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "right by 25\260, ";
                    if(j3 > 357) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "dead ahead, ";
                    if(j3 < 358 && j3 > 352) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "left by 5\260, ";
                    if(j3 < 353 && j3 > 347) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "left by 10\260, ";
                    if(j3 < 348 && j3 > 342) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "left by 15\260, ";
                    if(j3 < 343 && j3 > 332) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "left by 20\260, ";
                    if(j3 < 333 && j3 >= 330) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                        s4 = "left by 25\260, ";
                    if(i2 <= k3 && i2 > 1500 && k2 >= -30 && k2 <= 30 && Math.abs(i3) <= 30) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + "m" + s2);
                        float f7 = 7F;
                        engineSTimer = -(int)CandCGeneric.SecsToTicks(CandCGeneric.Rnd(f7, f7));
                    } else
                    if(i2 <= k3 && i2 <= 1500 && i2 >= 500 && k2 >= -30 && k2 <= 30 && Math.abs(i3) <= 30) // TODO: Fixed by SAS~Storebror: Useless double conversion removed
                    {
                        HUD.logCenter("                                          RO: " + s1 + s3 + ", " + l2 + "m" + s2);
                        float f8 = 5F;
                        engineSTimer = -(int)CandCGeneric.SecsToTicks(CandCGeneric.Rnd(f8, f8));
                    } else
                    {
                        freq = 7F;
                        engineSTimer = -(int)CandCGeneric.SecsToTicks(CandCGeneric.Rnd(freq, freq));
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public FD2Unit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class FuGUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
//                    boolean flag1 = false;
                    Engine.land();
//                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
//                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
//                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
//                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 3500F;
                    float f4 = f3;
                    if(d3 < 1000D)
                        f4 = (float)(d3 * 0.8D * 3.5D);
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil((double)i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if(j3 < 0)
                        j3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 5.15D)) * ((double)f4 * 0.25D));
                    int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    String s2 = "  ";
                    if((double)j3 < 2.5D)
                        s1 = "Dead ahead, ";
                    if((double)j3 >= 2.5D && (double)j3 <= 7.5D)
                        s1 = "Right 5, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s1 = "Right 10, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                    {
                        s1 = "Right 15, ";
                        s2 = ", evading";
                    }
                    if((double)j3 > 17.5D && j3 <= 25)
                        s1 = "Right 20, ";
                    if(j3 > 25 && j3 <= 35)
                    {
                        s1 = "Right 30, ";
                        s2 = ", evading";
                    }
                    if((double)j3 > 357.5D)
                        s1 = "Dead ahead, ";
                    if((double)j3 <= 357.5D && (double)j3 >= 352.5D)
                        s1 = "Left 5, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s1 = "Left 10, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                    {
                        s1 = "Left 15, ";
                        s2 = ", evading";
                    }
                    if((double)j3 < 342.5D && j3 >= 335)
                        s1 = "Left 20, ";
                    if(j3 < 335 && j3 >= 325)
                    {
                        s1 = "Left 30, ";
                        s2 = ", evading";
                    }
                    String s3 = "  ";
                    if(k2 < -10)
                        s3 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s3 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s3 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s3 = "up a bit";
                    if(k2 > 10)
                        s3 = "pull up";
                    String s4 = "  ";
                    if((double)j3 < 2.5D)
                        s4 = "dead ahead, ";
                    if((double)j3 >= 2.5D && (double)j3 <= 7.5D)
                        s4 = "right by 5\260, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s4 = "right by 10\260, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                        s4 = "right by 15\260, ";
                    if((double)j3 > 17.5D && j3 <= 25)
                        s4 = "right by 20\260, ";
                    if(j3 > 25 && j3 <= 35)
                        s4 = "right by 30\260, ";
                    if((double)j3 > 357.5D)
                        s4 = "dead ahead, ";
                    if((double)j3 <= 357.5D && (double)j3 >= 352.5D)
                        s4 = "left by 5\260, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s4 = "left by 10\260, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                        s4 = "left by 15\260, ";
                    if((double)j3 < 342.5D && j3 >= 335)
                        s4 = "left by 20\260, ";
                    if(j3 < 335 && j3 >= 325)
                        s4 = "left by 30\260, ";
                    if((double)i2 <= (double)k3 && (double)i2 > 1500D && k2 >= -35 && k2 <= 35 && Math.sqrt(i3 * i3) <= 35D)
                    {
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + "m" + s2);
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)k3 && (double)i2 <= 1500D && (double)i2 >= 200D && k2 >= -35 && k2 <= 35 && Math.sqrt(i3 * i3) <= 35D)
                    {
                        HUD.logCenter("                                          RO: " + s1 + s3 + ", " + l2 + "m" + s2);
                        freq = 4F;
                    } else
                    {
                        freq = 6F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public FuGUnit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class FuG218Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 5000F;
                    float f4 = f3;
                    if(d3 < (double)(1.25F * f3) && !flag1)
                        f4 = (float)d3 * 0.8F;
                    if(d3 < (double)(1.25F * f3) && flag1)
                        if(d3 <= (double)(1.25F * f3 * 0.5F))
                            f4 = (float)(d3 * 0.8D * 2D);
                        else
                            f4 = f3;
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil((double)i2 / 100D) * 100D);
                    int i3 = k1 + l1;
                    int j3 = i3;
                    if(j3 < 0)
                        j3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * ((double)f4 * 0.25D));
                    int k3 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s1 = "  ";
                    if(j3 < 5)
                        s1 = "Dead ahead, ";
                    if(j3 >= 5 && (double)j3 <= 7.5D)
                        s1 = "Right 5, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s1 = "Right 10, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                        s1 = "Right 15, ";
                    if((double)j3 > 17.5D && j3 <= 25)
                        s1 = "Right 20, ";
                    if(j3 > 25 && j3 <= 35)
                        s1 = "Right 30, ";
                    if(j3 > 35 && j3 <= 45)
                        s1 = "Right 40, ";
                    if(j3 > 45 && j3 <= 60)
                        s1 = "Turn right, ";
                    if(j3 > 355)
                        s1 = "Dead ahead, ";
                    if(j3 <= 355 && (double)j3 >= 352.5D)
                        s1 = "Left 5, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s1 = "Left 10, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                        s1 = "Left 15, ";
                    if((double)j3 < 342.5D && j3 >= 335)
                        s1 = "Left 20, ";
                    if(j3 < 335 && j3 >= 325)
                        s1 = "Left 30, ";
                    if(j3 < 325 && j3 >= 315)
                        s1 = "Left 40, ";
                    if(j3 < 345 && j3 >= 300)
                        s1 = "Turn left, ";
                    String s2 = "  ";
                    if(k2 < -10)
                        s2 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s2 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s2 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s2 = "up a bit";
                    if(k2 > 10)
                        s2 = "pull up";
                    String s3 = "  ";
                    if(j3 < 5)
                        s3 = "dead ahead, ";
                    if(j3 >= 5 && (double)j3 <= 7.5D)
                        s3 = "right by 5\260, ";
                    if((double)j3 > 7.5D && (double)j3 <= 12.5D)
                        s3 = "right by 10\260, ";
                    if((double)j3 > 12.5D && (double)j3 <= 17.5D)
                        s3 = "right by 15\260, ";
                    if((double)j3 > 17.5D && j3 <= 25)
                        s3 = "right by 20\260, ";
                    if(j3 > 25 && j3 <= 35)
                        s3 = "right by 30\260, ";
                    if(j3 > 35 && j3 <= 45)
                        s3 = "right by 40\260, ";
                    if(j3 > 45 && j3 <= 60)
                        s3 = "off our right, ";
                    if(j3 > 355)
                        s3 = "dead ahead, ";
                    if(j3 <= 355 && (double)j3 >= 352.5D)
                        s3 = "left by 5\260, ";
                    if((double)j3 < 352.5D && (double)j3 >= 347.5D)
                        s3 = "left by 10\260, ";
                    if((double)j3 < 347.5D && (double)j3 >= 342.5D)
                        s3 = "left by 15\260, ";
                    if((double)j3 < 342.5D && j3 >= 335)
                        s3 = "left by 20\260, ";
                    if(j3 < 335 && j3 >= 325)
                        s3 = "left by 30\260, ";
                    if(j3 < 325 && j3 >= 315)
                        s3 = "left by 40\260, ";
                    if(j3 < 345 && j3 >= 300)
                        s3 = "off our left, ";
                    if((double)i2 <= (double)k3 && (double)i2 > 1500D && k2 >= -60 && k2 <= 60 && Math.sqrt(i3 * i3) <= 60D)
                    {
                        HUD.logCenter("                                          RO: Contact " + s3 + s + ", " + l2 + "m");
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)k3 && (double)i2 <= 1500D && (double)i2 >= 120D && k2 >= -60 && k2 <= 60 && Math.sqrt(i3 * i3) <= 60D)
                    {
                        HUD.logCenter("                                          RO: " + s1 + s2 + ", " + l2 + "m");
                        freq = 4F;
                    } else
                    {
                        freq = 6F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public FuG218Unit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class FlensburgUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof TypeFighter) || (actor instanceof TypeBomber)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    Aircraft aircraft = World.getPlayerAircraft();
                    double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    int i = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                    if(i < 0)
                        i = 360 + i;
                    int j = (int)(-((double)actor.pos.getAbsOrient().getPitch() - 90D));
                    if(j < 0)
                        j = 360 + j;
                    // new String();
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
//                    double d9 = d - d3;
//                    double d10 = d1 - d4;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    float f1 = 57.32484F * (float)Math.atan2(d8, -d7);
                    int k = (int)(Math.floor((int)f) - 90D);
                    if(k < 0)
                        k = 360 + k;
                    int l = (int)(Math.floor((int)f1) + 90D);
                    if(l < 0)
                        l = 360 + l;
                    int i1 = k - i;
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.sqrt(d6 * d6);
                    int j1 = (int)(Math.ceil(Math.sqrt(d12 * d12 + d11 * d11) / 10D) * 10D);
                    float f2 = 57.32484F * (float)Math.atan2(j1, d13);
                    int k1 = (int)(Math.floor((int)f2) - 90D);
                    if(k1 < 0)
                        k1 = 360 + k1;
                    int l1 = k1 - j;
                    int i2 = 0;
                    if(l1 >= 220 && l1 <= 320 && Math.sqrt(i1 * i1) >= 120D)
                        i2 = 0x186a0;
                    if(j1 <= i2)
                        HUD.logCenter("                                         Flensburg: Target bearing " + l + "\260");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public FlensburgUnit()
        {
            Timer1 = Timer2 = 15F;
        }
    }

    public static class FireUnit extends CandCGeneric
    {

        public boolean danger()
        {
            boolean flag = false;
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            List list = Engine.targets();
            World.MaxVisualDistance = 50000F;
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(!flag && actor.pos.getAbsPoint().distance(point3d) < 10000D && (actor instanceof TypeBomber) && actor.getArmy() != myArmy)
                {
                    // Random random = new Random();
                    int k = World.Rnd().nextInt(500);
                    int l = k - 250;
                    k = World.Rnd().nextInt(500);
                    int i1 = k - 250;
                    Eff3DActor.New(this, null, new Loc(l, i1, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/CityFire.eff", 600F);
                    flag = true;
                    int j1 = World.Rnd().nextInt(10);
                    wait = (float)(1.0D + (double)j1 * 0.1D);
                }
            }

            return true;
        }

//        private int counter;
        private float wait;

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public FireUnit()
        {
//            counter = 0;
            wait = 1.0F;
            Timer1 = Timer2 = wait;
        }
    }

    public static class ASVmkIIIUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil((double)k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof ShipGeneric)
                        byte0 = 50;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 80;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 15;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 30;
                    if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 60D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVmkIIIUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class FACUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            boolean flag1 = false;
            int i = Mission.cur().sectFile().get("Mods", "FACDelay", 0) * 60;
            if(Time.current() > (long)i)
                active = true;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(actor.getArmy() == myArmy && (actor instanceof Aircraft) && actor.pos.getAbsPoint().distance(point3d) < 30000D)
                    flag = true;
            }

            for(Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
            {
                Actor actor1 = (Actor)entry1.getValue();
                if(active && Actor.isAlive(actor1) && ((actor1 instanceof TankGeneric) || (actor1 instanceof ArtilleryGeneric) || (actor1 instanceof CarGeneric)) && actor1.getArmy() != myArmy && actor1.pos.getAbsPoint().distance(point3d) < 2000D)
                {
                    pos.getAbs(point3d);
                    double d = (Main3D.cur3D().land2D.worldOfsX() + point3d.x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + point3d.y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = actor1.pos.getAbsPoint().distance(point3d);
//                    double d4 = point3d.z;
                    double d5 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d6 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
//                    double d7 = actor1.pos.getAbsPoint().z;
                    char c = (char)(int)(65D + Math.floor((d / 676D - Math.floor(d / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d / 26D - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if(d2 > 260D)
                        s1 = "" + c + c1;
                    else
                        s1 = "" + c1;
                    double d8 = d5 - d;
                    double d9 = d6 - d1;
                    float f = 57.32484F * (float)Math.atan2(d9, d8);
                    int j = (int)f;
                    j = (j + 180) % 360;
                    // new String();
                    String s2 = "west";
                    if((double)j <= 315D && (double)j >= 225D)
                        s2 = "north";
                    if((double)j <= 135D && (double)j >= 45D)
                        s2 = "south";
                    if((double)j <= 44D && (double)j >= 316D)
                        s2 = "west";
                    if((double)j <= 224D && (double)j >= 136D)
                        s2 = "east";
                    // new String();
                    String s3 = "units";
                    if(actor1 instanceof TgtTank)
                        s3 = "armor";
                    if(actor1 instanceof ArtilleryGeneric)
                        s3 = "units";
                    if(actor1 instanceof TgtVehicle)
                        s3 = "vehicle";
                    if((actor1 instanceof ArtilleryCY6.ProneInfantry) || (actor1 instanceof ArtilleryCY6.DugInInfantry))
                        s3 = "troops";
                    if(actor1 instanceof AAA)
                        flag1 = true;
                    String s4 = "";
                    if(flag1)
                        s4 = ", watch out for AA";
//                    boolean flag3 = false;
                    String s5 = "";
                    Engine.land();
                    int k = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor1.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor1.pos.getAbsPoint().y));
                    if(k >= 28 && k < 32)
                        s5 = ", near the water";
                    if(k >= 16 && k < 20)
                        s5 = ", in the town";
                    if(k >= 24 && k < 29)
                        s5 = ", near the treeline";
                    if(k >= 32 && k < 64 || k >= 128)
                        s5 = ", near the road";
                    if(k >= 64 && k < 128)
                        s5 = ", near the tracks";
                    String s6 = "";
                    Actor actor3 = null;
                    actor3 = War.GetNearestEnemy(actor1, actor1.getArmy(), 300F);
                    if(actor3 != null)
                        s6 = ", friendlies close";
                    int l = (int)(Math.ceil(d3 / 10D) * 10D);
                    int i1 = (int)Math.ceil(d1);
                    float f1 = World.getTimeofDay();
                    boolean flag4 = false;
                    if(f1 >= 0.0F && f1 <= 5F || f1 >= 21F && f1 <= 24F)
                        flag4 = true;
                    if(flag)
                    {
                        if(!flag2)
                        {
                            if(!flag4)
                            {
                                HUD.logCenter("                                                                             Good to see you! Popping Smoke!");
                                Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/Marksmoke.eff", -1F);
                            } else
                            {
                                HUD.logCenter("                                                                             Popping Flare!");
                                Eff3DActor.New(this, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", -1F);
                            }
                            flag2 = true;
                        } else
                        {
                            HUD.logCenter("                                                                             Enemy " + s3 + " " + l + " yards " + s2 + " of my mark" + s5 + s6 + s4 + "!");
                        }
                        Eff3DActor.New(actor1, null, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 90F, 0.0F), 1.0F, "Effects/Smokes/WPSmoke.eff", 60F);
                        popped = true;
                        List list = Engine.targets();
                        int j1 = list.size();
                        for(int k1 = 0; k1 < j1; k1++)
                        {
                            Actor actor4 = (Actor)list.get(k1);
                            Aircraft aircraft = (Aircraft)actor4;
                            if(((actor4 instanceof TypeStormovik) || (actor4 instanceof TypeFighter) || (actor4 instanceof TypeFastJet)) && !(actor4 instanceof TypeScout) && actor4.pos.getAbsPoint().distance(point3d) < 30000D && actor4.pos.getAbsPoint().distance(point3d) > 5000D && Mission.cur().sectFile().get("Mods", "FACNoGuide", 0) != 1)
                            {
                                AirGroup airgroup = ((Maneuver)((Aircraft)actor4).FM).Group;
                                Pilot pilot = (Pilot)((Aircraft)actor4).FM;
//                                if(pilot.get_task() != 7)
                                if(pilot.Group != null && pilot.get_task() != 7) // TODO: Fixed by SAS~Storebror, added pilot Group null check!
                                {
                                    pilot.Group.setGroupTask(4);
                                    pilot.Group.setGTargMode(0);
                                    pilot.Group.setFormationAndScale(airgroup.formationType, 3F, true);
                                    pilot.Group.setGTargMode(actor1.pos.getAbsPoint(), 200F);
                                    Voice.speakOk(aircraft);
                                }
                                
                                // TODO: Fixed by SAS~Storebror: WHAT THE FLYING FUCK???
                                //       Eating up CPU cycles for what?
//                                int l1;
//                                for(l1 = 0; l1 < 1000; l1++);
//                                if(l1 == 1000)
//                                    pilot.Group.setGroupTask(1);
                                if(pilot.Group != null) pilot.Group.setGroupTask(1);
                            }
                        }

                    } else
                    {
                        HUD.logCenter("                                                                             Request CAS at map grid " + s1 + "-" + i1);
                    }
                }
            }

            if(!flag && popped && !BDA)
            {
                for(Entry entry2 = Engine.name2Actor().nextEntry(null); entry2 != null; entry2 = Engine.name2Actor().nextEntry(entry2))
                {
                    Actor actor2 = (Actor)entry2.getValue();
                    if(!Actor.isAlive(actor2) && ((actor2 instanceof TankGeneric) || (actor2 instanceof ArtilleryGeneric) || (actor2 instanceof CarGeneric)) && actor2.getArmy() != myArmy && actor2.pos.getAbsPoint().distance(point3d) < 500D)
                    {
                        if((actor2 instanceof ArtilleryCY6.ProneInfantry) || (actor2 instanceof ArtilleryCY6.DugInInfantry))
                            troops += 5;
                        else
                        if(actor2 instanceof TgtTank)
                            armor++;
                        else
                        if((actor2 instanceof CarGeneric) || (actor2 instanceof TgtVehicle))
                            trucks++;
                        else
                        if(actor2 instanceof ArtilleryGeneric)
                            guns++;
                        if(armor > 0)
                        {
                            e1 = "" + armor + " tank ";
                            if(armor > 1)
                                e1 = "" + armor + "tanks ";
                        }
                        if(guns > 0)
                        {
                            e2 = "" + guns + " emplacement ";
                            if(guns > 1)
                                e1 = "" + guns + " emplacements ";
                        }
                        if(trucks > 0)
                        {
                            e3 = "" + trucks + " vehicle ";
                            if(trucks > 1)
                                e3 = "" + trucks + " vehicles ";
                        }
                        if(troops > 0)
                            e4 = "" + troops + " troops";
                        String s = e1 + e2 + e3 + e4 + " destroyed.";
                        if(troops + armor + trucks + guns <= 0)
                            s = " No targets hit.";
                        HUD.logCenter("                                                                             Thanks for the help! BDA: " + s);
                        BDA = true;
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean active;
        private boolean flag2;
//        private int spread;
        private int armor;
        private int trucks;
        private int guns;
        private int troops;
        private static String e1 = "";
        private static String e2 = "";
        private static String e3 = "";
        private static String e4 = "";
        private boolean popped;
        private boolean BDA;


        public FACUnit()
        {
            active = false;
            flag2 = false;
//            spread = 0;
            armor = 0;
            trucks = 0;
            guns = 0;
            troops = 0;
            popped = false;
            BDA = false;
            Timer1 = Timer2 = 90F;
        }
    }

    public static class DZUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(Actor.isAlive(actor) && actor != World.getPlayerAircraft() && (actor instanceof Aircraft) && actor.getArmy() == myArmy)
                {
                    pos.getAbs(point3d);
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
                    double d2 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d3 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d4 = d2 - d;
                    double d5 = d3 - d1;
                    int i = (int)Math.ceil(Math.sqrt(d5 * d5 + d4 * d4));
                    if(i <= 1000)
//                        ObjState.destroy(actor); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                        actor.postDestroy();
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public DZUnit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class DynamicWeatherUnit extends CandCGeneric
    {

        public boolean danger()
        {
            int i = Mission.cur().sectFile().get("Main", "DWInterval", 20) * 60;
            resetTimer(i);
            // Random random = new Random();
            int j = World.Rnd().nextInt(100);
            if(j <= 33)
                clouds++;
            if(j > 33 && j < 66)
                clouds--;
            if(clouds < bestclouds)
                clouds = bestclouds;
            if(clouds > worstclouds)
                clouds = worstclouds;
            j = World.Rnd().nextInt(100);
            if(j <= 33)
                height += 200;
            if(j > 33 && j < 66)
                height -= 200;
            if(height < 200)
                height = 200;
            if(height > 2000)
                height = 2000;
            if(Mission.cur() != null && start)
            {
                clouds = Mission.cur().sectFile().get("Main", "CloudType", 2);
                height = Mission.cur().sectFile().get("Main", "CloudHeight", 1500);
            }
            if(Mission.cur() != null && start)
            {
                bestclouds = Mission.cur().sectFile().get("Mods", "BestClouds", 0);
                if(bestclouds < 0 || bestclouds > 7)
                    bestclouds = 0;
            }
            if(Mission.cur() != null && start)
            {
                worstclouds = Mission.cur().sectFile().get("Mods", "WorstClouds", 7);
                if(worstclouds < 0 || worstclouds > 7)
                    worstclouds = 7;
            }
            if(worstclouds <= bestclouds)
            {
                worstclouds = 7;
                bestclouds = 0;
            }
            start = false;
            Mission.createClouds(clouds, height);
            World.land().cubeFullUpdate();
            return true;
        }

        private int clouds;
        private int height;
//        private int startclouds;
//        private int startheight;
        private int bestclouds;
        private int worstclouds;
        private boolean start;

        public DynamicWeatherUnit()
        {
            clouds = 2;
            height = 1500;
//            startclouds = 2;
//            startheight = 1500;
            bestclouds = 0;
            worstclouds = 7;
            start = true;
            Timer1 = Timer2 = 1200F;
        }
    }

    public static class CWUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(actor == World.getPlayerAircraft() && actor.pos.getAbsPoint().distance(point3d) < 10000D)
                    flag = true;
            }

            Aircraft aircraft = World.getPlayerAircraft();
            double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
            for(Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
            {
                Actor actor1 = (Actor)entry1.getValue();
                if(Actor.isAlive(actor1) && ((actor1 instanceof BigshipGeneric) || (actor1 instanceof ShipGeneric)) && actor1.getArmy() != myArmy && actor1.pos.getAbsPoint().distance(point3d) < 5000D)
                {
                    pos.getAbs(point3d);
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                    double d3 = (Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x) / 10000D;
                    double d4 = (Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y) / 10000D;
                    char c = (char)(int)(65D + Math.floor((d3 / 676D - Math.floor(d3 / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d3 / 26D - Math.floor(d3 / 26D)) * 26D));
                    // new String();
                    String s;
                    if(d2 > 260D)
                        s = "" + c + c1;
                    else
                        s = "" + c1;
                    // new String();
//                    double d5 = (double)(int)(Math.floor(actor1.pos.getAbsPoint().z) / 100D) * 100D;
//                    double d6 = (int)(Math.floor((actor1.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    // new String();
                    int i = (int)Math.ceil(d4);
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    double d9 = Math.floor((int)f) - 90D;
                    if(d9 < 0.0D)
                        d9 = 360D + d9;
                    if(!flag)
                        HUD.logCenter("                                                                     Enemy ship spotted at map grid " + s + "-" + i);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public CWUnit()
        {
            Timer1 = Timer2 = 30F;
        }
    }

    public static class HighReconUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if(footage == 10)
            {
                HUD.logCenter("                                          Run Complete!");
                World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            if(aircraft.pos.getAbsPoint().distance(point3d) <= 15000D && aircraft.getArmy() == myArmy && aircraft.pos.getAbsPoint().distance(point3d) <= 8000D)
            {
                int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
                if(i < 0)
                    i += 360;
                else
                if(i > 360)
                    i -= 360;
                double d = 57.32484D * Math.atan2(point3d.y - aircraft.pos.getAbsPoint().y, -(point3d.x - aircraft.pos.getAbsPoint().x));
                int j = (int)(Math.floor((int)d) - 90D);
                if(j < 0)
                    j += 360;
                else
                if(j > 360)
                    j -= 360;
                int k = j - i;
                if(k < 0)
                    k += 360;
                else
                if(k > 360)
                    k -= 360;
                int l = (int)aircraft.pos.getAbsOrient().getRoll();
                int i1 = (int)aircraft.pos.getAbsOrient().getPitch();
                VisibilityChecker.checkLandObstacle = true;
                VisibilityChecker.checkCabinObstacle = false;
                VisibilityChecker.checkPlaneObstacle = false;
                VisibilityChecker.checkObjObstacle = false;
                VisibilityChecker.targetPosInput = point3d;
                float f = Main3D.cur3D().clouds.getVisibility(aircraft.pos.getAbsPoint(), point3d);
                double d1 = Math.sqrt(Math.pow(point3d.y - aircraft.pos.getAbsPoint().y, 2D) + Math.pow(point3d.x - aircraft.pos.getAbsPoint().x, 2D));
                boolean flag = false;
                if((k <= 25 && k >= 0 || k >= 335 && k <= 360) && l > 350 && l < 370 && i1 > 350 && i1 < 370 && aircraft.pos.getAbsPoint().z - point3d.z > 1000D)
                    flag = true;
                if(d1 < 2000D && (double)f < 1.0D)
                {
                    HUD.logCenter("                                          Target Obscured!");
                    footage = 0;
                } else
                if(flag && d1 < 2000D && (double)f >= 1.0D)
                {
                    rolling = true;
                    if(footage <= 10)
                    {
                        HUD.logCenter("                                          Rolling..." + footage * 10 + "%");
                        footage++;
                    }
                } else
                if(!flag && d1 < 2000D && footage <= 10 && rolling)
                {
                    footage = 0;
                    rolling = false;
                    HUD.logCenter("                                          Run Incomplete. Resetting.");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int footage;
        private boolean rolling;

        public HighReconUnit()
        {
            footage = 0;
            rolling = false;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class LowReconUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            if(footage == 10)
            {
                HUD.logCenter("                                          Run Complete!");
                World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                destroy(); // TODO: Fixed by SAS~Storebror, don't destroy objects in Interpolater tick() method!!!
                this.postDestroy();
            }
            if(aircraft.pos.getAbsPoint().distance(point3d) <= 5000D && aircraft.getArmy() == myArmy && aircraft.pos.getAbsPoint().distance(point3d) <= 2000D)
            {
                int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
                if(i < 0)
                    i += 360;
                else
                if(i > 360)
                    i -= 360;
                double d = 57.32484D * Math.atan2(point3d.y - aircraft.pos.getAbsPoint().y, -(point3d.x - aircraft.pos.getAbsPoint().x));
                int j = (int)(Math.floor((int)d) - 90D);
                if(j < 0)
                    j += 360;
                else
                if(j > 360)
                    j -= 360;
                int k = j - i;
                if(k < 0)
                    k += 360;
                else
                if(k > 360)
                    k -= 360;
                int l = (int)aircraft.pos.getAbsOrient().getRoll();
                int i1 = (int)aircraft.pos.getAbsOrient().getPitch();
                if((k <= 25 && k >= 0 || k >= 335 && k <= 360) && l > 350 && l < 370 && i1 > 350 && i1 < 370 && aircraft.pos.getAbsPoint().z - point3d.z > 50D && aircraft.pos.getAbsPoint().z - point3d.z < 500D)
                {
                    rolling = true;
                    if(footage <= 10)
                    {
                        HUD.logCenter("                                          Rolling..." + footage * 10 + "%");
                        footage++;
                    }
                } else
                if(footage <= 10 && rolling)
                {
                    footage = 0;
                    rolling = false;
                    HUD.logCenter("                                          Run Incomplete. Resetting.");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int footage;
        private boolean rolling;

        public LowReconUnit()
        {
            footage = 0;
            rolling = false;
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class CorkscrewUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeBomber) && actor.getArmy() == myArmy && actor.getSpeed(vector3d) > 20D)
                {
                    Aircraft aircraft = War.GetNearestEnemyAircraft(actor, 500F, 9);
                    if(aircraft != null)
                    {
                        double d = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                        double d1 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                        double d2 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                        int i = (int)(-((double)actor.pos.getAbsOrient().getYaw() - 90D));
                        if(i < 0)
                            i = 360 + i;
                        double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                        double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
                        double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                        // new String();
                        double d6 = (int)Math.ceil(d2 - d5);
                        // new String();
                        double d7 = d3 - d;
                        double d8 = d4 - d1;
                        float f = 57.32484F * (float)Math.atan2(d8, -d7);
                        int j = (int)(Math.floor((int)f) - 90D);
                        if(j < 0)
                            j = 360 + j;
                        int k = j - i;
                        if(k < 0)
                            k = 360 + k;
                        boolean flag = false;
                        if(k >= 90 && k <= 270)
                            flag = true;
                        String s = "left";
                        if(k <= 180 && k >= 90)
                            s = "right";
//                        double d9 = d - d3;
//                        double d10 = d1 - d4;
                        // Random random = new Random();
                        int l = World.Rnd().nextInt(100);
                        if(actor.pos.getAbsPoint().distance(aircraft.pos.getAbsPoint()) <= 500D && l <= 10 && flag && d6 < 300D)
                            if(actor != World.getPlayerAircraft())
                            {
                                ((Pilot)((Aircraft)actor).FM).AP.setWayPoint(false);
                                ((Maneuver)((Aircraft)actor).FM).Group.setGroupTask(0);
                                ((Pilot)((Aircraft)actor).FM).set_task(0);
                                ((Pilot)((Aircraft)actor).FM).set_maneuver(54);
                            } else
                            {
                                HUD.logCenter("                                          Corkscrew " + s + ", now!");
                            }
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        public CorkscrewUnit()
        {
            Timer1 = Timer2 = 1.0F;
        }
    }

    public static class ASVmkVIIIUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil((double)k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof ShipGeneric)
                        byte0 = 50;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 80;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 15;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 30;
                    if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 60D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVmkVIIIUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class ASVUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric) && Actor.isAlive(actor))
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
                    // new String();
                    double d6 = d3 - d;
                    double d7 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d7, -d6);
                    int j = (int)(Math.floor((int)f) - 90D);
                    if(j < 0)
                        j = 360 + j;
                    int k = j - i;
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    String s = "Ship";
                    byte byte0 = 30;
                    if(actor instanceof BigshipGeneric)
                        s = "Warship";
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                    {
                        s = "Periscope";
                        byte0 = 5;
                    }
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                    {
                        s = "Surfaced sub";
                        byte0 = 15;
                    }
                    int l = (int)(Math.ceil(Math.sqrt(d9 * d9 + d8 * d8)) / 1000D);
                    if(l <= byte0 && d2 <= 3000D && Math.sqrt(k * k) <= 60D)
                        HUD.logCenter("                                              Contact: " + s + ", bearing " + j + "\260" + ", range " + l + "km");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASVUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class ASGUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil((double)k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof ShipGeneric)
                        byte0 = 50;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 64;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 10;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 29;
                    if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 60D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASGUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class ASDUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil((double)k1 * 0.621371192D);
                    String s = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof ShipGeneric)
                        byte0 = 30;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 40;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 3;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 15;
                    if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 60D)
                        HUD.logCenter("                                              " + s + " bearing " + i1 + "\260" + ", range " + j2 + " miles");
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ASDUnit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class ANAPS4Unit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if(((actor instanceof Aircraft) && actor.getSpeed(vector3d) > 20D || (actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft())
                {
                    pos.getAbs(point3d);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                    String s = ", level with us";
                    if(d2 - d5 - 200D >= 0.0D)
                        s = ", below us";
                    if((d2 - d5) + 200D < 0.0D)
                        s = ", above us";
                    // new String();
                    double d7 = d3 - d;
                    double d8 = d4 - d1;
                    float f = 57.32484F * (float)Math.atan2(d8, -d7);
                    int i1 = (int)(Math.floor((int)f) - 90D);
                    if(i1 < 0)
                        i1 = 360 + i1;
                    int j1 = i1 - i;
                    double d9 = d - d3;
                    double d10 = d1 - d4;
                    double d11 = Math.sqrt(d6 * d6);
                    int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                    float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                    int l1 = (int)(Math.floor((int)f1) - 90D);
                    if(l1 < 0)
                        l1 = 360 + l1;
                    int i2 = l1 - j;
                    k1 = (int)((double)k1 / 1000D);
                    int j2 = (int)Math.ceil((double)k1 * 0.621371192D);
                    String s1 = "Surface Contact";
                    byte byte0 = 9;
                    if(actor instanceof Aircraft)
                        s1 = "Aircraft";
                    if(!(actor instanceof Aircraft))
                        s = " ";
                    if(actor instanceof ShipGeneric)
                        byte0 = 40;
                    if(actor instanceof BigshipGeneric)
                        byte0 = 55;
                    if((actor instanceof SubTypeVIIC_Sub) || (actor instanceof USSGatoSS212_Sub) || (actor instanceof USSGreenlingSS213_Sub))
                        byte0 = 5;
                    if((actor instanceof SubTypeVIIC_Srf) || (actor instanceof USSGatoSS212_Srf) || (actor instanceof USSGreenlingSS213_Srf))
                        byte0 = 30;
                    if(k1 <= byte0 && i2 >= 255 && i2 <= 285 && Math.sqrt(j1 * j1) <= 35D)
                        HUD.logCenter("                                              " + s1 + " bearing " + i1 + "\260" + ", range " + j2 + " miles" + s);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        public ANAPS4Unit()
        {
            Timer1 = Timer2 = 5F;
        }
    }

    public static class ErrorUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Aircraft aircraft = World.getPlayerAircraft();
                pos.getAbs(point3d);
                if(aircraft.pos.getAbsPoint().distance(point3d) < 10000D)
                {
                    boolean flag = ((Maneuver)((Aircraft)aircraft).FM).hasBombs();
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                    double d2 = World.land().HQ(point3d.x, point3d.y);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
                    double d6 = aircraft.getSpeed(vector3d);
                    double d7 = ((Maneuver)((Aircraft)aircraft).FM).getAltitude();
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    float f = 57.32484F * (float)Math.atan2(d9, -d8);
                    double d10 = Math.floor((int)f) - 90D;
                    if(d10 < 0.0D)
                        d10 = 360D + d10;
                    double d11 = (double)(-aircraft.pos.getAbsOrient().getYaw()) + 90D;
                    if(d11 < 0.0D)
                        d11 += 360D;
                    double d12 = d3 - d;
                    double d13 = d4 - d1;
                    double d14 = Math.sqrt(d13 * d13 + d12 * d12);
                    double d15 = d7 - World.land().HQ(point3d.x, point3d.y);
                    if(d15 < 0.0D)
                        d15 = 0.0D;
                    double d16 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                    if(!marked && !flag)
                    {
                        error = (float)(d14 - d16);
                        marked = true;
                        HUD.logCenter("                                                                             Bombs Gone! Aimpoint Error: " + (int)error + "m");
                        if(error < 100F)
                            World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float error;
        private boolean marked;
//        private int randomInt;
//        private int maxerror;

        public ErrorUnit()
        {
            error = 0.0F;
            marked = false;
//            randomInt = 0;
//            maxerror = 300;
            Timer1 = Timer2 = 0.5F;
        }
    }

    public static class AimpointUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(!active)
                return false;
//            Point3d point3d = new Point3d();
//            Vector3d vector3d = new Vector3d();
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                pos.getAbs(point3d);
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof TypeBomber) || (actor instanceof TypeStormovik) && actor.getArmy() == myArmy && actor.pos.getAbsPoint().distance(point3d) < 10000D)
                {
                    boolean flag = ((Maneuver)((Aircraft)actor).FM).hasBombs();
                    double d = Main3D.cur3D().land2D.worldOfsX() + point3d.x;
                    double d1 = Main3D.cur3D().land2D.worldOfsY() + point3d.y;
//                    double d2 = World.land().HQ(point3d.x, point3d.y);
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
//                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    double d6 = actor.getSpeed(vector3d);
                    double d7 = ((Maneuver)((Aircraft)actor).FM).getAltitude();
                    double d8 = d - d3;
                    double d9 = d1 - d4;
                    float f = 57.32484F * (float)Math.atan2(d9, -d8);
                    double d10 = Math.floor((int)f) - 90D;
                    if(d10 < 0.0D)
                        d10 = 360D + d10;
                    double d11 = (double)(-actor.pos.getAbsOrient().getYaw()) + 90D;
                    if(d11 < 0.0D)
                        d11 += 360D;
                    double d12 = d3 - d;
                    double d13 = d4 - d1;
                    double d14 = Math.sqrt(d13 * d13 + d12 * d12);
                    double d15 = d7 - World.land().HQ(point3d.x, point3d.y);
                    if(d15 < 0.0D)
                        d15 = 0.0D;
                    double d16 = d6 * Math.sqrt(d15 * 0.20386999845504761D);
                    if(!marked && actor == World.getPlayerAircraft() && !flag)
                    {
                        error = (float)(d14 - d16);
                        marked = true;
                        HUD.logCenter("                                                                             Bombs Gone! Aimpoint Error: " + (int)error);
                    }
                    // Random random = new Random();
                    int i = Mission.cur().sectFile().get("Mods", "AimpointError", 300);
                    randomInt = World.Rnd().nextInt(i);
                    if(Mission.cur() != null && Mission.cur().sectFile().get("Mods", "AimpointNoMark", 0) == 1)
                        error = 0.0F;
                    if(Mission.cur() != null && Mission.cur().sectFile().get("Mods", "AimpointMode", 0) != 1)
                        d16 += (float)randomInt + error;
                    if(d14 <= d16)
                        ((Pilot)((Aircraft)actor).FM).bombsOut = true;
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float error;
        private boolean marked;
        private int randomInt;
//        private int maxerror;
        public boolean active;

        public AimpointUnit()
        {
            error = 0.0F;
            marked = false;
            randomInt = 0;
//            maxerror = 300;
            active = true;
            Timer1 = Timer2 = 1.0F;
            delay = 10F;
        }
    }

    public static class AImkXVUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(10) - 5F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    int i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D * (double)f2);
                    float f3 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil(((double)i2 * 3.2808399D) / 100D) * 100D);
                    int i3 = (int)(Math.ceil(((double)i2 * 3.2808399D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if(l2 >= 5280)
                    {
                        l2 = (int)Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if(l3 < 0)
                        l3 += 360;
                    int i4 = (int)((double)Math.round((double)j1 / 10D) * 10D);
                    if(Math.sqrt(k1 * k1) <= 20D)
                        i4 = (int)((double)Math.round((double)j1 / 5D) * 5D);
                    float f4 = 6000F;
                    float f5 = f4;
                    if(d3 < 1524D)
                    {
                        f5 = f4 * ((float)d3 / 6096F + 0.75F);
                        if(d6 < d2 && !flag1)
                            f5 = (float)d3 * 4F;
                    }
                    float f6 = (float)Math.toDegrees(Math.atan(d3 / (double)f4));
                    float f7 = 90F + f6;
                    int j4 = (int)((double)f5 * Math.cos(Math.toRadians(k2)));
                    float f8 = j4;
                    if((float)j > f7)
                        f8 = (float)j4 * (float)(Math.cos(Math.toRadians((float)(j - 90) * (f7 / 45F))) * 0.5D + 0.5D);
                    String s2 = "  ";
                    String s3 = "  ";
                    if((double)l3 <= 2.5D)
                        s2 = "Dead ahead, ";
                    if((double)l3 > 2.5D && (double)l3 <= 7.5D)
                        s2 = "Right 5, ";
                    if((double)l3 > 7.5D && (double)l3 <= 12.5D)
                        s2 = "Right 10, ";
                    if((double)l3 > 12.5D && (double)l3 <= 17.5D)
                        s2 = "Right 15, ";
                    if((double)l3 > 17.5D && l3 <= 25)
                        s2 = "Right 20, ";
                    if(l3 > 25 && l3 <= 35)
                        s2 = "Right 30, ";
                    if(l3 > 35 && l3 <= 45)
                        s2 = "Right 40, ";
                    if(l3 > 45 && l3 <= 55)
                        s2 = "Right 50, ";
                    if(l3 > 55 && l3 <= 65)
                        s2 = "Right 60, ";
                    if(l3 > 65 && l3 <= 75)
                    {
                        s2 = "Right 70, ";
                        s3 = ", evading";
                    }
                    if((double)l3 >= 357.5D)
                        s2 = "Dead ahead, ";
                    if((double)l3 < 357.5D && (double)l3 >= 352.5D)
                        s2 = "Left 5, ";
                    if((double)l3 < 352.5D && (double)l3 >= 347.5D)
                        s2 = "Left 10, ";
                    if((double)l3 < 347.5D && (double)l3 >= 342.5D)
                        s2 = "Left 15, ";
                    if((double)l3 < 342.5D && l3 >= 335)
                        s2 = "Left 20, ";
                    if(l3 < 335 && l3 >= 325)
                        s2 = "Left 30, ";
                    if(l3 < 325 && l3 >= 315)
                        s2 = "Left 40, ";
                    if(l3 < 315 && l3 >= 305)
                        s2 = "Left 50, ";
                    if(l3 < 305 && l3 >= 295)
                        s2 = "Left 60, ";
                    if(l3 < 295 && l3 >= 285)
                    {
                        s2 = "Left 70, ";
                        s3 = ", evading";
                    }
                    String s4 = "  ";
                    if(k2 >= -12 && (double)k2 < -10.5D)
                    {
                        s4 = "down 12, ";
                        s3 = ", evading";
                    }
                    if((double)k2 >= -10.5D && (double)k2 < -7.5D)
                    {
                        s4 = "down 9, ";
                        s3 = ", evading";
                    }
                    if((double)k2 >= -7.5D && (double)k2 < -4.5D)
                        s4 = "down 6, ";
                    if((double)k2 >= -4.5D && (double)k2 < -1.5D)
                        s4 = "down 3, ";
                    if((double)k2 >= -1.5D && (double)k2 <= 1.5D)
                        s4 = "level, ";
                    if(k2 <= 12 && (double)k2 > 10.5D)
                    {
                        s4 = "up 12, ";
                        s3 = ", evading";
                    }
                    if((double)k2 <= 10.5D && (double)k2 > 7.5D)
                    {
                        s4 = "up 9, ";
                        s3 = ", evading";
                    }
                    if((double)k2 <= 7.5D && (double)k2 > 4.5D)
                        s4 = "up 6, ";
                    if((double)k2 <= 4.5D && (double)k2 > 1.5D)
                        s4 = "up 3, ";
                    if((float)j > f7 && !flag1 || d3 < 1524D && d6 < d2 && !flag1)
                        s3 = ", ground returns";
                    if((float)(j - 40) > f7)
                        s3 = ", ground clutter";
                    if((double)i2 <= (double)f8 && j3 > 0 && k2 >= -12 && k2 <= 12 && Math.sqrt(k3 * k3) <= 75D)
                    {
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + l2 + s1 + s3);
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)f8 && j3 < 1 && (double)i2 >= 75D && k2 >= -12 && k2 <= 12 && Math.sqrt(k3 * k3) <= 75D)
                    {
                        HUD.logCenter("                                          RO: " + s2 + s4 + i3 + "ft" + s3);
                        freq = 3F;
                    } else
                    {
                        freq = 6F;
                        super.resetTimer(freq);
                    }
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public AImkXVUnit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class AImkXUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(10) - 5F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    int i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D * (double)f2);
                    float f3 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    float f4 = (float)(Math.ceil(((double)i2 * 3.2808399D) / 100D) * 100D);
                    int l2 = (int)(Math.ceil(((double)i2 * 3.2808399D) / 300D) * 300D);
                    int i3 = 0;
                    String s1 = "ft";
                    if(f4 >= 5280F)
                    {
                        f4 = (float)((double)Math.round((double)f4 / 5280D / 0.5D) * 0.5D);
                        s1 = "mi";
                        i3 = 1;
                    }
                    int j3 = k1 + l1;
                    int k3 = j3;
                    if(k3 < 0)
                        k3 += 360;
//                    int l3 = j2 - j;
                    int i4 = (int)((double)Math.round((double)j1 / 5D) * 5D);
                    float f5 = 10000F;
                    float f6 = f5;
                    if(d3 < 2130D)
                    {
                        f6 = f5 * ((float)d3 / 8520F + 0.75F);
                        if(d6 < d2 && !flag1)
                            f6 = (float)(d3 * 2D + Math.pow(d3, 1.12935D));
                    }
                    float f7 = (float)Math.toDegrees(Math.atan(d3 / (double)f5));
                    float f8 = 90F + f7;
                    int j4 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    float f9 = j4;
                    if((float)j > f8)
                        f9 = (float)j4 * (float)(Math.cos(Math.toRadians((float)(j - 90) * (f8 / 45F))) * 0.5D + 0.5D);
                    String s2 = "  ";
                    if((double)i >= 348.75D && (double)i < 11.25D)
                        s2 = ", heading north";
                    if((double)i >= 11.25D && (double)i < 33.75D)
                        s2 = ", heading NNE";
                    if((double)i >= 33.75D && (double)i < 56.25D)
                        s2 = ", heading NE";
                    if((double)i >= 56.25D && (double)i < 78.75D)
                        s2 = ", heading ENE";
                    if((double)i >= 78.75D && (double)i < 101.25D)
                        s2 = ", heading east";
                    if((double)i >= 101.25D && (double)i < 123.75D)
                        s2 = ", heading ESE";
                    if((double)i >= 123.75D && (double)i < 146.25D)
                        s2 = ", heading SE";
                    if((double)i >= 146.25D && (double)i < 168.75D)
                        s2 = ", heading SSE";
                    if((double)i >= 168.75D && (double)i < 191.25D)
                        s2 = ", heading south";
                    if((double)i >= 191.25D && (double)i < 213.75D)
                        s2 = ", heading SSW";
                    if((double)i >= 213.75D && (double)i < 236.25D)
                        s2 = ", heading SW";
                    if((double)i >= 236.25D && (double)i < 258.75D)
                        s2 = ", heading WSW";
                    if((double)i >= 258.75D && (double)i < 281.25D)
                        s2 = ", heading west";
                    if((double)i >= 281.25D && (double)i < 303.75D)
                        s2 = ", heading WNW";
                    if((double)i >= 303.75D && (double)i < 326.25D)
                        s2 = ", heading NW";
                    if((double)i >= 326.25D && (double)i < 348.75D)
                        s2 = ", heading NNW";
                    String s3 = "  ";
                    String s4 = "  ";
                    if(k3 <= 3)
                        s3 = "Dead ahead, ";
                    if(k3 > 3 && k3 <= 7)
                        s3 = "Right 5, ";
                    if(k3 > 7 && k3 <= 12)
                        s3 = "Right 10, ";
                    if(k3 > 12 && k3 <= 17)
                        s3 = "Right 15, ";
                    if(k3 > 17 && k3 <= 22)
                        s3 = "Right 20, ";
                    if(k3 > 22 && k3 <= 27)
                        s3 = "Right 25, ";
                    if(k3 > 27 && k3 <= 32)
                        s3 = "Right 30, ";
                    if(k3 > 32 && k3 <= 37)
                        s3 = "Right 35, ";
                    if(k3 > 37 && k3 <= 42)
                        s3 = "Right 40, ";
                    if(k3 > 42 && k3 <= 47)
                        s3 = "Right 45, ";
                    if(k3 > 47 && k3 <= 55)
                        s3 = "Right 50, ";
                    if(k3 > 55 && k3 <= 65)
                        s3 = "Right 60, ";
                    if(k3 > 65 && k3 <= 75)
                    {
                        s3 = "Right 70, ";
                        s4 = ", evading";
                    }
                    if(k3 >= 357)
                        s3 = "Dead ahead, ";
                    if(k3 < 357 && k3 >= 352)
                        s3 = "Left 5, ";
                    if(k3 < 352 && k3 >= 347)
                        s3 = "Left 10, ";
                    if(k3 < 347 && k3 >= 342)
                        s3 = "Left 15, ";
                    if(k3 < 342 && k3 >= 337)
                        s3 = "Left 20, ";
                    if(k3 < 337 && k3 >= 332)
                        s3 = "Left 25, ";
                    if(k3 < 332 && k3 >= 327)
                        s3 = "Left 30, ";
                    if(k3 < 327 && k3 >= 322)
                        s3 = "Left 35, ";
                    if(k3 < 322 && k3 >= 317)
                        s3 = "Left 40, ";
                    if(k3 < 317 && k3 >= 312)
                        s3 = "Left 45, ";
                    if(k3 < 312 && k3 >= 305)
                        s3 = "Left 50, ";
                    if(k3 < 305 && k3 >= 295)
                        s3 = "Left 60, ";
                    if(k3 < 295 && k3 >= 285)
                    {
                        s3 = "Left 70, ";
                        s4 = ", evading";
                    }
                    String s5 = "  ";
                    if(k2 >= -5 && k2 <= -3)
                    {
                        s5 = "down a bit, ";
                        s4 = ", evading";
                    }
                    if(k2 >= -3 && k2 <= 3)
                        s5 = "level, ";
                    if(k2 <= 40 && k2 >= 37)
                    {
                        s5 = "up 40, ";
                        s4 = ", evading";
                    }
                    if(k2 <= 37 && k2 > 33)
                        s5 = "up 35, ";
                    if(k2 <= 33 && k2 > 27)
                        s5 = "up 30, ";
                    if(k2 <= 27 && k2 > 23)
                        s5 = "up 25, ";
                    if(k2 <= 23 && k2 > 17)
                        s5 = "up 20, ";
                    if(k2 <= 17 && k2 > 13)
                        s5 = "up 15, ";
                    if(k2 <= 13 && k2 > 7)
                        s5 = "up 10, ";
                    if(k2 < 7 && k2 > 3)
                        s5 = "up a bit, ";
                    if((float)j > f8 && !flag1 || d3 < 2130D && d6 < d2 && !flag1)
                    {
                        s4 = ", ground returns";
                        s2 = ", ground returns";
                    }
                    if((float)(j - 40) > f8)
                    {
                        s4 = ", ground clutter";
                        s2 = ", ground clutter";
                    }
                    if((double)i2 <= (double)f9 && i3 > 0 && k2 >= -20 && k2 <= 40 && Math.sqrt(j3 * j3) <= 75D)
                    {
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + f4 + s1 + s2);
                        freq = 8F;
                    } else
                    if((double)i2 <= (double)f9 && i3 < 1 && (double)i2 >= 115D && k2 >= -5 && k2 <= 40 && Math.sqrt(j3 * j3) <= 75D)
                    {
                        HUD.logCenter("                                          RO: " + s3 + s5 + l2 + "ft" + s4);
                        freq = 4.6F;
                    } else
                    {
                        freq = 8F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public AImkXUnit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class AImkVIIIUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(10) - 5F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(2) - 1;
                    int i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D * (double)f2);
                    float f3 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f3) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil(((double)i2 * 3.2808399D) / 100D) * 100D);
                    int i3 = (int)(Math.ceil(((double)i2 * 3.2808399D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if(l2 >= 5280)
                    {
                        l2 = (int)Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if(l3 < 0)
                        l3 += 360;
                    int i4 = (int)((double)Math.round((double)j1 / 10D) * 10D);
                    if(Math.sqrt(k1 * k1) <= 5D)
                        i4 = j1;
                    float f4 = 9000F;
                    float f5 = f4;
                    if(d3 < 2000D)
                    {
                        f5 = f4 * ((float)d3 / 8000F + 0.75F);
                        if(d6 < d2 && !flag1)
                            f5 = (float)(d3 * 2D + Math.pow(d3, 1.1205D));
                    }
                    float f6 = (float)Math.toDegrees(Math.atan(d3 / (double)f4));
                    float f7 = 90F + f6;
                    float f8 = (float)(Math.cos(Math.toRadians(Math.sqrt(k1 * k1) * 1.4D)) * (double)f5);
                    if(Math.sqrt(k1 * k1) < Math.sqrt(k2 * k2))
                        f8 = (float)(Math.cos(Math.toRadians(Math.sqrt(k2 * k2) * 1.4D)) * (double)f5);
                    float f9 = f8;
                    if((float)j > f7)
                        f9 = f8 * (float)(Math.cos(Math.toRadians((float)(j - 90) * (f7 / 45F))) * 0.5D + 0.5D);
                    String s2 = "  ";
                    String s3 = "  ";
                    if((double)l3 <= 0.5D)
                        s2 = "Dead ahead, ";
                    if((double)l3 > 0.5D && (double)l3 <= 1.5D)
                        s2 = "Right 1, ";
                    if((double)l3 > 1.5D && (double)l3 <= 2.5D)
                        s2 = "Right 2, ";
                    if((double)l3 > 2.5D && (double)l3 <= 3.5D)
                        s2 = "Right 3, ";
                    if((double)l3 > 3.5D && (double)l3 <= 4.5D)
                        s2 = "Right 4, ";
                    if((double)l3 > 4.5D && (double)l3 <= 7.5D)
                        s2 = "Right 5, ";
                    if((double)l3 > 7.5D && l3 <= 15)
                        s2 = "Right 10, ";
                    if(l3 > 15 && l3 <= 25)
                        s2 = "Right 20, ";
                    if(l3 > 25 && l3 <= 35)
                        s2 = "Right 30, ";
                    if(l3 > 35 && l3 <= 45)
                    {
                        s2 = "Right 40, ";
                        s3 = ", evading";
                    }
                    if((double)l3 >= 359.5D)
                        s2 = "Dead ahead, ";
                    if((double)l3 < 359.5D && (double)l3 >= 358.5D)
                        s2 = "Left 1, ";
                    if((double)l3 < 358.5D && (double)l3 >= 357.5D)
                        s2 = "Left 2, ";
                    if((double)l3 < 357.5D && (double)l3 >= 356.5D)
                        s2 = "Left 3, ";
                    if((double)l3 < 356.5D && (double)l3 >= 355.5D)
                        s2 = "Left 4, ";
                    if((double)l3 < 355.5D && (double)l3 >= 352.5D)
                        s2 = "Left 5, ";
                    if((double)l3 < 352.5D && l3 >= 345)
                        s2 = "Left 10, ";
                    if(l3 < 345 && l3 >= 335)
                        s2 = "Left 20, ";
                    if(l3 < 335 && l3 >= 325)
                        s2 = "Left 30, ";
                    if(l3 < 325 && l3 >= 315)
                    {
                        s2 = "Left 40, ";
                        s3 = ", evading";
                    }
                    String s4 = "  ";
                    if(k2 >= -45 && k2 < -35)
                    {
                        s4 = "down 40, ";
                        s3 = ", evading";
                    }
                    if(k2 >= -35 && k2 < -25)
                        s4 = "down 30, ";
                    if(k2 >= -25 && k2 < -15)
                        s4 = "down 20, ";
                    if(k2 >= -15 && (double)k2 < -5.5D)
                        s4 = "down 10, ";
                    if((double)k2 >= -5.5D && (double)k2 < -4.5D)
                        s4 = "down 5, ";
                    if((double)k2 >= -4.5D && (double)k2 < -3.5D)
                        s4 = "down 4, ";
                    if((double)k2 >= -3.5D && (double)k2 < -2.5D)
                        s4 = "down 3, ";
                    if((double)k2 >= -2.5D && (double)k2 < -1.5D)
                        s4 = "down 2, ";
                    if((double)k2 >= -1.5D && (double)k2 < -0.5D)
                        s4 = "down 1, ";
                    if((double)k2 >= -0.5D && (double)k2 <= 0.5D)
                        s4 = "level, ";
                    if(k2 <= 45 && k2 > 35)
                    {
                        s4 = "up 40, ";
                        s3 = ", evading";
                    }
                    if(k2 <= 35 && k2 > 25)
                        s4 = "up 30, ";
                    if(k2 <= 25 && k2 > 15)
                        s4 = "up 20, ";
                    if(k2 <= 15 && (double)k2 > 5.5D)
                        s4 = "up 10, ";
                    if((double)k2 <= 5.5D && (double)k2 > 4.5D)
                        s4 = "up 5, ";
                    if((double)k2 <= 4.5D && (double)k2 > 3.5D)
                        s4 = "up 4, ";
                    if((double)k2 <= 3.5D && (double)k2 > 2.5D)
                        s4 = "up 3, ";
                    if((double)k2 <= 2.5D && (double)k2 > 1.5D)
                        s4 = "up 2, ";
                    if((double)k2 < 1.5D && (double)k2 > 0.5D)
                        s4 = "up 1, ";
                    if((float)j > f7 && !flag1 || d3 < 2000D && d6 < d2 && !flag1)
                        s3 = ", ground returns";
                    if((float)(j - 40) > f7)
                        s3 = ", ground clutter";
                    if((double)i2 <= (double)f9 && j3 > 0 && k2 >= -45 && k2 <= 45 && Math.sqrt(k3 * k3) <= 45D)
                    {
                        HUD.logCenter("                                          RO: Target bearing " + i4 + "\260" + ", " + s + ", " + l2 + s1 + s3);
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)f9 && j3 < 1 && (double)i2 >= 120D && k2 >= -45 && k2 <= 45 && Math.sqrt(k3 * k3) <= 45D)
                    {
                        HUD.logCenter("                                          RO: " + s2 + s4 + i3 + "ft" + s3);
                        freq = 3F;
                    } else
                    {
                        freq = 6F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public AImkVIIIUnit()
        {
            freq = 8F;
            Timer1 = Timer2 = freq;
        }
    }

    public static class AImkIVUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            boolean flag = false;
            Aircraft aircraft = World.getPlayerAircraft();
            double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
            double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
            double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
            double d3 = d2 - (double)Landscape.Hmin((float)aircraft.pos.getAbsPoint().x, (float)aircraft.pos.getAbsPoint().y);
            if(d3 < 0.0D)
                d3 = 0.0D;
            int i = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
            if(i < 0)
                i = 360 + i;
            int j = (int)(-((double)aircraft.pos.getAbsOrient().getPitch() - 90D));
            if(j < 0)
                j = 360 + j;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                Actor actor = (Actor)entry.getValue();
                if((actor instanceof Aircraft) && actor.getArmy() != myArmy && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
                {
                    pos.getAbs(point3d);
                    double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                    double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                    // new String();
                    // new String();
//                    int k = (int)(Math.floor(actor.pos.getAbsPoint().z * 0.10000000000000001D) * 10D);
//                    int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                    double d7 = (int)(Math.ceil((d2 - d6) / 10D) * 10D);
                    boolean flag1 = false;
                    Engine.land();
                    int i1 = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    float f = Mission.cur().sectFile().get("Weather", "WindSpeed", 0.0F);
                    if(i1 >= 28 && i1 < 32 && f < 7.5F)
                        flag1 = true;
                    String s = "level with us";
                    if(d2 - d6 - 300D >= 0.0D)
                        s = "below us";
                    if((d2 - d6) + 300D <= 0.0D)
                        s = "above us";
                    if(d2 - d6 - 300D < 0.0D && d2 - d6 - 150D >= 0.0D)
                        s = "slightly below";
                    if((d2 - d6) + 300D > 0.0D && (d2 - d6) + 150D < 0.0D)
                        s = "slightly above";
                    // new String();
                    double d8 = d4 - d;
                    double d9 = d5 - d1;
                    float f1 = 57.32484F * (float)Math.atan2(d9, -d8);
                    int j1 = (int)(Math.floor((int)f1) - 90D);
                    if(j1 < 0)
                        j1 = 360 + j1;
                    int k1 = j1 - i;
                    double d10 = d - d4;
                    double d11 = d1 - d5;
                    // Random random = new Random();
                    float f2 = ((float)World.Rnd().nextInt(20) - 10F) / 100F + 1.0F;
                    int l1 = World.Rnd().nextInt(6) - 3;
                    float f3 = 5630F;
                    float f4 = f3;
                    if(d3 < (double)(1.25F * f3) && !flag1)
                        f4 = (float)d3 * 0.8F;
                    if(d3 < (double)(1.25F * f3) && flag1)
                        if(d3 <= (double)(1.25F * f3 * 0.5F))
                            f4 = (float)(d3 * 0.8D * 2D);
                        else
                            f4 = f3;
                    int i2 = (int)(Math.ceil(Math.sqrt((d11 * d11 + d10 * d10) * (double)f2) / 10D) * 10D);
                    if((float)i2 > f3)
                        i2 = (int)(Math.ceil(Math.sqrt(d11 * d11 + d10 * d10) / 10D) * 10D);
                    float f5 = 57.32484F * (float)Math.atan2(i2, d7);
                    int j2 = (int)(Math.floor((int)f5) - 90D);
                    int k2 = (j2 - (90 - j)) + l1;
                    int l2 = (int)(Math.ceil(((double)i2 * 3.28084D) / 100D) * 100D);
                    int i3 = (int)(Math.ceil(((double)i2 * 3.28084D) / 300D) * 300D);
                    int j3 = 0;
                    String s1 = "ft";
                    if(l2 >= 5280)
                    {
                        l2 = (int)Math.floor(l2 / 5280);
                        s1 = "mi";
                        j3 = 1;
                    }
                    int k3 = k1 + l1;
                    int l3 = k3;
                    if(l3 < 0)
                        l3 += 360;
                    float f6 = (float)((double)f4 + Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 2.575D)) * ((double)f4 * 0.25D));
                    int i4 = (int)((double)f6 * Math.cos(Math.toRadians(k2)));
                    String s2 = "  ";
                    if(l3 < 5)
                        s2 = "Dead ahead, ";
                    if(l3 >= 5 && (double)l3 <= 7.5D)
                        s2 = "Right 5, ";
                    if((double)l3 > 7.5D && (double)l3 <= 12.5D)
                        s2 = "Right 10, ";
                    if((double)l3 > 12.5D && (double)l3 <= 17.5D)
                        s2 = "Right 15, ";
                    if((double)l3 > 17.5D && l3 <= 25)
                        s2 = "Right 20, ";
                    if(l3 > 25 && l3 <= 35)
                        s2 = "Right 30, ";
                    if(l3 > 35 && l3 <= 45)
                        s2 = "Right 40, ";
                    if(l3 > 45 && l3 <= 70)
                        s2 = "Turn right, ";
                    if(l3 > 355)
                        s2 = "Dead ahead, ";
                    if(l3 <= 355 && (double)l3 >= 352.5D)
                        s2 = "Left 5, ";
                    if((double)l3 < 352.5D && (double)l3 >= 347.5D)
                        s2 = "Left 10, ";
                    if((double)l3 < 347.5D && (double)l3 >= 342.5D)
                        s2 = "Left 15, ";
                    if((double)l3 < 342.5D && l3 >= 335)
                        s2 = "Left 20, ";
                    if(l3 < 335 && l3 >= 325)
                        s2 = "Left 30, ";
                    if(l3 < 325 && l3 >= 315)
                        s2 = "Left 40, ";
                    if(l3 < 345 && l3 >= 290)
                        s2 = "Turn left, ";
                    String s3 = "  ";
                    if(k2 < -10)
                        s3 = "nose down";
                    if(k2 >= -10 && k2 <= -5)
                        s3 = "down a bit";
                    if(k2 > -5 && k2 < 5)
                        s3 = "level";
                    if(k2 <= 10 && k2 >= 5)
                        s3 = "up a bit";
                    if(k2 > 10)
                        s3 = "pull up";
                    String s4 = "  ";
                    if(l3 < 5)
                        s4 = "dead ahead, ";
                    if(l3 >= 5 && (double)l3 <= 7.5D)
                        s4 = "right by 5\260, ";
                    if((double)l3 > 7.5D && (double)l3 <= 12.5D)
                        s4 = "right by 10\260, ";
                    if((double)l3 > 12.5D && (double)l3 <= 17.5D)
                        s4 = "right by 15\260, ";
                    if((double)l3 > 17.5D && l3 <= 25)
                        s4 = "right by 20\260, ";
                    if(l3 > 25 && l3 <= 35)
                        s4 = "right by 30\260, ";
                    if(l3 > 35 && l3 <= 45)
                        s4 = "right by 40\260, ";
                    if(l3 > 45 && l3 <= 70)
                        s4 = "off our right, ";
                    if(l3 > 355)
                        s4 = "dead ahead, ";
                    if(l3 <= 355 && (double)l3 >= 352.5D)
                        s4 = "left by 5\260, ";
                    if((double)l3 < 352.5D && (double)l3 >= 347.5D)
                        s4 = "left by 10\260, ";
                    if((double)l3 < 347.5D && (double)l3 >= 342.5D)
                        s4 = "left by 15\260, ";
                    if((double)l3 < 342.5D && l3 >= 335)
                        s4 = "left by 20\260, ";
                    if(l3 < 335 && l3 >= 325)
                        s4 = "left by 30\260, ";
                    if(l3 < 325 && l3 >= 315)
                        s4 = "left by 40\260, ";
                    if(l3 < 345 && l3 >= 290)
                        s4 = "off our left, ";
                    if((double)i2 <= (double)i4 && j3 > 0 && k2 >= -20 && k2 <= 20 && Math.sqrt(k3 * k3) <= 70D)
                    {
                        HUD.logCenter("                                          RO: Contact " + s4 + s + ", " + l2 + s1);
                        freq = 6F;
                    } else
                    if((double)i2 <= (double)i4 && j3 < 1 && (double)i2 >= 120D && k2 >= -20 && k2 <= 20 && Math.sqrt(k3 * k3) <= 70D)
                    {
                        HUD.logCenter("                                          RO: " + s2 + s3 + ", " + i3 + s1);
                        freq = 4F;
                    } else
                    {
                        freq = 6F;
                    }
                    super.resetTimer(freq);
                }
            }

            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private float freq;

        public AImkIVUnit()
        {
            freq = 8F;
            Timer1 = freq;
            Timer2 = freq;
        }
    }

    public static class AGCIUnit extends CandCGeneric
    {

        public boolean danger()
        {
            if(Mission.cur() != null)
                maxrange = Mission.cur().sectFile().get("Mods", "GCIRange", 100);
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
//            float f = 1000F;
            // Random random = new Random();
            int i = World.Rnd().nextInt(140);
            int j = i + 10;
            Aircraft aircraft1 = World.getPlayerAircraft();
            if (aircraft1 == null) return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!
            
            // TODO: Fix by SAS~Storebror:
            // Get rid of endless recursion through all game's target list (a few hundred times)
            // New method introduced to get the same result with on single target list search!
            Aircraft aircraft = GetNearestEnemyInSteps(aircraft1, 1000F, maxrange, 1000F, 9);
            
////            Aircraft aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//            Aircraft aircraft = GetNearestEnemy(aircraft1, 1000F, 9);
//            for(int k = 0; aircraft == null && k < maxrange; k++)
//            {
////                aircraft = NearestRadarTargets.GetNearestEnemyEcho(aircraft1, 1000F + f, 9); // TODO: Fixed by SAS~Storebror, avoid missing dependencies!
//                aircraft = GetNearestEnemy(aircraft1, 1000F + f, 9);
//                f += 1000F;
//            }
            if (aircraft == null) return true; // TODO: Fixed By SAS~Storebror, avoid Null Pointer Exceptions!

//            boolean flag1 = false;
            maxrange *= 1000;
            if(Mission.cur() != null && Mission.cur().sectFile().get("Mods", "GCIVarAlt", 0) == 1)
            {
                maxheight1 = (aircraft.pos.getAbsPoint().distance(point3d) / (double)maxrange) * 8000D;
                if(maxheight1 < (double)j)
                    maxheight1 = j;
                maxheight2 = (aircraft1.pos.getAbsPoint().distance(point3d) / (double)maxrange) * 8000D;
                if(maxheight2 < (double)j)
                    maxheight2 = j;
            }
            if(aircraft1.pos.getAbsPoint().distance(point3d) < (double)maxrange && aircraft1.pos.getAbsPoint().z >= maxheight2)
                flag = true;
            if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().distance(point3d) < (double)maxrange && aircraft.pos.getAbsPoint().z >= maxheight1 && aircraft != World.getPlayerAircraft() && aircraft.getArmy() != myArmy && aircraft1.getArmy() == myArmy)
            {
                double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 10000D;
                double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 10000D;
                double d2 = (Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x) / 1000D;
                double d3 = (Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y) / 1000D;
                double d4 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
                double d5 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                double d6 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                double d7 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D;
                double d8 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D;
                char c = (char)(int)(65D + Math.floor((d5 / 676D - Math.floor(d5 / 676D)) * 26D));
                char c1 = (char)(int)(65D + Math.floor((d5 / 26D - Math.floor(d5 / 26D)) * 26D));
                // new String();
                String s;
                if(d4 > 260D)
                    s = "" + c + c1;
                else
                    s = "" + c1;
                // new String();
                int l = (int)(Math.floor(aircraft.pos.getAbsPoint().z * 0.328084D) * 10D);
//                int i1 = (int)(Math.floor((aircraft.getSpeed(vector3d) * 0.62137119200000002D * 60D * 60D) / 10000D) * 10D);
                // new String();
                int j1 = (int)Math.ceil(d6);
                double d9 = d5 - d;
                double d10 = d6 - d1;
                float f1 = 57.32484F * (float)Math.atan2(d10, -d9);
                double d11 = Math.floor((int)f1) - 90D;
                if(d11 < 0.0D)
                    d11 = 360D + d11;
                int k1 = (int)d11;
                double d12 = d2 - d7;
                double d13 = d3 - d8;
                int l1 = (int)(-((double)aircraft.pos.getAbsOrient().getYaw() - 90D));
                if(l1 < 0)
                    l1 = 360 + l1;
                int i2 = (int)Math.ceil(Math.sqrt(d13 * d13 + d12 * d12));
                int j2 = (int)((double)i2 * 0.621371192D);
                if(flag)
                {
                    if(i2 > 4)
                        HUD.logCenter("                                          Target bearing " + k1 + "\260" + ", range " + j2 + " miles, height " + l + "ft, heading " + l1 + "\260");
                    else
                        HUD.logCenter(" ");
                } else
                {
                    HUD.logCenter("                                                                             Target at " + s + "-" + j1 + ", height " + l + "ft, heading " + l1 + "\260");
                }
            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Vector Field to avoid repetitive "new"/GC loops
        private Vector3d vector3d = new Vector3d();

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private int maxrange;
        private double maxheight1;
        private double maxheight2;

        public AGCIUnit()
        {
            maxrange = 0x186a0;
            maxheight1 = 150D;
            maxheight2 = 150D;
        }
    }

    public static class AFACUnit extends CandCGeneric
    {

        public boolean danger()
        {
//            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            int i = Mission.cur().sectFile().get("Mods", "FACDelay", 0) * 60;
            if(Time.current() > (long)i)
                active = true;
            Aircraft aircraft = World.getPlayerAircraft();
            if(aircraft.pos.getAbsPoint().distance(point3d) < 10000D)
                flag = true;
            for(Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
            {
                actor2 = (Actor)entry.getValue();
//                if(((actor2 instanceof TypeScout) || (actor2 instanceof AT6)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                if(((actor2 instanceof TypeScout) || isAT6(actor2)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                    flag1 = true;
            }

            for(Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
            {
                Actor actor = (Actor)entry1.getValue();
                if(active && flag1 && Actor.isAlive(actor) && ((actor instanceof TankGeneric) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric)) && actor.getArmy() != myArmy && actor.pos.getAbsPoint().distance(point3d) < 1000D)
                {
                    double d = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 10000D;
                    double d1 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 10000D;
                    double d2 = Main3D.cur3D().land2D.mapSizeX() / 1000D;
//                    double d3 = actor.pos.getAbsPoint().distance(point3d);
//                    double d4 = point3d.z;
                    double d5 = (Main3D.cur3D().land2D.worldOfsX() + markpoint.x) / 10000D;
                    double d6 = (Main3D.cur3D().land2D.worldOfsY() + markpoint.y) / 10000D;
//                    double d7 = actor.pos.getAbsPoint().z;
                    char c = (char)(int)(65D + Math.floor((d / 676D - Math.floor(d / 676D)) * 26D));
                    char c1 = (char)(int)(65D + Math.floor((d / 26D - Math.floor(d / 26D)) * 26D));
                    // new String();
                    String s1;
                    if(d2 > 260D)
                        s1 = "" + c + c1;
                    else
                        s1 = "" + c1;
                    double d8 = d5 - d;
                    double d9 = d6 - d1;
                    float f = 57.32484F * (float)Math.atan2(d9, d8);
                    int j = (int)f;
                    j = (((j + 180) % 360) / 10) * 10;
                    // new String();
                    String s2 = "units";
                    if((actor instanceof ArtilleryCY6.ProneInfantry) || (actor instanceof ArtilleryCY6.DugInInfantry))
                        s2 = "troops";
                    else
                    if(actor instanceof ArtilleryGeneric)
                        s2 = "units";
                    else
                    if(actor instanceof TgtTank)
                        s2 = "armor";
                    else
                    if(actor instanceof TgtVehicle)
                        s2 = "vehicle";
                    boolean flag3 = false;
                    if(actor instanceof AAA)
                        flag3 = true;
                    String s3 = "";
                    if(flag3)
                        s3 = ", watch out for AA";
//                    boolean flag4 = false;
                    String s4 = "";
                    Engine.land();
                    int k = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(actor.pos.getAbsPoint().x), Engine.land().WORLD2PIXY(actor.pos.getAbsPoint().y));
                    if(k >= 28 && k < 32)
                        s4 = ", near the water";
                    if(k >= 16 && k < 20)
                        s4 = ", in the town";
                    if(k >= 24 && k < 29)
                        s4 = ", near the treeline";
                    if(k >= 32 && k < 64 || k >= 128)
                        s4 = ", near the road";
                    if(k >= 64 && k < 128)
                        s4 = ", near the tracks";
                    String s5 = "";
                    Actor actor3 = null;
                    actor3 = War.GetNearestEnemy(actor, actor.getArmy(), 300F);
                    if(actor3 != null)
                        s5 = ", friendlies close";
//                    int l = (int)(Math.ceil(d3 / 10D) * 10D);
                    int i1 = (int)Math.ceil(d1);
                    if(flag && flag2)
                        HUD.logCenter("                                                                             Enemy " + s2 + ", bearing " + j + "\260" + s4 + s5 + s3 + "!");
                    else
                    if(flag && !flag2)
                    {
                        HUD.logCenter("                                                                             You're cleared hot. Marking target.");
                        markpoint = actor.pos.getAbsPoint();
                        for(entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1))
                        {
                            actor2 = (Actor)entry1.getValue();
//                            if(((actor2 instanceof TypeScout) || (actor2 instanceof AT6)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                            if(((actor2 instanceof TypeScout) || isAT6(actor2)) && actor2.getArmy() == myArmy && actor2.pos.getAbsPoint().distance(point3d) < 5000D)
                            {
                                pilot = (Pilot)((Aircraft)actor2).FM;
                                pilot.Group.setGTargMode(0);
                                pilot.Group.setGTargMode(actor);
                                pilot.Group.setGroupTask(4);
                            }
                        }

                        float f1 = World.getTimeofDay();
                        if(f1 >= 0.0F && f1 <= 5F || f1 >= 21F && f1 <= 24F)
                            pilot.AS.setNavLightsState(true);
                        flag2 = true;
                    } else
                    if(!flag)
                        HUD.logCenter("                                                                             Request CAS at map grid " + s1 + "-" + i1);
                }
            }

            if(!flag && flag2 && !BDA && flag1)
            {
                for(Entry entry2 = Engine.name2Actor().nextEntry(null); entry2 != null; entry2 = Engine.name2Actor().nextEntry(entry2))
                {
                    Actor actor1 = (Actor)entry2.getValue();
                    if(!Actor.isAlive(actor1) && ((actor1 instanceof TankGeneric) || (actor1 instanceof ArtilleryGeneric) || (actor1 instanceof CarGeneric)) && actor1.getArmy() != myArmy && actor1.pos.getAbsPoint().distance(point3d) < 500D)
                    {
                        if((actor1 instanceof ArtilleryCY6.ProneInfantry) || (actor1 instanceof ArtilleryCY6.DugInInfantry))
                            troops += 5;
                        else
                        if(actor1 instanceof TgtTank)
                            armor++;
                        else
                        if((actor1 instanceof CarGeneric) || (actor1 instanceof TgtVehicle))
                            trucks++;
                        else
                        if(actor1 instanceof ArtilleryGeneric)
                            guns++;
                        if(armor > 0)
                        {
                            e1 = "" + armor + " tank ";
                            if(armor > 1)
                                e1 = "" + armor + "tanks ";
                        }
                        if(guns > 0)
                        {
                            e2 = "" + guns + " emplacement ";
                            if(guns > 1)
                                e1 = "" + guns + " emplacements ";
                        }
                        if(trucks > 0)
                        {
                            e3 = "" + trucks + " vehicle ";
                            if(trucks > 1)
                                e3 = "" + trucks + " vehicles ";
                        }
                        if(troops > 0)
                            e4 = "" + troops + " troops";
                        String s = e1 + e2 + e3 + e4 + " destroyed.";
                        if(troops + armor + trucks + guns <= 0)
                            s = " No targets hit.";
                        HUD.logCenter("                                                                             Thanks for the help! BDA: " + s);
                        BDA = true;
                    }
                }

            }
            return true;
        }

        // TODO: Fixed by SAS~Storebror: Create Class-Level temporary Point Field to avoid repetitive "new"/GC loops
        private Point3d point3d = new Point3d();

        private boolean active;
//        private int spread;
        private int armor;
        private int trucks;
        private int guns;
        private int troops;
        private static String e1 = "";
        private static String e2 = "";
        private static String e3 = "";
        private static String e4 = "";
        private boolean BDA;
        private static Actor actor2 = null;
        private static Pilot pilot = null;
        private static Point3d markpoint = new Point3d();


        public AFACUnit()
        {
            active = false;
//            spread = 0;
            armor = 0;
            trucks = 0;
            guns = 0;
            troops = 0;
            BDA = false;
            Timer1 = Timer2 = 60F;
        }
    }

    // TODO: Fixed by SAS~Storebror: Strip surplus default Constructor 
//    public CandC()
//    {
//    }
   
    // TODO: Fixed by SAS~Storebror: Add generic methods and fields to avoid dependence of probably non-present mod classes!
    private static void HasVector(Aircraft aircraft, int i) {
        if (isHasVectorAvailableType == -1) {
            try {
                Class class1 = Class.forName("com.maddox.il2.ai.VisCheck");
                // TODO: ATTENTION! Needs SAS Common Utils 1.10 or later!
                hasVectorMethod = Reflection.getMethod(
                        class1,
                        "hasVector",
                        new Class[] {Aircraft.class, int.class}
                        );
                isHasVectorAvailableType = HAS_VECTOR_AVAILABLE;
            } catch (Exception e) {
                isHasVectorAvailableType = HAS_VECTOR_NOT_AVAILABLE;
            }
        }
        switch (isHasVectorAvailableType) {
            case HAS_VECTOR_AVAILABLE:
                try {
                    Reflection.invokeMethod(hasVectorMethod, new Object[] {aircraft, new Integer(i)});
                } catch (Exception e) {
                }
                return;
            case HAS_VECTOR_NOT_AVAILABLE:
            default:
                return;
        }
    }
    private static int isHasVectorAvailableType = -1;
    private static Method hasVectorMethod = null;
    private static final int HAS_VECTOR_NOT_AVAILABLE = 0;
    private static final int HAS_VECTOR_AVAILABLE = 1;
   
    private static Aircraft GetNearestEnemyInSteps(Actor actor, float maxDistanceStart, float maxDistanceEnd, float maxDistanceStep, int targetType) {
        return NearestRadarTargets.GetNearestEnemyEchoInSteps(actor, maxDistanceStart, maxDistanceEnd, maxDistanceStep, targetType);
    }
    
    private static Aircraft GetNearestEnemy(Actor actor, float maxDistance, int targetType) {
        return NearestRadarTargets.GetNearestEnemyEcho(actor, maxDistance, targetType);
    }
    
    private static boolean isAT6(Object o) {
        if (!Class_AT6_Initialized && Class_AT6 == null) {
            Class_AT6_Initialized = true;
            try {
                Class_AT6 = Class.forName("com.maddox.il2.objects.air.AT6");
            } catch (Exception e) {}
        }
        if (Class_AT6 == null) return false;
        return Class_AT6.isInstance(o);
    }
    private static Class Class_AT6 = null;
    private static boolean Class_AT6_Initialized = false;
    
    private static boolean isHRS3AI(Object o) {
        if (!Class_HRS3AI_Initialized && Class_HRS3AI == null) {
            Class_HRS3AI_Initialized = true;
            try {
                Class_HRS3AI = Class.forName("com.maddox.il2.objects.air.HRS3AI");
            } catch (Exception e) {}
        }
        if (Class_HRS3AI == null) return false;
        return Class_HRS3AI.isInstance(o);
    }
    private static Class Class_HRS3AI = null;
    private static boolean Class_HRS3AI_Initialized = false;

    private static boolean isFJ_3M(Object o) {
        if (!Class_FJ_3M_Initialized && Class_FJ_3M == null) {
            Class_FJ_3M_Initialized = true;
            try {
                Class_FJ_3M = Class.forName("com.maddox.il2.objects.air.FJ_3M");
            } catch (Exception e) {}
        }
        if (Class_FJ_3M == null) return false;
        return Class_FJ_3M.isInstance(o);
    }
    private static Class Class_FJ_3M = null;
    private static boolean Class_FJ_3M_Initialized = false;

    private static boolean isF84G3(Object o) {
        if (!Class_F84G3_Initialized && Class_F84G3 == null) {
            Class_F84G3_Initialized = true;
            try {
                Class_F84G3 = Class.forName("com.maddox.il2.objects.air.F84G3");
            } catch (Exception e) {}
        }
        if (Class_F84G3 == null) return false;
        return Class_F84G3.isInstance(o);
    }
    private static Class Class_F84G3 = null;
    private static boolean Class_F84G3_Initialized = false;

    private static boolean isA1H_Tanker(Object o) {
        if (!Class_A1H_Tanker_Initialized && Class_A1H_Tanker == null) {
            Class_A1H_Tanker_Initialized = true;
            try {
                Class_A1H_Tanker = Class.forName("com.maddox.il2.objects.air.A1H_Tanker");
            } catch (Exception e) {}
        }
        if (Class_A1H_Tanker == null) return false;
        return Class_A1H_Tanker.isInstance(o);
    }
    private static Class Class_A1H_Tanker = null;
    private static boolean Class_A1H_Tanker_Initialized = false;

    private static boolean isKB_29P(Object o) {
        if (!Class_KB_29P_Initialized && Class_KB_29P == null) {
            Class_KB_29P_Initialized = true;
            try {
                Class_KB_29P = Class.forName("com.maddox.il2.objects.air.KB_29P");
            } catch (Exception e) {}
        }
        if (Class_KB_29P == null) return false;
        return Class_KB_29P.isInstance(o);
    }
    private static Class Class_KB_29P = null;
    private static boolean Class_KB_29P_Initialized = false;

    static 
    {
        new CandCGeneric.SPAWN(AFACUnit.class);
        new CandCGeneric.SPAWN(AGCIUnit.class);
        new CandCGeneric.SPAWN(AImkIVUnit.class);
        new CandCGeneric.SPAWN(AImkVIIIUnit.class);
        new CandCGeneric.SPAWN(AImkXUnit.class);
        new CandCGeneric.SPAWN(AImkXVUnit.class);
        new CandCGeneric.SPAWN(AimpointUnit.class);
        new CandCGeneric.SPAWN(ErrorUnit.class);
        new CandCGeneric.SPAWN(ANAPS4Unit.class);
        new CandCGeneric.SPAWN(ASDUnit.class);
        new CandCGeneric.SPAWN(ASVUnit.class);
        new CandCGeneric.SPAWN(ASGUnit.class);
        new CandCGeneric.SPAWN(ASVmkVIIIUnit.class);
        new CandCGeneric.SPAWN(CorkscrewUnit.class);
        new CandCGeneric.SPAWN(CWUnit.class);
        new CandCGeneric.SPAWN(DynamicWeatherUnit.class);
        new CandCGeneric.SPAWN(DZUnit.class);
        new CandCGeneric.SPAWN(FACUnit.class);
        new CandCGeneric.SPAWN(ASVmkIIIUnit.class);
        new CandCGeneric.SPAWN(FuGUnit.class);
        new CandCGeneric.SPAWN(FD2Unit.class);
        new CandCGeneric.SPAWN(FuG218Unit.class);
        new CandCGeneric.SPAWN(FlensburgUnit.class);
        new CandCGeneric.SPAWN(FireUnit.class);
        new CandCGeneric.SPAWN(GCIUnit.class);
        new CandCGeneric.SPAWN(GNRUnit.class);
        new CandCGeneric.SPAWN(GCIUnit.class);
        new CandCGeneric.SPAWN(GTWUnit.class);
        new CandCGeneric.SPAWN(H2SUnit.class);
        new CandCGeneric.SPAWN(JammerUnit.class);
        new CandCGeneric.SPAWN(MADUnit.class);
        new CandCGeneric.SPAWN(MonicaUnit.class);
        new CandCGeneric.SPAWN(NavUnit.class);
        new CandCGeneric.SPAWN(NaxosUnit.class);
        new CandCGeneric.SPAWN(OBOEUnit.class);
        new CandCGeneric.SPAWN(OBSUnit.class);
        new CandCGeneric.SPAWN(RadioOperatorUnit.class);
        new CandCGeneric.SPAWN(RandomAircraft2Unit.class);
        new CandCGeneric.SPAWN(RandomAircraftUnit.class);
        new CandCGeneric.SPAWN(RandomShips2Unit.class);
        new CandCGeneric.SPAWN(RandomShipsUnit.class);
        new CandCGeneric.SPAWN(RandomVehicles2Unit.class);
        new CandCGeneric.SPAWN(RandomVehiclesUnit.class);
        new CandCGeneric.SPAWN(RandomTrainsUnit.class);
        new CandCGeneric.SPAWN(RandomFlakUnit.class);
        new CandCGeneric.SPAWN(RandomTimeUnit.class);
        new CandCGeneric.SPAWN(RandomSkillUnit.class);
        new CandCGeneric.SPAWN(RandomWeatherUnit.class);
        new CandCGeneric.SPAWN(RCGCIUnit.class);
        new CandCGeneric.SPAWN(RefuelUnit.class);
        new CandCGeneric.SPAWN(RESCAPUnit.class);
        new CandCGeneric.SPAWN(DynamicRescueUnit.class);
        new CandCGeneric.SPAWN(SARUnit.class);
        new CandCGeneric.SPAWN(SerrateUnit.class);
        new CandCGeneric.SPAWN(SN2bUnit.class);
        new CandCGeneric.SPAWN(SN2Unit.class);
        new CandCGeneric.SPAWN(AmbushUnit.class);
        new CandCGeneric.SPAWN(SpawnUnit.class);
        new CandCGeneric.SPAWN(SPRUnit.class);
        new CandCGeneric.SPAWN(StormFrontUnit.class);
        new CandCGeneric.SPAWN(TakiUnit.class);
        new CandCGeneric.SPAWN(VectorUnit.class);
        new CandCGeneric.SPAWN(WindowUnit.class);
        new CandCGeneric.SPAWN(BoxUnit.class);
        new CandCGeneric.SPAWN(BombUnit.class);
        new CandCGeneric.SPAWN(BRGUnit.class);
        new CandCGeneric.SPAWN(NGSUnit.class);
        new CandCGeneric.SPAWN(NGS1Unit.class);
        new CandCGeneric.SPAWN(SLDUnit.class);
        new CandCGeneric.SPAWN(SLD2Unit.class);
        new CandCGeneric.SPAWN(CityLightsUnit.class);
        new CandCGeneric.SPAWN(LowReconUnit.class);
        new CandCGeneric.SPAWN(HighReconUnit.class);
        new CandCGeneric.SPAWN(LevelUnit.class);
        new CandCGeneric.SPAWN(LandingUnit.class);
        new CandCGeneric.SPAWN(DynamicStreamUnit.class);
        new CandCGeneric.SPAWN(DynamicStrikeUnit.class);
    }
}
