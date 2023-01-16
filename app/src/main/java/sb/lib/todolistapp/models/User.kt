package sb.lib.todolistapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class User( val userEmail :String? , val userId :String?    ) : Auth()  , Parcelable