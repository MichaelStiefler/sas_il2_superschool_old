package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitKI_49_II_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!isFocused()) {
            return;
        } else {
            aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage() - 9F;
        mesh.chunkSetAngles("Turret3A", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret3B", 0.0F, f1, 0.0F);
        if (f < -33F)
            f = -33F;
        if (f > 33F)
            f = 33F;
        mesh.chunkSetAngles("Turret3D", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("Turret3E", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!isRealMode())
            return;
        if (!aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -50F)
            f = -50F;
        if (f > 50F)
            f = 50F;
        if (f1 > cvt(Math.abs(f), 0.0F, 50F, 40F, 25F))
            f1 = cvt(Math.abs(f), 0.0F, 50F, 40F, 25F);
        if (f1 < cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F))
            f1 = cvt(Math.abs(f), 0.0F, 50F, -10F, -3.5F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!isRealMode())
            return;
        if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if (bGunFire) {
            if (iCocking > 0)
                iCocking = 0;
            else
                iCocking = 1;
        } else {
            iCocking = 0;
        }
        resetYPRmodifier();
        xyz[1] = -0.07F * (float) iCocking;
        ypr[1] = 0.0F;
        mesh.chunkSetLocate("Turret3C", xyz, ypr);
    }

    public void doGunFire(boolean flag) {
        if (!isRealMode())
            return;
        if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public CockpitKI_49_II_TGunner() {
        super("3DO/Cockpit/Ki49-TGun/hier.him", "he111_gunner");
        iCocking = 0;
    }

    private int     iCocking;

    static {
        Property.set(CLASS.THIS(), "aiTuretNum", 1);
        Property.set(CLASS.THIS(), "weaponControlNum", 11);
        Property.set(CLASS.THIS(), "astatePilotIndx", 3);
    }
}
