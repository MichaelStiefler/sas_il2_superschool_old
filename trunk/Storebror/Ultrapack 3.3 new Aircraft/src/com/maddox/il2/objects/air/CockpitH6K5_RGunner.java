package com.maddox.il2.objects.air;

import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitH6K5_RGunner extends CockpitGunner
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            mesh.chunkSetAngles("Turret5A", 0.0F, -aircraft().FM.turret[3].tu[0], 0.0F);
            mesh.chunkSetAngles("Turret5B", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
            return true;
        }

        Interpolater()
        {
        }
    }


    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret4A", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret4B", 0.0F, f1, 0.0F);
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
        if(f < -60F)
            f = -60F;
        if(f > 30F)
            f = 30F;
        if(f1 < -45F)
            f1 = -45F;
        if(f1 > 45F)
            f1 = 45F;
        if(f < -30F)
        {
            if(f1 < cvt(f, -60F, -30F, -10F, -16F))
                f1 = cvt(f, -60F, -30F, -10F, -16F);
        } else
        if(f < 0.0F)
        {
            if(f1 < cvt(f, -30F, 0.0F, -16F, -23F))
                f1 = cvt(f, -30F, 0.0F, -16F, -23F);
            if(f1 > cvt(f, -10F, 0.0F, 45F, 33F))
                f1 = cvt(f, -10F, 0.0F, 45F, 33F);
        } else
        {
            if(f1 < cvt(f, 0.0F, 30F, -23F, -6F))
                f1 = cvt(f, 0.0F, 30F, -23F, -6F);
            if(f1 > cvt(f, 0.0F, 30F, 33F, 22F))
                f1 = cvt(f, 0.0F, 30F, 33F, 22F);
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        mesh.chunkSetAngles("Turret5A", 0.0F, -aircraft().FM.turret[4].tu[0], 0.0F);
        mesh.chunkSetAngles("Turret5B", 0.0F, aircraft().FM.turret[4].tu[1], 0.0F);
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
        mesh.chunkSetLocate("Turret4C", Cockpit.xyz, Cockpit.ypr);
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

    public CockpitH6K5_RGunner()
    {
        super("3DO/Cockpit/H6K5-RGun/hier.him", "he111_gunner");
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
        Property.set(CockpitH6K5_RGunner.class, "aiTuretNum", 2);
        Property.set(CockpitH6K5_RGunner.class, "weaponControlNum", 12);
        Property.set(CockpitH6K5_RGunner.class, "astatePilotIndx", 4);
    }
}
