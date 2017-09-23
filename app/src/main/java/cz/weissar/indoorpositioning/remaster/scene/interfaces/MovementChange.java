package cz.weissar.indoorpositioning.remaster.scene.interfaces;

import cz.weissar.indoorpositioning.remaster.utils.VectorHolder;

/**
 * Created by petrw on 23.09.2017.
 */

public interface MovementChange {
    void onMovement(VectorHolder.Axis axis, double distanceMetters);
}
