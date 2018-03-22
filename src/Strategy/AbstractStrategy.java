package Strategy;

import UnitFormations.BaseUnitFormation;
import battlesimulator.Military.General;
import battlesimulator.Military.Unit;
import battlesimulator.Utils;
import battlesimulator.Military.UnitMetrics;
import java.util.ArrayList;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Arlo
 */
public abstract class AbstractStrategy {

    ArrayList<Unit.UnitStateAlbum> friendlyUnits;
    ArrayList<Unit.UnitStateAlbum> enemyUnits;
    StrategyStage stage;

    public enum StrategyStage {

        FORMING_UP,
        ADVANCE,
        FINAL_APPROACH,
        COMBAT,
        RESERVES,
        MOP_UP
    }

    public AbstractStrategy() {
        this.stage = StrategyStage.FORMING_UP;
    }


    public void advanceStage() {
        StrategyStage newStage;
        switch (this.stage) {
            case FORMING_UP:
                newStage = StrategyStage.ADVANCE;
                break;
            case ADVANCE:
                newStage = StrategyStage.FINAL_APPROACH;
                break;
            case FINAL_APPROACH:
                newStage = StrategyStage.COMBAT;
                break;
            case COMBAT:
                newStage = StrategyStage.RESERVES;
                break;
            case RESERVES:
                newStage = StrategyStage.MOP_UP;
                break;
            case MOP_UP:
                newStage = StrategyStage.MOP_UP;
                break;
            default:
                newStage = StrategyStage.MOP_UP;
                break;
        }

        this.stage = newStage;
    }

    public abstract Map<String, BaseUnitFormation> setInitialFormation();
    public abstract boolean isValidStrategy(General general, ArrayList<Unit.UnitStateAlbum> friendlyUnits, ArrayList<Unit.UnitStateAlbum> enemyUnits);

    public abstract Map<String, AbstractObjective> determineObjectives();

    public UnitMetrics findNearestEnemy(Unit.UnitStateAlbum unitSearching) {
        UnitMetrics metric = null;
        long distanceSquared = Integer.MAX_VALUE;
        for (Unit.UnitStateAlbum enemyMetrics : this.enemyUnits) {
            long distanceToEnemy = Utils.getSquaredDistance(unitSearching.getLatestMetrics().getCenterMass(), enemyMetrics.getLatestMetrics().getCenterMass());
            if (distanceToEnemy < distanceSquared) {
                distanceSquared = distanceToEnemy;
                metric = enemyMetrics.getLatestMetrics();
            }
        }

        return metric;
    }

    public UnitMetrics findNearestAlly(Unit.UnitStateAlbum unitSearching) {
        UnitMetrics metric = null;
        long distanceSquared = Integer.MAX_VALUE;
        for (Unit.UnitStateAlbum friendlyMetrics : this.friendlyUnits) {
            long distanceToFriend = Utils.getSquaredDistance(unitSearching.getLatestMetrics().getCenterMass(), friendlyMetrics.getLatestMetrics().getCenterMass());
            if (distanceToFriend < distanceSquared) {
                distanceSquared = distanceToFriend;
                metric = friendlyMetrics.getLatestMetrics();
            }
        }

        return metric;
    }

    public void setStage(StrategyStage stage) {
        this.stage = stage;
    }

    public StrategyStage getStage() {
        return stage;
    }
}
