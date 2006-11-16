/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.coverage.grid;

// Annotations
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;
import org.opengis.spatialschema.geometry.DirectPosition;
import org.opengis.util.Cloneable;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Holds the set of grid coordinates that specifies the location of the
 * {@linkplain GridPoint grid point} within the {@linkplain Grid grid}.
 * 
 * @author Martin Schouwenburg
 * @author Wim Koolhoven
 * @author Martin Desruisseaux
 */
@UML(identifier="CV_GridCoordinates", specification=ISO_19123)
public interface GridCoordinates extends Cloneable {
    /**
     * Returns the number of dimensions. This method is equivalent to
     * <code>{@linkplain #getCoordinateValues()}.length</code>. It is
     * provided for efficienty.
     */
    @Extension
    int getDimension();

    /**
     * Returns one integer value for each dimension of the grid. The ordering of these coordinate
     * values shall be the same as that of the elements of {@link Grid#getAxisNames}. The value of
     * a single coordinate shall be the number of offsets from the origin of the grid in the direction
     * of a specific axis.
     * 
     * @return A copy of the coordinates. Changes in the returned array will not be reflected
     *         back in this {@code GridCoordinates} object.
     */
    @UML(identifier="coordValues", obligation=MANDATORY, specification=ISO_19123)
	int[] getCoordinateValues();

    /**
     * Returns the coordinate value at the specified dimension. This method is equivalent to
     * <code>{@linkplain #getCoordinateValues()}[<var>i</var>]</code>. It is provided for
     * efficienty.
     */
    @Extension
    int getCoordinateValue(int i);

    /**
     * Sets the coordinate value at the specified dimension (optional operation).
     *
     * @param  i The index of the value to set.
     * @param  value The new value.
     * @throws UnsupportedOperationException if this grid coordinates is not modifiable.
     */
    @Extension
    int setCoordinateValue(int i, int value) throws UnsupportedOperationException;
}
