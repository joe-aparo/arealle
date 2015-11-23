package net.jsa.arealle.task.geom;

import java.util.HashMap;
import java.util.Map;

/**
 * Shape type identifiers.
 * 
 * @author jsaparo
 */
public enum ShapeType {
    NULL(0),
    POINT(1),
    POLYLINE(3),
    POLYGON(5),
    MULTIPOINT(8),
    POINTZ(11),
    POLYLINEZ(13),
    POLYGONZ(15),
    MULTIPOINTZ(18),
    POINTM(21),
    POLYLINEM(23),
    POLYGONM(25),
    MULTIPOINTM(28),
    MULTIPATCH(31);

    private static final Map<Integer, ShapeType> TYPES_BY_CODE = new HashMap<Integer, ShapeType>();

    static {
    	TYPES_BY_CODE.put(NULL.getCode(), ShapeType.NULL);
    	TYPES_BY_CODE.put(POINT.getCode(), ShapeType.POINT);
    	TYPES_BY_CODE.put(POLYLINE.getCode(), ShapeType.POLYLINE);
    	TYPES_BY_CODE.put(POLYGON.getCode(), ShapeType.POLYGON);
    	TYPES_BY_CODE.put(MULTIPOINT.getCode(), ShapeType.MULTIPOINT);
    	TYPES_BY_CODE.put(POINTZ.getCode(), ShapeType.POINTZ);
    	TYPES_BY_CODE.put(POLYLINEZ.getCode(), ShapeType.POLYLINEZ);
    	TYPES_BY_CODE.put(POLYGONZ.getCode(), ShapeType.POLYGONZ);
    	TYPES_BY_CODE.put(MULTIPOINTZ.getCode(), ShapeType.MULTIPOINTZ);
    	TYPES_BY_CODE.put(POINTM.getCode(), ShapeType.POINTM);
    	TYPES_BY_CODE.put(POLYLINEM.getCode(), ShapeType.POLYLINEM);
    	TYPES_BY_CODE.put(POLYGONM.getCode(), ShapeType.POLYGONM);
    	TYPES_BY_CODE.put(MULTIPOINTM.getCode(), ShapeType.MULTIPOINTM);
    	TYPES_BY_CODE.put(MULTIPATCH.getCode(), ShapeType.MULTIPATCH);
    }

    private int code;

    /**
     * Constructor.
     * 
     * @param label Displayable label
     * @param code Logical code
     * @param sqlOp SQL equivalent
     */
    ShapeType(int code) {
        this.code = code;
    }

    /**
     * Get code.
     * 
     * @return A numeric code
     */
    public int getCode() {
        return code;
    }

    /**
     * Convert code to enumerated type.
     * 
     * @param code A code string
     * @return An enumerated type
     */
    public static ShapeType getByCode(int code) {
        return TYPES_BY_CODE.get(code);
    }
}
