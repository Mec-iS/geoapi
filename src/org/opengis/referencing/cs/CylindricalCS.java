/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.referencing.cs;

// Annotations
import org.opengis.annotation.UML;
import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A three-dimensional coordinate system consisting of a {@linkplain PolarCS polar coordinate
 * system} extended by a straight coordinate axis perpendicular to the plane spanned by the
 * polar coordinate system. A <code>CylindricalCS</code> shall have three
 * {@linkplain #getAxis axis associations}.
 *
 * <TABLE CELLPADDING='6' BORDER='1'>
 * <TR BGCOLOR="#EEEEFF"><TH NOWRAP>Used with CRS type(s)</TH></TR>
 * <TR><TD>
 *   {@link org.opengis.referencing.crs.EngineeringCRS Engineering}
 * </TD></TR></TABLE>
 *
 * @author ISO 19111
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version <A HREF="http://www.opengis.org/docs/03-073r1.zip">Abstract specification 2.0</A>
 * @since GeoAPI 1.0
 *
 * @see PolarCS
 */
@UML(identifier="CS_CylindricalCS", specification=ISO_19111)
public interface CylindricalCS extends CoordinateSystem {
}
