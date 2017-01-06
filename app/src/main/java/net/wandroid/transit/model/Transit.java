package net.wandroid.transit.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The model of the json data.
 * Transit is serializable so it can be sent through intent.
 * However, an improvement is to make them either parcelable
 * or create a Singleton class .
 */

public class Transit implements Serializable {

    public List<Route> routes;

    @SuppressWarnings("unused")
    public static class Route implements Serializable {
        public String type;
        public String provider;
        public List<Segment> segments;
        public Price price;

        public static class Price implements Serializable{
            public String currency;
            public int amount;
        }

        public static class Segment implements Serializable {
            public String name;
            public int num_stops;
            public List<Stop> stops;
            public String travel_mode;
            public String description;
            public String color;
            public String icon_url;
            public String polyline;

            public static class Stop implements Serializable {
                public float lat;
                public float lng;
                public String datetime;
                public String name;
                public String properties;
            }
        }
    }

    public Map<String, Provider> provider_attributes;

    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class Provider implements Serializable {
        public String provider_icon_url;
        public String disclaimer;
        public String display_name;
    }
}
