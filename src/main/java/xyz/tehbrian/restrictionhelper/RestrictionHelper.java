package xyz.tehbrian.restrictionhelper;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The main class used for interacting with RestrictionHelper.
 */
public class RestrictionHelper {

    /**
     * The internal list of restrictions to be checked against.
     */
    private final List<Restriction> restrictions = new ArrayList<>();
    /**
     * The {@link DebugLogger} constructed with the {@link Logger} provided by the plugin.
     */
    private final DebugLogger debugLogger;

    /**
     * Creates an instance of {@link RestrictionHelper}.
     *
     * @param logger your plugin's logger. use {@link JavaPlugin#getLogger()} to get.
     */
    public RestrictionHelper(final Logger logger) {
        Objects.requireNonNull(logger, "logger cannot be null");

        this.debugLogger = new DebugLogger(logger);
    }

    /**
     * Registers a restriction to be checked against.
     * <p>
     * NOTE: Since RestrictionHelper is a shade-in dependency, other
     * plugins <b>will not</b> be able to check against restrictions
     * your plugin registers.
     *
     * @param restriction the restriction to register
     */
    public void registerRestriction(final Restriction restriction) {
        restrictions.add(restriction);
    }

    /**
     * Unregisters a restriction.
     * <p>
     * NOTE: Since RestrictionHelper is a shade-in dependency, other
     * plugins <b>will not</b> be affected by the restrictions
     * your plugin unregisters.
     *
     * @param restriction the restriction to unregister
     */
    public void unregisterRestriction(final Restriction restriction) {
        restrictions.remove(restriction);
    }

    /**
     * Gets the internal list of registered restrictions.
     * <p>
     * NOTE: Since RestrictionHelper is a shade-in dependency, this
     * list <b>is not</b> shared with other plugins.
     */
    public List<Restriction> getRegisteredRestrictions() {
        return restrictions;
    }

    /**
     * Checks whether {@code player} has sufficient permission to perform
     * {@code action} at {@code loc} according to all registered restrictions.
     *
     * @param player the player
     * @param loc    the location to checked
     * @return true if the player has permission, false if not
     */
    public boolean checkRestrictions(final Player player, final Location loc, final ActionType actionType) {
        Objects.requireNonNull(player, "player cannot be null");
        Objects.requireNonNull(player, "location cannot be null");

        // Iterate through all registered restrictions.
        // If any restriction returns false, return false immediately.
        // If all restrictions return true, return true.
        for (Restriction restriction : restrictions) {
            if (!restriction.check(player, loc, actionType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the {@link DebugLogger} constructed with the {@link Logger} provided
     * to {@link RestrictionHelper#RestrictionHelper(Logger)}.
     *
     * @return the debug logger
     */
    public DebugLogger getDebugLogger() {
        return debugLogger;
    }
}

