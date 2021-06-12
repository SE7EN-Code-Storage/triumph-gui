/**
 * MIT License
 *
 * Copyright (c) 2021 TriumphTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.triumphteam.gui.components.util;

import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for detecting server version for legacy support :(
 */
public final class VersionHelper {

    // Unbreakable change
    private static final int V1_11 = 1110;
    // Material change
    private static final int V1_13 = 1130;
    // PDC
    private static final int V1_14 = 1140;
    // Paper adventure changes
    private static final int V1_16_5 = 1165;

    private static final int CURRENT_VERSION = getCurrentVersion();

    private static final boolean IS_PAPER = checkPaper();

    /**
     * Checks if the version supports Components or not
     * Paper versions above 1.16.5 would be true
     * Spigot always false
     */
    public static final boolean IS_COMPONENT_LEGACY = !IS_PAPER || CURRENT_VERSION < V1_16_5;

    /**
     * Checks if the version is lower than 1.13 due to the item changes
     */
    public static final boolean IS_ITEM_LEGACY = CURRENT_VERSION < V1_13;

    /**
     * Checks if the version supports the {@link org.bukkit.inventory.meta.ItemMeta#setUnbreakable(boolean)} method
     */
    public static final boolean IS_UNBREAKABLE_LEGACY = CURRENT_VERSION < V1_11;

    /**
     * Checks if the version supports {@link org.bukkit.persistence.PersistentDataContainer}
     */
    public static final boolean IS_PDC_VERSION = CURRENT_VERSION >= V1_14;

    /**
     * Check if the server has access to the Paper API
     * Taken from <a href="https://github.com/PaperMC/PaperLib">PaperLib</a>
     *
     * @return True if on Paper server (or forks), false anything else
     */
    private static boolean checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    /**
     * Gets the current server version
     *
     * @return A protocol like number representing the version, for example 1.16.5 - 1165
     */
    private static int getCurrentVersion() {
        final Matcher matcher = Pattern.compile("(?<version>\\d+\\.\\d+\\.?\\d+)").matcher(Bukkit.getBukkitVersion());
        if (!matcher.find()) return 0;

        Integer version = Ints.tryParse(matcher.group("version").replace(".", ""));
        if (version == null) return 0;
        // For handling versions like 1.17 (due to being 117 instead of 1170)
        if (version < 1000) version *= 10;
        return version;
    }

}