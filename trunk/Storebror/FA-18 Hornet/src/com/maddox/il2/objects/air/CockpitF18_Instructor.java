package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitF18_Instructor extends CockpitGunner
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            setNew.throttlel = (10F * setOld.throttlel + ((FlightModelMain) (fm)).EI.engines[0].getControlThrottle()) / 11F;
            setNew.throttler = (10F * setOld.throttler + ((FlightModelMain) (fm)).EI.engines[1].getControlThrottle()) / 11F;
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

        float throttlel;
        float throttler;
        AnglesFork azimuth;
        float vspeed;
        float dimPosition;

        private Variables()
        {
            azimuth = new AnglesFork();
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

    public CockpitF18_Instructor()
    {
        super("3DO/Cockpit/F-18_Instructor/hier.him", "he111_gunner");
        setOld = new Variables(null);
        setNew = new Variables(null);
        w = new Vector3f();
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

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private boolean bNeedSetUp;

    static 
    {
    	Class class1 = CockpitF18_Instructor.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 1);
    }






}