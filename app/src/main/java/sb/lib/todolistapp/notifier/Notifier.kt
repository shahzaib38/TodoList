package sb.lib.todolistapp.notifier

import sb.lib.todolistapp.list_sections.Section


interface Notifier {
    fun notifySectionItemMoved(section: Section, fromPosition: Int, toPosition: Int)
    fun notifySectionRangeChanged(
        section: Section ,
        positionStart: Int,
        itemCount: Int,
        payload: Any?)

    fun notifySectionRangeInserted(section: Section, positionStart: Int, itemCount: Int)
    fun notifySectionRangeRemoved(section: Section , positionStart: Int, itemCount: Int)


}