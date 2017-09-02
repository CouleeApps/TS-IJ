package com.torquescript.symbols;

import com.intellij.openapi.project.Project;
import java.util.Collection;
import java.util.Set;

public class TSSymbolList<T> {
    private Collection<T> items = null;
    private TSSymbolListGenerator<T> function;
    private long lastUpdate;
    private static final long CACHE_LIFETIME = 15 * /* ns */ 1000000;
    private static final String LOCK = "Probably slow";

    public TSSymbolList(TSSymbolListGenerator<T> generator) {
        function = generator;
    }

    /**
     * Get a list of all symbols in the project. This list is cached and updated every few seconds
     * so you don't have to find all the symbols for every function call.
     * @param project Containing project in which to search
     * @return A list of all symbol declarations
     */
    public Collection<T> getSymbolList(Project project) {
        //Need to synchronize this in case we update cache while something is accessing the symbols
        synchronized (LOCK) {
            boolean needUpdate = false;

            //If the cache has existed for long enough we should probably regenerate it
            if (items == null) {
                needUpdate = true;
            } else {
                if (System.nanoTime() - lastUpdate > CACHE_LIFETIME) {
                    needUpdate = true;
                }
            }
            //Cache is still warm, use it instead of searching
            if (!needUpdate) {
                return items;
            }
        }

        //Need to regenerate cache
        Collection<T> updated = function.generate(project);
        synchronized (LOCK) {
            lastUpdate = System.nanoTime();
            items = updated;
            return items;
        }
    }
}
