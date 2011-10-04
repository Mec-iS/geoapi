/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2011 Open Geospatial Consortium, Inc.
 *    All Rights Reserved. http://www.opengeospatial.org/ogc/legal
 *
 *    Permission to use, copy, and modify this software and its documentation, with
 *    or without modification, for any purpose and without fee or royalty is hereby
 *    granted, provided that you include the following on ALL copies of the software
 *    and documentation or portions thereof, including modifications, that you make:
 *
 *    1. The full text of this NOTICE in a location viewable to users of the
 *       redistributed or derivative work.
 *    2. Notice of any changes or modifications to the OGC files, including the
 *       date changes were made.
 *
 *    THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 *    NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *    TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 *    THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 *    PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 *
 *    COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 *    CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 *
 *    The name and trademarks of copyright holders may NOT be used in advertising or
 *    publicity pertaining to the software without specific, written prior permission.
 *    Title to copyright in this software and any associated documentation will at all
 *    times remain with copyright holders.
 */
package org.opengis.test.referencing.gigs;

import java.util.List;
import javax.measure.unit.Unit;
import javax.measure.converter.UnitConverter;
import javax.measure.converter.ConversionException;

import org.opengis.util.Factory;
import org.opengis.util.FactoryException;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.test.FactoryFilter;
import org.opengis.test.TestCase;

import org.junit.*;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import static java.lang.StrictMath.*;
import static javax.measure.unit.Unit.ONE;
import static javax.measure.unit.SI.METRE;
import static javax.measure.unit.NonSI.DEGREE_ANGLE;


/**
 * Pre-defined geodetic parameter library test.
 * The tests for this series are designed to verify the correctness of geodetic parameters that
 * are delivered with the software. The comparison to be taken as truth is the EPSG Dataset.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   3.1
 *
 * @see org.opengis.test.referencing.AuthorityFactoryTest
 */
public strictfp class Series2000Test extends TestCase {
    /**
     * Relative tolerance factor from GIGS documentation.
     */
    private static final double TOLERANCE = 1E-10;

    /**
     * Factory to build {@link CoordinateReferenceSystem} instances, or {@code null} if none.
     */
    protected final CRSAuthorityFactory crsFactory;

    /**
     * Factory to build {@link CoordinateSystem} instances, or {@code null} if none.
     */
    protected final CSAuthorityFactory csFactory;

    /**
     * Factory to build {@link Datum} instances, or {@code null} if none.
     */
    protected final DatumAuthorityFactory datumFactory;

    /**
     * Returns a default set of factories to use for running the tests. Those factories are given
     * in arguments to the constructor when this test class is instantiated directly by JUnit (for
     * example as a {@linkplain org.junit.runners.Suite.SuiteClasses suite} element), instead than
     * subclassed by the implementor. The factories are fetched as documented in the
     * {@link #factories(Class[])} javadoc.
     *
     * @return The default set of arguments to be given to the {@code AuthorityFactoryTest} constructor.
     *
     * @since 3.1
     */
    @Parameterized.Parameters
    public static List<Factory[]> factories() {
        return factories(FactoryFilter.ByAuthority.EPSG,
                CRSAuthorityFactory.class, CSAuthorityFactory.class, DatumAuthorityFactory.class);
    }

    /**
     * Creates a new test using the given factories. If a given factory is {@code null},
     * then the tests which depend on it will be skipped.
     *
     * @param crsFactory   Factory for creating {@link CoordinateReferenceSystem} instances.
     * @param csFactory    Factory for creating {@link CoordinateSystem} instances.
     * @param datumFactory Factory for creating {@link Datum} instances.
     */
    public Series2000Test(final CRSAuthorityFactory crsFactory,
            final CSAuthorityFactory csFactory, final DatumAuthorityFactory datumFactory)
    {
        this.crsFactory   = crsFactory;
        this.csFactory    = csFactory;
        this.datumFactory = datumFactory;
    }

    /**
     * Returns the name of the given object, or {@code null} if none.
     */
    private static String getName(final IdentifiedObject object) {
        if (object != null) {
            final Identifier name = object.getName();
            if (name != null) {
                return name.getCode();
            }
        }
        return null;
    }

    /**
     * Reference Units of Measure test.
     * <p>
     * <table cellpadding="3"><tr>
     *   <th nowrap align="left" valign="top">Test purpose:</th>
     *   <td>To verify reference units of measure bundled with the geoscience software.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Test method:</th>
     *   <td>Compare unit definitions included in the software against the EPSG Dataset.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Test data:</th>
     *   <td>EPSG Dataset and file {@code GIGS_2001_libUnit_v2-0_2011-06-28.xls}. This file
     *   contains three separate blocks of data for linear units, angular units and scaling units.
     *   It gives the EPSG code and name for the unit of measure, together with the ratio of the
     *   unit to the ISO base unit for that unit type.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Expected result:</th>
     *   <td>Unit of measure definitions bundled with software should have the ratio to the
     *   appropriate base unit as in the EPSG Dataset. The values of the base unit per unit
     *   should be correct to at least 10 significant figures. Units missing from the software
     *   or included in the software additional to those in the EPSG Dataset or at variance with
     *   those in the EPSG Dataset should be reported.</td>
     * </tr></table>
     *
     * @throws FactoryException If an error occurred while creating a Unit from an EPSG code.
     *         This does not include {@link NoSuchAuthorityCodeException}, which are reported
     *         as unsupported unit rather than test failure.
     * @throws ConversionException If an error occurred while converting a sample value from the
     *         tested unit to the base unit.
     */
    @Test
    public void test2001() throws FactoryException, ConversionException {
        final ExpectedData data = new ExpectedData("GIGS_2001_libUnit.csv",
                Integer.class,  // [0]: EPSG UoM Code
                String .class,  // [1]: Type
                String .class,  // [2]: Name of Units used in EPSG db parameters
                Double .class,  // [3]: Base units per unit
                Boolean.class,  // [4]: Particularly important to E&P industry?
                String .class); // [5]: Specific usage / Remarks

        while (data.next()) {
            final int    code   = data.getInt   (0);
            final String type   = data.getString(1);
            final String name   = data.getString(2);
            final double factor = data.getDouble(3);
            final Unit<?> unit, base;
            try {
                unit = csFactory.createUnit(String.valueOf(code));
            } catch (NoSuchAuthorityCodeException e) {
                // TODO: report this unsupported unit.
                return;
            }
            if      (type.equalsIgnoreCase("Linear")) base = METRE;
            else if (type.equalsIgnoreCase("Angle" )) base = DEGREE_ANGLE;
            else if (type.equalsIgnoreCase("Scale" )) base = ONE;
            else throw new DataException("Unknown type: " + type);
            final UnitConverter converter = unit.getConverterToAny(base);
            assertEquals(name,  0,      converter.convert( 0),  TOLERANCE);
            assertEquals(name,  factor, converter.convert( 1),  TOLERANCE * factor);
            assertEquals(name, -factor, converter.convert(-1), -TOLERANCE * factor);
            if (Double.isNaN(factor)) {
                // Special cases for sexagesimal degrees
                assertEquals(name,  10.0000, converter.convert( 10.00), 10*TOLERANCE);
                assertEquals(name, -10.0000, converter.convert(-10.00), 10*TOLERANCE);
                assertEquals(name,  20.0036, converter.convert( 20.01), 20*TOLERANCE);
                assertEquals(name, -20.0036, converter.convert(-20.01), 20*TOLERANCE);
                assertEquals(name,  30.3000, converter.convert( 30.50), 30*TOLERANCE);
                assertEquals(name, -30.3000, converter.convert(-30.50), 30*TOLERANCE);
                assertEquals(name,  40.5924, converter.convert( 40.99), 40*TOLERANCE);
                assertEquals(name, -40.5924, converter.convert(-40.99), 40*TOLERANCE);
            } else {
                for (double sample=-90; sample<=90; sample += 2.8125) {
                    final double expected = sample * factor;
                    assertEquals(name, expected, converter.convert(sample), (expected != 0) ? abs(expected)*TOLERANCE : TOLERANCE);
                }
            }
        }
    }

    /**
     * Reference ellipsoid test.
     * <p>
     * <table cellpadding="3"><tr>
     *   <th nowrap align="left" valign="top">Test purpose:</th>
     *   <td>To verify reference ellipsoid parameters bundled with the geoscience software.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Test method:</th>
     *   <td>Compare ellipsoid definitions included in the software against the EPSG Dataset.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Test data:</th>
     *   <td>EPSG Dataset and file {@code GIGS_2002_libEllipsoid_v2-0_2011-06-28.xls}. This file
     *   gives the EPSG code and name for the ellipsoid, commonly encountered alternative name(s)
     *   for the same object, the value and units for the semi-major axis, the conversion ratio to
     *   metres for these units, and then a second parameter which will be either the value of the
     *   inverse flattening (unitless) or the value of the semi-minor axis (in the same units as
     *   the semi-major axis). It may additionally contain a flag to indicate that the figure is
     *   a sphere: without this flag the figure is an oblate ellipsoid.</td>
     * </tr><tr>
     *   <th nowrap align="left" valign="top">Expected result:</th>
     *   <td>Ellipsoid definitions bundled with software, if any, should have same name and defining
     *   parameters as in the EPSG Dataset. Equivalent alternative parameters are acceptable but
     *   should be reported. The values of the parameters should be correct to at least 10 significant
     *   figures. For ellipsoids defined by Clarke and Everest, as well as those adopted by IUGG as
     *   International, several variants exist. These must be clearly distinguished. Ellipsoids
     *   missing from the software or included in the software additional to those in the EPSG Dataset
     *   or at variance with those in the EPSG Dataset should be reported.</td>
     * </tr></table>
     *
     * @throws FactoryException If an error occurred while creating an ellipsoid from an EPSG code.
     *         This does not include {@link NoSuchAuthorityCodeException}, which are reported
     *         as unsupported ellipsoid rather than test failure.
     */
    @Test
    public void test2002() throws FactoryException {
        final ExpectedData data = new ExpectedData("GIGS_2002_libEllipsoid.csv",
            Integer.class,  // [ 0]: EPSG Ellipsoid Code
            Boolean.class,  // [ 1]: Particularly important to E&P industry?
            String .class,  // [ 2]: EPSG Ellipsoid Name
            String .class,  // [ 3]: Alias(es) given by EPSG
            Double .class,  // [ 4]: Semi-major axis (a)
            String .class,  // [ 5]: Unit Name
            Double .class,  // [ 6]: Unit Conversion Factor
            Double .class,  // [ 7]: Semi-major axis (a) in metres
            Double .class,  // [ 8]: Second defining parameter: Inverse flattening (1/f)
            Double .class,  // [ 9]: Second defining parameter: Semi-minor axis (b)
            Boolean.class); // [10]: Sphere?

        while (data.next()) {
            final int     code      = data.getInt    ( 0);
            final String  name      = data.getString ( 2);
            final double  semiMajor = data.getDouble ( 4);
            final boolean isSphere  = data.getBoolean(10);
            final Ellipsoid ellipsoid;
            try {
                ellipsoid = datumFactory.createEllipsoid(String.valueOf(code));
            } catch (NoSuchAuthorityCodeException e) {
                // TODO: report this unsupported unit.
                return;
            }
            assertEquals("Mismatched ellipsoid name",         name,      getName(ellipsoid));
            assertEquals("Mismatched semi-major axis length", semiMajor, ellipsoid.getSemiMajorAxis(), TOLERANCE*semiMajor);
            assertEquals("Mismatched sphere flag",            isSphere,  ellipsoid.isSphere());
        }
    }
}