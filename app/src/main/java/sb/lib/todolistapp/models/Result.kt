package sb.lib.todolistapp.models



sealed class Result ()

data class onSuccess< T>(var t : T): Result()
data class onError<T>(var t : T) :  Result()
data class onShutDown<T>(var t : T) :  Result()
