package com.torquescript;

public enum  TSFunctionType {
    /**
     * Global function
     * Example: echo()
     */
    GLOBAL,
    /**
     * Global with a namespace, usually these are script defined.
     * Example: Mode::callback()
     */
    GLOBAL_NS,
    /**
     * Method call on an object
     * Example: .delete()
     */
    METHOD
}
