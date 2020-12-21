// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 23.10.2020 17:17:05
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitL_5.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Autopilotage;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.*;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitL_5 extends com.maddox.il2.objects.air.CockpitPilot
{
    private class Variables
    {

        float throttle;
        float prop;
        float mix;
        float stage;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables()
        {
        }

    }

    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.throttle = 0.85F * setOld.throttle + fm.CT.PowerControl * 0.15F;
                setNew.prop = 0.85F * setOld.prop + fm.CT.getStepControl() * 0.15F;
                setNew.stage = 0.85F * setOld.stage + (float)fm.EI.engines[0].getControlCompressor() * 0.15F;
                setNew.mix = 0.85F * setOld.mix + fm.EI.engines[0].getControlMix() * 0.15F;
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + waypointAzimuth() + com.maddox.il2.ai.World.Rnd().nextFloat(-30F, 30F)) / 11F;
                setNew.vspeed = (199F * setOld.vspeed + fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        com.maddox.il2.ai.WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
        {
            return 0.0F;
        } else
        {
            waypoint.getP(tmpP);
            tmpV.sub(tmpP, fm.Loc);
            return (float)(57.295779513082323D * java.lang.Math.atan2(-tmpV.y, tmpV.x));
        }
    }

    public CockpitL_5()
    {
        super("3DO/Cockpit/Sentinel/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        pictFlap = 0.0F;
        tmpP = new Point3d();
        tmpV = new Vector3d();
        cockpitNightMats = (new java.lang.String[] {
            "Instrumentos001", "Instrumentos002", "Instrumentos003"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        resetYPRmodifier();
        mesh.chunkSetAngles("Trim_Ele", 0.0F, 0.0F, 722F * fm.CT.getTrimElevatorControl());
        mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * fm.CT.getRudder());
        mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * fm.CT.getRudder());
        mesh.chunkSetAngles("Z_Acelerador", 0.0F, 0.0F, -70F * setNew.throttle);
        mesh.chunkSetAngles("Z_Palanca", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F);
        mesh.chunkSetAngles("z_Flap", 0.0F, 0.0F, 18F * (pictFlap = 0.75F * pictFlap + 0.95F * fm.CT.FlapsControl));
        resetYPRmodifier();
        resetYPRmodifier();
        if(fm.EI.engines[0].getStage() > 0 && fm.EI.engines[0].getStage() < 3)
            com.maddox.il2.objects.air.Cockpit.xyz[1] = 0.02825F;
        mesh.chunkSetLocate("Z_Starter", com.maddox.il2.objects.air.Cockpit.xyz, com.maddox.il2.objects.air.Cockpit.ypr);
        mesh.chunkSetAngles("Z_Magneto", 0.0F, 60F * (float)fm.EI.engines[0].getControlMagnetos(), 0.0F);
        mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, 3F * fm.EI.engines[0].getControlRadiator());
        mesh.chunkSetAngles("Z_Hora", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_Minuto", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_Altimetro1", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        mesh.chunkSetAngles("Z_Altimetro2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("Z_ASI", 0.0F, floatindex(cvt(com.maddox.il2.fm.Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_RPM", 0.0F, cvt(fm.EI.engines[0].getRPM(), 0.0F, 2500F, 0.0F, 504F), 0.0F);
        mesh.chunkSetAngles("Z_Temp1", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 250F, 0.0F, 97.5F), 0.0F);
        mesh.chunkSetAngles("Z_OilTemp", 0.0F, cvt(fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 78F), 0.0F);
        mesh.chunkSetAngles("Z_Oilpres", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilOut * fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
        mesh.chunkSetAngles("Z_Compass", setNew.azimuth, 0.0F, 0.0F);
        mesh.chunkSetAngles("Z_Fuel1", 0.0F, cvt(fm.M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        mesh.chunkSetAngles("Z_Fuel2", 0.0F, cvt(fm.M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        w.set(fm.getW());
        fm.Or.transform(w);
        mesh.chunkSetAngles("Z_Palo", 0.0F, cvt(w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        mesh.chunkSetAngles("Z_Bola", 0.0F, cvt(getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private float pictFlap;
    private com.maddox.il2.objects.weapons.Gun gun[] = {
        null, null, null, null
    };
    private com.maddox.il2.ai.BulletEmitter bgun[] = {
        null, null, null, null
    };
    private com.maddox.il2.ai.BulletEmitter rgun[] = {
        null, null, null, null
    };
    private com.maddox.il2.ai.BulletEmitter tgun[] = {
        null, null
    };
    private static final float speedometerScale[] = {
        0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 
        669.75F, 719F
    };
    private static final float variometerScale[] = {
        -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 
        124F, 147F, 170F
    };
    private com.maddox.JGP.Point3d tmpP;
    private com.maddox.JGP.Vector3d tmpV;



    static
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            0.83F, 0.83F, 0.83F, 0.83F
        });
    }



}