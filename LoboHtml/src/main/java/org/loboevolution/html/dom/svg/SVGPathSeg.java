/*
    GNU GENERAL LICENSE
    Copyright (C) 2014 - 2018 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: ivan.difrancesco@yahoo.it
 */
package org.loboevolution.html.dom.svg;

public interface SVGPathSeg {

	static final short PATHSEG_UNKNOWN = 0;

	static final short PATHSEG_CLOSEPATH = 1;

	static final short PATHSEG_MOVETO_ABS = 2;

	static final short PATHSEG_MOVETO_REL = 3;

	static final short PATHSEG_LINETO_ABS = 4;

	static final short PATHSEG_LINETO_REL = 5;

	static final short PATHSEG_CURVETO_CUBIC_ABS = 6;

	static final short PATHSEG_CURVETO_CUBIC_REL = 7;

	static final short PATHSEG_CURVETO_QUADRATIC_ABS = 8;

	static final short PATHSEG_CURVETO_QUADRATIC_REL = 9;

	static final short PATHSEG_ARC_ABS = 10;

	static final short PATHSEG_ARC_REL = 11;

	static final short PATHSEG_LINETO_HORIZONTAL_ABS = 12;

	static final short PATHSEG_LINETO_HORIZONTAL_REL = 13;

	static final short PATHSEG_LINETO_VERTICAL_ABS = 14;

	static final short PATHSEG_LINETO_VERTICAL_REL = 15;

	static final short PATHSEG_CURVETO_CUBIC_SMOOTH_ABS = 16;

	static final short PATHSEG_CURVETO_CUBIC_SMOOTH_REL = 17;

	static final short PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS = 18;

	static final short PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL = 19;

	short getPathSegType();

	String getPathSegTypeAsLetter();
}
