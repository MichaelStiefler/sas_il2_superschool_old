// Source File Name: Mig_15bis.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Mig_15UTI extends Mig_15F {

  public Mig_15UTI() {
      overrideBailout = false;
      ejectComplete = false;
      super.isTrainer = true;
      lTimeNextEject = 0;
  }
  
  public void doMurderPilot(int i)
  {
      switch(i)
      {
      default:
          break;

      case 0: // '\0'
          hierMesh().chunkVisible("Pilot1_D0", false);
          hierMesh().chunkVisible("Head1_D0", false);
          hierMesh().chunkVisible("Pilot1_D1", true);
          break;

      case 1: // '\001'
          hierMesh().chunkVisible("Pilot2_D0", false);
          hierMesh().chunkVisible("Head2_D0", false);
          hierMesh().chunkVisible("Pilot2_D1", true);
          break;
      }
  }
  
  protected boolean cutFM(int i, int j, Actor actor)
  {
      switch(i)
      {
      case 33: // '!'
          return super.cutFM(34, j, actor);

      case 36: // '$'
          return super.cutFM(37, j, actor);

      case 11: // '\013'
          cutFM(17, j, actor);
          super.FM.cut(17, j, actor);
          cutFM(18, j, actor);
          super.FM.cut(18, j, actor);
          return super.cutFM(i, j, actor);
      }
      return super.cutFM(i, j, actor);
  }
  
  public void rareAction(float f, boolean flag)
  {
      super.rareAction(f, flag);
      if ((FM.Gears.nearGround() || FM.Gears.onGround())
              && FM.EI.engines[0].getStage() != 6) {
        this.hierMesh().chunkVisible("HMask1_D0", false);
        this.hierMesh().chunkVisible("HMask2_D0", false);
      } else {
        this.hierMesh().chunkVisible("HMask1_D0",
        this.hierMesh().isChunkVisible("Pilot1_D0"));
        this.hierMesh().chunkVisible("HMask2_D0",
        this.hierMesh().isChunkVisible("Pilot2_D0"));
      }
  }
  
  public void doEjectCatapultStudent() {
      new MsgAction(false, this) {

          public void doAction(Object paramObject) {
              Aircraft localAircraft = (Aircraft) paramObject;
              if (Actor.isValid(localAircraft)) {
                  Loc localLoc1 = new Loc();
                  Loc localLoc2 = new Loc();
                  Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
                  HookNamed localHookNamed = new HookNamed(localAircraft,
                          "_ExternalSeat01");
                  localAircraft.pos.getAbs(localLoc2);
                  localHookNamed.computePos(localAircraft, localLoc2,
                          localLoc1);
                  localLoc1.transform(localVector3d);
                  localVector3d.x += localAircraft.FM.Vwld.x;
                  localVector3d.y += localAircraft.FM.Vwld.y;
                  localVector3d.z += localAircraft.FM.Vwld.z;
                  new EjectionSeat(1, localLoc1, localVector3d, localAircraft);
              }
          }
      };
      this.hierMesh().chunkVisible("Seat2_D0", false);
  }

  public void doEjectCatapultInstructor() {
      new MsgAction(false, this) {

          public void doAction(Object paramObject) {
              Aircraft localAircraft = (Aircraft) paramObject;
              if (Actor.isValid(localAircraft)) {
                  Loc localLoc1 = new Loc();
                  Loc localLoc2 = new Loc();
                  Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
                  HookNamed localHookNamed = new HookNamed(localAircraft,
                          "_ExternalSeat02");
                  localAircraft.pos.getAbs(localLoc2);
                  localHookNamed.computePos(localAircraft, localLoc2,
                          localLoc1);
                  localLoc1.transform(localVector3d);
                  localVector3d.x += localAircraft.FM.Vwld.x;
                  localVector3d.y += localAircraft.FM.Vwld.y;
                  localVector3d.z += localAircraft.FM.Vwld.z;
                  new EjectionSeat(1, localLoc1, localVector3d, localAircraft);
              }
          }
      };
      this.hierMesh().chunkVisible("Seat1_D0", false);
  }

  private void bailout() {
      if (overrideBailout) {
          if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
              if (FM.CT.cockpitDoorControl > 0.5F
                      && FM.CT.getCockpitDoor() > 0.5F) {
                  FM.AS.astateBailoutStep = (byte) 11;
                  doRemoveBlisters();
              } else {
                  FM.AS.astateBailoutStep = (byte) 2;
              }
          } else if (FM.AS.astateBailoutStep >= 2
                  && FM.AS.astateBailoutStep <= 3) {
              switch (FM.AS.astateBailoutStep) {
                  case 2:
                      if (FM.CT.cockpitDoorControl >= 0.5F) {
                          break;
                      }
                      doRemoveBlister1();
                      break;
                  case 3:
                      doRemoveBlisters();
                      lTimeNextEject = Time.current() + 1000;
                      break;
              }
              if (FM.AS.isMaster()) {
                  FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
              }
              FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
              if (FM.AS.astateBailoutStep == 4) {
                  FM.AS.astateBailoutStep = (byte) 11;
              }
          } else if (FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19) {
              int i = FM.AS.astateBailoutStep;
              if (FM.AS.isMaster()) {
                  FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
              }
              FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
              if (FM instanceof Maneuver && ((Maneuver) FM).get_maneuver() != 44) {
                  World.cur();
                  if (FM.AS.actor != World.getPlayerAircraft()) {
                      ((Maneuver) FM).set_maneuver(44);
                  }
              }
              
              if (FM.AS.astatePilotStates[i - 11] < 99) {
                  if (i == 11) {
                      this.doRemoveBodyFromPlane(2);
                      doEjectCatapultStudent();
                      lTimeNextEject = Time.current() + 1000;
                  } else if (i == 12) {
                      this.doRemoveBodyFromPlane(1);
                      doEjectCatapultInstructor();
                      FM.AS.astateBailoutStep = (byte) 51;
                      FM.setTakenMortalDamage(true, null);
                      FM.CT.WeaponControl[0] = false;
                      FM.CT.WeaponControl[1] = false;
                      FM.AS.astateBailoutStep = (byte) -1;
                      overrideBailout = false;
                      FM.AS.bIsAboutToBailout = true;
                      ejectComplete = true;
                  }
                  FM.AS.astatePilotStates[(i - 11)] = 99;
              } else {
                  EventLog.type("astatePilotStates[" + (i-11) + "]=" + FM.AS.astatePilotStates[i - 11]);
              }                    
          }
      }
  }

  private final void doRemoveBlister1() {
      if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1
              && FM.AS.getPilotHealth(0) > 0.0F) {
          this.hierMesh().hideSubTrees("Blister1_D0");
          Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
          localWreckage.collide(false);
          Vector3d localVector3d = new Vector3d();
          localVector3d.set(FM.Vwld);
          localWreckage.setSpeed(localVector3d);
      }
  }
  
  private final void doRemoveBlisters() {
      for (int i = 2; i < 10; i++) {
          if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
                  && FM.AS.getPilotHealth(i - 1) > 0.0F) {
              this.hierMesh().hideSubTrees("Blister" + i + "_D0");
              Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
              localWreckage.collide(false);
              Vector3d localVector3d = new Vector3d();
              localVector3d.set(FM.Vwld);
              localWreckage.setSpeed(localVector3d);
          }
      }
  }
 
	public void moveCockpitDoor(float paramFloat) {
		this.resetYPRmodifier();
		if (FM.AS.astatePlayerIndex == 1){
			if (paramFloat < 0.1F) {
				Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.08F, 0.0F,
						0.1F);
			} else {
				Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.17F, 0.99F, 0.1F,
						0.4F);
			}
			this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz,
					Aircraft.ypr);
			if (Config.isUSE_RENDER()) {
				if (Main3D.cur3D().cockpits != null
						&& Main3D.cur3D().cockpits[0] != null) {
					Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
				}

			}
		this.setDoorSnd(paramFloat);
		}
	}
  
	public void update(float f) {
		if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete
				&& FM.getSpeedKMH() > 15.0F) {
			overrideBailout = true;
			FM.AS.bIsAboutToBailout = false;
			if (Time.current() > lTimeNextEject) bailout();
		}
		try
		{  
			if (FM.AS.astatePlayerIndex == 1)
				FM.CT.setActiveDoor(1);
			else
				FM.CT.setActiveDoor(2);

			if (FM.CT.bMoveSideDoor){
				hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 90F * FM.CT.getCockpitDoor(),
						0.0F);

				if (Main3D.cur3D().cockpits != null
						&& Main3D.cur3D().cockpits[1] != null) {
					Main3D.cur3D().cockpits[1].onDoorMoved(FM.CT.getCockpitDoor());
				}
				this.setDoorSnd(FM.CT.getCockpitDoor());
			}
		}
		catch(java.lang.Throwable throwable) { }

		super.update(f);
	}
  
  public static boolean bChangedPit = false;
  private boolean overrideBailout;
  private boolean ejectComplete;
  private long lTimeNextEject = 0;


  static {
    Class class1 = com.maddox.il2.objects.air.Mig_15UTI.class;
    new NetAircraft.SPAWN(class1);
    Property.set(class1, "iconFar_shortClassName", "MiG-15");
    Property.set(class1, "meshName_ru", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_ru", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_sk", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_ro", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_hu", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName", "3DO/Plane/MiG-15UTI(Multi1)/hier.him");
    Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(class1, "yearService", 1949.9F);
    Property.set(class1, "yearExpired", 1960.3F);
    Property.set(class1, "FlightModel", "FlightModels/MiG-15UTI.fmd:UTI");
    Property.set(class1, "cockpitClass", new Class[]{
            com.maddox.il2.objects.air.CockpitMig_15F.class,
            com.maddox.il2.objects.air.CockpitMig_15UTIi.class
            });
    Property.set(class1, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[]{
              1, 0, 0, 9, 9
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[]{
              "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "default", new java.lang.String[]{
              null, "MGunUBki 120", null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "2xDroptanks", new java.lang.String[]{
    		null, "MGunUBki 120", null, "FTGunL 1", "FTGunR 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "none", new java.lang.String[]{
              null, null, null, null, null
            });
  }
}