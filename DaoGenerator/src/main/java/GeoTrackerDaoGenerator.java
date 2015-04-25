import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GeoTrackerDaoGenerator {

    public static void main(String... args) throws Exception {
        final Schema schema = new Schema(1, "nu.wasis.geotracker.model");
        schema.enableKeepSectionsByDefault();
        addGeoLocation(schema);
        new DaoGenerator().generateAll(schema, "../app/src-gen");
    }

    private static void addGeoLocation(final Schema schema) {
        final Entity geoLocation = schema.addEntity("GeoLocation");
        geoLocation.implementsInterface("nu.wasis.geotracker.model.DomainObject");
        geoLocation.addIdProperty();
        geoLocation.addDoubleProperty("latitude").notNull();
        geoLocation.addDoubleProperty("longitude").notNull();
        geoLocation.addDoubleProperty("altitude");
        geoLocation.addFloatProperty("accuracy");
        geoLocation.addFloatProperty("speed");
        geoLocation.addLongProperty("time");
    }

}
