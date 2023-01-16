package sb.lib.todolistapp.navigators

interface TodoNavigator : Navigator{
    fun newTask(name: String)
    fun changeDate()
    fun changeTime()


}