// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 5/5/2013 10:21:17 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitT4_Instructor.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, Cockpit

public class CockpitT4_Instructor extends CockpitGunner
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.altimeter = fm.getAltitude();
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
            float f = waypointAzimuth();
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), ((FlightModelMain) (fm)).Or.azimut());
            setNew.vspeed = (299F * setOld.vspeed + fm.getVertSpeed()) / 300F;
            if(cockpitDimControl)
            {
                if(setNew.dimPosition > 0.0F)
                    setNew.dimPosition = setOld.dimPosition - 0.05F;
            } else
            if(setNew.dimPosition < 1.0F)
                setNew.dimPosition = setOld.dimPosition + 0.05F;
            return true;
        }

        Interpolater()
        {
        }
    }
    
    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Canopy_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            aircraft().hierMesh().chunkVisible("Seat1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            aircraft().hierMesh().chunkVisible("Seat2_D0", false);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Canopy_D0", true);
            aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
            aircraft().hierMesh().chunkVisible("Seat1_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
            aircraft().hierMesh().chunkVisible("Seat2_D0", true);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        super.doFocusLeave();
    }

    private class Variables
    {

        float altimeter;
        float throttlel;
        float throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float vspeed;
        float dimPosition;
        float beaconDirection;
        float beaconRange;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

        Variables(Variables variables)
        {
            this();
        }
    }


    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        super.mesh.chunkSetAngles("InstructBase", 0.0F, 0.0F, 0.0F);
        super.mesh.chunkSetAngles("InstructBase1", -orient.getYaw(), orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient1)
    {
    }

    public CockpitT4_Instructor()
    {
        super("3DO/Cockpit/T-4Instructor/hier.him", "he111_gunner");
        gun = new Gun[4];
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
        tmpP = new Point3d();
        tmpV = new Vector3d();
        bNeedSetUp = true;
        setNew.dimPosition = 1.0F;
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
            bNeedSetUp = false;
    }

    public void reflectCockpitState()
    {
    }

    public void toggleDim()
    {
    }

    public void toggleLight()
    {
    }

    private void retoggleLight()
    {
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private Point3d tmpP;
    private Vector3d tmpV;
    private Gun gun[];
    private boolean bNeedSetUp;

    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitT4_Instructor.class, "aiTuretNum", 0);
        Property.set(com.maddox.il2.objects.air.CockpitT4_Instructor.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitT4_Instructor.class, "astatePilotIndx", 1);
    }






}