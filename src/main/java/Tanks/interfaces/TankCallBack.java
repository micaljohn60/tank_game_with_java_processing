package Tanks.interfaces;

/**
 * Interface defining a callback mechanism for ending a tank's turn.
 */
public interface TankCallBack {
    /**
     * Signals the end of a tank's turn with the specified parameters.
     *
     * @param hitObject The object hit during the turn (e.g., tank or terrain).
     * @param health    The health impact or damage caused during the turn.
     * @return The index representing the next tank's turn.
     */
    int endTurn(String hitObject,int health);
}
