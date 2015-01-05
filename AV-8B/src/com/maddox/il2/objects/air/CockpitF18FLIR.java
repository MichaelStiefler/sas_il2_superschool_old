// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11/4/2012 6:52:29 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitF18FLIR.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.rts.*;
import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Time;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, F_18

public class CockpitF18FLIR extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((F_18)aircraft()).FLIR = true;
            CmdEnv.top().exec("fov 33.3");
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        super.doFocusLeave();
        ((F_18)aircraft()).FLIR = false;
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);       
        orient1.set(orient);
        laser(orient);
        //laserTrack();
    }
       
    
    private float h;
    private float v;
    private float v1;
    private float h1;

    public void laser(Orient orient)// Laser designator
    {
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_18)aircraft()).pos.getAbs(point3d, orient2);       
        float roll = orient2.getRoll();
        float fn = orient2.getPitch();
        float antiroll = 360F - roll;
        float pitch = 0F;
    	if(fn>90F)
    		pitch = fn - 360F;
    	if(fn<90F)
    		pitch = fn;   	
    	super.mesh.chunkSetAngles("baseflir", 0.0F, -pitch, roll);
    	//super.mesh.chunkSetAngles("baseflir2", -antiroll, 0.0F, 0.0F);
    	//HUD.log(AircraftHotKeys.hudLogWeaponId, "roll " + 0.0F);
        float f = orient.getYaw() - orient2.getYaw() + 90F;
        if(f > 360F)
            f -= 360F;
        else
        if(f < 0.0F)
            f += 360F;
        float f1 = f;       
        if(f > 90F && f <= 180F)
            f = (float)Math.sqrt(Math.pow(f - 180F, 2D));
        else
        if(f > 180F && f <= 270F)
            f -= 180F;
        else
        if(f > 270F && f <= 360F)
            f = (float)Math.sqrt(Math.pow(f - 360F, 2D));
        float f2 = orient.getPitch() - (orient2.getPitch())*0.01F - 270F;        
        if(f2 > 360F)
            f2 -= 360F;
        else
        if(f2 < 0.0F)
            f2 += 360F;
        f2 *= 0.01745329F;
        f *= 0.01745329F;
        double d = Math.tan(f2) * point3d.z;
        dY = Math.sin(f) * d;
        dX = Math.cos(f) * d;
        float aa = ((F_18)aircraft()).azimult;
        float ta = ((F_18)aircraft()).tangate;
        if(f1 > 0.0F && f1 <= 90F)
            spot1.set(point3d.x + dY, point3d.y + dX, 0.0D);
        else
        if(f1 > 90F && f1 <= 180F)
            spot1.set(point3d.x + dY, point3d.y - dX, 0.0D);
        else
        if(f1 > 180F && f1 <= 270F)
            spot1.set(point3d.x - dY, point3d.y - dX, 0.0D);
        else
            spot1.set(point3d.x - dY, point3d.y + dX, 0.0D);              
        float y = 0F;
        float t = 0F;
        Point3d laser = new Point3d();
    	laser.set(((F_18)aircraft()).spot);
    	laser.sub(point3d);        	
        double d1 = ((Tuple3d) ((Point3d)laser)).y;
        double d2 = ((Tuple3d) ((Point3d)laser)).x;
        double d3 = ((Tuple3d) ((Point3d)laser)).z;
        double radius = Math.abs(Math.sqrt(d1*d1 + d2*d2));
        t = 267.8F - (float)Math.toDegrees(Math.atan(radius/d3)) + 1.9F;
        float te = 0F;
        float x = orient2.getYaw();
        if(x<=0F)
        	te = 180F + x;
        if(x>0)
        	te = x + 180F;            
        if(f1 > 90F && f1 <= 180F)
        y = -(float)Math.toDegrees(Math.atan(d1/d2)) + orient2.getYaw(); else
        if(f1 > 180F && f1 <= 270F)
        y = -(float)Math.toDegrees(Math.atan(d1/d2)) + te; else
        if(f1 > 270F && f1 <= 360F)
        y = -(float)Math.toDegrees(Math.atan(d1/d2)) + te; else
        if(f1 > 0F && f1 <= 90F)	
        y = -(float)Math.toDegrees(Math.atan(d1/d2)) + orient2.getYaw();
        if(y > 100F && y < 180F)
        	y = 100F;
        if(y < 260F && y > 180F)
        	y = 260F;
        
        if(!((F_18)aircraft()).hold)
        {          
           ((F_18)aircraft()).spot.set(spot1);          
           this.v = 0F;
           this.h = 0F;
           this.v1 = 0F;
           this.h1 = 0F;
           ((F_18)aircraft()).azimult = 0F;
           ((F_18)aircraft()).tangate = 0F;
        }
        if(((F_18)aircraft()).hold)
        {
        	((Tuple3d) ((Point3d)((F_18)aircraft()).spot)).y += -ta;
        	((Tuple3d) ((Point3d)((F_18)aircraft()).spot)).x += aa;
            autotrack.clear();
            List list = Engine.targets();
            int i = list.size();                   
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy()) //basically tell that target is not your own aircraft and not friendly aircraft
                {                       	                      	
                	Point3d pointOrtho = new Point3d();
                    pointOrtho.set(actor.pos.getAbsPoint());                          
                    if(((Tuple3d) (pointOrtho)).x > ((F_18)aircraft()).spot.x - 10D && ((Tuple3d) (pointOrtho)).x < ((F_18)aircraft()).spot.x + 10D && ((Tuple3d) (pointOrtho)).y < ((F_18)aircraft()).spot.y + 10D && ((Tuple3d) (pointOrtho)).y > ((F_18)aircraft()).spot.y - 10D)
                    {
                    	autotrack.add(pointOrtho); 
                    	((F_18)aircraft()).spot.set(pointOrtho);
                    	
                    }                                       
                }               	
            }
            int l = autotrack.size();
            Point3d ground = new Point3d();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Tracking " + l);
            if(l > 0)
            {	
            ground = (Point3d) autotrack.get(0);
            //((F_18)aircraft()).spot.set(ground);
            }
        }
        super.mesh.chunkSetAngles("Turret1A", y, 180F, 180F);
        super.mesh.chunkSetAngles("Turret1B", 180F, -t, 180F);              
    }
    
    private ArrayList autotrack = new ArrayList();

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = Math.abs(f);
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(f1 > 0.0F)
                f1 = 0.0F;
            if(f1 < -95F)
                f1 = -95F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitF18FLIR()
    {
        super("3DO/Cockpit/AV8FLIR/AV8FLIR.him", "he111_gunner");
        dY = 0.0D;
        dX = 0.0D;
        point3d1 = new Point3d();
        orient1 = new Orient();
        spot1 = new Point3d();
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        h = 0;
        v = 0;
    }

    
    private double dY;
    private double dX;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    public Point3d point3d1;
    public Orient orient1;
    public Point3d spot1;
    public Point3d spot2;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitF18FLIR.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitF18FLIR.class, "astatePilotIndx", 0);
    }
}