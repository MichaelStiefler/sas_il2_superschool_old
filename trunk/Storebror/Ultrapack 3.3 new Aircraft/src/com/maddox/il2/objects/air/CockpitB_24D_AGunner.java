package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB_24D_AGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        float f_0_ = this.fm.CT.getElevator();
        this.mesh.chunkSetAngles("VatorL_D0", 0.0F, -30F * f_0_, 0.0F);
        this.mesh.chunkSetAngles("VatorR_D0", 0.0F, -30F * f_0_, 0.0F);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void reflectCockpitState()
    {
        if((this.fm.AS.astateCockpitState & 8) != 0)
            this.mesh.chunkVisible("XGlassDamage1", true);
        if((this.fm.AS.astateCockpitState & 0x20) != 0)
            this.mesh.chunkVisible("XGlassDamage2", true);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        this.mesh.chunkVisible("VatorL_D0", hiermesh.isChunkVisible("VatorL_D0"));
        this.mesh.chunkVisible("VatorL_D1", hiermesh.isChunkVisible("VatorL_D1"));
        this.mesh.chunkVisible("VatorL_CAP", hiermesh.isChunkVisible("VatorL_CAP"));
        this.mesh.chunkVisible("VatorR_D0", hiermesh.isChunkVisible("VatorR_D0"));
        this.mesh.chunkVisible("VatorR_D1", hiermesh.isChunkVisible("VatorR_D1"));
        this.mesh.chunkVisible("VatorR_CAP", hiermesh.isChunkVisible("VatorR_CAP"));
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretC", 0.0F, cvt(orient.getYaw(), -38F, 38F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("TurretD", 0.0F, cvt(orient.getTangage(), -43F, 43F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("TurretE", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretF", 0.0F, orient.getTangage(), 0.0F);
        this.mesh.chunkSetAngles("TurretG", -cvt(orient.getYaw(), -33F, 33F, -33F, 33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurretH", 0.0F, cvt(orient.getTangage(), -10F, 32F, -10F, 32F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[0] = cvt(Math.max(Math.abs(orient.getYaw()), Math.abs(orient.getTangage())), 0.0F, 20F, 0.0F, 0.3F);
        this.mesh.chunkSetLocate("TurretI", Cockpit.xyz, Cockpit.ypr);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(isRealMode())
            if(!aiTurret().bIsOperable)
            {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else
            {
                float f = orient.getYaw();
                float f_1_ = orient.getTangage();
                if(f < -38F)
                    f = -38F;
                if(f > 38F)
                    f = 38F;
                if(f_1_ > 43F)
                    f_1_ = 43F;
                if(f_1_ < -41F)
                    f_1_ = -41F;
                orient.setYPR(f, f_1_, 0.0F);
                orient.wrap();
            }
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        }
    }

    public void doGunFire(boolean bool)
    {
        if(isRealMode())
        {
            if(this.emitter == null || !this.emitter.haveBullets() || !aiTurret().bIsOperable)
                this.bGunFire = false;
            else
                this.bGunFire = bool;
            this.fm.CT.WeaponControl[weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitB_24D_AGunner()
    {
        super("3DO/Cockpit/B-25J-AGun/AGunnerB24D.him", "bf109");
        bNeedSetUp = true;
    }

    private boolean bNeedSetUp;

    static 
    {
        Property.set(CockpitB_24D_AGunner.class, "aiTuretNum", 5);
        Property.set(CockpitB_24D_AGunner.class, "weaponControlNum", 15);
        Property.set(CockpitB_24D_AGunner.class, "astatePilotIndx", 4);
    }
}
