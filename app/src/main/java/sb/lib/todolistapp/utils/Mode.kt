package sb.lib.todolistapp.utils


enum class Mode {

    /**
     * Removes the selection or expansion feature
     */
    NONE,

    /**
     * Applies to a single item ie., only one item can be selected or expanded at a time
     */
    SINGLE,

    /**
     * Applies to a multiple items ie., multiple items can be selected or expanded at a time
     */
    MULTIPLE,

    /**
     * Inherits the mode from its parent
     */
    INHERIT
}