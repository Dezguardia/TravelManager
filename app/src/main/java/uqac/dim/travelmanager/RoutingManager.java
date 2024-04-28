package uqac.dim.travelmanager;

import android.content.Context;
import android.util.Log;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class RoutingManager {
    private final ArrayList<GeoPoint> geopoints = new ArrayList<GeoPoint>();

    public Polyline createRoute(GeoPoint startPoint, GeoPoint endPoint, Context context) {
        RoadManager roadManager = new OSRMRoadManager(context, "test");
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        Road road = roadManager.getRoad(waypoints);
        return RoadManager.buildRoadOverlay(road);
    }
    public void addToGeopoints(GeoPoint point) {
        geopoints.add(point);
        Log.w("ROUTINGMANAGER", geopoints.toString());
    }

    public ArrayList<GeoPoint> getGeopoints() {
        return geopoints;
    }
}
