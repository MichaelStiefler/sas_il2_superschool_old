// Source File Name: ActiveMissile.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;

public class ActiveMissile {

	public boolean isAI;
	public Actor theOwner;
	public int theOwnerArmy;
	public Actor theVictim;
	public int theVictimArmy;

	public ActiveMissile() {
	}

	public ActiveMissile(Actor owner, Actor victim, int ownerArmy, int victimArmy, boolean bAI) {
		this.theOwner = owner;
		this.theVictim = victim;
		this.theOwnerArmy = ownerArmy;
		this.theVictimArmy = victimArmy;
		this.isAI = bAI;
	}
}
