/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.coverage.grid;

// OpenGIS direct dependencies
import org.opengis.coverage.SampleDimensionType;  // For Javadoc

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Describes the packing of data values within grid coverages.
 * It includes the packing scheme of data values with less then 8 bits per value
 * within a byte, byte packing (Little Endian / Big Endian) for values with more
 * than 8 bits and the packing of the values within the dimensions.
 *
 * <P>&nbsp;</P>
 * <TABLE WIDTH="80%" ALIGN="center" CELLPADDING="18" BORDER="4" BGCOLOR="#FFE0B0">
 *   <TR><TD>
 *     <P align="justify"><STRONG>WARNING: THIS CLASS WILL CHANGE.</STRONG> Current API is derived from OGC
 *     <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverages Implementation specification 1.0</A>.
 *     We plan to replace it by new interfaces derived from ISO 19123 (<CITE>Schema for coverage geometry
 *     and functions</CITE>). Current interfaces should be considered as legacy and are included in this
 *     distribution only because they were part of GeoAPI 1.0 release. We will try to preserve as much 
 *     compatibility as possible, but no migration plan has been determined yet.</P>
 *   </TD></TR>
 * </TABLE>
 *
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @since GeoAPI 1.0
 */
@UML(identifier="GC_GridPacking", specification=OGC_01004)
public interface GridPacking {
    /**
     * Order of bytes packed in values for sample dimensions with greater than 8 bits.
     */
    @UML(identifier="byteInValuePacking", obligation=MANDATORY, specification=OGC_01004)
    ByteInValuePacking getByteInValuePacking();

    /**
     * Order of values packed in a byte for sample dimensions with less than 8 bits.
     * This include
     * {@link SampleDimensionType#UNSIGNED_1BIT UNSIGNED_1BIT},
     * {@link SampleDimensionType#UNSIGNED_2BITS UNSIGNED_2BITS} and
     * {@link SampleDimensionType#UNSIGNED_4BITS UNSIGNED_4BITS} data types.
     */
    @UML(identifier="valueInBytePacking", obligation=MANDATORY, specification=OGC_01004)
    ValueInBytePacking getValueInBytePacking();

    /**
     * Gives the ordinate index for the band.
     * This index indicates how to form a band-specific coordinate from a grid coordinate
     * and a sample dimension number. This indicates the order in which the grid values
     * are stored in streamed data. This packing order is used when grid values are
     * retrieved using the <code>getPackedDataBlock</code> or set using
     * <code>setPackedDataBlock</code> operations on {@link GridCoverage}.
     *
     *  bandPacking of
     *  <UL>
     *    <li>0 : the full band-specific coordinate is (b, n1, n2...)</li>
     *    <li>1 : the full band-specific coordinate is (n1, b, n2...)</li>
     *    <li>2 : the full band-specific coordinate is (n1, n2, b...)</li>
     *  </UL>
     *  Where
     *  <UL>
     *    <li>b is band</li>
     *    <li>n1 is dimension 1</li>
     *    <li>n2 is dimension 2</li>
     *  </UL>
     *  For 2 dimensional grids, band packing of 0 is referred to as band sequential,
     *  1 line interleaved and 2 pixel interleaved.
     *
     *
     * @see java.awt.image.BandedSampleModel
     * @see java.awt.image.PixelInterleavedSampleModel
     */
    @UML(identifier="bandPacking", obligation=MANDATORY, specification=OGC_01004)
    int getBandPacking();
}
