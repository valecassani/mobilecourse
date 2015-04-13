package it.polimi.mobilecourse.expenses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;

/**
 * Created by Valerio on 13/04/2015.
 */
public class RipetizioniItem extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contView = inflater.inflate(R.layout.ripet_item, container, false);

        SwipeLayout swipeLayout =  (SwipeLayout)contView.findViewById(R.id.ripet_item);

//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//set drag edge.
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        return contView;

    }
}
