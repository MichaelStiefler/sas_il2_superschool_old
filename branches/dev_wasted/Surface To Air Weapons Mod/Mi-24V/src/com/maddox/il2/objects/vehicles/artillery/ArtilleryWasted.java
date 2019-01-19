// Decompiled by DJ v3.12.12.101 Copyright 2016 Atanas Neshkov  Date: 09.10.2017 21:51:43
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ArtilleryWasted.java

package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.MI8MT;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;

import java.util.*;

// Referenced classes of package com.maddox.il2.objects.vehicles.artillery:
//            ArtilleryGeneric

public abstract class ArtilleryWasted
{
    public static class VDVInfantry extends ArtilleryGeneric
    {

//        public Actor findEnemy(Aim aim)
//        {
//            if(pickitup)
//                PickUp();
//            CheckEnemy();
//            return super.findEnemy(aim);
//        }

//        public boolean PickUp()
//        {
//            if(Time.current() < 300L)
//                return false;
//            Point3d point3d = new Point3d();
//            super.pos.getAbs(point3d);
//            Vector3d vector3d = new Vector3d();
//            float f = World.getTimeofDay();
//            String s = "Effects/Smokes/Yellowsmoke.eff";
//            if(f >= 0.0F && f <= 5F || f >= 21F && f <= 24F)
//                s = "3DO/Effects/Fireworks/FlareWhiteWide.eff";
//            Actor actor = War.GetNearestEnemy(this, getArmy(), 100F);
//            com.maddox.il2.objects.air.Aircraft aircraft = World.getPlayerAircraft();
//            for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
//            {
//                Actor actor1 = (Actor)entry.getValue();
//                super.pos.getAbs(point3d);
//                if(actor1.getArmy() == getArmy() && (actor1 instanceof MI8MT) && (actor1 != World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 300D && actor1.getSpeed(vector3d) <= 27D || actor1 == World.getPlayerAircraft() && actor1.pos.getAbsPoint().distance(point3d) <= 100D && actor1.getSpeed(vector3d) <= 1.0D))
//                {
//                    HUD.logCenter("                                                                             We got him! Heading home!");
//                    pickup = true;
//                    World.cur().scoreCounter.targetOnItems.add(new ScoreItem(102, 100D, false));
//                    destroy();
//                }
//            }
//
//            return true;
//        }
//
//        public void CheckEnemy()
//        {
//            Point3d point3d = new Point3d();
//            super.pos.getAbs(point3d);
//            List list = Engine.targets();
//            int i = list.size();
//            for(int j = 0; j < i; j++)
//            {
//                Actor actor = (Actor)list.get(j);
//                if(actor != null && actor.pos.getAbsPoint().distance(point3d) <= 300D && actor.getArmy() != getArmy())
//                {
//                    Crouch();
//                    return;
//                }
//            }
//
//        }
//
//        public void Crouch()
//        {
//            Vector3d vector3d = new Vector3d();
//            Random random = new Random();
//            int i = random.nextInt(100);
//            if(!crouched && i < 20)
//            {
//                setMesh("3do/Infantry/VDVInfantry/Summer/hier2.him");
//                crouched = true;
//                Vector3d vector3d1 = null;
//                setSpeed(vector3d1);
//            } else
//            if(crouched && i < 20)
//            {
//                setMesh("3do/Infantry/VDVInfantry/Summer/hier.him");
//                crouched = false;
//            }
//        }
//
//        public boolean pickitup;
//        private boolean crouched;
//        private boolean pickup;
//        private boolean popped;
//        private float delay;

        public VDVInfantry()
        {
//            crouched = false;
//            pickup = false;
//            popped = false;
//            delay = 0.0F;
        }
    }

    public static class VDVRPGInfantry extends ArtilleryGeneric
    {

        public VDVRPGInfantry()
        {
        	Engine.targets().add(this);
        }
    }


    public ArtilleryWasted()
    {
    }

    static 
    {
        new ArtilleryGeneric.SPAWN(com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVInfantry.class);
        new ArtilleryGeneric.SPAWN(com.maddox.il2.objects.vehicles.artillery.ArtilleryWasted.VDVRPGInfantry.class);
    }
}
