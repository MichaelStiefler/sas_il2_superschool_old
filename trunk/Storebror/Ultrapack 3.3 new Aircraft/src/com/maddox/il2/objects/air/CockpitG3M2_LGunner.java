package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitG3M2_LGunner extends CockpitGunner
{

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("RearAXX_D0", false);
            if(curMat == null)
            {
                curMat = aircraft().hierMesh().material(aircraft().hierMesh().materialFind("Pilot2"));
                newMat = (Mat)curMat.Clone();
                newMat.setLayer(0);
                newMat.set((short)0, false);
            }
            aircraft().hierMesh().materialReplace("Pilot2", newMat);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().materialReplace("Pilot2", curMat);
            aircraft().hierMesh().chunkVisible("RearAXX_D0", aircraft().isChunkAnyDamageVisible("CF_D"));
            super.doFocusLeave();
            return;
        }
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret5A", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret5B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(f < -30F)
            f = -30F;
        if(f > 60F)
            f = 60F;
        if(f1 < -45F)
            f1 = -45F;
        if(f1 > 45F)
            f1 = 45F;
        if(f < 0.0F)
        {
            if(f1 < cvt(f, -30F, 0.0F, -6F, -23F))
                f1 = cvt(f, -30F, 0.0F, -6F, -23F);
            if(f1 > cvt(f, -30F, 0.0F, 22F, 33F))
                f1 = cvt(f, -30F, 0.0F, 22F, 33F);
        } else
        if(f < 30F)
        {
            if(f1 < cvt(f, 0.0F, 30F, -23F, -16F))
                f1 = cvt(f, 0.0F, 30F, -23F, -16F);
            if(f1 > cvt(f, 0.0F, 10F, 33F, 45F))
                f1 = cvt(f, 0.0F, 10F, 33F, 45F);
        } else
        if(f1 < cvt(f, 30F, 60F, -16F, -10F))
            f1 = cvt(f, 30F, 60F, -16F, -10F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        mesh.chunkSetAngles("Turret4A", 0.0F, -aircraft().FM.turret[3].tu[0], 0.0F);
        mesh.chunkSetAngles("Turret4B", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
        } else
        {
            iCocking = 0;
        }
        resetYPRmodifier();
        Cockpit.xyz[1] = -0.07F * (float)iCocking;
        mesh.chunkSetLocate("Turret5C", Cockpit.xyz, Cockpit.ypr);
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

    public CockpitG3M2_LGunner()
    {
        super("3DO/Cockpit/G3M2-LGun/hier.him", "he111_gunner");
        curMat = null;
        iCocking = 0;
    }

    public void reflectCockpitState()
    {
    }

    Mat curMat;
    Mat newMat;
    private int iCocking;

    static 
    {
        Property.set(CockpitG3M2_LGunner.class, "aiTuretNum", 4);
        Property.set(CockpitG3M2_LGunner.class, "weaponControlNum", 11);
        Property.set(CockpitG3M2_LGunner.class, "astatePilotIndx", 4);
    }
}
