/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.game.order;

import com.maddox.il2.ai.Wing;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;

class Section extends Actor {

    Section() {
        this.airc = new Aircraft[2];
    }

    public static Section New(Wing wing, String s) {
        Section section = (Section) Actor.getByName(s);
        if (section != null)
            return section;
        else
            return new Section(wing, s);
    }

    public Section(Wing wing, String s) {
        this.airc = new Aircraft[2];
        this.setName(s);
        this.setOwner(wing);
    }

    Aircraft airc[];
    Aircraft sectLeader;
    Aircraft sectWingman;
}
