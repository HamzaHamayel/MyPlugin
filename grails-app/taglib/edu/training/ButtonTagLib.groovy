package edu.training

import grails.artefact.TagLibrary
import grails.gsp.TagLib

@TagLib
class ButtonTagLib implements TagLibrary{

    static namespace = "btn"
    static allowedColors = ['default','info','danger','primary','success','warning']
    static allowedSize = ['sm','xs','lg']

    /**
     * blue button element.
     *
     * @attr type
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure blue = { attrs   ->
        attrs.color = "primary"
        out << button(attrs)
    }

    /**
     * blue2 button element.
     *
     * @attr type
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure blue2 = { attrs   ->
        attrs.color = "info"
        out << button(attrs)
    }



    /**
     * orange button element.
     *
     * @attr type
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure orange = { attrs   ->
        attrs.color = "warning"
        out << button(attrs)
    }



    /**
     * green button element.
     *
     * @attr type
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure green = { attrs   ->
        attrs.color = "success"
        out << button(attrs)
    }

    /**
     * red button element.
     *
     * @attr type
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure red = { attrs   ->
        attrs.color = "danger"
        out << button(attrs)
    }

    /**
     * submit button element.
     *
     * @attr color
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure submit = { attrs   ->
        attrs.type = "submit"
        out << button(attrs)
    }

    /**
     * reset button element.
     *
     * @attr color
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     */
    Closure reset = { attrs   ->
        attrs.type = "reset"
        out << button(attrs)
    }



    /**
     * button element.
     *
     * @attr type REQUIRED
     * @attr color
     * @attr class
     * @attr label
     * @attr size
     * @attr onclick
     * @attr isOutline
     * @attr href
     */
    Closure button = { attrs   ->

        String type = attrs.remove("type")?:"button"
        String label = attrs.remove("label")
        String color = attrs.remove("color")?:"default"
        String size = attrs.remove("size")
        String clazz = attrs.remove("class")
        String onclick = attrs.remove("onclick")
        String href = attrs.remove("href")


        booleanToAttribute(attrs,"isOutline")

        if(!(color in allowedColors)){
            throw new Exception("color must be in ${allowedColors}")
        }
        if(size && !(size in allowedSize)){
            throw new Exception("size must be in ${allowedSize}")
        }

        out << """  <button type="${type}" class="btn ${attrs.isOutline ? "btn-outline" : ""} btn-${color} ${size?("btn"+size):""} ${clazz}" ${onclick?""" onclick="${onclick}" """:''} ${href?""" onclick="window.location.href='${href}'" """:''}>${label}</button> """
    }



    private void booleanToAttribute(Map attrs, String attrName) {
        def attrValue = attrs.remove(attrName)
        if (attrValue instanceof CharSequence) {
            attrValue = attrValue.toString().trim()
        }
        // If the value is the same as the name or if it is a boolean value,
        // reintroduce the attribute to the map according to the w3c rules, so it is output later
        if ((attrValue instanceof Boolean && attrValue) ||
                (attrValue instanceof String && (((String)attrValue).equalsIgnoreCase("true") || ((String)attrValue).equalsIgnoreCase(attrName)))) {
            attrs.put(attrName, attrName)
        } else if (attrValue instanceof String && !((String)attrValue).equalsIgnoreCase("false")) {
            // If the value is not the string 'false', then we should just pass it on to
            // keep compatibility with existing code
            attrs.put(attrName, attrValue)
        }
    }

}
