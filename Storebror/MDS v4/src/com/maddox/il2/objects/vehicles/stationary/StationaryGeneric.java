/*4.10.1 class*/
package com.maddox.il2.objects.vehicles.stationary;
import java.io.IOException;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.effects.Explosions;
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
import com.maddox.util.TableFunction2;

public abstract class StationaryGeneric extends ActorHMesh implements MsgExplosionListener, MsgShotListener, Prey, Obstacle, ActorAlign
{
	private StationaryProperties prop = null;
	private float heightAboveLandSurface;
	private int dying = 0;
	static final int DYING_NONE = 0;
	static final int DYING_DEAD = 1;
	private static StationaryProperties constr_arg1 = null;
	private static ActorSpawnArg constr_arg2 = null;
	private static Point3d p = new Point3d();
	private static Orient o = new Orient();
	private static Vector3f n = new Vector3f();
	
	public static class SPAWN implements ActorSpawn
	{
		public Class cls;
		public StationaryProperties proper;
		
		private static float getF(SectFile sectfile, String string, String string_0_, float f, float f_1_)
		{
			float f_2_ = sectfile.get(string, string_0_, -9865.345F);
			if (f_2_ == -9865.345F || f_2_ < f || f_2_ > f_1_)
			{
				if (f_2_ == -9865.345F)
					System.out.println("Stationary: Parameter [" + string + "]:<" + string_0_ + "> " + "not found");
				else
					System.out.println("Stationary: Value of [" + string + "]:<" + string_0_ + "> (" + f_2_ + ")" + " is out of range (" + f + ";" + f_1_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_2_;
		}
		
		private static String getS(SectFile sectfile, String string, String string_3_)
		{
			String string_4_ = sectfile.get(string, string_3_);
			if (string_4_ == null || string_4_.length() <= 0)
			{
				System.out.print("Stationary: Parameter [" + string + "]:<" + string_3_ + "> ");
				System.out.println(string_4_ == null ? "not found" : "is empty");
				throw new RuntimeException("Can't set property");
			}
			return string_4_;
		}
		
		private static String getS(SectFile sectfile, String string, String string_5_, String string_6_)
		{
			String string_7_ = sectfile.get(string, string_5_);
			if (string_7_ == null || string_7_.length() <= 0)
				return string_6_;
			return string_7_;
		}
		
		private static StationaryProperties LoadStationaryProperties(SectFile sectfile, String string, Class var_class)
		{
			StationaryProperties stationaryproperties = new StationaryProperties();
			String string_8_ = getS(sectfile, string, "PanzerType", null);
			if (string_8_ == null)
				string_8_ = "Tank";
			stationaryproperties.fnShotPanzer = TableFunctions.GetFunc2(string_8_ + "ShotPanzer");
			stationaryproperties.fnExplodePanzer = TableFunctions.GetFunc2(string_8_ + "ExplodePanzer");
			stationaryproperties.PANZER_TNT_TYPE = getF(sectfile, string, "PanzerSubtype", 0.0F, 100.0F);
			stationaryproperties.meshSummer = getS(sectfile, string, "MeshSummer");
			stationaryproperties.meshDesert = getS(sectfile, string, "MeshDesert", stationaryproperties.meshSummer);
			stationaryproperties.meshWinter = getS(sectfile, string, "MeshWinter", stationaryproperties.meshSummer);
			stationaryproperties.meshSummer1 = getS(sectfile, string, "MeshSummerDamage", null);
			stationaryproperties.meshDesert1 = getS(sectfile, string, "MeshDesertDamage", stationaryproperties.meshSummer1);
			stationaryproperties.meshWinter1 = getS(sectfile, string, "MeshWinterDamage", stationaryproperties.meshSummer1);
			int i = ((stationaryproperties.meshSummer1 == null ? 1 : 0) + (stationaryproperties.meshDesert1 == null ? 1 : 0) + (stationaryproperties.meshWinter1 == null ? 1 : 0));
			if (i != 0 && i != 3)
			{
				System.out.println("Stationary: Uncomplete set of damage meshes for '" + string + "'");
				throw new RuntimeException("Can't register stationary object");
			}
			stationaryproperties.explodeName = getS(sectfile, string, "Explode", "Stationary");
			stationaryproperties.PANZER_BODY_FRONT = getF(sectfile, string, "PanzerBodyFront", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerBodyBack", -9865.345F) == -9865.345F)
			{
				stationaryproperties.PANZER_BODY_BACK = stationaryproperties.PANZER_BODY_FRONT;
				stationaryproperties.PANZER_BODY_SIDE = stationaryproperties.PANZER_BODY_FRONT;
				stationaryproperties.PANZER_BODY_TOP = stationaryproperties.PANZER_BODY_FRONT;
			}
			else
			{
				stationaryproperties.PANZER_BODY_BACK = getF(sectfile, string, "PanzerBodyBack", 0.0010F, 9.999F);
				stationaryproperties.PANZER_BODY_SIDE = getF(sectfile, string, "PanzerBodySide", 0.0010F, 9.999F);
				stationaryproperties.PANZER_BODY_TOP = getF(sectfile, string, "PanzerBodyTop", 0.0010F, 9.999F);
			}
			if (sectfile.get(string, "PanzerHead", -9865.345F) == -9865.345F)
				stationaryproperties.PANZER_HEAD = stationaryproperties.PANZER_BODY_FRONT;
			else
				stationaryproperties.PANZER_HEAD = getF(sectfile, string, "PanzerHead", 0.0010F, 9.999F);
			if (sectfile.get(string, "PanzerHeadTop", -9865.345F) == -9865.345F)
				stationaryproperties.PANZER_HEAD_TOP = stationaryproperties.PANZER_BODY_TOP;
			else
				stationaryproperties.PANZER_HEAD_TOP = getF(sectfile, string, "PanzerHeadTop", 0.0010F, 9.999F);
			float f = Math.min(Math.min(stationaryproperties.PANZER_BODY_BACK, stationaryproperties.PANZER_BODY_TOP), Math.min(stationaryproperties.PANZER_BODY_SIDE, stationaryproperties.PANZER_HEAD_TOP));
			stationaryproperties.HITBY_MASK = f > 0.015F ? -2 : -1;
			Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
			Property.set(var_class, "meshName", stationaryproperties.meshSummer);
			return stationaryproperties;
		}
		
		public SPAWN(Class var_class)
		{
			try
			{
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_9_ = string.lastIndexOf('$');
				if (i < i_9_)
					i = i_9_;
				String string_10_ = string.substring(i + 1);
				proper = LoadStationaryProperties(Statics.getTechnicsFile(), string_10_, var_class);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Problem in spawn: " + var_class.getName());
			}
			cls = var_class;
			Spawn.add(cls, this);
		}
		
		public Actor actorSpawn(ActorSpawnArg actorspawnarg)
		{
			switch (World.cur().camouflage)
			{
				case 1 :
					proper.meshName = proper.meshWinter;
					proper.meshName1 = proper.meshWinter1;
					break;
				case 2 :
					proper.meshName = proper.meshDesert;
					proper.meshName1 = proper.meshDesert1;
					break;
				default :
					proper.meshName = proper.meshSummer;
					proper.meshName1 = proper.meshSummer1;
			}
			StationaryGeneric stationarygeneric;
			try
			{
				StationaryGeneric.constr_arg1 = proper;
				StationaryGeneric.constr_arg2 = actorspawnarg;
				stationarygeneric = (StationaryGeneric)cls.newInstance();
				StationaryGeneric.constr_arg1 = null;
				StationaryGeneric.constr_arg2 = null;
			}
			catch (Exception exception)
			{
				StationaryGeneric.constr_arg1 = null;
				StationaryGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create Stationary object [class:" + cls.getName() + "]");
				return null;
			}
			return stationarygeneric;
		}
	}
	
	class Mirror extends ActorNet
	{
		NetMsgFiltered out = new NetMsgFiltered();
		
		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
			{
				switch (netmsginput.readByte())
				{
					case 73 :
					{
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
							post(netmsgguaranted);
						}
						short i = netmsginput.readShort();
						if (i > 0 && dying != 1)
						{
							StationaryGeneric.this.Die(null, (short)1, false);
							
							//TODO: Disabled by |ZUTI|: probably leftover from MDS v1.13 that got into 410 code
							//----------------------------------------------------------------------
							/*
							try
							{
								ZutiTargetsSupportMethods.staticActorDied(StationaryGeneric.this);
							}
							catch (Exception exception)
							{
								System.out.println("StationaryGeneric error, ID_01: " + exception.toString());
							}
							*/
							//----------------------------------------------------------------------
						}
						return true;
					}
					case 68 :
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
							post(netmsgguaranted);
						}
						if (dying != 1)
						{
							com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
							Actor actor = (netobj == null ? null : ((ActorNet)netobj).actor());
							StationaryGeneric.this.Die(actor, (short)1, true);
							
							//TODO: Disabled by |ZUTI|: probably leftover from MDS v1.13 that got into 410 code
							//----------------------------------------------------------------------
							/*
							try
							{
								ZutiTargetsSupportMethods.staticActorDied(StationaryGeneric.this);
							}
							catch (Exception exception)
							{
								System.out.println("StationaryGeneric error, ID_02: " + exception.toString());
							}
							*/
							//----------------------------------------------------------------------
						}
						return true;
					case 100 :
					{
						NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
						postTo(masterChannel(), netmsgguaranted);
						return true;
					}
					default :
						return false;
				}
			}
			return true;
		}
		
		public Mirror(Actor actor, NetChannel netchannel, int i)
		{
			super(actor, netchannel, i);
		}
	}
	
	class Master extends ActorNet
	{
		public Master(Actor actor)
		{
			super(actor);
		}
		
		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
			{
				if (netmsginput.readByte() != 100)
					return false;
			}
			else
				return false;
			if (dying == 1)
				return true;
			com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
			Actor actor = netobj == null ? null : ((ActorNet)netobj).actor();
			StationaryGeneric.this.Die(actor, (short)0, true);
			return true;
		}
	}
	
	public static class StationaryProperties
	{
		public String meshName = null;
		public String meshName1 = null;
		public String meshSummer = null;
		public String meshDesert = null;
		public String meshWinter = null;
		public String meshSummer1 = null;
		public String meshDesert1 = null;
		public String meshWinter1 = null;
		public TableFunction2 fnShotPanzer = null;
		public TableFunction2 fnExplodePanzer = null;
		public float PANZER_BODY_FRONT = 0.0010F;
		public float PANZER_BODY_BACK = 0.0010F;
		public float PANZER_BODY_SIDE = 0.0010F;
		public float PANZER_BODY_TOP = 0.0010F;
		public float PANZER_HEAD = 0.0010F;
		public float PANZER_HEAD_TOP = 0.0010F;
		public float PANZER_TNT_TYPE = 1.0F;
		public int HITBY_MASK = -2;
		public String explodeName = null;
	}
	
	public static final double Rnd(double d, double d_11_)
	{
		return World.Rnd().nextDouble(d, d_11_);
	}
	
	public static final float Rnd(float f, float f_12_)
	{
		return World.Rnd().nextFloat(f, f_12_);
	}
	
	private boolean RndB(float f)
	{
		return World.Rnd().nextFloat(0.0F, 1.0F) < f;
	}
	
	public boolean isStaticPos()
	{
		return true;
	}
	
	public void msgShot(Shot shot)
	{
		shot.bodyMaterial = 2;
		if (dying == 0 && !(shot.power <= 0.0F) && (!isNetMirror() || !shot.isMirage()))
		{
			if (shot.powerType == 1)
			{
				if (!RndB(0.15F))
					Die(shot.initiator, (short)0, true);
			}
			else
			{
				float f = Shot.panzerThickness(pos.getAbsOrient(), shot.v, shot.chunkName.equalsIgnoreCase("Head"), prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD, prop.PANZER_HEAD_TOP);
				f *= Rnd(0.93F, 1.07F);
				float f_13_ = prop.fnShotPanzer.Value(shot.power, f);
				if (f_13_ < 1000.0F && (f_13_ <= 1.0F || RndB(1.0F / f_13_)))
					Die(shot.initiator, (short)0, true);
			}
		}
	}
	
	public void msgExplosion(Explosion explosion)
	{
		if (dying == 0 && (!isNetMirror() || !explosion.isMirage()) && !(explosion.power <= 0.0F))
		{
			int i = explosion.powerType;
			if (explosion != null)
			{
				/* empty */
			}
			if (i == 1)
			{
				if (TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.7F, 0.25F, prop.PANZER_BODY_FRONT, prop.PANZER_BODY_SIDE, prop.PANZER_BODY_BACK, prop.PANZER_BODY_TOP, prop.PANZER_HEAD,
						prop.PANZER_HEAD_TOP))
					Die(explosion.initiator, (short)0, true);
			}
			else
			{
				int i_14_ = explosion.powerType;
				if (explosion != null)
				{
					/* empty */
				}
				if (i_14_ == 2 && explosion.chunkName != null)
					Die(explosion.initiator, (short)0, true);
				else
				{
					float f;
					if (explosion.chunkName != null)
						f = 0.5F * explosion.power;
					else
						f = explosion.receivedTNTpower(this);
					f *= Rnd(0.95F, 1.05F);
					float f_15_ = prop.fnExplodePanzer.Value(f, prop.PANZER_TNT_TYPE);
					if (f_15_ < 1000.0F && (f_15_ <= 1.0F || RndB(1.0F / f_15_)))
						Die(explosion.initiator, (short)0, true);
				}
			}
		}
	}
	
	private void ShowExplode(float f, Actor actor)
	{
		if (f > 0.0F)
			f = Rnd(f, f * 1.6F);
		Explosions.runByName(prop.explodeName, this, "Smoke", "SmokeHead", f, actor);
	}
	
	private void Die(Actor actor, short i, boolean bool)
	{
		if (dying == 0)
		{
			if (i <= 0)
			{
				if (isNetMirror())
				{
					send_DeathRequest(actor);
					return;
				}
			}
			dying = 1;
			World.onActorDied(this, actor);
			if (prop.meshName1 == null)
				mesh().makeAllMaterialsDarker(0.22F, 0.35F);
			else
				setMesh(prop.meshName1);
			int i_17_ = mesh().hookFind("Ground_Level");
			if (i_17_ != -1)
			{
				Matrix4d matrix4d = new Matrix4d();
				mesh().hookMatrix(i_17_, matrix4d);
				heightAboveLandSurface = (float)-matrix4d.m23;
			}
			Align();
			if (bool)
				ShowExplode(15.0F, actor);
			if (bool)
				send_DeathCommand(actor);
		}
	}
	
	public void destroy()
	{
		if (!isDestroyed())
			super.destroy();
	}
	
	public Object getSwitchListener(Message message)
	{
		return this;
	}
	
	protected StationaryGeneric()
	{
		this(constr_arg1, constr_arg2);
	}
	
	private StationaryGeneric(StationaryProperties stationaryproperties, ActorSpawnArg actorspawnarg)
	{
		super(stationaryproperties.meshName);
		prop = stationaryproperties;
		actorspawnarg.setStationary(this);
		collide(true);
		drawing(true);
		createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
		heightAboveLandSurface = 0.0F;
		int i = mesh().hookFind("Ground_Level");
		if (i != -1)
		{
			Matrix4d matrix4d = new Matrix4d();
			mesh().hookMatrix(i, matrix4d);
			heightAboveLandSurface = (float)-matrix4d.m23;
		}
		else
			System.out.println("Stationary " + this.getClass().getName() + ": hook Ground_Level not found");
		Align();
	}
	
	private void Align()
	{
		pos.getAbs(p);
		p.z = Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface;
		o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
		Engine.land().N(p.x, p.y, n);
		o.orient(n);
		pos.setAbs(p, o);
	}
	
	public void align()
	{
		Align();
	}
	
	public int HitbyMask()
	{
		return prop.HITBY_MASK;
	}
	
	public int chooseBulletType(BulletProperties[] bulletpropertieses)
	{
		if (dying != 0)
			return -1;
		if (bulletpropertieses.length == 1)
			return 0;
		if (bulletpropertieses.length <= 0)
			return -1;
		if (this instanceof TgtTank)
		{
			if (bulletpropertieses[0].cumulativePower > 0.0F)
				return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F)
				return 1;
			if (bulletpropertieses[0].power <= 0.0F)
				return 0;
			if (bulletpropertieses[1].power <= 0.0F)
				return 1;
		}
		else
		{
			if (bulletpropertieses[0].power <= 0.0F)
				return 0;
			if (bulletpropertieses[1].power <= 0.0F)
				return 1;
			if (bulletpropertieses[0].cumulativePower > 0.0F)
				return 0;
			if (bulletpropertieses[1].cumulativePower > 0.0F)
				return 1;
		}
		if (bulletpropertieses[0].powerType == 1)
			return 0;
		if (bulletpropertieses[1].powerType == 1)
			return 1;
		if (bulletpropertieses[0].powerType == 0)
			return 1;
		return 0;
	}
	
	public int chooseShotpoint(BulletProperties bulletproperties)
	{
		if (dying != 0)
			return -1;
		return 0;
	}
	
	public boolean getShotpointOffset(int i, Point3d point3d)
	{
		if (dying != 0)
			return false;
		if (i != 0)
			return false;
		if (point3d != null)
			point3d.set(0.0, 0.0, 0.0);
		return true;
	}
	
	public boolean unmovableInFuture()
	{
		return true;
	}
	
	public void collisionDeath()
	{
		if (!isNet())
		{
			ShowExplode(-1.0F, null);
			destroy();
		}
	}
	
	public float futurePosition(float f, Point3d point3d)
	{
		pos.getAbs(point3d);
		return f <= 0.0F ? 0.0F : f;
	}
	
	private void send_DeathCommand(Actor actor)
	{
		if (isNetMaster())
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try
			{
				netmsgguaranted.writeByte(68);
				netmsgguaranted.writeNetObj(actor == null ? (ActorNet)null : actor.net);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}
	
	private void send_DeathRequest(Actor actor)
	{
		if (isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream))
		{
			try
			{
				NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
				netmsgguaranted.writeByte(100);
				netmsgguaranted.writeNetObj(actor == null ? (ActorNet)null : actor.net);
				net.postTo(net.masterChannel(), netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}
	
	public void createNetObject(NetChannel netchannel, int i)
	{
		if (netchannel == null)
			net = new Master(this);
		else
			net = new Mirror(this, netchannel, i);
	}
	
	public void netFirstUpdate(NetChannel netchannel) throws IOException
	{
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(73);
		if (dying == 0)
			netmsgguaranted.writeShort(0);
		else
			netmsgguaranted.writeShort(1);
		net.postTo(netchannel, netmsgguaranted);
	}
}