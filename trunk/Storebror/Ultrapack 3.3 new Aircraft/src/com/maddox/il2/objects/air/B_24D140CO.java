package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_24D140CO extends B_24_413
{

    public void registerPit(CockpitB24D cockpitb24d)
    {
        pit = cockpitb24d;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        hierMesh().chunkVisible("Turret4A_D0", false);
        hierMesh().chunkVisible("Turret4B_D0", false);
        hierMesh().chunkVisible("Turret5A_D0", false);
        hierMesh().chunkVisible("Turret5B_D0", false);
        hierMesh().chunkVisible("WindDL_D0", false);
        hierMesh().chunkVisible("WindDR_D0", false);
        this.FM.crew = 9;
        this.FM.AS.astatePilotFunctions[0] = 1;
        this.FM.AS.astatePilotFunctions[1] = 2;
        this.FM.AS.astatePilotFunctions[2] = 9;
        this.FM.AS.astatePilotFunctions[3] = 4;
        this.FM.AS.astatePilotFunctions[4] = 5;
        this.FM.AS.astatePilotFunctions[5] = 5;
        this.FM.AS.astatePilotFunctions[6] = 3;
        this.FM.AS.astatePilotFunctions[7] = 7;
        this.FM.AS.astatePilotFunctions[8] = 6;
    }

    public B_24D140CO()
    {
        suspension = 0.0F;
        suspR = 0.0F;
        suspL = 0.0F;
        suspC = 0.0F;
        bBallRemoved = false;
        topLeftGunJammed = false;
        topRightGunJammed = false;
        rearLeftGunJammed = false;
        rearRightGunJammed = false;
        fBallPos = 1.0F;
        iBallPos = 1.0F;
        btme = -1L;
        fRGunPos = 0.0F;
        iRGunPos = 0;
        fLGunPos = 0.0F;
        iLGunPos = 0;
        pit = null;
        slideRWindow = false;
        canopyF = 0.0F;
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        if(f > canopyF && pit != null && canopyF == 0.0F)
            slideRWindow = pit.isViewRight();
        Aircraft.xyz[1] = f * 0.33F;
        if(slideRWindow)
            hierMesh().chunkSetLocate("WindowR_D0", Aircraft.xyz, Aircraft.ypr);
        else
            hierMesh().chunkSetLocate("WindowL_D0", Aircraft.xyz, Aircraft.ypr);
        canopyF = f;
        if((double)canopyF < 0.01D)
            canopyF = 0.0F;
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 27:
            killPilot(this, 8);
            hierMesh().chunkVisible("Turret3A_D0", false);
            hierMesh().chunkVisible("Turret3B_D0", false);
            hierMesh().chunkVisible("Turret3BAux_D0", false);
            doWreck("Turret3B_D0");
            fBallPos = 0.0F;
            iBallPos = 0.0F;
            bBallRemoved = true;
            break;

        case 19:
            killPilot(this, 4);
            killPilot(this, 5);
            killPilot(this, 7);
            killPilot(this, 8);
            cut("StabL");
            cut("StabR");
            break;

        case 13:
            killPilot(this, 0);
            killPilot(this, 1);
            killPilot(this, 2);
            killPilot(this, 6);
            hierMesh().chunkVisible("Nose_Cap", true);
            hierMesh().chunkVisible("Nose_D0", false);
            hierMesh().chunkVisible("Nose_D1", false);
            hierMesh().chunkVisible("Nose_D2", false);
            hierMesh().chunkVisible("NoseAux_D0", false);
            hierMesh().chunkVisible("Blister1_D0", false);
            hierMesh().chunkVisible("GearC2_D0", false);
            hierMesh().chunkVisible("GearC2a_D0", false);
            hierMesh().chunkVisible("GearC3_D0", false);
            hierMesh().chunkVisible("GearC1_D0", false);
            hierMesh().chunkVisible("GearC8_D0", false);
            hierMesh().chunkVisible("GearC9_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", false);
            hierMesh().chunkVisible("Pilot2_D1", false);
            hierMesh().chunkVisible("Pilot3_D1", false);
            hierMesh().chunkVisible("Pilot7_D1", false);
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Head3_D0", false);
            hierMesh().chunkVisible("Pilot7_D0", false);
            hierMesh().chunkVisible("Head7_D0", false);
            hierMesh().chunkVisible("WindowR_D0", false);
            hierMesh().chunkVisible("WindowL_D0", false);
            hierMesh().chunkVisible("FGun1A_D0", false);
            hierMesh().chunkVisible("FGun1B_D0", false);
            hierMesh().chunkVisible("Turret7A_D0", false);
            hierMesh().chunkVisible("Turret7B_D0", false);
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret1B_D0", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doWreck(String s)
    {
        if(hierMesh().chunkFindCheck(s) != -1)
        {
            hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -5F)
            {
                f = -5F;
                flag = false;
            }
            if(f > 50F)
            {
                f = 50F;
                flag = false;
            }
            if(f1 < -40F)
            {
                f1 = -40F;
                flag = false;
            }
            if(f1 > 15F)
            {
                f1 = 15F;
                flag = false;
            }
            break;

        case 1:
            float f2 = -6.5F;
            if(f1 < -6.5F)
            {
                f1 = -6.5F;
                flag = false;
            }
            if(f1 > 85F)
            {
                f1 = 85F;
                flag = false;
            }
            if(f1 < 7.5F)
            {
                if(f > -21F && f < -14F)
                    flag = false;
                if(f < 21F && f > 14F)
                    flag = false;
            }
            if(f < 14F && f > -14F)
                f2 = -0.9F;
            if(f < 23F && f > -23F)
                f2 = -5F;
            if(f > 53F && f < 81F)
                f2 = Aircraft.cvt(f, 53F, 81F, -6.5F, 0.4F);
            if(f > 81F && f < 100F)
                f2 = Aircraft.cvt(f, 93F, 100F, 0.3F, -6.5F);
            if(f < -53F && f > -81F)
                f2 = Aircraft.cvt(f, -81F, -53F, 0.4F, -6.5F);
            if(f < -81F && f > -100F)
                f2 = Aircraft.cvt(f, -100F, -93F, -6.5F, 0.3F);
            if(f1 < f2)
            {
                f1 = f2;
                flag = false;
            }
            break;

        case 2:
            if((double)fBallPos < 0.99D)
            {
                float f3 = Aircraft.cvt(fBallPos, 0.5F, 1.0F, 180F, 0.0F);
                float f5 = Aircraft.cvt(fBallPos, 0.5F, 1.0F, -90F, -22F);
                if(f > f3)
                    f = f3;
                if(f < -f3)
                    f = -f3;
                if(f1 < f5)
                    f1 = f5;
                if(f1 > f5)
                    f1 = f5;
                flag = false;
                break;
            }
            if(f1 < -89F)
            {
                f1 = -89F;
                flag = false;
            }
            if(f1 > 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            break;

        case 3:
            if(f < -66F)
            {
                f = -66F;
                flag = false;
            }
            if(f > 38F)
            {
                f = 38F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 40F)
            {
                f1 = 40F;
                flag = false;
            }
            if(fLGunPos < 0.9F)
                flag = false;
            float f4 = -45F;
            float f6 = 40F;
            if(f > 18F)
            {
                f4 = Aircraft.cvt(f, 18F, 39F, 4F, 5.5F);
                f6 = Aircraft.cvt(f, 18F, 39F, 2.5F, -2.5F);
            }
            if(f < -43F)
                if(f > -53F)
                {
                    f4 = Aircraft.cvt(f, -53F, -43F, 27F, 24F);
                    f6 = Aircraft.cvt(f, -53F, -43F, -10F, -6.5F);
                } else
                if(f <= -53F && f > -63F)
                {
                    f4 = Aircraft.cvt(f, -63F, -57F, 15F, 27F);
                    f6 = Aircraft.cvt(f, -63F, -57F, 0.0F, -10F);
                } else
                {
                    f4 = 12.5F;
                    f6 = 2.0F;
                }
            if(f1 < f4 && f1 > f6)
                flag = false;
            break;

        case 4:
            if(f < -39F)
            {
                f = -39F;
                flag = false;
            }
            if(f > 66F)
            {
                f = 66F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 40F)
            {
                f1 = 40F;
                flag = false;
            }
            if(fRGunPos < 0.9F)
                flag = false;
            float f7 = -45F;
            float f8 = 40F;
            if(f < -18F)
            {
                f7 = Aircraft.cvt(f, -39F, -18F, 5.5F, 4F);
                f8 = Aircraft.cvt(f, -39F, -18F, -2.5F, 2.5F);
            }
            if(f > 43F)
                if(f < 53F)
                {
                    f7 = Aircraft.cvt(f, 43F, 53F, 24F, 27F);
                    f8 = Aircraft.cvt(f, 43F, 53F, -6.5F, -10F);
                } else
                if(f >= 53F && f < 63F)
                {
                    f7 = Aircraft.cvt(f, 57F, 63F, 27F, 15F);
                    f8 = Aircraft.cvt(f, 57F, 63F, -10F, 0.0F);
                } else
                {
                    f7 = 12.5F;
                    f8 = 2.0F;
                }
            if(f1 < f7 && f1 > f8)
                flag = false;
            break;

        case 5:
            if(f < -60F)
            {
                f = -60F;
                flag = false;
            }
            if(f > 60F)
            {
                f = 60F;
                flag = false;
            }
            if(f1 < -29F)
            {
                f1 = -29F;
                flag = false;
            }
            if(f1 > 71F)
            {
                f1 = 71F;
                flag = false;
            }
            break;

        case 6:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 0.0F)
            {
                f = 0.0F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 15F)
            {
                f1 = 15F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void update(float f)
    {
        super.update(f);
        if(this.FM.CT.getGear() > 0.9F)
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.xyz[1] = Aircraft.cvt(suspC, 0.0F, 0.25F, 0.0F, 0.25F);
            Aircraft.ypr[1] = this.fCSteer;
            hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if(!this.FM.AS.isMaster())
            return;
        if(iBallPos == 0.0F)
        {
            if(fBallPos > iBallPos)
            {
                fBallPos -= 0.15F * f;
                if(fBallPos < 0.0F)
                    fBallPos = 0.0F;
            }
            resetYPRmodifier();
            Aircraft.xyz[2] = -0.71F * fBallPos;
            hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
        } else
        if(iBallPos == 1.0F)
        {
            if(fBallPos < iBallPos)
            {
                fBallPos += 0.15F * f;
                if(fBallPos > 1.0F)
                {
                    fBallPos = 1.0F;
                    iBallPos = 0.5F;
                    this.FM.turret[2].bIsOperable = true;
                }
            }
            resetYPRmodifier();
            Aircraft.xyz[2] = -0.7F * fBallPos;
            hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if(Time.current() > btme)
        {
            btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
            if(this.FM.turret[1].target == null && this.FM.turret[2].target == null && this.FM.turret[3].target == null && this.FM.turret[4].target == null && this.FM.turret[5].target == null)
            {
                this.FM.turret[2].bIsOperable = false;
                iBallPos = 0.0F;
            }
            if(this.FM.turret[1].target != null && this.FM.AS.astatePilotStates[8] < 90 && iBallPos == 0.0F)
                iBallPos = 1.0F;
        }
        if(iLGunPos == 0)
        {
            if(fLGunPos > 0.0F)
            {
                fLGunPos -= 0.2F * f;
                hierMesh().chunkVisible("Turret4A_D0", false);
                hierMesh().chunkVisible("Turret4B_D0", false);
                hierMesh().chunkVisible("WindDL_D0", false);
            }
        } else
        if(iLGunPos == 1 && fLGunPos < 1.0F)
        {
            fLGunPos += 0.2F * f;
            if(fLGunPos > 0.8F && fLGunPos < 0.9F)
            {
                hierMesh().chunkVisible("Turret4A_D0", true);
                hierMesh().chunkVisible("Turret4B_D0", true);
                hierMesh().chunkVisible("WindDL_D0", true);
            }
        }
        hierMesh().chunkSetAngles("HatchDoorL_D0", 0.0F, Aircraft.cvt(1.0F - fLGunPos, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        if(this.FM.turret[3].bIsAIControlled)
        {
            if(this.FM.turret[3].target != null && this.FM.AS.astatePilotStates[4] < 90)
            {
                iLGunPos = 1;
                this.FM.turret[3].bIsOperable = true;
            }
        } else
        if(this.FM.turret[3].bIsOperable)
            iLGunPos = 1;
        else
            iLGunPos = 0;
        if(iRGunPos == 0)
        {
            if(fRGunPos > 0.0F)
            {
                fRGunPos -= 0.2F * f;
                hierMesh().chunkVisible("Turret5A_D0", false);
                hierMesh().chunkVisible("Turret5B_D0", false);
                hierMesh().chunkVisible("WindDR_D0", false);
            }
        } else
        if(iRGunPos == 1 && fRGunPos < 1.0F)
        {
            fRGunPos += 0.2F * f;
            if(fRGunPos > 0.8F && fRGunPos < 0.9F)
            {
                hierMesh().chunkVisible("Turret5A_D0", true);
                hierMesh().chunkVisible("Turret5B_D0", true);
                hierMesh().chunkVisible("WindDR_D0", true);
            }
        }
        hierMesh().chunkSetAngles("HatchDoorR_D0", 0.0F, Aircraft.cvt(1.0F - fRGunPos, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        if(this.FM.turret[4].bIsAIControlled)
        {
            if(this.FM.turret[4].target != null && this.FM.AS.astatePilotStates[5] < 90)
            {
                iRGunPos = 1;
                this.FM.turret[4].bIsOperable = true;
            }
        } else
        if(this.FM.turret[4].bIsOperable)
            iRGunPos = 1;
        else
            iRGunPos = 0;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = 0;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("01"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                else
                if(s.endsWith("02"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("03"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                else
                if(s.endsWith("04"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                else
                if(s.endsWith("05"))
                {
                    getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.z) + 0.000001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x80);
                } else
                if(s.endsWith("06"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("07"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                else
                if(s.endsWith("08"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(Aircraft.v1.y) + 0.000001D), shot);
                else
                if(s.endsWith("09"))
                    getEnergyPastArmor(6.3499999046325684D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("10"))
                {
                    getEnergyPastArmor(22D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else
                if(s.endsWith("11"))
                {
                    if(getEnergyPastArmor(52D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot) > 0.0F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                } else
                if(s.endsWith("12"))
                    getEnergyPastArmor(52D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("13"))
                    getEnergyPastArmor(6.35D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("14"))
                    getEnergyPastArmor(7.9375D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("15"))
                {
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else
                if(s.endsWith("16"))
                {
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else
                if(s.endsWith("17"))
                    getEnergyPastArmor(15.875D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("18"))
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("19"))
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("20"))
                    getEnergyPastArmor(51D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("21"))
                    getEnergyPastArmor(22D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("22"))
                    getEnergyPastArmor(52D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("23"))
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
                else
                if(s.endsWith("24"))
                    getEnergyPastArmor(9.525D / (Math.abs(Aircraft.v1.x) + 0.000001D), shot);
            if(s.equals("xxfrontwindow"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if(s.equals("xxleftwindow"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if(s.equals("xxrightwindow"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if(s.equals("xxtopwindow"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if(s.equals("xxpanelleft"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if(s.equals("xxpanelright"))
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            if(s.startsWith("xxammo"))
            {
                int j = s.charAt(6) - 48;
                if(s.length() == 8)
                    j = 10 + (s.charAt(7) - 48);
                if(getEnergyPastArmor(6.87F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                {
                    switch(j)
                    {
                    case 1:
                        j = 1;
                        i = 0;
                        break;

                    case 2:
                        return;

                    case 3:
                        j = 11;
                        i = 0;
                        topLeftGunJammed = true;
                        break;

                    case 4:
                        j = 11;
                        i = 1;
                        topRightGunJammed = true;
                        break;

                    case 5:
                        j = 12;
                        i = 0;
                        break;

                    case 6:
                        j = 12;
                        i = 1;
                        break;

                    case 7:
                        j = 14;
                        i = 0;
                        break;

                    case 8:
                        j = 13;
                        i = 0;
                        break;

                    case 9:
                        j = 10;
                        i = 0;
                        rearLeftGunJammed = true;
                        break;

                    case 10:
                        j = 10;
                        i = 1;
                        rearRightGunJammed = true;
                        break;

                    case 11:
                        j = 15;
                        i = 0;
                        break;

                    case 12:
                        j = 16;
                        i = 0;
                        break;
                    }
                    this.FM.AS.setJamBullets(j, i);
                    return;
                }
            }
            if(s.startsWith("xxcontrols"))
            {
                int k = s.charAt(10) - 48;
                if(s.length() == 12)
                    k = 10 + (s.charAt(11) - 48);
                switch(k)
                {
                default:
                    break;

                case 1:
                case 2:
                case 11:
                case 12:
                    if(getEnergyPastArmor(5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                    }
                    break;

                case 3:
                case 4:
                case 5:
                case 6:
                    if(World.Rnd().nextFloat() < 0.252F && getEnergyPastArmor(5.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                    getEnergyPastArmor(2.0F, shot);
                    break;

                case 7:
                case 8:
                case 9:
                case 10:
                    if(World.Rnd().nextFloat() < 0.252F && getEnergyPastArmor(5.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    getEnergyPastArmor(2.0F, shot);
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int l = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            this.FM.AS.setEngineStuck(shot.initiator, l);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.005F)
                    {
                        this.FM.EI.engines[l].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        this.FM.EI.engines[l].setReadyness(shot.initiator, this.FM.EI.engines[l].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[l].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[l].getCylindersRatio() * 0.75F)
                    {
                        this.FM.EI.engines[l].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit, " + this.FM.EI.engines[l].getCylindersOperable() + "/" + this.FM.EI.engines[l].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 18000F)
                        {
                            this.FM.AS.hitEngine(shot.initiator, l, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + l + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("mag1"))
                {
                    this.FM.EI.engines[l].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #0 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("mag2"))
                {
                    this.FM.EI.engines[l].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Magneto #1 Destroyed..");
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("oil1") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    this.FM.AS.setOilState(shot.initiator, l, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Filter Pierced..");
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr1") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockr2") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder2_D" + chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                int i1 = s.charAt(5) - 49;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F)
                    this.FM.AS.hitOil(shot.initiator, i1);
                Aircraft.debugprintln(this, "*** Engine (" + i1 + ") Module: Oil Tank Pierced..");
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if(s.startsWith("xxradio"))
            {
                getEnergyPastArmor(World.Rnd().nextFloat(5F, 25F), shot);
                return;
            }
            if(s.startsWith("xxautopilot"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 4F), shot) > 0.0F)
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxspark2") && chunkDamageVisible("Keel2") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel2 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel2_D" + chunkDamageVisible("Keel2"), shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.06F, shot) > 0.0F)
                {
                    if(this.FM.AS.astateTankStates[j1] == 0)
                    {
                        this.FM.AS.hitTank(shot.initiator, j1, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if(shot.powerType == 3)
                    {
                        if(shot.power < 16100F)
                        {
                            if(this.FM.AS.astateTankStates[j1] < 4 && World.Rnd().nextFloat() < 0.21F)
                                this.FM.AS.hitTank(shot.initiator, j1, 1);
                        } else
                        {
                            this.FM.AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                        }
                    } else
                    if(shot.power > 16100F)
                        this.FM.AS.hitTank(shot.initiator, j1, World.Rnd().nextInt(1, 1 + (int)(shot.power / 16100F)));
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            return;
        }
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
            return;
        }
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
            return;
        }
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
            return;
        }
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
            return;
        }
        if(s.startsWith("xrudder2"))
        {
            if(chunkDamageVisible("Rudder2") < 1)
                hitChunk("Rudder2", shot);
            return;
        }
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
            return;
        }
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
            return;
        }
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            return;
        }
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
            return;
        }
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            return;
        }
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            return;
        }
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            return;
        }
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            return;
        }
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            return;
        }
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
            return;
        }
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            return;
        }
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
            return;
        }
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            return;
        }
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            return;
        }
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
            return;
        }
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
            return;
        }
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.05F)
            {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                this.FM.Gears.setHydroOperable(false);
            }
            return;
        }
        if(s.startsWith("xturret"))
            return;
        if(s.startsWith("xmgun"))
        {
            int k1 = 10 * (s.charAt(5) - 48) + (s.charAt(6) - 48);
            if(getEnergyPastArmor(6.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
            {
                switch(k1)
                {
                case 1:
                    k1 = 1;
                    i = 0;
                    break;

                case 2:
                    return;

                case 3:
                    k1 = 11;
                    i = 0;
                    topLeftGunJammed = true;
                    break;

                case 4:
                    k1 = 11;
                    i = 1;
                    topRightGunJammed = true;
                    break;

                case 5:
                    k1 = 12;
                    i = 0;
                    break;

                case 6:
                    k1 = 12;
                    i = 1;
                    break;

                case 7:
                    k1 = 14;
                    i = 0;
                    break;

                case 8:
                    k1 = 13;
                    i = 0;
                    break;

                case 9:
                    k1 = 10;
                    i = 0;
                    rearLeftGunJammed = true;
                    break;

                case 10:
                    k1 = 10;
                    i = 1;
                    rearRightGunJammed = true;
                    break;

                case 11:
                    k1 = 15;
                    i = 0;
                    break;

                case 12:
                    k1 = 16;
                    i = 0;
                    break;
                }
                this.FM.AS.setJamBullets(k1, i);
            }
            return;
        }
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int l1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                l1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                l1 = s.charAt(6) - 49;
            } else
            {
                l1 = s.charAt(5) - 49;
            }
            hitFlesh(l1, shot, byte0);
            return;
        } else
        {
            return;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -20F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -20F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        if(this.FM.Gears.onGround())
            suspension += 0.008F;
        else
            suspension -= 0.008F;
        if(suspension < 0.0F)
        {
            suspension = 0.0F;
            if(!this.FM.isPlayers())
                this.FM.Gears.bTailwheelLocked = true;
        }
        if(suspension > 0.1F)
            suspension = 0.1F;
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        float f = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 35F, 0.0F, 1.0F);
        suspL = this.FM.Gears.gWheelSinking[0] * f + suspension;
        Aircraft.xyz[1] = Aircraft.cvt(suspL, 0.0F, 0.25F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        suspR = this.FM.Gears.gWheelSinking[1] * f + suspension;
        Aircraft.xyz[1] = Aircraft.cvt(suspR, 0.0F, 0.25F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        suspC = this.FM.Gears.gWheelSinking[2] * f + suspension;
    }

    public float getBombSightPDI()
    {
        return 0.0F;
    }

    private float suspension;
    public float suspR;
    public float suspL;
    public float suspC;
    public static boolean bChangedPit = false;
    public boolean bBallRemoved;
    protected boolean topLeftGunJammed;
    protected boolean topRightGunJammed;
    protected boolean rearLeftGunJammed;
    protected boolean rearRightGunJammed;
    public float fRGunPos;
    public int iRGunPos;
    public float fLGunPos;
    public int iLGunPos;
    public float fBallPos;
    public float iBallPos;
    public long btme;
    private CockpitB24D pit;
    private boolean slideRWindow;
    public float canopyF;
    public boolean bSightClutch;
    public float fSightHeadTurn;

    static 
    {
        Class class1 = B_24D140CO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-24");
        Property.set(class1, "meshName", "3DO/Plane/B-24D-140-CO/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/B-24D-140-CO/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/B-24J.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitB24D.class, CockpitB24D140CO_Bombardier.class, CockpitB24D_FLGunner.class, CockpitB24D_FRGunner.class, CockpitB24D_TGunner.class, CockpitB24D_RGunner.class, CockpitB24D_LGunner.class, CockpitB24D_BGunner.class, CockpitB24D_AGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 10, 11, 11, 12, 12, 13, 14, 15, 15, 
            16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN12", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN03", "_MGUN04", 
            "_MGUN11", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", 
            "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", 
            "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24"
        });
    }
}
