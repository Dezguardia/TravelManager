package uqac.dim.travelmanager;

import android.content.Context;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class RoutingManager {
    private Context context;

    public Polyline createRoute(GeoPoint startPoint, GeoPoint endPoint) {
        RoadManager roadManager = new OSRMRoadManager(context.getApplicationContext(), "test");
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        Road road = roadManager.getRoad(waypoints);
        return RoadManager.buildRoadOverlay(road);

    }
}
