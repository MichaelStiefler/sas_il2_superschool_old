package com.maddox.il2.objects.vehicles.lights;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorMeshDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.effects.LightsGlare;
import com.maddox.il2.objects.vehicles.aeronautics.Balloon;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.HashMapExt;
import com.maddox.util.TableFunction2;

public abstract class SearchlightGeneric extends ActorHMesh
    implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, ActorAlign, HunterInterface
{
    public static class SPAWN
        implements ActorSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            // System.out.println("SearchlightGeneric SPAWN getF(SectFile sectfile, String s, String s1, float f, float f1)");
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Searchlight: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Searchlight: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                throw new RuntimeException("Can't set property");
            } else
            {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1)
        {
            // System.out.println("SearchlightGeneric SPAWN getS(SectFile sectfile, String s, String s1)");
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                System.out.print("Searchlight: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1, String s2)
        {
            // System.out.println("SearchlightGeneric SPAWN getS(SectFile sectfile, String s, String s1, String s2)");
            String s3 = sectfile.get(s, s1);
            if(s3 == null || s3.length() <= 0)
                return s2;
            else
                return s3;
        }

        private static SearchlightProperties LoadSearchlightProperties(SectFile sectfile, String s, Class class1)
        {
            // System.out.println("SearchlightGeneric SPAWN LoadSearchlightProperties(SectFile sectfile, String s, Class class1)");
            SearchlightProperties searchlightproperties = new SearchlightProperties();
            String s1 = getS(sectfile, s, "PanzerType", null);
            if(s1 == null)
                s1 = "Car";
            searchlightproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            searchlightproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            searchlightproperties.PANZER_TNT_TYPE = getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            searchlightproperties.meshSummer = getS(sectfile, s, "MeshSummer");
            searchlightproperties.meshDesert = getS(sectfile, s, "MeshDesert", searchlightproperties.meshSummer);
            searchlightproperties.meshWinter = getS(sectfile, s, "MeshWinter", searchlightproperties.meshSummer);
            searchlightproperties.PANZER = getF(sectfile, s, "PanzerBody", 0.001F, 9.999F);
            searchlightproperties.HITBY_MASK = searchlightproperties.PANZER <= 0.015F ? -1 : -2;
            searchlightproperties.explodeName = getS(sectfile, s, "Explode", "Searchlight");
            searchlightproperties.Hclear = getF(sectfile, s, "MaxDistance", 6F, 12000F);
            float f = getF(sectfile, s, "HeadYawHalfRange", 0.0F, 180F);
            searchlightproperties.HEAD_YAW_RANGE.set(-f, f);
            searchlightproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            searchlightproperties.GUN_STD_PITCH = getF(sectfile, s, "GunStdPitch", -15F, 90F);
            searchlightproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 90F);
            searchlightproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            searchlightproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            searchlightproperties.SEARCH_MAX_CONE_ANGLE = getF(sectfile, s, "SearchMaxConeAngle", 0.01F, 60F);
            searchlightproperties.FOUND_MAX_CONE_ANGLE = getF(sectfile, s, "FoundMaxConeAngle", 0.0F, 20F);
            searchlightproperties.LIGHT_COLOR.x = getF(sectfile, s, "ColorR", 0.0F, 1.0F);
            searchlightproperties.LIGHT_COLOR.y = getF(sectfile, s, "ColorG", 0.0F, 1.0F);
            searchlightproperties.LIGHT_COLOR.z = getF(sectfile, s, "ColorB", 0.0F, 1.0F);
            searchlightproperties.LIGHT_LAND_I = getF(sectfile, s, "LandIntensity", 0.1F, 100F);
            searchlightproperties.LIGHT_LAND_R = getF(sectfile, s, "LandRadius", 1.0F, 300F);
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", searchlightproperties.meshSummer);
            return searchlightproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            // System.out.println("SearchlightGeneric SPAWN actorSpawn(ActorSpawnArg actorspawnarg)");
            switch(World.cur().camouflage)
            {
            case 1: // '\001'
                proper.meshName = proper.meshWinter;
                break;

            case 2: // '\002'
                proper.meshName = proper.meshDesert;
                break;

            default:
                proper.meshName = proper.meshSummer;
                break;
            }
            SearchlightGeneric searchlightgeneric = null;
            try
            {
                SearchlightGeneric.constr_arg1 = proper;
                SearchlightGeneric.constr_arg2 = actorspawnarg;
                searchlightgeneric = (SearchlightGeneric)cls.newInstance();
                SearchlightGeneric.constr_arg1 = null;
                SearchlightGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                SearchlightGeneric.constr_arg1 = null;
                SearchlightGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Searchlight object [class:" + cls.getName() + "]");
                return null;
            }
            return searchlightgeneric;
        }

        public Class cls;
        public SearchlightProperties proper;

        public SPAWN(Class class1)
        {
            // System.out.println("SearchlightGeneric SPAWN SPAWN(Class class1)");
            try
            {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if(i < j)
                    i = j;
                String s1 = s.substring(i + 1);
                proper = LoadSearchlightProperties(Statics.getTechnicsFile(), s1, class1);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            cls = class1;
            Spawn.add(cls, this);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            // System.out.println("SearchlightGeneric Mirror netInput(NetMsgInput netmsginput)");
            if(netmsginput.isGuaranted())
            {
                switch(netmsginput.readByte())
                {
                case 73: // 'I'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted);
                    }
                    short word0 = netmsginput.readShort();
                    float f = netmsginput.readFloat();
                    float f2 = netmsginput.readFloat();
                    if(word0 <= 0)
                    {
                        if(dying != 1)
                        {
                            aime.forgetAiming();
                            setGunAngles(f, f2);
                        }
                    } else
                    if(dying != 1)
                    {
                        setGunAngles(f, f2);
                        Die(null, word0, false);
                    }
                    return true;

                case 82: // 'R'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted1);
                    }
                    dying = 0;
                    setDiedFlag(false);
                    aime.forgetAiming();
                    activateMesh(true, false);
                    setDefaultLivePose();
                    lastTimeWhenFound = 0L;
                    return true;

                case 68: // 'D'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                        post(netmsgguaranted2);
                    }
                    short word1 = netmsginput.readShort();
                    float f1 = netmsginput.readFloat();
                    float f3 = netmsginput.readFloat();
                    if(word1 > 0 && dying != 1)
                    {
                        setGunAngles(f1, f3);
                        com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                        Actor actor2 = netobj2 != null ? ((ActorNet)netobj2).actor() : null;
                        Die(actor2, word1, true);
                    }
                    return true;
                }
                return false;
            }
            switch(netmsginput.readByte())
            {
            default:
                break;

            case 84: // 'T'
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(false);
                    postReal(Message.currentRealTime(), out);
                }
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                Track_Mirror(actor);
                break;

            case 70: // 'F'
                if(isMirrored())
                {
                    out.unLockAndSet(netmsginput, 1);
                    out.setIncludeTime(true);
                    postReal(Message.currentRealTime(), out);
                }
                com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                Actor actor1 = netobj1 != null ? ((ActorNet)netobj1).actor() : null;
                float f4 = netmsginput.readFloat();
                float f5 = 0.001F * (float)(Message.currentGameTime() - Time.current()) + f4;
                Fire_Mirror(actor1, f5);
                break;

            case 68: // 'D'
                out.unLockAndSet(netmsginput, 1);
                out.setIncludeTime(false);
                postRealTo(Message.currentRealTime(), masterChannel(), out);
                return true;
            }
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            // System.out.println("SearchlightGeneric Mirror Mirror(Actor actor, NetChannel netchannel, int i)");
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            // System.out.println("SearchlightGeneric Master netInput(NetMsgInput netmsginput)");
            if(netmsginput.isGuaranted())
                return true;
            if(netmsginput.readByte() != 68)
                return false;
            if(dying == 1)
            {
                return true;
            } else
            {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                Die(actor, (short)0, true);
                return true;
            }
        }

        public Master(Actor actor)
        {
            super(actor);
            // System.out.println("SearchlightGeneric Master Master(Actor actor)");
        }
    }

    class TickGame extends Interpolate
    {

        public boolean tick()
        {
//            // System.out.println("SearchlightGeneric TickGame tick()");
            //FIXME
            if (1==1) return true;
            if(dying == 1)
                if(respawnDelay-- <= 0L)
                {
                    if(!Mission.isDeathmatch())
                    {
                        if(aime != null)
                        {
                            aime.forgetAll();
                            aime = null;
                        }
                        return false;
                    }
                    if(!isNetMaster())
                    {
                        respawnDelay = 10000L;
                        return true;
                    } else
                    {
                        dying = 0;
                        setDiedFlag(false);
                        aime.forgetAiming();
                        activateMesh(true, false);
                        setDefaultLivePose();
                        send_RespawnCommand();
                        lastTimeWhenFound = 0L;
                        return true;
                    }
                } else
                {
                    return true;
                }
            aime.tick_();
            nightTime = World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK;
            if(lightWantedState)
            {
                if(nightTime && !lightIsOn)
                    activateMesh(true, true);
            } else
            if(lightIsOn)
                activateMesh(true, false);
            return true;
        }

        TickGame()
        {
            // System.out.println("SearchlightGeneric TickGame TickGame()");
        }
    }

    class TickBuilder extends Interpolate
    {

        public boolean tick()
        {
            // System.out.println("SearchlightGeneric TickBuilder tick()");
            nightTime = World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK;
            if(lightIsOn)
            {
                if(!nightTime)
                    activateMesh(true, false);
            } else
            if(nightTime)
                activateMesh(true, true);
            return true;
        }

        TickBuilder()
        {
            // System.out.println("SearchlightGeneric TickBuilder TickBuilder()");
        }
    }

    class MyDrawer extends ActorMeshDraw
    {

        public void killLightMap()
        {
            // System.out.println("SearchlightGeneric MyDrawer killLightMap()");
            if(lightMap != null)
            {
                lightMap.clear();
                lightMap = null;
            }
        }

        public void pushAngles(float f, float f1)
        {
            // System.out.println("SearchlightGeneric MyDrawer pushAngles(float f, float f1)");
            long l = Time.tickNext();
            if(interpolateAngles || t1 <= l)
            {
                if(t1 == l)
                {
                    fa.setDstDeg(f);
                    fb.setDstDeg(f1);
                    t1 = l;
                } else
                {
                    fa.setDeg(fa.getDstDeg(), f);
                    fb.setDeg(fb.getDstDeg(), f1);
                    t0 = t1;
                    t1 = l;
                }
                interpolateAngles = true;
            } else
            {
                t0 = t1 = l;
                fa.setDeg(f, f);
                fb.setDeg(f1, f1);
                interpolateAngles = true;
            }
        }

        public int preRender(Actor actor)
        {
            // System.out.println("SearchlightGeneric MyDrawer preRender(Actor actor)");
            if(interpolateAngles)
            {
                long l = Time.current();
                float f;
                float f1;
                if(t1 <= t0 || l >= t1)
                {
                    f = fa.getDstDeg();
                    f1 = fb.getDstDeg();
                } else
                if(l <= t0)
                {
                    f = fa.getSrcDeg();
                    f1 = fb.getSrcDeg();
                } else
                {
                    float f2 = (float)(l - t0) / (float)(t1 - t0);
                    f = fa.getDeg(f2);
                    f1 = fb.getDeg(f2);
                }
                ((ActorHMesh)actor).hierMesh().chunkSetAngles("Head", f, 0.0F, 0.0F);
                ((ActorHMesh)actor).hierMesh().chunkSetAngles("Gun", f1, 0.0F, 0.0F);
            }
            if(lightIsOn)
            {
                ((ActorHMesh)actor).hierMesh().setCurChunk("Ray_ON");
                ((ActorHMesh)actor).hierMesh().getChunkLTM(SearchlightGeneric.locLmatr);
                SearchlightGeneric.locLpos.set(10D, 0.0D, 0.0D);
                SearchlightGeneric.locLmatr.transform(SearchlightGeneric.locLpos);
                landLight.relPos.set(SearchlightGeneric.locLpos);
            }
            return super.preRender(actor);
        }

        long t0;
        long t1;
        AnglesFork fa;
        AnglesFork fb;
        public boolean interpolateAngles;

        MyDrawer()
        {
            // System.out.println("SearchlightGeneric MyDrawer MyDrawer()");
            t0 = 0L;
            t1 = 0L;
            fa = new AnglesFork();
            fb = new AnglesFork();
            interpolateAngles = false;
        }
    }

    public static class SearchlightProperties
    {

        public String meshName;
        public String meshDeadName;
        public String meshSummer;
        public String meshDesert;
        public String meshWinter;
        public String meshDeadSummer;
        public String meshDeadDesert;
        public String meshDeadWinter;
        public TableFunction2 fnShotPanzer;
        public TableFunction2 fnExplodePanzer;
        public float PANZER;
        public float PANZER_TNT_TYPE;
        public String explodeName;
        public int HITBY_MASK;
        public AnglesRange HEAD_YAW_RANGE;
        public float GUN_MIN_PITCH;
        public float GUN_STD_PITCH;
        public float GUN_MAX_PITCH;
        public float HEAD_MAX_YAW_SPEED;
        public float GUN_MAX_PITCH_SPEED;
        public float SEARCH_MAX_CONE_ANGLE;
        public float FOUND_MAX_CONE_ANGLE;
        public Point3f LIGHT_COLOR;
        public float LIGHT_LAND_I;
        public float LIGHT_LAND_R;
        public double Hclear;
        public double H;
        public double R0;
        public double R1;
        public double TANGA;

        public SearchlightProperties()
        {
            // System.out.println("SearchlightGeneric SearchlightProperties SearchlightProperties()");
            meshName = null;
            meshDeadName = null;
            meshSummer = null;
            meshDesert = null;
            meshWinter = null;
            meshDeadSummer = null;
            meshDeadDesert = null;
            meshDeadWinter = null;
            fnShotPanzer = null;
            fnExplodePanzer = null;
            PANZER = 0.001F;
            PANZER_TNT_TYPE = 1.0F;
            explodeName = null;
            HITBY_MASK = -1;
            HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            GUN_MIN_PITCH = 30F;
            GUN_STD_PITCH = 90F;
            GUN_MAX_PITCH = 90F;
            HEAD_MAX_YAW_SPEED = 720F;
            GUN_MAX_PITCH_SPEED = 60F;
            SEARCH_MAX_CONE_ANGLE = 30F;
            FOUND_MAX_CONE_ANGLE = 0.2F;
            LIGHT_COLOR = new Point3f(1.0F, 1.0F, 1.0F);
            LIGHT_LAND_I = 3F;
            LIGHT_LAND_R = 3F;
            Hclear = 0.0D;
            H = 0.0D;
            R0 = 1.0D;
            R1 = 2D;
            TANGA = 1.0D;
        }
    }


    public static void resetGame()
    {
        // System.out.println("SearchlightGeneric resetGame()");
        hashmap_ON.clear();
        hashmap_ALL.clear();
        hashmap_ALL_is_changed = true;
        someObjectWasLightedInPreviousCall = true;
        next_entry_get = null;
    }

    private static void register_ONOFF(SearchlightGeneric searchlightgeneric, boolean flag)
    {
        // System.out.println("SearchlightGeneric register_ONOFF(SearchlightGeneric searchlightgeneric, boolean flag)");
        if(flag)
            hashmap_ON.put(searchlightgeneric, searchlightgeneric);
        else
            hashmap_ON.remove(searchlightgeneric);
        if(!hashmap_ALL.containsKey(searchlightgeneric))
        {
            hashmap_ALL.put(searchlightgeneric, searchlightgeneric);
            hashmap_ALL_is_changed = true;
        }
    }

    public static int possibleGlare()
    {
//        // System.out.println("SearchlightGeneric possibleGlare()");
        if(!hashmap_ALL.isEmpty() && World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK)
        {
            if(hashmap_ALL_is_changed)
            {
                hashmap_ALL_is_changed = false;
                return 2;
            } else
            {
                return 1;
            }
        } else
        {
            return 0;
        }
    }

    public static int numlightsGlare()
    {
        // System.out.println("SearchlightGeneric numlightsGlare()");
        return hashmap_ALL.size();
    }

    private static float computeAngleFromCam(Point3d point3d)
    {
        // System.out.println("SearchlightGeneric computeAngleFromCam(Point3d point3d)");
        targRayDir.sub(point3d, nosePos);
        double d = targRayDir.length();
        if(d < 0.001D)
            return -1F;
        targRayDir.scale(1.0D / d);
        double d1 = noseDir.dot(targRayDir);
        if(d1 < 0.01D)
            return -1F;
        else
            return Geom.RAD2DEG((float)Math.acos(d1));
    }

    public static boolean computeGlare(LightsGlare lightsglare, Point3d point3d)
    {
        // System.out.println("SearchlightGeneric computeGlare(LightsGlare lightsglare, Point3d point3d)");
        Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()].pos.getRender(Cam2WorldLoc);
        Cam2WorldLoc.get(nosePos);
        noseDir.set(1.0D, 0.0D, 0.0D);
        Cam2WorldLoc.transform(noseDir);
        java.util.Map.Entry entry = hashmap_ALL.nextEntry(null);
//        LightsGlare _tmp = lightsglare;
        int i = hashmap_ALL.size() * 3;
        for(int j = 0; j < i; j += 3)
        {
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric)entry.getKey();
            entry = hashmap_ALL.nextEntry(entry);
            if(!searchlightgeneric.lightIsOn)
            {
                float f = computeAngleFromCam(searchlightgeneric.B);
                lightsglare.glareData[j] = f < 0.0F ? -1F : 0.0F;
                lightsglare.glareData[j + 1] = f;
            } else
            {
                double d = searchlightgeneric.L.dot(nosePos) - searchlightgeneric.L.dot(searchlightgeneric.B);
                if(d <= 0.0D || d >= searchlightgeneric.prop.H)
                {
                    float f1 = computeAngleFromCam(searchlightgeneric.B);
                    lightsglare.glareData[j] = f1 < 0.0F ? -1F : 0.0F;
                    lightsglare.glareData[j + 1] = f1;
                } else
                {
                    locLpos.scaleAdd(d, searchlightgeneric.L, searchlightgeneric.B);
                    d /= searchlightgeneric.prop.H;
                    double d1 = searchlightgeneric.prop.R0 + (searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d;
                    tmpv.sub(nosePos, locLpos);
                    if(tmpv.lengthSquared() >= d1 * d1)
                    {
                        float f2 = computeAngleFromCam(searchlightgeneric.B);
                        lightsglare.glareData[j] = f2 < 0.0F ? -1F : 0.0F;
                        lightsglare.glareData[j + 1] = f2;
                    } else
                    {
                        double d2 = (1.0D - d) * (double)searchlightgeneric.prop.LIGHT_LAND_I;
                        if(d2 <= 0.0D)
                        {
                            float f3 = computeAngleFromCam(searchlightgeneric.B);
                            lightsglare.glareData[j] = f3 < 0.0F ? -1F : 0.0F;
                            lightsglare.glareData[j + 1] = f3;
                        } else
                        {
                            float f4 = lightsglare.computeFlash(searchlightgeneric, searchlightgeneric.B, accumColor);
                            if(f4 > 0.0F)
                                f4 = (float)((double)f4 * d2);
                            lightsglare.glareData[j] = f4;
                            lightsglare.glareData[j + 1] = accumColor.x;
                        }
                    }
                }
            }
//            LightsGlare _tmp1 = lightsglare;
        }

        return true;
    }

    public static void getnextposandcolorGlare(Point3d point3d, Point3f point3f)
    {
        // System.out.println("SearchlightGeneric getnextposandcolorGlare(Point3d point3d, Point3f point3f)");
        if(point3d == null)
        {
            next_entry_get = hashmap_ALL.nextEntry(null);
            return;
        } else
        {
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric)next_entry_get.getKey();
            next_entry_get = hashmap_ALL.nextEntry(next_entry_get);
            point3d.set(searchlightgeneric.B);
            point3f.set(searchlightgeneric.prop.LIGHT_COLOR);
            return;
        }
    }

    private static void clearSearchlightSourcesInObjects()
    {
        // System.out.println("SearchlightGeneric clearSearchlightSourcesInObjects()");
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!(actor instanceof Aircraft) && !(actor instanceof Balloon) || actor.draw == null)
                continue;
            HashMapExt hashmapext = actor.draw.lightMap();
            if(hashmapext == null)
                continue;
            LightPointActor lightpointactor = (LightPointActor)hashmapext.remove("SL");
            if(lightpointactor != null)
                lightpointactor.destroy();
        }

    }

    public static void lightPlanesBySearchlights()
    {
//        // System.out.println("SearchlightGeneric lightPlanesBySearchlights()");
        //FIXME
        if (1==1) return;
        if(hashmap_ON.isEmpty())
        {
            if(someObjectWasLightedInPreviousCall)
            {
                clearSearchlightSourcesInObjects();
                someObjectWasLightedInPreviousCall = false;
            }
            return;
        }
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!(actor instanceof Aircraft) && !(actor instanceof Balloon))
                continue;
            actor.pos.getRender(planeP);
            double d = 0.0D;
            if(planeP.z - World.land().HQ(planeP.x, planeP.y) > 10D)
            {
//                boolean flag = false;
                accumColor.set(0.0F, 0.0F, 0.0F);
                accumDir.set(0.0D, 0.0D, 0.0D);
                for(java.util.Map.Entry entry = hashmap_ON.nextEntry(null); entry != null;)
                {
                    SearchlightGeneric searchlightgeneric = (SearchlightGeneric)entry.getKey();
                    double d1 = searchlightgeneric.L.dot(planeP) - searchlightgeneric.L.dot(searchlightgeneric.B);
                    if(d1 <= 0.0D || d1 >= searchlightgeneric.prop.H)
                    {
                        entry = hashmap_ON.nextEntry(entry);
                    } else
                    {
                        locLpos.scaleAdd(d1, searchlightgeneric.L, searchlightgeneric.B);
                        d1 /= searchlightgeneric.prop.H;
                        double d2 = searchlightgeneric.prop.R0 + (searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d1;
                        double d3 = d2 + (double)actor.collisionR() * 0.75D;
                        tmpv.sub(planeP, locLpos);
                        double d4 = tmpv.lengthSquared();
                        if(d4 >= d3 * d3)
                            entry = hashmap_ON.nextEntry(entry);
                        else
                        if(d3 - d2 <= 0.0D)
                        {
                            entry = hashmap_ON.nextEntry(entry);
                        } else
                        {
                            d4 = Math.sqrt(d4);
                            double d5 = 1.0D - (d4 - d2) / (d3 - d2);
                            if(d5 >= 1.0D)
                                d5 = 1.0D;
                            double d6 = (1.0D - d1) * (double)searchlightgeneric.prop.LIGHT_LAND_I;
                            d6 *= d5;
                            if(d6 <= 0.0D)
                            {
                                entry = hashmap_ON.nextEntry(entry);
                            } else
                            {
                                d += d6;
                                accumColor.scaleAdd((float)d6, searchlightgeneric.prop.LIGHT_COLOR);
                                accumDir.scaleAdd(d6, searchlightgeneric.L);
                                entry = hashmap_ON.nextEntry(entry);
                            }
                        }
                    }
                }

            }
            if(actor.draw == null)
                continue;
            HashMapExt hashmapext = actor.draw.lightMap();
            if(hashmapext == null)
                continue;
            LightPointActor lightpointactor = (LightPointActor)hashmapext.get("SL");
            if(d <= 0.0D)
            {
                if(lightpointactor != null)
                    lightpointactor.light.setEmit(0.0F, 0.0F);
                continue;
            }
            someObjectWasLightedInPreviousCall = true;
            if(lightpointactor == null)
            {
                lightpointactor = new LightPointActor(new LightPoint());
                hashmapext.put("SL", lightpointactor);
            }
            accumDir.normalize();
            accumDir.negate();
            float f = 100123F;
            accumDir.scale(1000D);
            actor.pos.getRender(locLloc);
            locLloc.transformInv(accumDir);
            float f1 = Math.max(accumColor.x, Math.max(accumColor.y, accumColor.z));
            accumColor.scale(1.0F / f1);
            if(d >= 1.3500000238418579D)
                d = 1.3500000238418579D;
            lightpointactor.light.setEmit((float)d, f);
            lightpointactor.light.setColor(accumColor.x, accumColor.y, accumColor.z);
            lightpointactor.relPos.set(accumDir);
        }

    }

    public static void lightCloudsBySearchlights()
    {
        //FIXME
        if (1==1) return;
//        // System.out.println("SearchlightGeneric lightCloudsBySearchlights()");
        if(hashmap_ON.isEmpty())
            return;
        int i = Mission.curCloudsType();
        if(i <= 0)
            return;
        double d = Mission.curCloudsHeight();
        java.util.Map.Entry entry = hashmap_ON.nextEntry(null);
        do
        {
            if(entry == null)
                break;
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric)entry.getKey();
            entry = hashmap_ON.nextEntry(entry);
            if(searchlightgeneric.L.z > 0.01D)
            {
                double d1 = (d - searchlightgeneric.B.z) / searchlightgeneric.L.z;
                if(d1 >= 0.0D && d1 < searchlightgeneric.prop.H)
                {
                    p3d.scaleAdd(d1, searchlightgeneric.L, searchlightgeneric.B);
                    d1 /= searchlightgeneric.prop.H;
                    double d2 = searchlightgeneric.prop.R0 + (searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d1;
                    d2 *= 5D;
                    if(d2 <= 100D)
                        d2 = 100D;
                    if(d2 >= 400D)
                        d2 = 400D;
                    double d3 = 3F * searchlightgeneric.prop.LIGHT_LAND_I;
                    d3 *= 1.0D - d1;
                    if(d3 > 0.0D)
                    {
                        searchlightgeneric.cloudLight.setPos(p3d);
                        searchlightgeneric.cloudLight.setEmit((float)d3, (float)d2);
                        searchlightgeneric.cloudLight.addToRender();
                    }
                }
            }
        } while(true);
    }

    public static final double Rnd(double d, double d1)
    {
        // System.out.println("SearchlightGeneric Rnd(double d, double d1)");
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        // System.out.println("SearchlightGeneric Rnd(float f, float f1)");
        return World.Rnd().nextFloat(f, f1);
    }

    private boolean RndB(float f)
    {
        // System.out.println("SearchlightGeneric RndB(float f)");
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    private static final long SecsToTicks(float f)
    {
        // System.out.println("SearchlightGeneric SecsToTicks(float f)");
        long l = (long)(0.5D + (double)(f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    protected final boolean Head360()
    {
        // System.out.println("SearchlightGeneric Head360()");
        return prop.HEAD_YAW_RANGE.fullcircle();
    }

    private void activateMesh(boolean flag, boolean flag1)
    {
        // System.out.println("SearchlightGeneric activateMesh(boolean flag, boolean flag1)");
        if(!flag)
        {
            hierMesh().chunkVisible("Body", false);
            hierMesh().chunkVisible("Head", false);
            hierMesh().chunkVisible("Gun", false);
            hierMesh().chunkVisible("Ray_ON", false);
            hierMesh().chunkVisible("Ray_OFF", false);
            hierMesh().chunkVisible("Dead", true);
            lightIsOn = false;
            lightWantedState = false;
            if(landLight != null)
            {
                landLight.destroy();
                landLight = null;
            }
            ((MyDrawer)draw).killLightMap();
            register_ONOFF(this, false);
            return;
        }
        hierMesh().chunkVisible("Body", true);
        hierMesh().chunkVisible("Head", true);
        hierMesh().chunkVisible("Gun", true);
        hierMesh().chunkVisible("Ray_ON", flag1);
        hierMesh().chunkVisible("Ray_OFF", !flag1);
        hierMesh().chunkVisible("Dead", false);
        lightIsOn = flag1;
        lightWantedState = flag1;
        if(landLight != null)
        {
            landLight.destroy();
            landLight = null;
        }
        ((MyDrawer)draw).killLightMap();
        if(flag1)
        {
            landLight = new LightPointActor(new LightPointWorld(), new Point3d(0.0D, 0.0D, 0.0D));
            landLight.light.setColor(prop.LIGHT_COLOR.x, prop.LIGHT_COLOR.y, prop.LIGHT_COLOR.z);
            landLight.light.setEmit(prop.LIGHT_LAND_I, prop.LIGHT_LAND_R);
            draw.lightMap().put("light", landLight);
            cloudLight.setColor(prop.LIGHT_COLOR.x, prop.LIGHT_COLOR.y, prop.LIGHT_COLOR.z);
        }
        register_ONOFF(this, flag1);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        // System.out.println("SearchlightGeneric msgCollisionRequest(Actor actor, boolean aflag[])");
        if((actor instanceof ActorMesh) && ((ActorMesh)actor).isStaticPos())
        {
            aflag[0] = false;
            return;
        } else
        {
            return;
        }
    }

    public void msgShot(Shot shot)
    {
        // System.out.println("SearchlightGeneric msgShot(Shot shot)");
        shot.bodyMaterial = 2;
        if(dying != 0)
            return;
        if(shot.power <= 0.0F)
            return;
        if(isNetMirror() && shot.isMirage())
            return;
        if(shot.powerType == 1)
            if(RndB(0.15F))
            {
                return;
            } else
            {
                Die(shot.initiator, (short)0, true);
                return;
            }
        float f = prop.PANZER;
        f *= Rnd(0.93F, 1.07F);
        float f1 = prop.fnShotPanzer.Value(shot.power, f);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(shot.initiator, (short)0, true);
    }

    public void msgExplosion(Explosion explosion)
    {
        // System.out.println("SearchlightGeneric msgExplosion(Explosion explosion)");
        if(dying != 0)
            return;
        if(isNetMirror() && explosion.isMirage())
            return;
        if(explosion.power <= 0.0F)
            return;
//        Explosion _tmp = explosion;
        if(explosion.powerType == 1)
        {
            if(TankGeneric.splintersKill(explosion, prop.fnShotPanzer, 0.5F, 0.5F, this, 0.7F, 0.25F, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER))
                Die(explosion.initiator, (short)0, true);
            return;
        }
//        Explosion _tmp1 = explosion;
        if(explosion.powerType == 2 && explosion.chunkName != null)
        {
            Die(explosion.initiator, (short)0, true);
            return;
        }
        float f;
        if(explosion.chunkName != null)
            f = 0.5F * explosion.power;
        else
            f = explosion.receivedTNTpower(this);
        f *= Rnd(0.95F, 1.05F);
        float f1 = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(explosion.initiator, (short)0, true);
    }

    private void ShowExplode(float f)
    {
        // System.out.println("SearchlightGeneric ShowExplode(float f)");
        if(f > 0.0F)
            f = Rnd(f, f * 1.6F);
        Explosions.runByName(prop.explodeName, this, "", "", f);
    }

    private float[] computeDeathPose(short word0)
    {
        // System.out.println("SearchlightGeneric computeDeathPose(short word0)");
        RangeRandom rangerandom = new RangeRandom(word0);
        float af[] = new float[10];
        af[0] = headYaw + rangerandom.nextFloat(-15F, 15F);
        af[1] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 9F);
        af[2] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 9F);
        af[3] = -gunPitch + rangerandom.nextFloat(-15F, 15F);
        af[4] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(2.0F, 5F);
        af[5] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(5F, 9F);
        af[6] = 0.0F;
        af[7] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(4F, 8F);
        af[8] = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(7F, 12F);
        af[9] = heightAboveLandSurface - rangerandom.nextFloat(0.0F, 0.25F);
        return af;
    }

    private void Die(Actor actor, short word0, boolean flag)
    {
        // System.out.println("SearchlightGeneric Die(Actor actor, short word0, boolean flag)");
        if(dying != 0)
            return;
        if(word0 <= 0)
        {
            if(isNetMirror())
            {
                send_DeathRequest(actor);
                return;
            }
            word0 = (short)(int)Rnd(1.0F, 30000F);
        }
        deathSeed = word0;
        dying = 1;
        World.onActorDied(this, actor);
        if(aime != null)
            aime.forgetAiming();
        float af[] = computeDeathPose(word0);
        hierMesh().chunkSetAngles("Head", af[0], af[1], af[2]);
        hierMesh().chunkSetAngles("Gun", af[3], af[4], af[5]);
        hierMesh().chunkSetAngles("Body", af[6], af[7], af[8]);
        heightAboveLandSurface = af[9];
        ((MyDrawer)draw).interpolateAngles = false;
        Align();
        activateMesh(false, false);
        if(flag)
            ShowExplode(14F);
        if(flag)
            send_DeathCommand(actor);
    }

    private void setGunAngles(float f, float f1)
    {
        // System.out.println("SearchlightGeneric setGunAngles(float f, float f1)");
        headYaw = f;
        gunPitch = f1;
        hierMesh().chunkSetAngles("Head", headYaw, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Gun", -gunPitch, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        ((MyDrawer)draw).pushAngles(headYaw, -gunPitch);
        hierMesh().setCurChunk("Ray_ON");
        hierMesh().getChunkLocObj(locLloc);
        locLloc.add(pos.getAbs());
        L.set(1.0D, 0.0D, 0.0D);
        locLloc.transform(L);
        L.normalize();
        locLloc.get(B);
    }

    public void destroy()
    {
        // System.out.println("SearchlightGeneric destroy()");
        if(isDestroyed())
            return;
        if(aime != null)
        {
            aime.forgetAll();
            aime = null;
        }
        super.destroy();
    }

    public Object getSwitchListener(Message message)
    {
        // System.out.println("SearchlightGeneric getSwitchListener(Message message)");
        return this;
    }

    public boolean isStaticPos()
    {
        // System.out.println("SearchlightGeneric isStaticPos()");
        return false;
    }

    private void setDefaultLivePose()
    {
        // System.out.println("SearchlightGeneric setDefaultLivePose()");
        Matrix4d matrix4d = new Matrix4d();
        heightAboveLandSurface = 0.0F;
        int i = hierMesh().hookFind("Ground_Level");
        if(i != -1)
        {
            hierMesh().hookMatrix(i, matrix4d);
            heightAboveLandSurface = (float)(-matrix4d.m23);
        }
        Point3d point3d = new Point3d();
        hierMesh().hookMatrix(hierMesh().hookFind("ConeNear"), matrix4d);
        point3d.x = matrix4d.m03;
        point3d.y = matrix4d.m13;
        point3d.z = matrix4d.m23;
        Point3d point3d1 = new Point3d();
        hierMesh().hookMatrix(hierMesh().hookFind("ConeFar"), matrix4d);
        point3d1.x = matrix4d.m03;
        point3d1.y = matrix4d.m13;
        point3d1.z = matrix4d.m23;
        hierMesh().setCurChunk("Ray_ON");
        hierMesh().getChunkLocObj(locLloc);
        locLloc.transformInv(point3d);
        locLloc.transformInv(point3d1);
        if(Math.abs(point3d.x) > 0.10000000000000001D)
            System.out.println("**** Wrong position or orientation of ConeNear");
        prop.R0 = Math.sqrt(point3d.y * point3d.y + point3d.z * point3d.z);
        prop.R1 = Math.sqrt(point3d1.y * point3d1.y + point3d1.z * point3d1.z);
        if(point3d1.x - point3d.x < 0.10000000000000001D || prop.R1 < prop.R0)
            System.out.println("**** Wrong position or orientation of ConeFar");
        prop.TANGA = (prop.R1 - prop.R0) / (point3d1.x - point3d.x);
        float f = 1.0F;
        int j = Mission.curCloudsType();
        switch(j)
        {
        case 1: // '\001'
            f = 1.0F;
            break;

        case 2: // '\002'
            f = 0.6F;
            break;

        case 3: // '\003'
            f = 0.3F;
            break;

        case 4: // '\004'
            f = 0.12F;
            break;

        case 5: // '\005'
            f = 0.12F;
            break;

        case 6: // '\006'
            f = 0.12F;
            break;
        }
        prop.H = prop.Hclear * (double)f;
        prop.R1 = prop.R0 + prop.H * prop.TANGA;
        setGunAngles(0.0F, prop.GUN_STD_PITCH);
        ((MyDrawer)draw).interpolateAngles = false;
        Align();
    }

    protected SearchlightGeneric()
    {
        this(constr_arg1, constr_arg2);
        // System.out.println("SearchlightGeneric SearchlightGeneric()");
    }

    private SearchlightGeneric(SearchlightProperties searchlightproperties, ActorSpawnArg actorspawnarg)
    {
        super(searchlightproperties.meshName);
        // System.out.println("SearchlightGeneric SearchlightGeneric(SearchlightProperties searchlightproperties, ActorSpawnArg actorspawnarg)");
        prop = null;
        cloudLight = new LightPoint();
        dying = 0;
        respawnDelay = 0L;
        B = new Point3d();
        L = new Vector3d();
        outCommand = new NetMsgFiltered();
        prop = searchlightproperties;
        actorspawnarg.setStationary(this);
        collide(true);
        drawing(true);
        draw = new MyDrawer();
        lastTimeWhenFound = 0L;
        smoothMove = false;
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        startDelay = 0L;
        if(actorspawnarg.timeLenExist)
        {
            startDelay = (long)(actorspawnarg.timeLen * 60F * 1000F + 0.5F);
            if(startDelay < 0L)
                startDelay = 0L;
        }
        headYaw = 0.0F;
        gunPitch = 0.0F;
        activateMesh(true, false);
        setDefaultLivePose();
        if(Time.isRealOnly())
        {
            flags |= 0x2000;
            if(!interpEnd("tick_builder"))
            {
                aime = null;
                interpPut(new TickBuilder(), "tick_builder", Time.currentReal(), null);
            }
        } else
        {
            startMove();
        }
    }

    private void Align()
    {
        // System.out.println("SearchlightGeneric Align()");
        pos.getAbs(p3d);
        p3d.z = Engine.land().HQ(p3d.x, p3d.y) + (double)heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        pos.setAbs(p3d, o);
    }

    public void align()
    {
        // System.out.println("SearchlightGeneric align()");
        Align();
    }

    public void startMove()
    {
        // System.out.println("SearchlightGeneric startMove()");
        if(!interpEnd("tick_game"))
        {
            if(aime != null)
            {
                aime.forgetAll();
                aime = null;
            }
            aime = new Aim(this, isNetMirror());
            interpPut(new TickGame(), "tick_game", Time.current(), null);
        }
    }

    public int WeaponsMask()
    {
        // System.out.println("SearchlightGeneric WeaponsMask()");
        return -1;
    }

    public int HitbyMask()
    {
//        // System.out.println("SearchlightGeneric HitbyMask()");
        return prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        // System.out.println("SearchlightGeneric chooseBulletType(BulletProperties abulletproperties[])");
        if(dying != 0)
            return -1;
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 0;
        if(abulletproperties[1].power <= 0.0F)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 0;
        if(abulletproperties[1].cumulativePower > 0.0F)
            return 1;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        return abulletproperties[0].powerType != 2 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        // System.out.println("SearchlightGeneric chooseShotpoint(BulletProperties bulletproperties)");
        return dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        // System.out.println("SearchlightGeneric getShotpointOffset(int i, Point3d point3d)");
        if(dying != 0)
            return false;
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    public float AttackMaxDistance()
    {
        // System.out.println("SearchlightGeneric AttackMaxDistance()");
        return (float)prop.H;
    }

    public boolean unmovableInFuture()
    {
        // System.out.println("SearchlightGeneric unmovableInFuture()");
        return true;
    }

    public void collisionDeath()
    {
        // System.out.println("SearchlightGeneric collisionDeath()");
        if(isNet())
        {
            return;
        } else
        {
            ShowExplode(-1F);
            destroy();
            return;
        }
    }

    public float futurePosition(float f, Point3d point3d)
    {
        // System.out.println("SearchlightGeneric futurePosition(float f, Point3d point3d)");
        pos.getAbs(point3d);
        return f > 0.0F ? f : 0.0F;
    }

    private void send_DeathCommand(Actor actor)
    {
        // System.out.println("SearchlightGeneric send_DeathCommand(Actor actor)");
        if(!isNetMaster())
            return;
        if(Mission.isDeathmatch())
        {
            float f = Mission.respawnTime("Searchlight");
            respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
        } else
        {
            respawnDelay = 0L;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeShort(deathSeed);
            netmsgguaranted.writeFloat(headYaw);
            netmsgguaranted.writeFloat(gunPitch);
            netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_RespawnCommand()
    {
        // System.out.println("SearchlightGeneric send_RespawnCommand()");
        if(!isNetMaster() || !Mission.isDeathmatch())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(82);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_FireCommand(Actor actor, float f)
    {
        // System.out.println("SearchlightGeneric send_FireCommand(Actor actor, float f)");
        if(!isNetMaster())
            return;
        if(!net.isMirrored())
            return;
        if(!Actor.isValid(actor) || !actor.isNet())
            return;
        if(f < 0.0F)
            try
            {
                outCommand.unLockAndClear();
                outCommand.writeByte(84);
                outCommand.writeNetObj(actor.net);
                outCommand.setIncludeTime(false);
                net.post(Time.current(), outCommand);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        else
            try
            {
                outCommand.unLockAndClear();
                outCommand.writeByte(70);
                outCommand.writeFloat(f);
                outCommand.writeNetObj(actor.net);
                outCommand.setIncludeTime(true);
                net.post(Time.current(), outCommand);
            }
            catch(Exception exception1)
            {
                System.out.println(exception1.getMessage());
                exception1.printStackTrace();
            }
    }

    private void send_DeathRequest(Actor actor)
    {
        // System.out.println("SearchlightGeneric send_DeathRequest(Actor actor)");
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        try
        {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            netmsgfiltered.setIncludeTime(false);
            net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void createNetObject(NetChannel netchannel, int i)
    {
        // System.out.println("SearchlightGeneric createNetObject(NetChannel netchannel, int i)");
        if(netchannel == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        // System.out.println("SearchlightGeneric netFirstUpdate(NetChannel netchannel)");
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if(dying == 0)
            netmsgguaranted.writeShort(0);
        else
            netmsgguaranted.writeShort(deathSeed);
        netmsgguaranted.writeFloat(headYaw);
        netmsgguaranted.writeFloat(gunPitch);
        net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim)
    {
        // System.out.println("SearchlightGeneric getReloadingTime(Aim aim)");
        return 0.0F;
    }

    public float chainFireTime(Aim aim)
    {
        // System.out.println("SearchlightGeneric chainFireTime(Aim aim)");
        return 3F;
    }

    public float probabKeepSameEnemy(Actor actor)
    {
        // System.out.println("SearchlightGeneric probabKeepSameEnemy(Actor actor)");
        return Time.current() - lastTimeWhenFound > TM_TO_REMEMBER_BAD_TARGET ? 0.0F : 1.0F;
    }

    public float minTimeRelaxAfterFight()
    {
        // System.out.println("SearchlightGeneric minTimeRelaxAfterFight()");
        return 0.0F;
    }

    public void gunStartParking(Aim aim)
    {
        // System.out.println("SearchlightGeneric gunStartParking(Aim aim)");
        lightWantedState = false;
        aim.setRotationForParking(headYaw, gunPitch, 0.0F, prop.GUN_STD_PITCH, prop.HEAD_YAW_RANGE, prop.HEAD_MAX_YAW_SPEED, prop.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, Aim aim)
    {
        // System.out.println("SearchlightGeneric gunInMove(boolean flag, Aim aim)");
        float f = aim.t();
        if(!flag && smoothMove)
            f = 0.5F * (1.0F + Geom.sinDeg(f * 180F - 90F));
        lightWantedState = !flag;
        float f1 = aim.anglesYaw.getDeg(f);
        float f2 = aim.anglesPitch.getDeg(f);
        setGunAngles(f1, f2);
        pos.inValidate(false);
        Actor actor = aim.getEnemy();
        if(Actor.isValid(actor) && actor.isAlive() && actor.getArmy() != 0)
        {
            Point3d point3d = new Point3d();
            actor.pos.getAbs(point3d);
            Point3d point3d1 = new Point3d();
            pos.getAbs(point3d1);
            Vector3d vector3d = new Vector3d();
            vector3d.sub(point3d, point3d1);
            Orient orient = new Orient();
            orient.setYPR(pos.getAbsOrient().getYaw() + f1, f2, 0.0F);
            Vector3d vector3d1 = new Vector3d(1.0D, 0.0D, 0.0D);
            orient.transform(vector3d1);
            double d = vector3d.length();
            double d1 = vector3d1.dot(vector3d);
            if(d1 > 0.0D)
            {
                double d2 = Math.sqrt(d * d - d1 * d1);
                float f3 = (float)(prop.R0 + prop.TANGA * d);
                float f4 = actor.collisionR();
                float f5 = (float)(d2 - (double)f4 - (double)f3);
                if(f5 <= 0.0F)
                {
                    lastTimeWhenFound = Time.current();
                    ((Aircraft)actor).tmSearchlighted = lastTimeWhenFound;
                }
            }
        }
    }

    public Actor findEnemy(Aim aim)
    {
        // System.out.println("SearchlightGeneric findEnemy(Aim aim)");
        if(!nightTime || dying == 1)
            return null;
        if(isNetMirror())
            return null;
        if(Time.current() < startDelay)
        {
            return null;
        } else
        {
            lastTimeWhenFound = Time.current();
            Actor actor = null;
            NearestEnemies.set(WeaponsMask());
            actor = NearestEnemies.getAFoundFlyingPlane(pos.getAbsPoint(), AttackMaxDistance(), getArmy(), 250F);
            return actor;
        }
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
    {
        // System.out.println("SearchlightGeneric enterToFireMode(int i, Actor actor, float f, Aim aim)");
        if(!isNetMirror())
            send_FireCommand(actor, i != 0 ? f : -1F);
        lightWantedState = true;
        return true;
    }

    private void Track_Mirror(Actor actor)
    {
        // System.out.println("SearchlightGeneric Track_Mirror(Actor actor)");
        if(dying == 1)
            return;
        if(actor == null)
            return;
        if(aime == null)
        {
            return;
        } else
        {
            lastTimeWhenFound = Time.current();
            lightWantedState = true;
            aime.passive_StartFiring(0, actor, 0, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(Actor actor, float f)
    {
        // System.out.println("SearchlightGeneric Fire_Mirror(Actor actor, float f)");
        if(dying == 1)
            return;
        if(actor == null)
            return;
        if(aime == null)
            return;
        if(f <= 0.2F)
            f = 0.2F;
        if(f >= 15F)
            f = 15F;
        lastTimeWhenFound = Time.current();
        lightWantedState = true;
        aime.passive_StartFiring(1, actor, 0, f);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag)
    {
        // System.out.println("SearchlightGeneric targetGun(Aim aim, Actor actor, float f, boolean flag)");
        flag = false;
        smoothMove = false;
        if(!nightTime || dying == 1)
            return 0;
        if(!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
            return 0;
        lightWantedState = true;
        float f1 = f * Rnd(0.8F, 1.2F);
        Point3d point3d = new Point3d();
        actor.futurePosition(f1, point3d);
        Point3d point3d1 = new Point3d();
        pos.getAbs(point3d1);
        point3d1.z += 2D;
        Vector3d vector3d = new Vector3d();
        vector3d.sub(point3d, point3d1);
        vector3d.normalize();
        Orient orient = new Orient();
        orient.setAT0(vector3d);
        float f3 = ((Aircraft)actor).tmSearchlighted;
        float f2;
        if(f3 == 0L)
        {
            f2 = prop.SEARCH_MAX_CONE_ANGLE / 2.0F;
            smoothMove = true;
        } else
        if(Time.current() - f3 > TM_LOOSE_IN_DARK)
        {
            float f5 = (float)(Time.current() - f3) / 1000F;
            if(f5 >= 2.0F)
            {
                f5 = 2.0F;
                smoothMove = true;
            }
            f2 = (prop.SEARCH_MAX_CONE_ANGLE / 4F) * f5;
        } else
        {
            f2 = prop.FOUND_MAX_CONE_ANGLE / 2.0F;
        }
        orient.increment(Rnd(-1F, 1.0F) * f2, Rnd(-1F, 1.0F) * f2, 0.0F);
        vector3d.set(1.0D, 0.0D, 0.0D);
        orient.transform(vector3d);
        float f4;
        if(flag)
        {
            f3 = 99999F;
            f4 = 99999F;
        } else
        {
            f3 = prop.HEAD_MAX_YAW_SPEED;
            f4 = prop.GUN_MAX_PITCH_SPEED;
        }
        int i = aim.setRotationForTargeting(this, pos.getAbs().getOrient(), point3d1, headYaw, gunPitch, vector3d, 0.0F, f1, prop.HEAD_YAW_RANGE, prop.GUN_MIN_PITCH, prop.GUN_MAX_PITCH, f3, f4, 0.0F);
        if(i == 2 && Time.current() - lastTimeWhenFound > TM_TO_REMEMBER_BAD_TARGET)
        {
            aim.anglesYaw.setDeg(headYaw);
            aim.anglesPitch.setDeg(gunPitch);
            return 0;
        } else
        {
            return i;
        }
    }

    public void singleShot(Aim aim)
    {
        // System.out.println("SearchlightGeneric singleShot(Aim aim)");
    }

    public void startFire(Aim aim)
    {
        // System.out.println("SearchlightGeneric startFire(Aim aim)");
    }

    public void continueFire(Aim aim)
    {
        // System.out.println("SearchlightGeneric continueFire(Aim aim)");
    }

    public void stopFire(Aim aim)
    {
        // System.out.println("SearchlightGeneric stopFire(Aim aim)");
    }

    private static final int TM_LOOSE_IN_DARK = 1000;
    private static final int TM_TO_REMEMBER_BAD_TARGET = 22000;
    private static final float SUNZ_MIN_TO_WORK = -0.22F;
    private SearchlightProperties prop;
    private float heightAboveLandSurface;
    private Aim aime;
    private float headYaw;
    private float gunPitch;
    private long startDelay;
    private long lastTimeWhenFound;
    private boolean smoothMove;
    private boolean lightWantedState;
    private boolean lightIsOn;
    private boolean nightTime;
    private LightPointActor landLight;
    private LightPoint cloudLight;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private short deathSeed;
    private long respawnDelay;
    private Point3d B;
    private Vector3d L;
    private static SearchlightProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p3d = new Point3d();
    private static Orient o = new Orient();
//    private static Vector3f n = new Vector3f();
    private static Vector3d tmpv = new Vector3d();
    private static Loc locLloc = new Loc();
    private static Point3d locLpos = new Point3d();
    private static Matrix4d locLmatr = new Matrix4d();
    private static boolean someObjectWasLightedInPreviousCall = true;
    private static HashMapExt hashmap_ON = new HashMapExt();
    private static HashMapExt hashmap_ALL = new HashMapExt();
    private static boolean hashmap_ALL_is_changed = true;
    private static java.util.Map.Entry next_entry_get = null;
    private static Point3f accumColor = new Point3f();
    private static Vector3d accumDir = new Vector3d();
    private static Point3d planeP = new Point3d();
    public static Point3d nosePos = new Point3d();
    private static Vector3d noseDir = new Vector3d();
    private static Vector3d targRayDir = new Vector3d();
//    private static Point3d targetPos = new Point3d();
    private static Loc Cam2WorldLoc = new Loc();
    private NetMsgFiltered outCommand;
}