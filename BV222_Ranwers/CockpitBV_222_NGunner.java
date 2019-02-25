
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.*;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner

public class CockpitBV_222_NGunner extends CockpitGunner
{

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret1A", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
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
        if(f < -20F)
            f = -20F;
        if(f > 20F)
            f = 20F;
        if(f1 > 20F)
            f1 = 20F;
        if(f1 < -20F)
            f1 = -20F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
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
        xyz[1] = -0.07F * (float)iCocking;
        ypr[1] = 0.0F;
        mesh.chunkSetLocate("Turret1C", xyz, ypr);
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

    public CockpitBV_222_NGunner()
    {
        super("3DO/Cockpit/G4M1-11-NGun/NGunnerBV222.him", "he111");
        iCocking = 0;
    }

    public void reflectCockpitState()
    {
    }

    private int iCocking;

    static 
    {
        Property.set(CLASS.THIS(), "aiTuretNum", 7);
        Property.set(CLASS.THIS(), "weaponControlNum", 17);
        Property.set(CLASS.THIS(), "astatePilotIndx", 1);
    }
}