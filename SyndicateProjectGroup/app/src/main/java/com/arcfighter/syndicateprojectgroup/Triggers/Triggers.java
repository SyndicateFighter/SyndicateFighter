package com.arcfighter.syndicateprojectgroup.Triggers;

import android.graphics.Color;
import android.location.Location;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;

/**
 * Created by Andrew on 11/19/15.
 */
public class Triggers {

    protected Location currentLocation;
    protected Graphic[] nearbyTriggers;
    protected Point[] nearbyTriggerPointsWGS;

    public Triggers (Location mcurrentLocation){
        this.currentLocation = mcurrentLocation;
        this.nearbyTriggers = generateCloseByTriggers(mcurrentLocation);
    }

    private Graphic[] generateCloseByTriggers(Location mcurrentLocation) {

        //TODO MAKE IT PRETTY!!
        Symbol polysym = new SimpleFillSymbol(Color.BLUE);

        Graphic[] mnearbyTriggers = new Graphic[25];
        nearbyTriggerPointsWGS = new Point[25];

        double currentLat = mcurrentLocation.getLatitude();
        double currentLong = mcurrentLocation.getLongitude();

        Point currentPoint = GeometryEngine.project(currentLong, currentLat, SpatialReference.create(3857));

        int currentx = (int) currentPoint.getX();
        int currenty = (int) currentPoint.getY();


        //TODO what is the max number possible for x and y? so we can wrap around?
        //also feels like this won't work for north/south pole very well
        //For now just +/-
        int xfactor = ((int)Math.round(currentx / 1600.0)) -2;
        int yfactor = ((int)Math.round(currenty / 1600.0)) -2;

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                //TODO for now, all points/polygons are separate, this may change in future
                Point p = new Point((xfactor*1600), (yfactor*1600));
                Polygon poly = GeometryEngine.buffer(p, SpatialReference.create(3857), 400, Unit.create(LinearUnit.Code.METER));
                Graphic g = new Graphic(poly,polysym);

                Point wgsP = (Point) GeometryEngine.project(p,SpatialReference.create(3857), SpatialReference.create(4326));

                int pos = (i*5)+j;

                //TODO this is probably not the best way to init nearbyTriggerPointsWGS
                nearbyTriggerPointsWGS[pos]= wgsP;

                //good for mnearbyTriggers though
                mnearbyTriggers[pos]= g;

                yfactor++;
            }
            xfactor++;
            yfactor = yfactor-5;
        }
        return mnearbyTriggers;

    };

    public Graphic[] getNearbyTriggers(){
        return nearbyTriggers;
    }

    public Point[] getNearbyTriggerPointsWGS(){ return nearbyTriggerPointsWGS;}

}
