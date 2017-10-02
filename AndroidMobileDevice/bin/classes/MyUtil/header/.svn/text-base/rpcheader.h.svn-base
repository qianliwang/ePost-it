struct ADD_GEOPAGE {
    REGION region;
    TRIGGER trigger;
    U8 URL[255];
}

struct TRIGGER {
    U8  inOut;
    U8  direction;
}

struct POINT_2D {
    U64     x;
    U64     y;
}

struct POINT_3D {
    U64     x;
    U64     y;
    U64     z;
}


struct TIME_STAMPED_LOCATION {
    U64     x;
    U64     y;
    U64     z;
    U64     t;
}

struct REGION {
    U8      shape;

    U64     centerX;
    U64     centerY;
    U64     radius;

    U64     upperX;
    U64     upperY;
    U64     lowerX;
    U64     lowerY;
}

struct LOCATION_UPDATE_POLICY {
    U8      periodicUpdate;
    U32     timeDuration;
}

struct REQUEST_RESIDENT_DOMAIN {
    U32                     id;
    REGION                  zone;
    U32                     load;
    TIME_STAMPED_LOCATION   location;
    LOCATION_UPDATE_POLICY  policy;
    REGION                  domain;
}

struct UPDATE_TIME_STAMPED_LOCATION {
    U32     id;
    U64     x;
    U64     y;
    U64     z;
    U64     t;
}
