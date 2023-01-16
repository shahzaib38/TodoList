package sb.lib.todolistapp.list_sections;


import androidx.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE) @IntDef({
        PositionType.TOP, PositionType.LEFT, PositionType.MIDDLE, PositionType.RIGHT, PositionType.BOTTOM
}) public @interface PositionType {


    int LEFT = 1;
    int TOP = 2;
    int RIGHT = 4;
    int BOTTOM = 8;
    int MIDDLE = 0;
}