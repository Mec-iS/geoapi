/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source$
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.opengis.util;

// Direct J2SE dependencies
import java.util.Locale;


/**
 * A {@linkplain String string} that has been internationalized into several {@linkplain Locale locales}.
 * This interface is used as a replacement for the {@link String} type whenever an attribute needs to be
 * internationalization capable.
 *
 * <P>The {@linkplain Comparable natural ordering} is defined by the {@linkplain String#compareTo
 * lexicographical ordering of strings} in the default locale, as returned by {@link #toString()}.
 * This string also defines the {@linkplain CharSequence character sequence} for this object.</P>
 *
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @since GeoAPI 1.1
 *
 * @see javax.xml.registry.infomodel.InternationalString
 * @see NameFactory#createInternationalString
 */
public interface InternationalString extends CharSequence, Comparable {
    /**
     * Returns this string in the given locale. If no string is available in the given locale,
     * then some default locale is used. The default locale is implementation-dependent. It
     * may or may not be the {@linkplain Locale#getDefault() system default}.
     *
     * @param  locale The desired locale for the string to be returned, or {@code null}
     *         for a string in the implementation default locale.
     * @return The string in the given locale if available, or in the default locale otherwise.
     */
    String toString(Locale locale);

    /**
     * Returns this string in the default locale. The default locale is implementation-dependent.
     * It may or may not be the {@linkplain Locale#getDefault() system default}. If the default
     * locale is the {@linkplain Locale#getDefault() system default} (a recommended practice),
     * then invoking this method is equivalent to invoking
     * <code>{@linkplain #toString(Locale) toString}({@linkplain Locale#getDefault})</code>.
     *
     * <P>All methods from {@link CharSequence} operate on this string. This string is also
     * used as the criterion for {@linkplain Comparable natural ordering}.</P>
     *
     * @return The string in the default locale.
     */
    String toString();
}
