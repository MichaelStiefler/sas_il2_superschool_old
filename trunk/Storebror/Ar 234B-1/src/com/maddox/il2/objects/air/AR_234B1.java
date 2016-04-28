package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Motor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class AR_234B1 extends AR_234B2 implements TypeScout {
    public boolean typeBomberToggleAutomation() { return false; }
    public void typeBomberAdjDistanceReset() {}
    public void typeBomberAdjDistancePlus() {}
    public void typeBomberAdjDistanceMinus() {}
    public void typeBomberAdjSideslipReset() {}
    public void typeBomberAdjSideslipPlus() {}
    public void typeBomberAdjSideslipMinus() {}
    public void typeBomberAdjAltitudeReset() {}
    public void typeBomberAdjAltitudePlus() {}
    public void typeBomberAdjAltitudeMinus() {}
    public void typeBomberAdjSpeedReset() {}
    public void typeBomberAdjSpeedPlus() {}
    public void typeBomberAdjSpeedMinus() {}
    public void typeBomberUpdate(float f) {}
    public void typeBomberReplicateToNet(NetMsgGuaranted netMsgGuaranted) throws IOException {}
    public void typeBomberReplicateFromNet(NetMsgInput netMsgInput) throws IOException {}
    
    public void update(float f) {
        super.update(f);
        for (int i=LEFT_ENGINE; i<=RIGHT_ENGINE; i++) {
            if (Math.abs(this.FM.EI.engines[i].getControlThrottle() - this.lastLockedThrust[i]) > THROTTLE_CHANGE_TRESHOLD ||
                    (this.FM.EI.engines[i].getStage() > Motor._E_STAGE_NULL && this.FM.EI.engines[i].getStage() < Motor._E_STAGE_NOMINAL)) {
                this.lastLockedThrust[i] = this.FM.EI.engines[i].getControlThrottle();
                this.lastThrustChange = Time.current();
            }
            this.throttleLocked =  (Time.current() - this.lastThrustChange > THROTTLE_LOCK_TRESHOLD);
        }
    }
    
    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this != World.getPlayerAircraft()) return;
        this.checkEngineStates();
        // Check if forward (Nr. 1) or aft Tank (Nr. 2) is hit
        this.checkTankState(FORWARD_TANK, AFT_TANK);
        this.checkTankState(AFT_TANK, FORWARD_TANK);
    }
    
    private void checkEngineStates() {
        for (int i=LEFT_ENGINE; i<=RIGHT_ENGINE; i++) {
            if (!this.engineIsUp(i)) {
                this.tankSelectors[i] = SHUTOFF;
            }
            if (this.FM.AS.astateEngineStates[i] >= 4) {
                if (this.nextEngineFireActionTime[i] == 0) {
                    System.out.println("ENGINE " + (i+1) + " caught fire !!!");
                    this.nextEngineFireActionTime[i] = Time.current() + FIRE_TIME;
                } else if (Time.current() > this.nextEngineFireActionTime[i]) {
                    switch (this.nextEngineFireAction[i]) {
                        case ENGINE_FIRE_ACTION_SHUTOFF:
                            this.FM.AS.setEngineDies(this, i);
                            this.tankSelectors[i] = SHUTOFF;
                            this.nextEngineFireActionTime[i] = Time.current() + FIRE_TIME;
                            this.nextEngineFireAction[i] = ENGINE_FIRE_ACTION_EXT;
                            break;
                        case ENGINE_FIRE_ACTION_EXT:
                            this.FM.AS.setEngineState(this, i, 2);
                            Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Aircraft/EngineExtinguisher1.eff", 3.0F);
                            this.FM.EI.engines[i].tOilIn -= 20;
                            this.FM.EI.engines[i].tOilOut -= 20;
                            this.FM.EI.engines[i].tWaterOut -= 40;
                            
                            LightPointActor astateEngineBurnLights[] = (LightPointActor[])Reflection.getValue(this.FM.AS, "astateEngineBurnLights");
                            for (int j=0; j<astateEngineBurnLights.length; j++)
                                if(astateEngineBurnLights[i] != null)
                                {
                                    this.draw.lightMap().remove("_EngineBurnLight" + i);
                                    astateEngineBurnLights[i].destroy();
                                    astateEngineBurnLights[i] = null;
                                }
                            this.nextEngineFireActionTime[i] = 0;
                            break;
                        default:
                            break;
                            
                    }
                }
            }
        }
    }
    
    private boolean engineIsUp(int engineIndex) {
        return (this.FM.EI.engines[engineIndex].getStage() > Motor._E_STAGE_NULL && this.FM.EI.engines[engineIndex].getStage() < Motor._E_STAGE_DEAD);   
    }
    
    private void checkTankState(int theTank, int otherTank) {
        this.tankStates[theTank] = this.FM.AS.astateTankStates[theTank];
        if ((this.tankStates[theTank] != TANK_STATE_OK && this.tankStates[otherTank] == TANK_STATE_OK)
                || this.tankStates[theTank] == TANK_STATE_BURNS && this.tankStates[otherTank] < TANK_STATE_BURNS) {
            for (int i=LEFT_ENGINE; i<=RIGHT_ENGINE; i++) {
                if (this.engineIsUp(i))
                    this.tankSelectors[i] = otherTank;
            }
        } else if ((this.tankStates[theTank] != TANK_STATE_OK && this.tankStates[otherTank] != TANK_STATE_OK) || this.tankStates[theTank] == this.tankStates[otherTank]) {
            if (!this.engineIsUp(0) && !this.engineIsUp(1)) return;
            if (this.engineIsUp(0) && this.engineIsUp(1)) {
                this.tankSelectors[0] = FORWARD_TANK;
                this.tankSelectors[1] = AFT_TANK;
                return;
            }
            if (Time.current() < this.nextTankToggleTime) return;
            int toggleTankIndex = (this.engineIsUp(0))?0:1;
            if (this.tankSelectors[toggleTankIndex] == FORWARD_TANK)
                this.tankSelectors[toggleTankIndex] = AFT_TANK;
            else
                this.tankSelectors[toggleTankIndex] = FORWARD_TANK;
            this.nextTankToggleTime = Time.current() + TANK_TOGGLE_TIME;
        }
    }
    
    protected int tankSelectors[] = {FORWARD_TANK, AFT_TANK};
    protected int tankStates[] = {0, 0};
    private static final int TANK_STATE_OK = 0;
    private static final int TANK_STATE_BURNS = 6;
    private static final int FORWARD_TANK = 0;
    private static final int AFT_TANK = 1;
    private static final int SHUTOFF = 2;
    private static final long FIRE_TIME = 5000;

    private static final long TANK_TOGGLE_TIME = 30000;
    private long nextTankToggleTime = 0;
    
    protected long nextEngineFireActionTime[] = {0, 0};
    protected int nextEngineFireAction[] = {ENGINE_FIRE_ACTION_SHUTOFF, ENGINE_FIRE_ACTION_SHUTOFF};
    protected int engineStates[] = {0, 0};
    protected static final int LEFT_ENGINE = 0;
    protected static final int RIGHT_ENGINE = 1;
    private static final int ENGINE_FIRE_ACTION_SHUTOFF = 0;
    private static final int ENGINE_FIRE_ACTION_EXT = 1;
    
    protected boolean throttleLocked = false;
    private float lastLockedThrust[] = {0F, 0F};
    private long lastThrustChange = 0;
    private static final float THROTTLE_CHANGE_TRESHOLD = 0.03F;
    private static final long THROTTLE_LOCK_TRESHOLD = 5000;
    
    static {
        Class class1 = AR_234B1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ar 234");
        Property.set(class1, "meshName", "3DO/Plane/Ar-234B-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());

        Property.set(class1, "yearService", 1944.0F);
        Property.set(class1, "yearExpired", 1948.8F);

        Property.set(class1, "FlightModel", "FlightModels/Ar-234B-1.fmd:AR234B1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAR_234B1.class });
        Property.set(class1, "LOSElevation", 1.14075F);

        weaponTriggersRegister(class1, new int[] { });
        weaponHooksRegister(class1, new String[] { });
    }
}
