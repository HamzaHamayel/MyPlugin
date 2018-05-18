package edu.training

import grails.artefact.TagLibrary
import grails.gsp.TagLib

@TagLib
class AlertTagLib implements TagLibrary{

    static namespace = "alert"
    static allowedTypes = ['danger','success','info','warning']

    def responseAlert = { attrs ->
        out << "<div id='alertMessage' >"
        if(flash.alert){
            out << flash.alert
        }
        out << "</div>"
    }

    /**
     * @attr value REQUIRED
     * @attr isDismissible
     */
    Closure warningAlert = { attrs   ->
        attrs.type = "warning"
        out << alert(attrs)
    }

    /**
     * @attr value REQUIRED
     * @attr isDismissible
     */
    Closure successAlert = { attrs   ->
        attrs.type = "success"
        out << alert(attrs)
    }

    /**
     * @attr value REQUIRED
     * @attr isDismissible
     */
    Closure infoAlert = { attrs   ->
        attrs.type = "info"
        out << alert(attrs)
    }



    /**
     * @attr value REQUIRED
     * @attr isDismissible
     */
    Closure errorAlert = { attrs   ->
        attrs.type = "danger"
        out << alert(attrs)
    }



    /**
     * @attr type REQUIRED
     * @attr value REQUIRED
     * @attr isDismissible
     */
    Closure alert = { attrs   ->
        def type = attrs["type"]
        def value = attrs["value"]

        attrs.isDismissible = "true"


        booleanToAttribute(attrs,"isDismissible")

        if(!(type in allowedTypes)){
            throw new Exception("type must be in ${allowedTypes}")
        }

        out << """  <div class="alert alert-${type} ${attrs.isDismissible ? "alert-dismissable":""}">
                        ${attrs.isDismissible ? """  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button> """:""}
                        ${value}
                    </div>   
        """
    }





    /**
     * format error list.
     *
     * @attr errorsObject REQUIRED the field name
     * @attr isDismissible
     */
    Closure errorListAlert = { attrs   ->
        def errorsObject = attrs["errorsObject"]
        def errorsMessages
        def string = ""
        errorsMessages = errorsObject?.errors?.allErrors
        if (errorsMessages) {
            string+=""" <ul> """
            errorsMessages.each {
                string += """<li>${g.message(error: it)}</li>"""
            }
            string+=""" </ul>"""
            attrs.value = string
            out << errorAlert(attrs)
        }
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

