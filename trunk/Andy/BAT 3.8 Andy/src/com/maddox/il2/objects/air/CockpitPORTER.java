// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 11.01.2020 10:20:27
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: fullnames 
// Source File Name:   CockpitPORTER.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit

public class CockpitPORTER extends com.maddox.il2.objects.air.CockpitPilot
{
    class Interpolater extends com.maddox.il2.engine.InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.altimeter = fm.getAltitude();
                w.set(fm.getW());
                ((com.maddox.il2.fm.FlightModelMain) (fm)).Or.transform(w);
                setNew.turn = (12F * setOld.turn + ((com.maddox.JGP.Tuple3f) (w)).z) / 13F;
                setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
                setNew.throttle = 0.85F * setOld.throttle + ((com.maddox.il2.fm.FlightModelMain) (fm)).CT.PowerControl * 0.15F;
                if(java.lang.Math.abs(((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getKren()) < 45F)
                    setNew.azimuth = (35F * setOld.azimuth + -((com.maddox.il2.fm.FlightModelMain) (fm)).Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.waypointAzimuth = (10F * setOld.waypointAzimuth + (waypointAzimuth() - setOld.azimuth) + com.maddox.il2.ai.World.Rnd().nextFloat(-30F, 30F)) / 11F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float throttle;
        float prop;
        float turn;
        float mix;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables()
        {
        }

    }


    public CockpitPORTER()
    {
        super("3DO/Cockpit/PORTER/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        pictGear = 0.0F;
        pictFlap = 0.0F;
        bNeedSetUp = true;
        super.cockpitNightMats = (new java.lang.String[] {
            "Compass", "gauge1", "gauge2", "gauge3", "gauge4", "gauge5", "Instrumentos002"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, com.maddox.rts.Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        super.mesh.chunkSetAngles("Z_Acelerador", 0.0F, 0.0F, -70F * setNew.throttle);
        super.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, 3F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getControlRadiator());
        super.mesh.chunkSetAngles("Z_Palanca", 0.0F, (pictAiler = 0.85F * pictAiler + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.AileronControl) * 20F, (pictElev = 0.85F * pictElev + 0.15F * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).CT.ElevatorControl) * 10F);
        resetYPRmodifier();
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() > 0 && ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getStage() < 3)
            com.maddox.il2.objects.air.Cockpit.xyz[1] = 0.02825F;
        super.mesh.chunkSetAngles("NeedManPress", 0.0F, pictManifold = 0.85F * pictManifold + 0.15F * cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getManifoldPressure() * 760F, 260F, 1200F, 33.3F, 360F), 0.0F);
        super.mesh.chunkSetAngles("NeedAlt_Km", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 10000F, 0.0F, 450F), 0.0F);
        float f1 = com.maddox.il2.fm.Pitot.Indicator((float)((com.maddox.JGP.Tuple3d) (((com.maddox.il2.fm.FlightModelMain) (super.fm)).Loc)).z, super.fm.getSpeedKMH());
        if(f1 < 100F)
            super.mesh.chunkSetAngles("NeedSpeed", 0.0F, -cvt(f1, 0.0F, 100F, 0.0F, -28.4F), 0.0F);
        else
        if(f1 < 200F)
            super.mesh.chunkSetAngles("NeedSpeed", 0.0F, -cvt(f1, 100F, 200F, -28.4F, -102F), 0.0F);
        else
        if(f1 < 300F)
            super.mesh.chunkSetAngles("NeedSpeed", 0.0F, -cvt(f1, 200F, 300F, -102F, -191.5F), 0.0F);
        else
            super.mesh.chunkSetAngles("NeedSpeed", 0.0F, -cvt(f1, 300F, 450F, -191.5F, -326F), 0.0F);
        super.mesh.chunkSetAngles("NeedFuel", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 250F, 0.0F, 255.5F), 0.0F);
        super.mesh.chunkSetAngles("NeedFuel", 0.0F, cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).M.fuel, 0.0F, 250F, 0.0F, 255.5F), 0.0F);
        float f2 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut;
        if(f2 < 20F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 0.0F, 20F, 0.0F, 15F), 0.0F);
        else
        if(f2 < 40F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 20F, 40F, 15F, 50F), 0.0F);
        else
        if(f2 < 60F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 40F, 60F, 50F, 102.5F), 0.0F);
        else
        if(f2 < 80F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 60F, 80F, 102.5F, 186F), 0.0F);
        else
        if(f2 < 100F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 80F, 100F, 186F, 283F), 0.0F);
        else
        if(f2 < 120F)
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 100F, 120F, 283F, 314F), 0.0F);
        else
            super.mesh.chunkSetAngles("NeedOilTemp", 0.0F, cvt(f2, 120F, 140F, 314F, 345F), 0.0F);
        f2 = ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getRPM();
        super.mesh.chunkSetAngles("NeedRPM", 0.0F, cvt(f2, 0.0F, 3000F, 0.0F, 310F), 0.0F);
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren() < -110F || ((com.maddox.il2.fm.FlightModelMain) (super.fm)).Or.getKren() > 110F)
            rpmGeneratedPressure = rpmGeneratedPressure - 0.5F;
        else
        if(f2 < rpmGeneratedPressure)
            rpmGeneratedPressure = rpmGeneratedPressure - (rpmGeneratedPressure - f2) * 0.01F;
        else
            rpmGeneratedPressure = rpmGeneratedPressure + (f2 - rpmGeneratedPressure) * 0.001F;
        if(rpmGeneratedPressure < 600F)
            oilPressure = cvt(rpmGeneratedPressure, 0.0F, 600F, 0.0F, 4F);
        else
        if(rpmGeneratedPressure < 900F)
            oilPressure = cvt(rpmGeneratedPressure, 600F, 900F, 4F, 7F);
        else
            oilPressure = cvt(rpmGeneratedPressure, 900F, 1200F, 7F, 10F);
        float f3 = 0.0F;
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut > 90F)
            f3 = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 90F, 120F, 1.1F, 1.5F);
        else
        if(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut < 50F)
            f3 = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 0.0F, 50F, 1.5F, 0.9F);
        else
            f3 = cvt(((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        float f4 = f3 * ((com.maddox.il2.fm.FlightModelMain) (super.fm)).EI.engines[0].getReadyness() * oilPressure;
        if(f4 < 12F)
            super.mesh.chunkSetAngles("NeedOilPress", 0.0F, cvt(f4, 0.0F, 12F, 0.0F, 230F), 0.0F);
        else
        if(f4 < 16F)
            super.mesh.chunkSetAngles("NeedOilPress", 0.0F, cvt(f4, 12F, 16F, 230F, 285F), 0.0F);
        else
            super.mesh.chunkSetAngles("NeedOilPress", 0.0F, cvt(f4, 16F, 32F, 285F, 320F), 0.0F);
        super.mesh.chunkSetAngles("NeedFuelPress", 0.0F, cvt(rpmGeneratedPressure, 0.0F, 1200F, 0.0F, 260F), 0.0F);
        super.mesh.chunkSetAngles("Z_Suction1", 0.0F, cvt(f4, 16F, 32F, 285F, 320F), 0.0F);
        super.mesh.chunkSetAngles("NeedBank", 0.0F, cvt(setNew.turn, -0.2F, 0.2F, 22.5F, -22.5F), 0.0F);
        super.mesh.chunkSetAngles("NeedTurn", 0.0F, -cvt(getBall(8D), -8F, 8F, 9.5F, -9.5F), 0.0F);
        super.mesh.chunkSetAngles("NeedClimb", 0.0F, -cvt(setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F);
        super.mesh.chunkSetAngles("Z_Compass1", 0.0F, -interp(setNew.azimuth, setOld.azimuth, f), 0.0F);
        super.mesh.chunkSetAngles("Z_Hora", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        super.mesh.chunkSetAngles("Z_Minuto", 0.0F, cvt(com.maddox.il2.ai.World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
    }

    protected void reflectPlaneMats()
    {
        com.maddox.il2.engine.HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected void reflectPlaneToModel()
    {
    }

    public void toggleLight()
    {
        super.cockpitLightControl = !super.cockpitLightControl;
        if(super.cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    private void retoggleLight()
    {
        if(super.cockpitLightControl)
        {
            setNightMats(false);
            setNightMats(true);
        } else
        {
            setNightMats(true);
            setNightMats(false);
        }
    }

    private com.maddox.il2.objects.air.Variables setOld;
    private com.maddox.il2.objects.air.Variables setNew;
    private com.maddox.il2.objects.air.Variables setTmp;
    private com.maddox.JGP.Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float pictGear;
    private float pictFlap;
    private boolean bNeedSetUp;
    private float pictSupc;
    private float pictManifold;
    private float rpmGeneratedPressure;
    private float oilPressure;







}