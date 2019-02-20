package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.rts.Property;

public class Huey_GunshipAI extends HueyX
    implements TypeScout, TypeTransport, TypeStormovik
{

    public Huey_GunshipAI()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(World.Sun().ToSun.z < -0.22F)
            this.FM.AS.setNavLightsState(true);
        else
            this.FM.AS.setNavLightsState(false);
    }

    private void stability()
    {
        double d = 0.0D;
        Vector3d vector3d = new Vector3d();
        getSpeed(vector3d);
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        float f = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
        if(f < 10F && FM.getSpeedKMH() < 60F && vector3d.z < -1D)
        {
            vector3d.z *= 0.90000000000000002D;
            setSpeed(vector3d);
        }
        if(this.FM.Gears.nOfGearsOnGr > 2)
        {
            vector3d.x *= 0.98999999999999999D;
            vector3d.y *= 0.98999999999999999D;
            setSpeed(vector3d);
        }
        if(FM.getSpeedKMH() > 180F)
        {
            d = (FM.getSpeedKMH() - FM.VmaxFLAPS) / 10F;
            if(d < 0.0D)
                d = 0.0D;
        }
        Point3d point3d1 = new Point3d(0.0D, 0.0D, 0.0D);
        point3d1.x = 0.0D - ((double)(FM.Or.getTangage() / 10F) - (double)FM.CT.getElevator() * 2.5D);
        point3d1.y = 0.0D - ((double)(FM.Or.getKren() / 10F) - (double)FM.CT.getAileron() * 2.5D) - d;
        point3d1.z = 2D;
        FM.EI.engines[0].setPropPos(point3d1);
        FM.producedAF.x += 7000D * (double)(-FM.CT.getElevator() * FM.EI.engines[0].getPowerOutput());
        FM.producedAF.y += 6000D * (double)(-FM.CT.getAileron() * FM.EI.engines[0].getPowerOutput());
    }

    public void update(float f)
    {
        tiltRotor(f);
        stability();
        if(((Pilot)this.FM).get_maneuver() == 25 && this.FM.AP.way.isLandingOnShip() && this.FM.Gears.nOfGearsOnGr >= 3)
            this.FM.CT.BrakeControl = 1.0F;
        super.update(f);
        float f1 = FM.EI.getPowerOutput() * Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 600F, 2.0F, 0.0F);
        if(FM.CT.getAirBrake() > 0.5F)
        {
            if(FM.Or.getTangage() > 5F)
            {
                FM.getW().scale(Aircraft.cvt(FM.Or.getTangage(), 45F, 90F, 1.0F, 0.1F));
                float f2 = FM.Or.getTangage();
                if(Math.abs(FM.Or.getKren()) > 90F)
                    f2 = 90F + (90F - f2);
                float f3 = f2 - 90F;
                FM.CT.trimElevator = Aircraft.cvt(f3, -20F, 20F, 0.5F, -0.5F);
                f3 = FM.Or.getKren();
                if(Math.abs(f3) > 90F)
                    if(f3 > 0.0F)
                        f3 = 180F - f3;
                    else
                        f3 = -180F - f3;
                FM.CT.trimAileron = Aircraft.cvt(f3, -20F, 20F, 0.5F, -0.5F);
                FM.CT.trimRudder = Aircraft.cvt(f3, -15F, 15F, 0.04F, -0.04F);
            }
        } else
        {
            FM.CT.trimAileron = 0.0F;
            FM.CT.trimElevator = 0.0F;
            FM.CT.trimRudder = 0.0F;
        }
        FM.Or.increment(f1 * (FM.CT.getRudder() + FM.CT.getTrimRudderControl()), f1 * (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()), f1 * (FM.CT.getAileron() + FM.CT.getTrimAileronControl()));
    }

    static 
    {
        Class class1 = Huey_GunshipAI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HueyAI");
        Property.set(class1, "meshName", "3DO/Plane/AIHuey/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1956F);
        Property.set(class1, "yearExpired", 1988.5F);
        Property.set(class1, "FlightModel", "FlightModels/OCXHueyAI.fmd:OCXHueyAI_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 9, 9, 9, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 9, 9, 9, 9, 9, 9, 0, 
            0, 0, 0, 9, 9, 1, 1, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", 
            "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", 
            "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_MGUN03", 
            "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev10", "_ExternalDev11", "_MGUN07", "_MGUN08", "_ExternalDev12"
        });
    }
}
