package example.com.espressotest;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by awonwon on 2015/7/21.
 */
public class ViewUtil {
    @SuppressWarnings("unchecked")
    public static <T extends View> T getHolder(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
