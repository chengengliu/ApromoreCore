
package org.oryxeditor.server.diagram.label;

/*-
 * #%L
 * Signavio Core Components
 * %%
 * Copyright (C) 2006 - 2020 The University of Melbourne.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * #L%
 */

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper class for a set of {@link Anchor} that can be used to define positioning policies of labels
 *
 * @author philipp.maschke
 */
public class Anchors {

    /**
     * Enumeration of anchor positions
     *
     * @author philipp.maschke
     */
    public enum Anchor {
        BOTTOM("bottom"), TOP("top"), LEFT("left"), RIGHT("right");

        /**
         * Returns the matching object for the given string
         *
         * @param enumString
         * @return
         * @throws IllegalArgumentException if no matching enumeration object was found
         */
        public static Anchor fromString(String enumString) {
            if (enumString == null) return null;

            for (Anchor attrEnum : values()) {
                if (attrEnum.label.equals(enumString) || attrEnum.name().equals(enumString))
                    return attrEnum;
            }

            throw new IllegalArgumentException("No matching enum constant found in '"
                    + Anchor.class.getSimpleName() + "' for: " + enumString);
        }

        private String label;


        Anchor(String label) {
            this.label = label;
        }


        @Override
        public String toString() {
            return label;
        }
    }


    public static Anchors fromString(String anchorsString) {
        if (anchorsString == null) return null;
        anchorsString = anchorsString.trim();
        if (anchorsString.equals("")) return null;
        Anchors result = new Anchors();
        for (String anchorString : anchorsString.split(" ")) {
            result.addAnchor(Anchor.fromString(anchorString));
        }
        return result;
    }


    private Set<Anchor> anchors = new HashSet<Anchor>();

    public Anchors(Anchor... anchors) {
        for (Anchor anchor : anchors) {
            this.anchors.add(anchor);
        }
    }


    public void addAnchor(Anchor newAnchor) {
        anchors.add(newAnchor);
    }


    public String toString() {
        StringBuffer buff = new StringBuffer();
        for (Anchor anchor : anchors) {
            if (buff.length() > 0) buff.append(" ");
            buff.append(anchor.toString());
        }
        return buff.toString();
    }


    public int size() {
        return anchors.size();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anchors == null) ? 0 : anchors.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Anchors other = (Anchors) obj;
        if (anchors == null) {
            if (other.anchors != null)
                return false;
        } else if (!anchors.equals(other.anchors))
            return false;
        return true;
    }


    public boolean contains(Anchor anchor) {
        return anchors.contains(anchor);
    }
}
