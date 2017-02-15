
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, F_14, Cockpit, TypeLaserSpotter, 
//            Aircraft

public class CockpitF_14FLIR extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((F_14)aircraft()).FLIR = true;
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
        ((F_14)aircraft()).FLIR = false;
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        orient1.set(orient);
        laserTrack(orient);
        instrument(orient);
    }

    private void instrument(Orient orient)
    {
        if(!((F_14)aircraft()).Nvision)
        {
            super.mesh.chunkVisible("Screen", false);
            super.mesh.chunkVisible("Dark", true);
        } else
        {
            super.mesh.chunkVisible("Screen", true);
            super.mesh.chunkVisible("Dark", false);
        }
        float f = cvt(orient.getYaw(), -120F, 120F, -0.25F, 0.25F);
        resetYPRmodifier();
        Cockpit.xyz[0] = f;
        super.mesh.chunkSetLocate("Z_Z_FLIR_HDG", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float f1 = cvt(orient.getPitch(), 270F, 360F, -0.5F, 0.0F);
        Cockpit.xyz[1] = f1;
        super.mesh.chunkSetLocate("Z_Z_FLIR_VERT", Cockpit.xyz, Cockpit.ypr);
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_14)aircraft()).pos.getAbs(point3d, orient2);
        float f2 = orient2.getRoll();
        float f3 = 360F - f2;
        if(f3 > 180F)
            f3 -= 360F;
        float f4 = orient2.getPitch();
        float f5 = orient.getPitch();
        if(f4 > 90F)
            f5 = orient.getPitch() - 360F;
        if(f4 < 90F)
            f5 = orient.getPitch();
        if(f4 > 90F)
            f1 = (f4 - 360F) + f5;
        if(f4 < 90F)
            f1 = f4 - f5;
        super.mesh.chunkSetAngles("Z_Z_FLIR_BRG", 0.0F, f3, 0.0F);
        resetYPRmodifier();
        f1 = cvt(f1, 0.0F, -180F, 0.0F, 0.15F);
        Cockpit.xyz[1] = f1;
        super.mesh.chunkSetLocate("Z_Z_FLIR_BRG2", Cockpit.xyz, Cockpit.ypr);
    }

    public void laserTrack(Orient orient)
    {
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_14)aircraft()).pos.getAbs(point3d, orient2);
        float f = orient2.getRoll();
        float f1 = orient2.getPitch();
        float f2 = 0.0F;
        if(f1 > 90F)
            f2 = f1 - 360F;
        if(f1 < 90F)
            f2 = f1;
        super.mesh.chunkSetAngles("baseflir", 0.0F, -f2, f);
        if(!((F_14)aircraft()).hold)
        {
            LaserHook[1] = new HookNamed(mesh, "_Laser1");
            ((F_14)aircraft()).pos.getRender(Actor._tmpLoc);
            LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP1);
            LaserLoc1.set(40000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP2);
            Engine.land();
            if(Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL))
            {
                LaserPL.z -= 0.94999999999999996D;
                LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
                ((TypeLaserSpotter)((F_14)aircraft())).spot.set(LaserP2);
                Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            }
            super.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
            super.mesh.chunkSetAngles("Turret1B", 180F, -orient.getPitch(), 180F);
            super.mesh.chunkVisible("Z_Z_FLIR_Lock", false);
        } else
        if(((F_14)aircraft()).hold)
        {
            super.mesh.chunkVisible("Z_Z_FLIR_Lock", true);
            LaserP3.x = LaserP2.x;
            LaserP3.y = LaserP2.y;
            LaserP3.z = LaserP2.z;
            Eff3DActor eff3dactor1 = Eff3DActor.New(null, null, new Loc(LaserP3.x, LaserP3.y, LaserP3.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            autotrack.clear();
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                {
                    Point3d point3d4 = new Point3d();
                    point3d4.set(actor.pos.getAbsPoint());
                    if(((Tuple3d) (point3d4)).x > ((TypeLaserSpotter)((F_14)aircraft())).spot.x - 8D)
                    {
                        if(((Tuple3d) (point3d4)).x < ((TypeLaserSpotter)((F_14)aircraft())).spot.x + 8D)
                        {
                            if(((Tuple3d) (point3d4)).y < ((TypeLaserSpotter)((F_14)aircraft())).spot.y + 8D)
                            {
                                if(((Tuple3d) (point3d4)).y > ((TypeLaserSpotter)((F_14)aircraft())).spot.y - 8D)
                                    autotrack.add(point3d4);
                            }
                        }
                    }
                }
            }

            if(autotrack.size() > 0)
            {
                super.mesh.chunkVisible("Z_Z_FLIR_Tracking", true);
                Point3d point3d2 = new Point3d();
                point3d2.set((Point3d)autotrack.get(0));
                ((TypeLaserSpotter)((F_14)aircraft())).spot.set(point3d2);
            } else
            {
                super.mesh.chunkVisible("Z_Z_FLIR_Tracking", false);
            }
            Point3d point3d3 = new Point3d();
            point3d3.set(((TypeLaserSpotter)((F_14)aircraft())).spot);
            point3d3.sub(point3d);
            double d = ((Tuple3d) (point3d3)).y;
            double d1 = ((Tuple3d) (point3d3)).x;
            double d2 = ((Tuple3d) (point3d3)).z;
            double d3 = Math.abs(Math.sqrt(d * d + d1 * d1));
            float f3 = (orient.getYaw() - orient2.getYaw()) + 90F;
            if(f3 > 360F)
                f3 -= 360F;
            else
            if(f3 < 0.0F)
                f3 += 360F;
            float f4 = f3;
            float f5 = 270.8F - (float)Math.toDegrees(Math.atan(d3 / d2));
            float f6 = 0.0F;
            float f7 = orient2.getYaw();
            if(f7 <= 0.0F)
                f6 = 180F + f7;
            if(f7 > 0.0F)
                f6 = f7 + 180F;
            float f8 = 0.0F;
            if(f4 > 90F && f4 <= 180F)
                f8 = -(float)Math.toDegrees(Math.atan(d / d1)) + orient2.getYaw();
            else
            if(f4 > 180F && f4 <= 270F)
                f8 = -(float)Math.toDegrees(Math.atan(d / d1)) + f6;
            else
            if(f4 > 270F && f4 <= 360F)
                f8 = -(float)Math.toDegrees(Math.atan(d / d1)) + f6;
            else
            if(f4 > 0.0F && f4 <= 90F)
                f8 = -(float)Math.toDegrees(Math.atan(d / d1)) + orient2.getYaw();
            super.mesh.chunkSetAngles("Turret1A", f8, 180F, 180F);
            super.mesh.chunkSetAngles("Turret1B", 180F, -f5, 180F);
        }
    }

    public void laser(Orient orient)
    {
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_14)aircraft()).pos.getAbs(point3d, orient2);
        float f = orient2.getRoll();
        float f1 = orient2.getPitch();
        float f2 = 360F - f;
        if(f2 > 180F)
            f2 -= 360F;
        float f3 = cvt(f2, -90F, 90F, -1.001F, 1.001F);
        float f4 = 0.0F;
        if(f1 > 90F)
            f4 = f1 - 360F;
        if(f1 < 90F)
            f4 = f1;
        super.mesh.chunkSetAngles("baseflir", 0.0F, -f4, f);
        float f5 = (orient.getYaw() - orient2.getYaw()) + 90F + f3 * f4 * 10F;
        if(f5 > 360F)
            f5 -= 360F;
        else
        if(f5 < 0.0F)
            f5 += 360F;
        float f6 = f5;
        if(f5 > 90F && f5 <= 180F)
            f5 = (float)Math.sqrt(Math.pow(f5 - 180F, 2D));
        else
        if(f5 > 180F && f5 <= 270F)
            f5 -= 180F;
        else
        if(f5 > 270F && f5 <= 360F)
            f5 = (float)Math.sqrt(Math.pow(f5 - 360F, 2D));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "roll " + f3 * f4 * 10F + " " + f5);
        float f7 = orient.getPitch() - orient2.getPitch() * 0.01F - 270F;
        if(f7 > 360F)
            f7 -= 360F;
        else
        if(f7 < 0.0F)
            f7 += 360F;
        f7 *= 0.01745329F;
        f5 *= 0.01745329F;
        double d = point3d.z - 0.5D;
        double d1 = point3d.x + 2D;
        double d2 = Math.tan(f7) * (d - 0.5D);
        dY = Math.sin(f5) * d2;
        dX = Math.cos(f5) * d2;
        float f8 = ((F_14)aircraft()).azimult;
        float f9 = ((F_14)aircraft()).tangate;
        if(f6 > 0.0F && f6 <= 90F)
            spot1.set(d1 + dY, point3d.y + dX, 0.0D);
        else
        if(f6 > 90F && f6 <= 180F)
            spot1.set(d1 + dY, point3d.y - dX, 0.0D);
        else
        if(f6 > 180F && f6 <= 270F)
            spot1.set(d1 - dY, point3d.y - dX, 0.0D);
        else
            spot1.set(d1 - dY, point3d.y + dX, 0.0D);
        float f10 = 0.0F;
        float f11 = 0.0F;
        Point3d point3d2 = new Point3d();
        point3d2.set(((TypeLaserSpotter)((F_14)aircraft())).spot);
        point3d2.sub(point3d);
        double d3 = ((Tuple3d) (point3d2)).y;
        double d4 = ((Tuple3d) (point3d2)).x;
        double d5 = ((Tuple3d) (point3d2)).z;
        double d6 = Math.abs(Math.sqrt(d3 * d3 + d4 * d4));
        f11 = (267.8F - (float)Math.toDegrees(Math.atan(d6 / d5))) + 1.9F;
        float f12 = 0.0F;
        float f13 = orient2.getYaw();
        if(f13 <= 0.0F)
            f12 = 180F + f13;
        if(f13 > 0.0F)
            f12 = f13 + 180F;
        if(f6 > 90F && f6 <= 180F)
            f10 = -(float)Math.toDegrees(Math.atan(d3 / d4)) + orient2.getYaw();
        else
        if(f6 > 180F && f6 <= 270F)
            f10 = -(float)Math.toDegrees(Math.atan(d3 / d4)) + f12;
        else
        if(f6 > 270F && f6 <= 360F)
            f10 = -(float)Math.toDegrees(Math.atan(d3 / d4)) + f12;
        else
        if(f6 > 0.0F && f6 <= 90F)
            f10 = -(float)Math.toDegrees(Math.atan(d3 / d4)) + orient2.getYaw();
        if(f10 > 100F && f10 < 180F)
            f10 = 100F;
        if(f10 < 260F && f10 > 180F)
            f10 = 260F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "x " + Math.round(spot1.x) + "y " + Math.round(spot1.y) + "z " + Math.round(spot1.z));
        if(!((F_14)aircraft()).hold)
        {
            ((TypeLaserSpotter)((F_14)aircraft())).spot.set(spot1);
            v = 0.0F;
            h = 0.0F;
            v1 = 0.0F;
            h1 = 0.0F;
            ((F_14)aircraft()).azimult = 0.0F;
            ((F_14)aircraft()).tangate = 0.0F;
        }
        if(((F_14)aircraft()).hold)
        {
            ((TypeLaserSpotter)((F_14)aircraft())).spot.y += -f9;
            ((TypeLaserSpotter)((F_14)aircraft())).spot.x += f8;
            autotrack.clear();
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric) || (actor instanceof BridgeSegment)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                {
                    Point3d point3d3 = new Point3d();
                    point3d3.set(actor.pos.getAbsPoint());
                    if(((Tuple3d) (point3d3)).x > ((TypeLaserSpotter)((F_14)aircraft())).spot.x - 10D)
                    {
                        if(((Tuple3d) (point3d3)).x < ((TypeLaserSpotter)((F_14)aircraft())).spot.x + 10D)
                        {
                            if(((Tuple3d) (point3d3)).y < ((TypeLaserSpotter)((F_14)aircraft())).spot.y + 10D)
                            {
                                if(((Tuple3d) (point3d3)).y > ((TypeLaserSpotter)((F_14)aircraft())).spot.y - 10D)
                                {
                                    autotrack.add(point3d3);
                                    ((TypeLaserSpotter)((F_14)aircraft())).spot.set(point3d3);
                                }
                            }
                        }
                    }
                }
            }

        }
        super.mesh.chunkSetAngles("Turret1A", f10, 180F, 180F);
        super.mesh.chunkSetAngles("Turret1B", 180F, -f11, 180F);
    }

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

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public boolean isEnableFocusing()
    {
        if(aircraft().thisWeaponsName.startsWith("GAttack:") || aircraft().thisWeaponsName.startsWith("Fighter:") || aircraft().thisWeaponsName.startsWith("Recon:") || aircraft().thisWeaponsName.startsWith("Def"))
            return false;
        else
            return super.isEnableFocusing();
    }

    public CockpitF_14FLIR()
    {
        super("3DO/Cockpit/F-14FLIR/F-14FLIR.him", "he111_gunner");
        autotrack = new ArrayList();
        dY = 0.0D;
        dX = 0.0D;
        point3d1 = new Point3d();
        orient1 = new Orient();
        spot1 = new Point3d();
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        h = 0.0F;
        v = 0.0F;
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

    private float h;
    private float v;
    private float v1;
    private float h1;
    private ArrayList autotrack;
    private double dY;
    private double dX;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    public Point3d point3d1;
    public Orient orient1;
    public Point3d spot1;
    public Point3d spot2;
    private Hook LaserHook[] = {
        null, null, null, null
    };
    private Loc LaserLoc1 = new Loc();
    private Point3d LaserP1 = new Point3d();
    private Point3d LaserP2 = new Point3d();
    private Point3d LaserPL = new Point3d();
    private Point3d LaserP3 = new Point3d();

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitF_14FLIR.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitF_14FLIR.class, "astatePilotIndx", 0);
    }
}