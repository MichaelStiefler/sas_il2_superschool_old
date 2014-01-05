/*    */ package com.maddox.il2.objects.weapons;

/*    */ 
/*    */ import com.maddox.rts.Property;
/*    */ 
/*    */ public class BombGunAO2_5_3 extends BombGun
/*    */ {
/*    */   public void setBombDelay(float paramFloat)
/*    */   {
/* 13 */     this.bombDelay = 0.0F;
/* 14 */     if (this.bomb != null)
/* 15 */       this.bomb.delayExplosion = this.bombDelay;
/*    */   }

   static {
	   		Class localClass = BombGunAO2_5_3.class;
/* 20 */     Property.set(localClass, "bulletClass", (Object)com.maddox.il2.objects.weapons.BombAO2_5_3.class);
/* 21 */     Property.set(localClass, "bullets", 48);
/* 22 */     Property.set(localClass, "shotFreq", 32.0F);
/*    */ 
/* 24 */     Property.set(localClass, "cassette", 1);
/* 25 */     Property.set(localClass, "sound", "weapon.bombgun_AO10");
/*    */   }
/*    */ }