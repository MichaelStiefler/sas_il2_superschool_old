package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitKI_49_II_AGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!isFocused()) {
            return;
        } else {
            aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        mesh.materialReplace("Matt1D0o", mat);
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("Turret2A", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret2B", 0.0F, f1, 0.0F);
        if (Math.abs(f) > 2.0F || Math.abs(f1) > 2.0F) {
            a2 = (float) Math.toDegrees(Math.atan2(f, f1));
            a2 *= cvt(f1, 0.0F, 55F, 1.0F, 0.75F);
        }
        if (f < -33F)
            f = -33F;
        if (f > 33F)
            f = 33F;
        if (f1 < -23F)
            f1 = -23F;
        if (f1 > 33F)
            f1 = 33F;
        mesh.chunkSetAngles("Turret2C", 0.0F, f, 0.0F);
        mesh.chunkSetAngles("Turret2D", 0.0F, f1, 0.0F);
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
        if (f < -45F)
            f = -45F;
        if (f > 45F)
            f = 45F;
        if (f1 > 25F)
            f1 = 25F;
        if (f1 < -45F)
            f1 = -45F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!isRealMode())
            return;
        if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        cockPos = 0.5F * cockPos + 0.5F * a2;
        mesh.chunkSetAngles("Turret2E", 0.0F, cockPos, 0.0F);
        mesh.chunkSetAngles("Rudder1_D0", 0.0F, -30F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("VatorL_D0", 0.0F, -30F * fm.CT.getElevator(), 0.0F);
        mesh.chunkSetAngles("VatorR_D0", 0.0F, -30F * fm.CT.getElevator(), 0.0F);
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

    public CockpitKI_49_II_AGunner() {
        super("3DO/Cockpit/Ki49-AGun/hier.him", "he111");
        bNeedSetUp = true;
        cockPos = 0.0F;
        a2 = 0.0F;
    }

    public void reflectWorldToInstruments(float f) {
        if (bNeedSetUp) {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
    }

    public void reflectCockpitState() {
    }

    private boolean bNeedSetUp;
    private float   cockPos;
    private float   a2;

    static {
        Class class1 = CockpitKI_49_II_AGunner.class;
        Property.set(class1, "aiTuretNum", 3);
        Property.set(class1, "weaponControlNum", 13);
        Property.set(class1, "astatePilotIndx", 5);
    }
}
